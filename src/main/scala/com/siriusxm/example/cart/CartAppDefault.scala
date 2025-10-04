package com.siriusxm.example.cart

import com.siriusxm.example.service.ProductPriceService
import zio.{Console, ZIO, ZIOAppDefault}

object CartAppDefault extends ZIOAppDefault {

  private final val validProducts = Set("cheerios", "cornflakes", "frosties", "shreddies", "weetabix", "fake_brand")

  override def run: ZIO[Any, Throwable, Unit] = {
    getProductInfo(validProducts).flatMap { resultString =>
      ZIO.logInfo(s"The result of CartAppDefault.getProductInfo is ${resultString}")
    }
  }

  private def getProductInfo(validProducts: Set[String]): ZIO[Any, Throwable, String] = {
    ZIO.foreach(validProducts) { product =>
      ProductPriceService.priceLookup(product).map { price =>
        s"Product: ${product}, Price=${price}"
      }
    }.map(_.mkString(" | "))
  }
}