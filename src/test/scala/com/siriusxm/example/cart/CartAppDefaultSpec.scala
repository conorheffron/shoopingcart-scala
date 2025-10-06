package com.siriusxm.example.cart

import com.siriusxm.example.cart.CartAppDefault
import zio.Scope
import zio.test.Assertion.containsString
import zio.test.{Spec, TestEnvironment, ZIOSpecDefault, assertCompletes, assertZIO}

object CartAppDefaultSpec extends ZIOSpecDefault {
  override def spec: Spec[TestEnvironment & Scope, Any] = suite("Part App Run")(
    test("Test the run function completes successfully without error") {
      for {
        _ <- CartAppDefault.run // Run the main application
      } yield assertCompletes // Assert that it completes without errors
    },
    test("Test the run function completes successfully for all default products") {
      // A ZIO effect that produces a string
      val resultEffect = CartAppDefault.run

      // Run the effect and assert that the string contains "ZIO"
      assertZIO(resultEffect) (
        containsString("frosties -> 4.99 | " +
          "fake_brand -> 0.0 | " +
          "cornflakes -> 2.52 | " +
          "cheerios -> 8.43 | " +
          "flahavans -> 0.0 | " +
          "weetabix -> 9.98 | " +
          "shreddies -> 4.68"))
    }
  )
}