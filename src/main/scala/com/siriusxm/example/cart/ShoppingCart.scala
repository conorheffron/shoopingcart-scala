package com.siriusxm.example.cart

import com.siriusxm.example.cart.Cart
import com.siriusxm.example.dto.ProductInfo
import com.siriusxm.example.service.CerealService.priceLookup
import zio.{Ref, Task, UIO, ZIO}

import scala.math.BigDecimal

object ShoppingCart extends Cart:

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
            (None, entries.updated(product.title, (existingPrice, existingCount + addCount)))
      }.flatMap {
        case None => ZIO.unit
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
        tax <- taxPayable
      yield (st + tax).setScale(DecimalScale, RoundingMode)

    /** Number of products in cart */
    def numLineItems: UIO[Int] =
      data.get.map(_.size)

    /** Number of items in cart */
    def numItems: UIO[Int] =
      data.get.map (entries => entries.values.map(_._2).sum)