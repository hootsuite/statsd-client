name := "ScalaStatsd"

version := "2.0.4"

scalaVersion := "2.11.1"

crossScalaVersions := Seq("2.10.4", "2.11.1")

organization := "com.hootsuite"

publishTo := Some(Resolver.file("HootSuite repo", file("/var/www/maven-development")))

resolvers += "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases"

resolvers += "Sonatype" at "https://oss.sonatype.org/content/repositories/releases/"

libraryDependencies ++= Seq(
  "com.typesafe"  %  "config"    % "1.2.1",
  "org.scalatest" %% "scalatest" % "2.2.0"    % "test"
)

