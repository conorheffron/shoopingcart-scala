package com.siriusxm.example.service

import zio.{Ref, UIO}

trait CartServiceI {
  type Entries = Map[ProductTitle, (Price, Count)]

  private type ProductTitle = String
  private type Price = Float
  private type Count = Int

  val EmptyEntries: Entries = Map.empty
}