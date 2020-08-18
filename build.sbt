import Dependencies._

name := "fpure-telegram-bot"
version := "0.0.1"
scalaVersion := "2.13.3"

libraryDependencies ++= Seq(
  catsCore,
  catsEffect,
  catsTaglessCore,
  catsTaglessMacros,
  circeCore,
  circeGeneric,
  circeParser,
  derevoCats,
  derevoCatsTagless,
  tofuCore,
  tofuConcurrent,
  tofuDerivation,
  tofuOpticsCore,
  tofuOpticsMacro,
  trace4catsCore,
  trace4catsInject,
  trace4catsJaeger,
  http4sCore,
  http4sDsl,
  http4sCirce,
  http4sClient,
  http4sServer,
  http4sBlazeClient,
  http4sBlazeServer,
  logback,
  compilerPlugin(kindProjector),
  compilerPlugin(betterMonadicFor)
)

scalacOptions ++= Seq(
  "-Ymacro-annotations"
)
