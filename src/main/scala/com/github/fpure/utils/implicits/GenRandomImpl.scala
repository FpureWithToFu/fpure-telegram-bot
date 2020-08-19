package com.github.fpure.utils.implicits

import cats.Monad
import tofu.HasContext
import tofu.generate.GenRandom
import tofu.higherKind.Embed

object GenRandomImpl {
  implicit def contextEmbedGenRandom[F[_]: Monad](
    implicit FGR: F HasContext GenRandom[F], EGR: Embed[GenRandom]
  ): GenRandom[F] = EGR.embed(FGR.context)
}
