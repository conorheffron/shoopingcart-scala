package com.siriusxm.example.service

import com.siriusxm.example.service.CartService.ShoppingCart
import zio.{Ref, UIO}

trait CartI {
  protected val TaxRate = 0.125d // Tax payable, charged at 12.5% on the subtotal
  protected val DecimalScale = 2 // money rounded to 2 decimal places
  protected val RoundingMode: BigDecimal.RoundingMode.Value = BigDecimal.RoundingMode.UP

  private type ProductTitle = String
  private type Price = Float
  private type Count = Int

  protected type Entries = Map[ProductTitle, (Price, Count)]

  private val EmptyEntries: Entries = Map.empty

  def newCart: UIO[ShoppingCart] =
    Ref.make(EmptyEntries).map(new ShoppingCart(_))
}