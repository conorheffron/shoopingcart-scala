package com.siriusxm.example.dto

import zio.json.{DeriveJsonDecoder, JsonDecoder}

case class ProductInfo(title: String, price: Float)

object ProductInfo {
  // given is scala 3 feature for implicit val or given instance (JSON mapper)
  given decoder: JsonDecoder[ProductInfo] = DeriveJsonDecoder.gen[ProductInfo]
}