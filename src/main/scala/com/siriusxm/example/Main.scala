package com.siriusxm.example

import com.siriusxm.example.cart.CerealProductInfo
import zio.{Console, ZIO, ZIOAppDefault}

object Main extends ZIOAppDefault {

  val validProducts = Set("cheerios", "cornflakes", "frosties", "shreddies", "weetabix", "fake_brand")

  override def run: ZIO[Any, Throwable, Unit] = {
    getProductInfo(validProducts).flatMap { resultString =>
      Console.printLine(resultString)
    }
  }

  private def getProductInfo(validProducts: Set[String]): ZIO[Any, Throwable, String] = {
    ZIO.foreach(validProducts) { product =>
      CerealProductInfo.priceLookup(product).map { price =>
        s"Product: $product, price=$price"
      }
    }.map(_.mkString("\n"))
  }
}