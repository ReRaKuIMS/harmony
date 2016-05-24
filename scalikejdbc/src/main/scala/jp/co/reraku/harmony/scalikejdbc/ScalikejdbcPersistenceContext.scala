package jp.co.reraku.harmony.scalikejdbc

import jp.co.reraku.harmony.RepositoryContextProvider

import scalikejdbc.{ DB, DBSession }

class ScalikejdbcRepositoryContextProvider() extends RepositoryContextProvider[DBSession] {
  def read[A](process: DBSession => A): A = DB.readOnly(process)

  def write[A](process: DBSession => A): A = DB.localTx(process)
}
