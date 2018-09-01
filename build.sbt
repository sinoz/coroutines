ThisBuild / organization := "com.storm-enroute"

ThisBuild / version := "1.0"

ThisBuild / scalaVersion := "2.12.4"

lazy val sharedDependencies = Seq(
  "org.scala-lang" % "scala-reflect" % "2.12.4",
  "org.scalatest" %% "scalatest" % "3.0.5" % "test"
)

lazy val common = project("coroutines-common")
  .settings(
    libraryDependencies ++= sharedDependencies
  )

lazy val patterns = project("coroutines-patterns")
  .dependsOn(root)

lazy val root = Project("coroutines", base = file("."))
  .settings(
    libraryDependencies ++= sharedDependencies
  )
  .aggregate(common)
  .dependsOn(common)

def project(id: String) = Project(id, base = file(id))