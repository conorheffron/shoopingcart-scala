package com.siriusxm.example.service

import com.siriusxm.example.dto.ProductInfo
import com.siriusxm.example.service.ProductPriceService.findPriceByProductTitle
import zio.{Ref, Task, UIO}

import java.math.RoundingMode
import scala.math.BigDecimal

object CartService extends CartServiceI:

  /** Add line item by product title & quantity, findPriceByProductTitle provides price value if it exists. */
  def addLineItem(data: Ref[Entries], title: String, quantity: Int): Task[Unit] =
    for price <- findPriceByProductTitle(title)
        _ <- addLineItem(data, ProductInfo(title, price), quantity)
    yield ()

  /** Add line item by ProductInfo[title, price] & addQuantity */
  def addLineItem(data: Ref[Entries], product: ProductInfo, addQuantity: Int): Task[Unit] = {
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