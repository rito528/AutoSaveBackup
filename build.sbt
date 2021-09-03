name := "AutoSaveBackup"

version := "1.0.2"

scalaVersion := "2.13.6"

mainClass := Some("com.gmail.rotoyutoriapp.AutoSaveBackup")

assemblyJarName := { s"${name.value}-${version.value}.jar" }

resolvers += "spigot-repo" at "https://hub.spigotmc.org/nexus/content/repositories/snapshots/"
resolvers += Resolver.jcenterRepo
resolvers += Resolver.bintrayIvyRepo("com.eed3si9n", "sbt-plugins")
resolvers += Resolver.mavenLocal
libraryDependencies += "org.spigotmc" % "spigot-api" % "1.17.1-R0.1-SNAPSHOT" % "provided"
libraryDependencies += "com.fasterxml.jackson.core" % "jackson-core" % "2.13.0-rc1"
libraryDependencies += "com.fasterxml.jackson.core" % "jackson-databind" % "2.13.0-rc1"
libraryDependencies += "com.fasterxml.jackson.core" % "jackson-annotations" % "2.13.0-rc1"
libraryDependencies ++= Seq(
  "joda-time" % "joda-time" % "2.9.2",
  "org.joda" % "joda-convert" % "1.8" // http://www.joda.org/joda-convert/
)

assemblyMergeStrategy in assembly := {
  case PathList("javax", "servlet", xs @ _*)         => MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith ".properties" => MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith ".xml" => MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith ".types" => MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith ".class" => MergeStrategy.first
  case "application.conf"                            => MergeStrategy.concat
  case "unwanted.txt"                                => MergeStrategy.discard
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}
