package com.siriusxm.example.service

import com.siriusxm.example.service.CartService
import zio.Scope
import zio.test.{Spec, TestEnvironment, ZIOSpecDefault, assertTrue}

object CartServiceSpec extends ZIOSpecDefault {
  override def spec: Spec[TestEnvironment & Scope, Any] = suite("Shopping Cart")(
    test("Add 2 × cornflakes @ 2.52 each,\n" +
      "Add 1 × weetabix @ 9.98 each,\n" +
      "Subtotal = 15.02,\n" +
      "Tax = 1.88,\n" +
      "Total = 16.90,\n" +
      "Total No. of items in cart = 3 (2 + 1),\n" +
      "Total No. of products in cart = 2") {
      for cart <- CartService.newCart
        _ <- cart.addLineItem("cornflakes", 2)
        _ <- cart.addLineItem("weetabix", 1)
        subtotal <- cart.subtotal
        tax <- cart.taxPayable
        total <- cart.totalPayable
        ni <- cart.numItems
        nli <- cart.numLineItems
      yield assertTrue((subtotal, tax, total, ni, nli) == (15.02, 1.88, 16.90, 3, 2))
    },

    test("Add 6 x cornflakes & 1 x weetabix") {
      for cart <- CartService.newCart
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
  )
}