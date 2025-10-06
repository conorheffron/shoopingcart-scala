package com.siriusxm.example.service

import com.siriusxm.example.dto.ShoppingCart
import com.siriusxm.example.service.CartService
import zio.Scope
import zio.test.{Spec, TestEnvironment, ZIOSpecDefault, assertTrue}

object CartServiceSpec extends ZIOSpecDefault {
  override def spec: Spec[TestEnvironment & Scope, Any] = suite("Shopping Cart Service")(
    test("Add 2 × cornflakes @ 2.52 each,\n" +
      "Add 1 × weetabix @ 9.98 each,\n" +
      "Subtotal = 15.02,\n" +
      "Tax = 1.88,\n" +
      "Total = 16.90,\n" +
      "Total No. of items in cart = 3 (2 + 1),\n" +
      "Total No. of products in cart = 2") {
      for cart <- ShoppingCart.newCart
        _ <- CartService.addLineItem(cart.data, "cornflakes", 2)
        _ <- CartService.addLineItem(cart.data, "weetabix", 1)
        subtotal <- CartService.subtotal(cart.data)
        tax <- CartService.taxPayable(cart.data)
        total <- CartService.totalPayable(cart.data)
        ni <- CartService.numItems(cart.data)
        nli <- CartService.numLineItems(cart.data)
      yield assertTrue((subtotal, tax, total, ni, nli) == (15.02, 1.88, 16.90, 3, 2))
    },

    test("Add 6 x cornflakes & 1 x weetabix") {
      for cart <- ShoppingCart.newCart
          _ <- CartService.addLineItem(cart.data, "cornflakes", 2)
          _ <- CartService.addLineItem(cart.data, "cornflakes", 2)
          _ <- CartService.addLineItem(cart.data, "corn flakes", 2)
          _ <- CartService.addLineItem(cart.data, "weetabix", 1)
          subtotal <- CartService.subtotal(cart.data)
          tax <- CartService.taxPayable(cart.data)
          total <- CartService.totalPayable(cart.data)
          ni <- CartService.numItems(cart.data)
          nli <- CartService.numLineItems(cart.data)
      yield assertTrue((subtotal, tax, total, ni, nli) == (20.06, 2.51, 22.57, 7, 3))
    },
  )
}