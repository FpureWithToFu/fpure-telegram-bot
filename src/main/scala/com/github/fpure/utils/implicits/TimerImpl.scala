package com.github.fpure.utils.implicits

import cats.Monad
import cats.effect.{Clock, Timer}
import tofu.HasContext
import tofu.higherKind.Embed
import tofu.higherKind.derived.genEmbed

object TimerImpl {
  implicit val clockEmbed: Embed[Clock] = genEmbed[Clock]
  implicit val timerEmbed: Embed[Timer] = genEmbed[Timer]

  implicit def contextEmbedClock[F[_]: Monad](
    implicit FC: F HasContext Clock[F], EC: Embed[Clock]
  ): Clock[F] = EC.embed(FC.context)

  implicit def contextEmbedTimer[F[_]: Monad](
    implicit FT: F HasContext Timer[F], ET: Embed[Timer]
  ): Timer[F] = ET.embed(FT.context)
}
