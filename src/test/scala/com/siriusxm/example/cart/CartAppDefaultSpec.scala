package com.siriusxm.example.cart

import com.siriusxm.example.cart.CartAppDefault
import zio.Scope
import zio.test.{Spec, TestEnvironment, ZIOSpecDefault, assertCompletes}

object CartAppDefaultSpec extends ZIOSpecDefault {
  override def spec: Spec[TestEnvironment & Scope, Any] = suite("Main") {
    test("Test the main runs successfully for all valid products") {
      for {
        _ <- CartAppDefault.run // Run the main application
      } yield assertCompletes // Assert that it completes without errors
    }
  }
}