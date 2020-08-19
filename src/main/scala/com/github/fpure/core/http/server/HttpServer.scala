package com.github.fpure.core.http.server

import derevo.derive
import tofu.data.derived.ContextEmbed
import tofu.higherKind.derived.representableK

@derive(representableK)
trait HttpServer[F[_]] {
  def serve: F[Unit]
}

object HttpServer extends ContextEmbed[HttpServer] {
  def apply[F[_]](implicit inst: HttpServer[F]): HttpServer[F] = inst
}
