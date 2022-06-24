
/*
 build.sbt adapted from https://github.com/pbassiner/sbt-multi-project-example/blob/master/build.sbt
*/


name := "bwhc-icd-catalogs"
organization in ThisBuild := "de.bwhc"
version in ThisBuild:= "1.0-SNAPSHOT"

lazy val scala212 = "2.12.10"
lazy val scala213 = "2.13.8"
lazy val supportedScalaVersions =
  List(
    scala212,
    scala213
  )

scalaVersion in ThisBuild := scala213

//-----------------------------------------------------------------------------
// PROJECTS
//-----------------------------------------------------------------------------

lazy val global = project
  .in(file("."))
  .settings(
    settings,
    crossScalaVersions := Nil,
    publish / skip := true
  )
  .aggregate(
     api,
     impl,
     tests
  )



lazy val api = project
  .settings(
    name := "icd-catalogs-api",
    settings,
    libraryDependencies ++= Seq(
      dependencies.play_json
    ),
    crossScalaVersions := supportedScalaVersions
  )

lazy val impl = project
  .settings(
    name := "icd-catalogs-impl",
    settings,
    libraryDependencies ++= Seq(
      dependencies.scala_xml
    ),
    crossScalaVersions := supportedScalaVersions
  )
  .dependsOn(api)

lazy val tests = project
  .settings(
    name := "tests",
    settings,
    libraryDependencies ++= Seq(
      dependencies.scalatest
    ),
    crossScalaVersions := supportedScalaVersions,
    publish / skip := true
  )
  .dependsOn(
    api,
    impl % Test
  )



//-----------------------------------------------------------------------------
// DEPENDENCIES
//-----------------------------------------------------------------------------

lazy val dependencies =
  new {
    val scalatest  = "org.scalatest"          %% "scalatest"        % "3.0.8" % Test
    val slf4j      = "org.slf4j"              %  "slf4j-api"        % "1.7.32"
    val play_json  = "com.typesafe.play"      %% "play-json"        % "2.8.1"
    val scala_xml  = "org.scala-lang.modules" %% "scala-xml"        % "2.0.0"
  }

lazy val commonDependencies = Seq(
  dependencies.slf4j,
  dependencies.scalatest,
)


//-----------------------------------------------------------------------------
// SETTINGS
//-----------------------------------------------------------------------------

lazy val settings = commonSettings


lazy val compilerOptions = Seq(
  "-unchecked",
  "-feature",
  "-Xfatal-warnings",
//  "-language:existentials",
//  "-language:higherKinds",
//  "-language:implicitConversions",
  "-language:postfixOps",
  "-deprecation",
  "-encoding", "utf8"
)

lazy val commonSettings = Seq(
  scalacOptions ++= compilerOptions,
  resolvers ++= Seq(
    "Local Maven Repository" at "file://" + Path.userHome.absolutePath + "/.m2/repository",
    Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots")
  )
)

