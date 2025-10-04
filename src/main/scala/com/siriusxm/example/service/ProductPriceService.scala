package com.siriusxm.example.service

import com.siriusxm.example.dto.ProductInfo
import sttp.client4.httpclient.zio.HttpClientZioBackend
import sttp.client4.ziojson.asJson
import sttp.client4.{UriContext, basicRequest}
import zio.ZIO

object ProductPriceService:
  private val baseUrl = "https://raw.githubusercontent.com/mattjanks16/shopping-cart-test-data/main"
  private val req = basicRequest

  def priceLookup(productTitle: String): ZIO[Any, Throwable, Float] =
      if (productTitle == null || productTitle.isEmpty)
        ZIO.fail(new IllegalArgumentException("Input must be non-empty String"))
      else
        val request = basicRequest.get(uri"$baseUrl/${productTitle.toLowerCase}.json")
        // Use ZIO.scoped to manage the lifecycle of the HTTP client
        ZIO.scoped {
          for {
            httpClient <- HttpClientZioBackend.scoped() // Create the HTTP client
            response <- request.response(asJson[ProductInfo]).send(httpClient) // Send the request
            _ <- ZIO.logInfo(s"Request URL: ${request.uri}, Response Code: ${response.code}, Response Body: ${response.body}")
            price <- response.body match {
              case Right(productInfo) => ZIO.succeed(productInfo.price) // Extract price if successful
              case Left(error) =>
                ZIO.logError(s"Failed to parse response: $error") *> ZIO.succeed(0.0f) // Log error and default to 0.0
            }
          } yield price
        }