package jp.co.reraku.harmony

trait Identifier[+A] extends Serializable {
  def value: A
}

object Identifier {
  def empty[A]: Identifier[A] =
    EmptyIdentifier

  def apply[A](value: A): Identifier[A] =
    new IdentifierImpl(value)

  def unapply[A](id: Identifier[A]): Option[A] =
    Some(id.value)
}

class EmptyIdentifier extends Identifier[Nothing] {
  def value = throw new EmptyIdentifierException("")

  override def equals(obj: Any) = obj match {
    case that: EmptyIdentifier => this eq that
    case _                     => false
  }

  override def hashCode = 31 * 1

  override def toString = "EmptyIdentifier"
}

class EmptyIdentifierException(message: String) extends Exception(message)

object EmptyIdentifier extends EmptyIdentifier

private[harmony] class IdentifierImpl[A](val value: A) extends Identifier[A] {
  override def equals(obj: Any) = obj match {
    case that: EmptyIdentifier => false
    case that: Identifier[_]   => value == that.value
    case _                     => false
  }

  override def hashCode = 31 * value.##

  override def toString = s"Identifier($value)"
}
