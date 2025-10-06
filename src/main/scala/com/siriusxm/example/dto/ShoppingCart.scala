package com.siriusxm.example.dto

import com.siriusxm.example.service.CartService.Entries
import zio.{Ref, UIO}

class ShoppingCart(val data: Ref[Entries])

// ShoppingCart companion object for creating instances
object ShoppingCart {
  /* Creates new instance of an empty shopping cart */
  def newCart: UIO[ShoppingCart] =
    Ref.make(Map.empty).map(new ShoppingCart(_))
}