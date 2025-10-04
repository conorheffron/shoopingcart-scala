package com.siriusxm.example

import com.siriusxm.example.service.CerealService
import zio.{Console, ZIO, ZIOAppDefault}

object Main extends ZIOAppDefault {

  val validProducts = Set("cheerios", "cornflakes", "frosties", "shreddies", "weetabix", "fake_brand")

  override def run: ZIO[Any, Throwable, Unit] = {
    getProductInfo(validProducts).flatMap { resultString =>
      ZIO.logInfo(resultString)
    }
  }

  private def getProductInfo(validProducts: Set[String]): ZIO[Any, Throwable, String] = {
    ZIO.foreach(validProducts) { product =>
      CerealService.priceLookup(product).map { price =>
        s"Product: ${product}, Price=${price}"
      }
    }.map(_.mkString("\n"))
  }
}