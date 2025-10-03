package com.siriusxm.example.cart

import zio.test.assertTrue
import zio.test.Spec
import zio.test.TestEnvironment
import zio.test.ZIOSpecDefault
import zio.{Scope, ZIO}

object ShoppingCartSpec extends ZIOSpecDefault {
  val Tolerance = 0 // for sum comparisons
  val MaxPrice = 1E5
  val validProducts = Set("cheerios", "cornflakes", "frosties", "shreddies", "weetabix")

  override def spec: Spec[TestEnvironment & Scope, Any] = suite("CerealProductInfo.priceLookup")(
    test("Add 2 × cornflakes @ 2.52 each, " +
      "Add 1 × weetabix @ 9.98 each, " +
      "Subtotal = 15.02, " +
      "Tax = 1.88, " +
      "Total = 16.90") {
      for cart <- ShoppingCart.newCart
        _ <- cart.addLineItem("cornflakes", 2)
        _ <- cart.addLineItem("weetabix", 1)
        subtotal <- cart.subtotal
        tax <- cart.taxPayable
        total <- cart.totalPayable
      yield assertTrue((subtotal, tax, total) == (15.02, 1.88, 16.90))
    },

    test("returns price > 0f for valid products") {
      ZIO.foreach(validProducts) { name =>
        CerealProductInfo.priceLookup(name)
          .map(price => assertTrue(price > 0f))
      }.map(_.reduce(_ && _))
    },

    test("fails for invalid product names") {
      val invalidProducts = Set(null, "")
      ZIO.foreach(invalidProducts) { name =>
        CerealProductInfo.priceLookup(name)
          .exit
          .map(exit => assertTrue(exit.isFailure))
      }.map(_.reduce(_ && _))
    },

    test("returns 0.0f for product names that do not have JSON object set up") {
      val invalidProducts = Set("wheetos", "invalid", "999")
      ZIO.foreach(invalidProducts) { name =>
        CerealProductInfo.priceLookup(name)
          .map(price => assertTrue(price == 0.0f))
      }.map(_.reduce(_ && _))
    },
  )
}