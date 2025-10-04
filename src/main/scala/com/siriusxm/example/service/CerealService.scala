package com.siriusxm.example.service

import com.siriusxm.example.dto.ProductInfo
import sttp.client4.httpclient.zio.HttpClientZioBackend
import sttp.client4.ziojson.asJson
import sttp.client4.{UriContext, basicRequest}
import zio.ZIO
import zio.json.{DeriveJsonDecoder, JsonDecoder}

object CerealService:
  private val baseUrl = "https://raw.githubusercontent.com/mattjanks16/shopping-cart-test-data/main"

  given JsonDecoder[ProductInfo] = DeriveJsonDecoder.gen[ProductInfo]

  private val req = basicRequest

  def priceLookup(productTitle: String): ZIO[Any, Throwable, Float] =
      if (productTitle == null || productTitle.isEmpty) {
        ZIO.fail(new IllegalArgumentException("Input must be non-empty String"))
      }  else {
        val request = basicRequest.get(uri"$baseUrl/${productTitle.toLowerCase}.json")
        for {
          backend <- HttpClientZioBackend() // Create the HTTP client
          response <- request.response(asJson[ProductInfo]).send(backend) // Send the request
          _ <- ZIO.logInfo(s"Response Code: ${response.code}, Response Body: ${response.body}")
          ret <- ZIO.fromEither(response.body.map(_.price)).orElse(ZIO.from(0.0f))// return price or default to 0.0
        } yield ret
      }