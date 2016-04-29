package jp.co.reraku.harmony

trait Outbound[R <: Request[R], P, E] {
  def apply(request: R, product: P): E

  def recover(request: R, exception: Exception): E
}

class OutboundException(message: String) extends Exception(message)
