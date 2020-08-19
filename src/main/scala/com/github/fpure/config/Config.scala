package com.github.fpure.config

import cats.{Monad, Show}
import cats.syntax.show._
import derevo.derive
import com.github.fpure.config.Config.HttpConfig
import com.typesafe.config.ConfigFactory
import tofu.config.{Configurable, MessageList}
import tofu.optics.macros.{ClassyOptics, promote}
import tofu.config.typesafe._
import tofu.logging.Loggable
import tofu.syntax.monadic._

@ClassyOptics
@derive(Configurable)
case class Config(@promote http: HttpConfig)

object Config {
  def load[I[_]: Monad]: I[Either[MessageList, Config]] =
    syncParseConfig[Config](ConfigFactory.defaultApplication()).pure[I]

  @ClassyOptics
  case class HttpConfig(client: HttpClientConfig, server: HttpServerConfig)

  case class HttpClientConfig(url: String)
  case class HttpServerConfig(port: Int, host: String)

  implicit val HttpServerConfigShow: Show[HttpServerConfig] =
    server => s"HttpServerConfig(port=${server.port}, host=${server.host})"
  implicit val HttpServerConfigLogging: Loggable[HttpServerConfig] = Loggable.show

  implicit val httpClientConfigShow: Show[HttpClientConfig] =
    client => s"HttpClientConfig(url=${client.url})"
  implicit val httpClientConfigLogging: Loggable[HttpClientConfig] = Loggable.show

  implicit val httpConfigShow: Show[HttpConfig] =
    httpConf => s"HttpConfig(client=${httpConf.client.show}, server=${httpConf.server.show})"
  implicit val httpConfigLogging: Loggable[HttpConfig] = Loggable.show

  implicit val configShow: Show[Config] =
    conf => s"Config(http=${conf.http.show})"
  implicit val configLogging: Loggable[Config] = Loggable.show
}
