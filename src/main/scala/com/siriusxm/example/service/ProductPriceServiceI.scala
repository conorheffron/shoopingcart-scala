package com.siriusxm.example.service

import com.typesafe.config.ConfigFactory

trait ProductPriceServiceI {
  protected val productBaseUrl: String = ConfigFactory.load().getString("server.productBaseUrl")
}