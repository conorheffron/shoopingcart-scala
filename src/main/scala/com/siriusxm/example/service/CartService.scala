package com.siriusxm.example.service

import com.siriusxm.example.dto.ProductInfo
import com.siriusxm.example.service.ProductPriceService.findPriceByProductTitle
import zio.{Ref, Task, UIO}

import java.math.RoundingMode
import scala.math.BigDecimal

object CartService extends CartI:

  class ShoppingCart(private val data: Ref[Entries]):
    /** Add line item by product title, looking up price. */
    def addLineItem(title: String, count: Int): Task[Unit] =
      for price <- findPriceByProductTitle(title)
          _ <- addLineItem(ProductInfo(title, price), count)
      yield ()

    /** Add line item by ProductInfo. */
    def addLineItem(product: ProductInfo, addCount: Int): Task[Unit] = {
      data.modify { entries =>
        // Check if the product already exists in the entries
        entries.get(product.title) match {
          case None =>
            // If the product doesn't exist, add it with the given count
            (None, entries.updated(product.title, (product.price, addCount)))
          case Some((existingPrice, existingCount)) =>
            // If the product exists, update the count by adding the new count
            (Some, entries.updated(product.title, (existingPrice, existingCount + addCount)))
        }
      }.as((): Unit)
    }

    /** Subtotal, rounded. */
    def subtotal: UIO[BigDecimal] =
      data.get.map { entries =>
        entries
          .foldLeft(BigDecimal(0)) { case (sum, (_, (price, count))) =>
            sum + BigDecimal(price) * count
          }
          .setScale(DecimalScale, RoundingMode)
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
      data.get.map { entries =>
        entries.values.map { case (_, count) => count }.sum
      }