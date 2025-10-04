package com.siriusxm.example.service

import com.siriusxm.example.dto.ProductInfo
import com.siriusxm.example.service.ProductPriceService.findPriceByProductTitle
import zio.{Ref, Task, UIO}

import java.math.RoundingMode
import scala.math.BigDecimal

object CartService extends CartServiceI:

  class ShoppingCart(private val data: Ref[Entries]):
    /** Add line item by product title & quantity, findPriceByProductTitle provides price value if it exists. */
    def addLineItem(title: String, quantity: Int): Task[Unit] =
      for price <- findPriceByProductTitle(title)
          _ <- addLineItem(ProductInfo(title, price), quantity)
      yield ()
  
    /** Add line item by ProductInfo[title, price] & addQuantity */
    def addLineItem(product: ProductInfo, addQuantity: Int): Task[Unit] = {
      data.modify { entries =>
        // Check if the product already exists in the entries
        entries.get(product.title) match {
          case None =>
            // If the product doesn't exist, add it with the given count
            (None, entries.updated(product.title, (product.price, addQuantity)))
          case Some((existingPrice, existingCount)) =>
            // If the product exists, update the count by adding the new count
            (Some, entries.updated(product.title, (existingPrice, existingCount + addQuantity)))
        }
      }
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
      subtotal.map(st => (st * TaxRate).setScale(DecimalScale, RoundingMode))
  
    /** Calculate Total payable, rounded up */
    def totalPayable: UIO[BigDecimal] =
      for
        st <- subtotal
        tax <- taxPayable
      yield (st + tax).setScale(DecimalScale, RoundingMode)
  
    /** Calculate Number of products in cart */
    def numLineItems: UIO[Int] =
      data.get.map(_.size)
  
    /** Calculate Number of items (total quantity) in cart */
    def numItems: UIO[Int] =
      data.get.map { entries =>
        entries.values.map { case (_, count) => count }.sum
      }