package com.siriusxm.example.cart

import com.siriusxm.example.service.ProductPriceService
import zio.{Console, ZIO, ZIOAppDefault}

object CartAppDefault extends ZIOAppDefault {

  private final val validProducts = Set("cheerios", "cornflakes", "frosties", "shreddies", "weetabix", "fake_brand")

  /* Main process for app run -> logs valid product information
  including one missing JSON object / file ('fake_brand'). */
  override def run: ZIO[Any, Throwable, Unit] = {
    getProductInfo(validProducts).flatMap { resultString =>
      ZIO.logInfo(s"The result of CartAppDefault.getProductInfo is $resultString")
    }
  }

  /* getProductInfo calls findPriceByProductTitle for each product title 
  & returns formatted string including all results */
  private def getProductInfo(validProducts: Set[String]): ZIO[Any, Throwable, String] = {
    ZIO.foreach(validProducts) { product =>
      ProductPriceService.findPriceByProductTitle(product).map { price =>
        s"Product: $product, Price=$price"
      }
    }.map(_.mkString(" | "))
  }
}