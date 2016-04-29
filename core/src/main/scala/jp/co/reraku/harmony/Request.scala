package jp.co.reraku.harmony

import scala.util.{ Try, Success }

trait Request[R <: Request[R]] { self: R =>
  def always(): Try[R] = Success(this)
}

trait Command[C <: Command[C]] extends Request[C] { self: C => }
trait Query[Q <: Query[Q]] extends Request[Q] { self: Q => }
