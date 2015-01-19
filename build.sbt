name := "ScalaStatsd"

version := "2.0.5-SNAPSHOT"

scalaVersion := "2.11.1"

crossScalaVersions := Seq("2.10.4", "2.11.1")

organization := "com.hootsuite"

publishTo := Some("HootSuite Local repo" at "http://maven.hootops.com:8081/artifactory/hootsuite-local")

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

libraryDependencies ++= Seq(
  "com.typesafe"  %  "config"    % "1.2.1",
  "org.scalatest" %% "scalatest" % "2.2.0"    % "test"
)
