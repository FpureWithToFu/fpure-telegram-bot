package com.github.fpure

import cats.effect.{ExitCode, IO, IOApp, Resource, Timer}
import cats.tagless.syntax.functorK.toFunctorKOps
import com.github.fpure.config.Config
import com.github.fpure.core.http.server.HttpServer
import com.github.fpure.core.http.server.impls.DummyHttpServer
import com.github.fpure.env.CtxEnv
import tofu.concurrent.ContextT
import tofu.generate.GenRandom
import tofu.logging.{Logging, Logs}
import tofu.syntax.funk.funK

object HttpServerRunner extends IOApp with CtxEnv.OpticsDerive {
  def run(args: List[String]): IO[ExitCode] = {
    val res: Resource[IO, HttpServer[IO]] = for {
      env <- initEnv
      implicit0(logger: Logging[Eff]) = env.logger
      implicit0(_timer: Timer[Eff]) = env.timer
      implicit0(genRandom: GenRandom[Eff]) = env.genRandom
      server <- DummyHttpServer.make[I, Eff]
    } yield server.mapK(funK(_.run(env)))

    // Check if server works
    res.use(_.serve).as(ExitCode.Success)
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
}
