// From https://gist.github.com/xuwei-k/6278769

lazy val buildFiles = SettingKey[Map[File, Seq[Byte]]]("build-files")
 
buildFiles := getBuildFiles((baseDirectory in ThisBuild).value) 
 
def getBuildFiles(base: File) =
  ((base * "*.sbt") +++ ((base / "project") ** ("*.scala" | "*.sbt"))).get.map { f =>
  	f -> collection.mutable.WrappedArray.make[Byte](Hash(f))
  }.toMap
 
def changed(base: File, files: Map[File, Seq[Byte]]): Boolean =
  getBuildFiles(base) != files
 
shellPrompt in ThisBuild := { state => {
    if(changed((baseDirectory in ThisBuild).value, buildFiles.value))
      scala.Console.RED + "build files changed. please reload project\n" + scala.Console.RESET
    else ""
  } + "> "
}
