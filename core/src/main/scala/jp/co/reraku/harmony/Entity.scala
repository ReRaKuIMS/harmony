package jp.co.reraku.harmony

trait Entity[ID <: Identifier[_]] {
  val id: ID

  override final def equals(obj: Any) = obj match {
    case that: Entity[_] => id == that.id
    case _               => false
  }

  override final def hashCode = 31 * id.##
}
