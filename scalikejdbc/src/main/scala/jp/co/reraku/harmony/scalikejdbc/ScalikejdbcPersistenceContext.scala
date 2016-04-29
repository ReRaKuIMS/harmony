package jp.co.reraku.harmony.scalikejdbc

import jp.co.reraku.harmony.PersistenceContext

import scalikejdbc.{ DB, DBSession }

class ScalikejdbcPersistenceContext() extends PersistenceContext[DBSession] {
  def read[A](process: DBSession => A): A = DB.readOnly(process)

  def write[A](process: DBSession => A): A = DB.localTx(process)
}
