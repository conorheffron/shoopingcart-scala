package com.siriusxm.example.dto

import com.siriusxm.example.dto.ProductInfo
import com.siriusxm.example.service.CartService
import com.siriusxm.example.service.CartService.{EmptyEntries, Entries}
import zio.test.Assertion.equalTo
import zio.test.{Spec, TestEnvironment, ZIOSpecDefault, assert, assertTrue}
import zio.{Ref, Scope, ZIO}

import scala.collection.Map

object ShoppingCartSpec extends ZIOSpecDefault {
  override def spec: Spec[TestEnvironment & Scope, Any] = suite("Shopping Cart")(
    test("getCartEntries Empty Map") {
      for {
        cart <- ShoppingCart.newCart
        result <- cart.getCartEntries.get
      } yield assert(result)(equalTo(Map.empty))
    },
    test("getCartEntries 2 Initial Entries without Price") {
      for {
        cart <- ShoppingCart.newCart
        _ <- CartService.addLineItem(cart.getCartEntries, "Nesquik", 2)
        _ <- CartService.addLineItem(cart.getCartEntries, "Flahavans Oats", 2)
        result <- cart.getCartEntries.get
      } yield assert(result)(equalTo(
        Map(
          "Nesquik" -> (0, 2),
          "Flahavans Oats"-> (0, 2)
        ))
      )
    },
  )
}