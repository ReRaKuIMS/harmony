package jp.co.reraku.harmony

trait PersistenceContext[X] {
  def read[A](process: X => A): A

  def write[A](process: X => A): A
}
