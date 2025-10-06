val scala3Version = "3.7.2"
val V = new {
  val quill = "4.8.0"
  val zio = "2.0.18"
  val sttp = "4.0.0-M6"
}

lazy val root = project
  .in(file("."))
  .settings(
    name := "shopping-cart",
    version := "1.0.14-RELEASE",

    scalaVersion := scala3Version,

    // Compile scope
    libraryDependencies ++= Seq(
      "com.softwaremill.sttp.client4" %% "zio-json" % V.sttp,
      "com.softwaremill.sttp.client4" %% "zio" % V.sttp,
      "io.getquill" %% "quill-jdbc-zio" % V.quill,
      "ch.qos.logback" % "logback-classic" % "1.5.18",
    ),

    // Test scope
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio-test" % V.zio % Test,
      "dev.zio" %% "zio-test-sbt" % V.zio % Test,
      "org.scalameta" %% "munit" % "1.0.4" % Test,
    ),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
  )

ThisBuild / libraryDependencySchemes += "org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always

