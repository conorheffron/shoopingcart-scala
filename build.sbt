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
    version := "1.0.3-RELEASE",

    scalaVersion := scala3Version,

    // Compile scope
    libraryDependencies ++= Seq(
      "com.softwaremill.sttp.client4" %% "zio-json" % V.sttp,
      "com.softwaremill.sttp.client4" %% "zio" % V.sttp,
    ),

    // Test scope
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio-test" % V.zio % Test,
      "dev.zio" %% "zio-test-sbt" % V.zio % Test,
    ),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
  )
