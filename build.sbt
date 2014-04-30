name := "ScalaStatsd"

version := "3.0.1"

scalaVersion := "2.11.6"

crossScalaVersions := Seq("2.10.5", "2.11.6")

organization := "com.hootsuite"

publishTo := Some(Resolver.file("HootSuite repo", file("/var/www/maven-development")))

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

libraryDependencies ++= Seq(
  "com.typesafe"  %  "config"    % "1.2.1",
  "org.scalatest" %% "scalatest" % "2.2.0"    % "test"
)
