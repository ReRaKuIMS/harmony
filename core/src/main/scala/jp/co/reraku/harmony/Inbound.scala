package jp.co.reraku.harmony

trait Inbound[E, R <: Request[R]] {
  def apply(externals: E): R
}

class InboundException(message: String) extends Exception(message)
