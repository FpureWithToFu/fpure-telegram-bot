package com.github.fpure.core.http.client

import derevo.derive
import tofu.data.derived.ContextEmbed
import tofu.higherKind.derived.representableK

@derive(representableK)
trait HttpClient[F[_]] {
  def send(request: String): F[String]
}

object HttpClient extends ContextEmbed[HttpClient] {
  def apply[F[_]](implicit inst: HttpClient[F]): HttpClient[F] = inst
}
