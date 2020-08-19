package com.github

import cats.effect.{IO, Resource}
import com.github.fpure.env.CtxEnv
import tofu.concurrent.ContextT

package object fpure {
  type I[+A] = IO[A]
  type Init[+A] = Resource[IO, A]
  type Eff[+A] = ContextT[IO, CtxEnv, A]
}
