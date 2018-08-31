lazy val globalScalaVersion = "2.12.4"

name := "coroutines"

organization := "com.storm-enroute"

version := "1.0"

scalaVersion := globalScalaVersion

lazy val dependencies = Seq(
  "org.scala-lang" % "scala-reflect" % globalScalaVersion,
  "org.scalatest" %% "scalatest" % "3.0.5" % "test"
)

lazy val common = project("coroutines-common")
  .settings(
    libraryDependencies ++= dependencies
  )

lazy val extra = project("coroutines-extra")
  .dependsOn(root)

lazy val root = Project("coroutines", base = file("."))
  .settings(
    libraryDependencies ++= dependencies
  )
  .dependsOn(common)

def project(id: String) = Project(id, base = file(id))