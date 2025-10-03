package com.siriusxm.example.cart

import zio.UIO
import zio.Task
import zio.Ref
import zio.ZIO
import com.siriusxm.example.cart.CerealProductInfo.priceLookup

import scala.math.BigDecimal

object ShoppingCart:
  // Tax payable, charged at 12.5% on the subtotal
  private val TaxRate = 0.125d
  private val DecimalScale = 2 // money rounded to 2 decimal places
  private val RoundingMode: BigDecimal.RoundingMode.Value = BigDecimal.RoundingMode.UP

  private type Entries = Map[ProductTitle, (Price, Count)]
  private type ProductTitle = String
  private type Price = Float
  private type Count = Int
  private val EmptyEntries: Entries = Map.empty

  def newCart: UIO[ShoppingCart] =
    Ref.make(EmptyEntries).map(new ShoppingCart(_))

  case class ProductInfo(title: String, price: Float)

  class ShoppingCart(private val data: Ref[Entries]):
    /** Add line item by product title, looking up price. */
    def addLineItem(title: String, count: Int): Task[Unit] =
      for price <- priceLookup(title)
        _ <- addLineItem(ProductInfo(title, price), count)
      yield ()

    /** Add line item by ProductInfo. */
    def addLineItem(product: ProductInfo, addCount: Int): Task[Unit] =
      data.modify { entries =>
        entries.get(product.title) match
          case None =>
            (None, entries.updated(product.title, (product.price, addCount)))
          case Some((existingPrice, existingCount)) =>
            if product.price == existingPrice then
              (None, entries.updated(product.title, (existingPrice, existingCount + addCount)))
            else
              (Some(new RuntimeException(s"Unexpected error occurred for product ${product.title}")), entries)
      }.flatMap {
        case None    => ZIO.unit
        case Some(e) => ZIO.fail(e)
      }

    /** Subtotal, rounded. */
    def subtotal: UIO[BigDecimal] =
      data.get.map { entries =>
        val total = entries.foldLeft(BigDecimal(0)) { case (sum, (_, (price, count))) =>
          sum + BigDecimal(price) * count
        }
        total.setScale(DecimalScale, RoundingMode)
      }

    /** Tax payable, rounded. */
    def taxPayable: UIO[BigDecimal] =
      subtotal.map(st => (st * TaxRate).setScale(DecimalScale, RoundingMode))

    /** Total payable, rounded. */
    def totalPayable: UIO[BigDecimal] =
      for
        st <- subtotal
        tax    <- taxPayable
      yield (st + tax).setScale(DecimalScale, RoundingMode)

    /** Number of products in cart */
    def numLineItems: UIO[Int] =
      data.get.map(_.size)

    /** Number of items in cart */
    def numItems: UIO[Int] =
      data.get.map (entries => entries.values.map(_._2).sum)