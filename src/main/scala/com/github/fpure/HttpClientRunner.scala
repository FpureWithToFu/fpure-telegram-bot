package com.github.fpure

import cats.effect.{ExitCode, IO, IOApp, Resource, Timer}
import cats.instances.list._
import cats.syntax.parallel._
import cats.tagless.syntax.functorK.toFunctorKOps
import com.github.fpure.config.Config
import com.github.fpure.core.http.client.HttpClient
import com.github.fpure.core.http.client.impls.DummyHttpClient
import com.github.fpure.env.CtxEnv
import tofu.concurrent.ContextT
import tofu.generate.GenRandom
import tofu.logging.{Logging, Logs}
import tofu.syntax.funk.funK

object HttpClientRunner extends IOApp with CtxEnv.OpticsDerive {
  def run(args: List[String]): IO[ExitCode] = {
    val res: Resource[IO, HttpClient[IO]] = for {
      env <- initEnv
      implicit0(logger: Logging[Eff]) = env.logger
      client <- DummyHttpClient.make[I, Eff]
    } yield client.mapK(funK(_.run(env)))

    // Check if client works
    res.use(client =>
      List("Serge", "Symon", "Cat", "Dog", "Scala").parTraverse_(str => client.send(s"Hello, $str!"))
    ).as(ExitCode.Success)
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
