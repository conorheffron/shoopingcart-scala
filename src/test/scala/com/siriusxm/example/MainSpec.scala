package com.siriusxm.example

import zio.test.{Spec, TestEnvironment, ZIOSpecDefault, assertCompletes, assertCompletesZIO, assertTrue}
import zio.{Scope}

object MainSpec extends ZIOSpecDefault {
  override def spec: Spec[TestEnvironment & Scope, Any] = suite("Main") {
    test("Test the main runs successfully for all valid products") {
      for {
        _ <- Main.run // Run the main application
      } yield assertCompletes // Assert that it completes without errors
    }
  }
}
