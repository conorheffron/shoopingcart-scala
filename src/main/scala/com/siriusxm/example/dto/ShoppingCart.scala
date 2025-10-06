package com.siriusxm.example.dto

import com.siriusxm.example.service.CartService.Entries
import zio.{Ref, UIO}

class ShoppingCart(private val data: Ref[Entries]):
  def getCartEntries: Ref[Entries] = data

// ShoppingCart companion object for creating instances
object ShoppingCart {
  /** Creates new instance of an empty shopping cart */
  def newCart: UIO[ShoppingCart] =
    Ref.make(Map.empty).map(new ShoppingCart(_))
}