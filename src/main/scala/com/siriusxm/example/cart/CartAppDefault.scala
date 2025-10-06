package com.siriusxm.example.cart

import com.siriusxm.example.service.ProductPriceService
import zio.{Unsafe, ZIO, ZIOAppDefault}

object CartAppDefault extends ZIOAppDefault {

  private final val defaultProducts = Set("cheerios", "cornflakes", "frosties", "shreddies", "weetabix", "fake_brand")

  /* Main process for app run -> logs valid product information
  including one missing JSON object / file ('fake_brand'). */
  override def run: ZIO[Any, Throwable, Unit] = {
    getProductInfoStr(defaultProducts).flatMap { resultStr =>
      ZIO.logInfo(s"The result of CartAppDefault.getProductInfoStr is $resultStr")
    }
  }

  def run(products: Set[String]): Map[String, Float] = {
    getProductInfoMap(products)
  }

  /* getProductInfo calls findPriceByProductTitle for each product title
  & returns formatted string including all results */
  private def getProductInfoStr(products: Set[String]): ZIO[Any, Throwable, String] = {
    ZIO.foreach(products) { product =>
      ProductPriceService.findPriceByProductTitle(product).map { price =>
        s"Product: $product, Price=$price"
      }
    }.map(_.mkString(" | "))
  }

  private def getProductInfoMap(products: Set[String]): Map[String, Float] = {
    val myEffect: ZIO[Any, Throwable, Map[String, Float]] = ZIO.foreach(products) { product =>
      ProductPriceService.findPriceByProductTitle(product).map { price =>
        product -> price // Create a key-value pair (product -> price)
      }
    }.map(_.toMap) // Convert the result to Map of key-value pairs
    val responseJson: Map[String, Float] = Unsafe.unsafe { implicit unsafe =>
      runtime.unsafe.run(myEffect).getOrThrowFiberFailure() // Extracts the Map or throws an exception
    }
    responseJson
  }
}