package com.siriusxm.example.cart

import com.siriusxm.example.cart.ShoppingCart.ProductInfo
import com.typesafe.scalalogging.Logger
import sttp.client4.UriContext
import sttp.client4.httpclient.zio.HttpClientZioBackend
import sttp.client4.quick.*
import sttp.client4.ziojson.*
import zio.*
import zio.json.{DeriveJsonDecoder, JsonDecoder}

object CerealProductInfo:
  private val baseUrl = "https://raw.githubusercontent.com/mattjanks16/shopping-cart-test-data/main"
//  https: //raw.githubusercontent.com/mattjanks16/shopping-cart-test-data/main/cheerios.json
//  https: //raw.githubusercontent.com/mattjanks16/shopping-cart-test-data/main/cornflakes.js
//  https: //raw.githubusercontent.com/mattjanks16/shopping-cart-test-data/main/frosties.json
//  https: //raw.githubusercontent.com/mattjanks16/shopping-cart-test-data/main/shreddies.jso
//  https: //raw.githubusercontent.com/mattjanks16/shopping-cart-test-data/main/weetabix.json

  private val logger = Logger[CerealProductInfo.type]

  given JsonDecoder[ProductInfo] = DeriveJsonDecoder.gen[ProductInfo]

  private val req = basicRequest

  def priceLookup(productTitle: String): Task[Float] =
      for backend <- HttpClientZioBackend()
          resp <- req.get(uri"$baseUrl/${productTitle.toLowerCase}.json")
          .response(asJson[ProductInfo])
          .send(backend)
        ret <- ZIO.fromEither(resp.body.map(_.price))
      yield ret
