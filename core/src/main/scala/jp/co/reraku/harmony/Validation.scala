package jp.co.reraku.harmony

class ValidationException(message: String) extends Exception(message)

object ValidationException {
  def fromViolations(violations: Seq[Violation]): ValidationException = {
    val message = violations match {
      case Seq()                 => "no violations"
      case Seq(first)            => first.message
      case Seq(first, second)    => s"${first.message}, otherwise 1 violation"
      case Seq(first, rest @ _*) => s"${first.message}, otherwise ${rest.length} violations"
    }

    new ValidationException(message)
  }
}

trait Validator[V] {
  def apply(value: V): Seq[Violation]
}

object Validator {
  def apply[V](validate: V => Option[Violation]): Validator[V] =
    new Validator[V] {
      override def apply(value: V): Seq[Violation] = validate(value).toSeq
    }

  def seq[V](validate: V => Seq[Violation]): Validator[V] =
    new Validator[V] {
      override def apply(value: V): Seq[Violation] = validate(value)
    }
}

object PositiveIntValidator extends Validator[Int] {
  override def apply(value: Int): Seq[Violation] = {
    if (value < 0) {
      Seq(Negative("an integer should not be negative"))
    }
    else if (value == 0) {
      Seq(Zero("an integer should not be zero"))
    }
    else {
      Seq.empty
    }
  }
}

object PositiveDoubleValidator extends Validator[Double] {
  override def apply(value: Double): Seq[Violation] = {
    if (value < 0) {
      Seq(Negative("a real number should not be negative"))
    }
    else if (value == 0) {
      Seq(Zero("a real number should not be zero"))
    }
    else {
      Seq.empty
    }
  }
}

trait Violation {
  val message: String
}

// ID violation

case class WrongID(message: String) extends Violation

// presence violation

case class Required(message: String) extends Violation
case class NotRequired(message: String) extends Violation

// length violation

case class WrongLength(message: String) extends Violation
case class LengthTooLong(message: String) extends Violation
case class LengthTooShort(message: String) extends Violation

// numeric violation

case class Zero(message: String) extends Violation
case class Negative(message: String) extends Violation

// date time violation

case class DateTimeInconsistency(message: String) extends Violation

// format violation

case class Malformed(message: String) extends Violation

// binary violation

case class BadContentType(message: String) extends Violation
case class SizeExceeded(message: String) extends Violation
