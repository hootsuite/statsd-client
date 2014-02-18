name := "ScalaStatsd"

version := "2.0.0"

crossPaths := false

organization := "com.hootsuite"

scalaVersion := "2.10.3"

publishTo := Some(Resolver.file("HootSuite repo", file("/var/www/maven-development")))

resolvers += "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases"

resolvers += "Sonatype" at "https://oss.sonatype.org/content/repositories/releases/"

libraryDependencies ++= Seq(
  "com.typesafe"  %  "config"    % "1.2.0",
  "org.scalatest" %% "scalatest" % "2.0"    % "test"
)

