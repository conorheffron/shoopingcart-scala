package com.siriusxm.example.service

import com.siriusxm.example.dto.ProductInfo
import sttp.client4.httpclient.zio.HttpClientZioBackend
import sttp.client4.ziojson.asJson
import sttp.client4.{Request, UriContext, basicRequest}
import sttp.model.StatusCode
import zio.ZIO

object ProductPriceService:
  private val baseUrl = "https://conorheffron.github.io/shopping-cart-test-data/"

  def findPriceByProductTitle(productTitle: String): ZIO[Any, Throwable, Float] =
      if (productTitle == null || productTitle.isEmpty)
        ZIO.fail(new IllegalArgumentException("Input must be non-empty String"))
      else
        val request = basicRequest.get(uri"$baseUrl/${productTitle.toLowerCase}.json")
        findPriceByProductTitle(request)

  /** Use ZIO.scoped to manage the lifecycle of the HTTP client */
  def findPriceByProductTitle(request: Request[Either[String, String]]): ZIO[Any, Throwable, Float] =
    ZIO.scoped {
      for {
        httpClient <- HttpClientZioBackend.scoped() // Open HTTP client connection
        response <- request.response(asJson[ProductInfo]).send(httpClient) // Send the request
        price <- response.code match {
          case StatusCode.Ok =>
            response.body match {
              case Right(productInfo) =>
                ZIO.logInfo(s"Request URL: ${request.uri}, " +
                  s"Response Code: ${response.code}, " +
                  s"Response Body: ${response.body}")
                  *> ZIO.succeed(productInfo.price) // Extract price
              case Left(error) =>
                ZIO.logError(s"Request URL: ${request.uri}, " +
                  s"Response Code: ${response.code}, " +
                  s"Failed to find price: $error")
                  *> ZIO.succeed(0f) // Log error & return price as 0.0f
            }
          case StatusCode.NotFound =>
            ZIO.logError(s"Request URL: ${request.uri}, " +
              s"Response Code: ${response.code}, Resource Not Found")
              *> ZIO.succeed(0f) // Log not found 404 error & return 0.0f
        }
      } yield price
    }