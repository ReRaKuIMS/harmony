package jp.co.reraku.harmony

trait PersistenceContext[S] {
  def read[A](process: S => A): A

  def write[A](process: S => A): A
}
