package com.github.fpure.env

import cats.effect.Timer
import com.github.fpure.config.Config
import tofu.WithLocal
import tofu.generate.GenRandom
import tofu.logging.Logging
import tofu.optics.Contains
import tofu.optics.macros.{ClassyOptics, promote}

@ClassyOptics
case class CtxEnv[F[_]](
                         @promote config: Config,
                         logger: Logging[F],
                         timer: Timer[F],
                         genRandom: GenRandom[F]
                       )

object CtxEnv {
  trait OpticsDerive {
    implicit def ctxEnvSubContext[F[_], C](
      implicit ctx: CtxEnv[F] Contains C, wl: WithLocal[F, CtxEnv[F]]
    ): F WithLocal C = {
      WithLocal[F, CtxEnv[F]].subcontext(ctx)
    }
  }
}
