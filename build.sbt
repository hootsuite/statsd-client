name := "ScalaStatsd"

version := "1.0"

organization := "com.hootsuite"

scalaVersion := "2.10.0"

publishTo := Some(Resolver.file("HootSuite repo", file("/var/www/maven-development")))

resolvers += "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases"

resolvers += "Sonatype" at "https://oss.sonatype.org/content/repositories/releases/"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.1.0"
)