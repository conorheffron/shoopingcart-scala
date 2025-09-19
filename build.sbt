val scala3Version = "3.7.2"
val V = new {
  val quill = "4.8.0"
  val zio = "2.0.18"
  val sttp = "4.0.0-M6"
}

lazy val root = project
  .in(file("."))
  .settings(
    name := "fp-zio",
    version := "1.0.0-RELEASE",

    scalaVersion := scala3Version,

    // Compile scope
    libraryDependencies ++= Seq(
      "com.softwaremill.sttp.client4" %% "zio-json" % V.sttp,
      "com.softwaremill.sttp.client4" %% "zio" % V.sttp,
      "io.getquill" %% "quill-jdbc-zio" % V.quill,
      "org.postgresql" % "postgresql" % "42.6.0",
      "ch.qos.logback" % "logback-classic" % "1.4.7",
    ),

    // Test scope
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio-test" % V.zio % Test,
      "dev.zio" %% "zio-test-sbt" % V.zio % Test,
      "dev.zio" %% "zio-test-magnolia" % V.zio % Test
    ),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
  )
