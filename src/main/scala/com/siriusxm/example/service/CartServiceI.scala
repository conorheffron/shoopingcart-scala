package com.siriusxm.example.service

import zio.{Ref, UIO}

trait CartServiceI {

  // constants / enum vals
  protected val TaxRate = 0.125d // Tax payable, charged at 12.5% on the subtotal
  protected val DecimalScale = 2 // money rounded to 2 decimal places
  protected val RoundingMode: BigDecimal.RoundingMode.Value = BigDecimal.RoundingMode.UP

  // Value types for line item data
  private type ProductTitle = String
  private type Price = Float
  private type Quantity = Int

  // Entries: line item type Map(key=ProductTitle, value=(Price, Quantity))
  type Entries = Map[ProductTitle, (Price, Quantity)]
}