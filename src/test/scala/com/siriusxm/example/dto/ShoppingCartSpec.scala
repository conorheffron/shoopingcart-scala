package com.siriusxm.example.dto

import com.siriusxm.example.service.CartService
import zio.test.Assertion.equalTo
import zio.test.{Spec, TestEnvironment, ZIOSpecDefault, assert}
import zio.Scope

import scala.collection.Map

object ShoppingCartSpec extends ZIOSpecDefault {
  override def spec: Spec[TestEnvironment & Scope, Any] = suite("Shopping Cart")(
    test("getCartEntries with Line Items that have quantity set") {
      for {
        cart <- ShoppingCart.newCart
        _ <- CartService.addLineItem(cart.data, ProductInfo("Nesquik", 3.90f), 5)
        _ <- CartService.addLineItem(cart.data, ProductInfo("Flahavans Oats", 3.70f), 2)
        _ <- CartService.addLineItem(cart.data, ProductInfo("Oreo Cereal", 3.80f), 3)
        result <- cart.data.get
      } yield assert(result)(
        equalTo(Map("Nesquik" -> (3.90f, 5),
          "Flahavans Oats"-> (3.70f, 2),
          "Oreo Cereal" -> (3.80f, 3))
        )
      )
    },
    test("getCartEntries Empty Map") {
      for {
        cart <- ShoppingCart.newCart
        result <- cart.data.get
      } yield assert(result)(equalTo(Map.empty))
    },
    test("getCartEntries 2 Initial Entries without Price") {
      for {
        cart <- ShoppingCart.newCart
        _ <- CartService.addLineItem(cart.data, "Nesquik", 2)
        _ <- CartService.addLineItem(cart.data, "Flahavans Oats", 2)
        result <- cart.data.get
      } yield assert(result)(equalTo(
        Map(
          "Nesquik" -> (0f, 2),
          "Flahavans Oats"-> (0f, 2)
        ))
      )
    },
  )
}