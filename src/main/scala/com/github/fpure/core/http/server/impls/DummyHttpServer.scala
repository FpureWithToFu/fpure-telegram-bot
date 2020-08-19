package com.github.fpure.core.http.server.impls

import cats.Monad
import cats.effect.{Resource, Timer}
import cats.instances.int._
import com.github.fpure.config.Config.HttpServerConfig
import com.github.fpure.core.http.server.HttpServer
import tofu.{BracketThrow, HasContext}
import tofu.common.Console
import tofu.generate.GenRandom
import tofu.logging.Logging
import tofu.syntax.console._
import tofu.syntax.context._
import tofu.syntax.logging._
import tofu.syntax.monadic._

import scala.concurrent.duration._

class DummyHttpServer[F[_]: Monad: Logging: Console: BracketThrow: Timer: GenRandom: *[_] HasContext HttpServerConfig] extends HttpServer[F] {
  def serve: F[Unit] = config >> (Timer[F].sleep(10.second) >> loop).foreverM[Unit]

  def config: F[Unit] = for {
    conf <- context[F]
    _ <- debug"HttpConfig: $conf"
  } yield ()

  def loop: F[Unit] = for {
    rnd <- GenRandom.nextInt(1000)
    _ <- puts"Server is alive with $rnd"
  } yield ()
}

object DummyHttpServer {
  def make[I[_]: Monad, F[_]: Monad: Logging: Console: BracketThrow: Timer: GenRandom: *[_] HasContext HttpServerConfig]: Resource[I, HttpServer[F]] = {
    new DummyHttpServer[F].pure[Resource[I, *]]
  }
}
