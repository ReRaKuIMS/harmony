import sbt._
import sbt.Keys._
import com.typesafe.sbt.SbtScalariform._
import com.typesafe.sbt.SbtScalariform.ScalariformKeys._

object HarmonyBuild extends Build {
  import scalariform.formatter.preferences._

  lazy val harmonySettings = Seq(
    organization := "jp.co.reraku",
    scalaVersion := "2.11.8",
    scalacOptions ++= Seq("-target:jvm-1.7", "-unchecked", "-deprecation", "-Yinline-warnings", "-Xcheckinit", "-encoding", "utf8", "-feature"),
    scalacOptions ++= Seq("-language:higherKinds", "-language:postfixOps", "-language:implicitConversions")
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
    settings = harmonySettings ++ Seq(
      description := "Harmony is advanced a project by DDD"
    )
  ) aggregate(harmonyCore, harmonyScalatra, harmonyScalikejdbc)

  lazy val harmonyCore = Project(
    id = "harmony",
    base = file("core"),
    settings = harmonySettings ++ Seq(
      description := "The core of Harmony"
    )
  )

  val ScalatraVersion = "2.4.0"

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

  lazy val harmonyScalikejdbc = Project(
    id = "harmony-scalikejdbc",
    base = file("scalikejdbc"),
    settings = harmonySettings ++ Seq(
      description := "ScalikeJDBC support as an infrastructure layer",
      libraryDependencies ++= Seq(
        "org.scalikejdbc" %% "scalikejdbc" % "2.3.5"
      )
    )
  ) dependsOn(harmonyCore % "compile;test->test;provided->provided")
}
