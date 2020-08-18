package com.github.fpure.config

import cats.Monad
import derevo.derive
import com.github.fpure.config.Config.HttpConfig
import com.typesafe.config.ConfigFactory
import tofu.config.{Configurable, MessageList}
import tofu.optics.macros.ClassyOptics
import tofu.config.typesafe._
import tofu.syntax.monadic._

@ClassyOptics
@derive(Configurable)
case class Config(http: HttpConfig)

object Config {
  def load[I[_]: Monad]: I[Either[MessageList, Config]] =
    syncParseConfig[Config](ConfigFactory.defaultApplication()).pure[I]

  case class HttpConfig(client: HttpClientConfig, server: HttpServerConfig)
  case class HttpClientConfig(url: String)
  case class HttpServerConfig(port: Int, host: String)
}
