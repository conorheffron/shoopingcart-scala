package com.siriusxm.example.service

import com.siriusxm.example.service.ProductPriceService
import zio.test.assertTrue
import zio.test.Spec
import zio.test.TestEnvironment
import zio.test.ZIOSpecDefault
import zio.{Scope, ZIO}

object ProductPriceServiceSpec extends ZIOSpecDefault {
  val validProducts = Set("cheerios", "cornflakes", "frosties", "shreddies", "weetabix")

  override def spec: Spec[TestEnvironment & Scope, Any] = suite("Product Price Service")(
    test("returns price > 0f for valid products") {
      ZIO.foreach(validProducts) { name =>
        ProductPriceService.findPriceByProductTitle(name)
          .map(price => assertTrue(price > 0f))
      }.map(_.reduce(_ && _))
    },

    test("fails for invalid product names") {
      val invalidProducts = Set(null, "")
      ZIO.foreach(invalidProducts) { name =>
        ProductPriceService.findPriceByProductTitle(name)
          .exit
          .map(exit => assertTrue(exit.isFailure))
      }.map(_.reduce(_ && _))
    },

    test("returns 0.0f for product names that do not have JSON object set up") {
      val invalidProducts = Set("wheetos", "invalid", "999")
      ZIO.foreach(invalidProducts) { name =>
        ProductPriceService.findPriceByProductTitle(name)
          .map(price => assertTrue(price == 0.0f))
      }.map(_.reduce(_ && _))
    },

    test("returns 0.0f for product flahavans") {
      val invalidProducts = Set("flahavans")
      ZIO.foreach(invalidProducts) { name =>
        ProductPriceService.findPriceByProductTitle(name)
          .map(price => assertTrue(price == 0.0f))
      }.map(_.reduce(_ && _))
    },
  )
}