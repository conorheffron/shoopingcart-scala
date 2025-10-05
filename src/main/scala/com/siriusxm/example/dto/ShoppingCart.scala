package com.siriusxm.example.dto

import com.siriusxm.example.service.CartService.Entries
import zio.{Ref, UIO}

class ShoppingCart(private val data: Ref[Entries]):
  private val TaxRate = 0.125d // Tax payable, charged at 12.5% on the subtotal
  private val DecimalScale = 2 // money rounded to 2 decimal places
  private val RoundingMode: BigDecimal.RoundingMode.Value = BigDecimal.RoundingMode.UP

  def getCartEntries: Ref[Entries] = data

  /** Calculate Number of products in cart */
  def numLineItems: UIO[Int] =
    data.get.map(_.size)

  /** Calculate Number of items (total quantity) in cart */
  def numItems: UIO[Int] =
    data.get.map { entries =>
      entries.values.map { case (_, count) => count }.sum
    }

  /** Calculate subtotal, rounded up */
  def subtotal: UIO[BigDecimal] =
    data.get.map { entries =>
      entries
        .foldLeft(BigDecimal(0)) { case (sum, (_, (price, count))) =>
          sum + BigDecimal(price) * count
        }
        .setScale(DecimalScale, RoundingMode)
    }

  /** Calculate Tax payable, rounded up */
  def taxPayable: UIO[BigDecimal] =
    subtotal.map(st
    => (st * TaxRate).setScale(DecimalScale, RoundingMode)

    )

  /** Calculate Total payable, rounded up */
  def totalPayable: UIO[BigDecimal] =
    for
      st <- subtotal
      tax <- taxPayable
    yield (st + tax).setScale(DecimalScale, RoundingMode)

// ShoppingCart companion object for creating instances
object ShoppingCart {
  /** Creates new instance of an empty shopping cart */
  def newCart: UIO[ShoppingCart] =
    Ref.make(Map.empty).map(new ShoppingCart(_))
}