package com.siriusxm.example.service

import com.siriusxm.example.service.CerealService
import zio.test.assertTrue
import zio.test.Spec
import zio.test.TestEnvironment
import zio.test.ZIOSpecDefault
import zio.{Scope, ZIO}

object CerealServiceSpec extends ZIOSpecDefault {
  val validProducts = Set("cheerios", "cornflakes", "frosties", "shreddies", "weetabix")

  override def spec: Spec[TestEnvironment & Scope, Any] = suite("Cereal Service")(
    test("returns price > 0f for valid products") {
      ZIO.foreach(validProducts) { name =>
        CerealService.priceLookup(name)
          .map(price => assertTrue(price > 0f))
      }.map(_.reduce(_ && _))
    },

    test("fails for invalid product names") {
      val invalidProducts = Set(null, "")
      ZIO.foreach(invalidProducts) { name =>
        CerealService.priceLookup(name)
          .exit
          .map(exit => assertTrue(exit.isFailure))
      }.map(_.reduce(_ && _))
    },

    test("returns 0.0f for product names that do not have JSON object set up") {
      val invalidProducts = Set("wheetos", "invalid", "999")
      ZIO.foreach(invalidProducts) { name =>
        CerealService.priceLookup(name)
          .map(price => assertTrue(price == 0.0f))
      }.map(_.reduce(_ && _))
    },
  )
}