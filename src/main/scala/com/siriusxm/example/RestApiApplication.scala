package com.siriusxm.example

import com.siriusxm.example.cart.CartAppDefault
import sttp.tapir.*
import sttp.tapir.server.netty.sync.NettySyncServer
import zio.json.EncoderOps
import com.typesafe.config.ConfigFactory

@main def restApiApplication(): Unit =
  val productsInfoEndpoint = endpoint
    .get
    .in("product" / "info")
    .in(query[String]("titles"))
    .out(stringJsonBody)
    .handleSuccess(titles => CartAppDefault.run(titles.split(",").map(_.trim).toSet).toJson)

  NettySyncServer()
    .port(ConfigFactory.load().getInt("server.port"))
    .addEndpoint(productsInfoEndpoint)
    .startAndWait()