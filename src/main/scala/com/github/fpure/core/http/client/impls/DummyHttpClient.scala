package com.github.fpure.core.http.client.impls

import cats.Monad
import cats.effect.Resource
import com.github.fpure.config.Config.HttpClientConfig
import com.github.fpure.core.http.client.HttpClient
import tofu.HasContext
import tofu.logging.Logging
import tofu.syntax.context._
import tofu.syntax.logging._
import tofu.syntax.monadic._

class DummyHttpClient[F[_]: Monad: Logging: *[_] HasContext HttpClientConfig] extends HttpClient[F] {
  def send(request: String): F[String] = for {
    conf <- context[F]
    _ <- debug"HttpConfig: $conf"
    msg = s"Send Request: $request to URL: ${conf.url}"
    _ <- debug"$msg"
  } yield msg
}

object DummyHttpClient {
  def make[I[_]: Monad, F[_]: Monad: Logging: *[_] HasContext HttpClientConfig]: Resource[I, HttpClient[F]] = {
    new DummyHttpClient[F].pure[Resource[I, *]]
  }
}
