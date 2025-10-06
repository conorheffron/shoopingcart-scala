package com.siriusxm.example.cart

import com.siriusxm.example.service.ProductPriceService
import zio.{Unsafe, ZIO, ZIOAppDefault}

object CartAppDefault extends ZIOAppDefault {

  private final val defaultProducts = Set("cheerios",
    "cornflakes",
    "frosties",
    "shreddies",
    "weetabix",
    "flahavans",
    "fake_brand")

  /* Default application process -> logs default product information
  including one missing JSON object ('fake_brand') and one malformed JSON object ('flahavans'). */
  override def run: ZIO[Any, Throwable, String] = {
    val defaultRunOutput = getProductInfoMap(defaultProducts).mkString(" | ")
    ZIO.logInfo(s"Default run products=$defaultProducts and output=$defaultRunOutput")
    ZIO.succeed(defaultRunOutput)
  }

  /* Run function for GET REST API call by product titles query parameter */
  def run(products: Set[String]): Map[String, Float] = {
    getProductInfoMap(products)
  }

  /* getProductInfoMap calls findPriceByProductTitle for each product title
    & returns Map<ProductTitle, Price> including all results */
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