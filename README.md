# fpure-telegram-bot
**Simple Scala Telegram Bot inspired by ToFu DI tricks via Embed and ContextT**
````scala
import cats.effect.{IO, Resource}
import com.github.fpure.env.CtxEnv
import tofu.concurrent.ContextT

// Execution Effect
type I[+A] = IO[A]

// Initialisation Effect
type Init[+A] = Resource[IO, A]

// Effect Transformer, allowing to have context referring CtxEnv
type Eff[+A] = ContextT[IO, CtxEnv, A]
````