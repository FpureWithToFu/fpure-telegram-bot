package com.github.fpure

import cats.Monad
import cats.effect.{ExitCode, IO, IOApp}
import cats.syntax.show._
import com.github.fpure.config.Config
import tofu.common.Console
import tofu.syntax.console._
import tofu.syntax.monadic._

object Example extends IOApp {
  def run(args: List[String]): I[ExitCode] = {
    program[IO]
  }

  // Simple test no ToFu DI Context`s
  def program[F[_]: Monad: Console]: F[ExitCode] = for {
    config <- Config.load[F]
    _ <- config.fold(
      errs => errs.foreach(err => println(err.show)),
      conf => println(conf)
    ).pure[F]
    _ <- puts"Hello World!"
  } yield ExitCode.Success
}
