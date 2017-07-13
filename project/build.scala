import sbt._
import sbt.Keys._
import com.typesafe.sbt.SbtScalariform._
import com.typesafe.sbt.SbtScalariform.ScalariformKeys._
import com.typesafe.sbt.pgp.PgpKeys._

object HarmonyBuild extends Build {
  import scalariform.formatter.preferences._

  lazy val harmonySettings = Seq(
    organization := "jp.co.reraku",
    version := "0.2.1",
    crossScalaVersions := Seq("2.12.2", "2.11.8"),
    scalaVersion := crossScalaVersions.value.head,
    scalacOptions ++= Seq(
      "-target:jvm-1.8",
      "-unchecked",
      "-deprecation",
      "-Xcheckinit",
      "-encoding",
      "utf8",
      "-feature",
      "-Ywarn-unused-import",
      "-language:higherKinds",
      "-language:postfixOps",
      "-language:implicitConversions"
    ),
    publishTo := Some(Resolver.file("file", file(".")))
  ) ++ scalariformSettings ++ Seq(
    preferences := preferences.value
      .setPreference(AlignParameters, true)
      .setPreference(AlignArguments, true)
      .setPreference(AlignSingleLineCaseStatements, true)
      .setPreference(CompactControlReadability, true)
  )

  lazy val harmonyProject = Project(
    id = "harmony-project",
    base = file("."),
    settings = harmonySettings ++ doNotPublish ++ Seq(
      description := "Harmony is advanced a project by DDD"
    )
  ) aggregate(harmonyCore, harmonyScalatra)

  lazy val harmonyCore = Project(
    id = "harmony",
    base = file("core"),
    settings = harmonySettings ++ Seq(
      description := "The core of Harmony"
    )
  )

  val ScalatraVersion = "2.5.1"

  lazy val harmonyScalatra = Project(
    id = "harmony-scalatra",
    base = file("scalatra"),
    settings = harmonySettings ++ Seq(
      description := "Scalatra support as a presentation layer",
      libraryDependencies ++= Seq(
        "javax.servlet" %  "javax.servlet-api" % "3.1.0",
        "org.scalatra"  %% "scalatra"          % ScalatraVersion,
        "org.scalatra"  %% "scalatra-json"     % ScalatraVersion
      )
    )
  ) dependsOn(harmonyCore % "compile;test->test;provided->provided")

  lazy val doNotPublish = Seq(
    publish := {},
    publishLocal := {},
    publishSigned := {},
    publishLocalSigned := {}
  )
}
