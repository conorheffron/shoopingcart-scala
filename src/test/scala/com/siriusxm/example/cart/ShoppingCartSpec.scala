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
    test("Add 2 × cornflakes @ 2.52 each,\n" +
      "Add 1 × weetabix @ 9.98 each,\n" +
      "Subtotal = 15.02,\n" +
      "Tax = 1.88,\n" +
      "Total = 16.90,\n" +
      "Total No. of items in cart = 3 (2 + 1),\n" +
      "Total No. of products in cart = 2") {
      for cart <- ShoppingCart.newCart
        _ <- cart.addLineItem("cornflakes", 2)
        _ <- cart.addLineItem("weetabix", 1)
        subtotal <- cart.subtotal
        tax <- cart.taxPayable
        total <- cart.totalPayable
        ni <- cart.numItems
        nli <- cart.numLineItems
      yield assertTrue((subtotal, tax, total, ni, nli) == (15.02, 1.88, 16.90, 3, 2))
    },

    test("Add 5 × corn flakes 2 and fail") {
      for cart <- ShoppingCart.newCart
          _ <- cart.addLineItem("cornflakes", 2)
          _ <- cart.addLineItem("cornflakes", 2)
          _ <- cart.addLineItem("corn flakes", 2)
          _ <- cart.addLineItem("weetabix", 1)
          subtotal <- cart.subtotal
          tax <- cart.taxPayable
          total <- cart.totalPayable
          ni <- cart.numItems
          nli <- cart.numLineItems
      yield assertTrue((subtotal, tax, total, ni, nli) == (20.06, 2.51, 22.57, 7, 3))
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