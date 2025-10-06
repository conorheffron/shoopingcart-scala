package com.siriusxm.example

import com.siriusxm.example.cart.CartAppDefault
import sttp.tapir.*
import sttp.tapir.server.netty.sync.NettySyncServer
import zio.json.EncoderOps

@main def RestApiApplication(): Unit =
  
  val productsInfoEndpoint = endpoint
    .get
    .in("product" / "info")
    .in(query[String]("titles"))
    .out(stringJsonBody)
    .handleSuccess(titles => CartAppDefault.run(titles.split(",").map(_.trim).toSet).toJson)

  NettySyncServer()
    .port(8080)
    .addEndpoint(productsInfoEndpoint)
    .startAndWait()