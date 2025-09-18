package com.siriusxm.example

import zio.{ZIOAppDefault, Console, Clock}

object Main extends ZIOAppDefault {
  override def run = Clock.currentDateTime
    .flatMap(dt => Console.printLine("Now is: " + dt))
}