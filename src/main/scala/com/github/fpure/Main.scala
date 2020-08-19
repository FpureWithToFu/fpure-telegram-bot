package com.github.fpure

import cats.Monad
import cats.effect.{ExitCode, IO, IOApp, Resource, Timer}
import cats.tagless.syntax.functorK._
import cats.instances.list._
import cats.syntax.parallel._
import cats.syntax.show._
import com.github.fpure.config.Config
import com.github.fpure.core.http.client.HttpClient
import com.github.fpure.core.http.client.impls.DummyHttpClient
import com.github.fpure.core.http.server.HttpServer
import com.github.fpure.core.http.server.impls.DummyHttpServer
import com.github.fpure.env.CtxEnv
import tofu.common.Console
import tofu.concurrent.ContextT
import tofu.generate.GenRandom
import tofu.logging.{Logging, Logs}
import tofu.syntax.console._
import tofu.syntax.monadic._
import tofu.syntax.funk.funK

object Main extends IOApp with CtxEnv.OpticsDerive {
  type I[+A] = IO[A]
  type Init[+A] = Resource[IO, A]
  type Eff[+A] = ContextT[IO, CtxEnv, A]

  def run(args: List[String]): IO[ExitCode] = {
    val res: Resource[IO, (HttpClient[IO], HttpServer[IO])] = for {
      env <- initEnv
      implicit0(logger: Logging[Eff]) = env.logger
      implicit0(_timer: Timer[Eff]) = env.timer
      implicit0(genRandom: GenRandom[Eff]) = env.genRandom
      client <- DummyHttpClient.make[I, Eff]
      server <- DummyHttpServer.make[I, Eff]
    } yield (client.mapK(funK(_.run(env))), server.mapK(funK(_.run(env))))

    // Check if server works
    res.map(_._2).use(_.serve).as(ExitCode.Success)

    // Check if client works
    /*
    res.map(_._1).use(client =>
      List("Serge", "Symon", "Cat", "Dog", "Scala").parTraverse_(str => client.send(s"Hello, $str!"))
    ).as(ExitCode.Success)
    */
  }

  def initEnv: Init[CtxEnv[Eff]] = for {
    config <- Resource.liftF(Config.load[I].map(_.getOrElse(throw new Exception("Cannot parse Config"))))
    logger <- Resource.liftF(Logs.sync[I, Eff].byName("Logger"))
    _timer = Timer[IO].mapK(ContextT.liftF[IO, CtxEnv])
    genRandom <- Resource.liftF(GenRandom.instance[I, Eff]())
  } yield CtxEnv(
    config = config,
    logger = logger,
    timer = _timer,
    genRandom = genRandom
  )

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
