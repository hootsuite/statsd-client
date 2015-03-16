name := "ScalaStatsd"

version := "3.0.0"

scalaVersion := "2.11.6"

crossScalaVersions := Seq("2.10.5", "2.11.6")

organization := "com.hootsuite"

publishTo := Some("HootSuite Local repo" at "http://maven.hootops.com:8081/artifactory/hootsuite-local")

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

libraryDependencies ++= Seq(
  "com.typesafe"  %  "config"    % "1.2.1",
  "org.scalatest" %% "scalatest" % "2.2.0"    % "test"
)
