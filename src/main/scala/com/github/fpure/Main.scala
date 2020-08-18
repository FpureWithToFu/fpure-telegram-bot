package com.github.fpure

import cats.Monad
import cats.effect.{ExitCode, IO, IOApp}
import com.github.fpure.config.Config
import tofu.common.Console
import tofu.syntax.console._
import tofu.syntax.monadic._

object Main extends IOApp {
  def run(args: List[String]): IO[ExitCode] = {
    def program[F[_]: Monad: Console]: F[ExitCode] = for {
      config <- Config.load[F]
      _ <- config.fold(
                    errs => errs.foreach(err => puts"$err"),
                    conf => println(conf)
                  ).pure[F]
      _ <- puts"Hello World!"
    } yield ExitCode.Success

    program[IO]
  }
}
