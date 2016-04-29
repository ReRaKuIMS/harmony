package jp.co.reraku.harmony

trait Repository[ID <: Identifier[_], E <: Entity[ID], S, M[+_]] {
  def resolve(id: ID)(implicit context: PersistenceContext[S]): M[E]

  def store(entity: E)(implicit context: PersistenceContext[S]): M[E]
}

class RepositoryException(message: String) extends Exception(message)

class EntityNotFoundException(message: String) extends RepositoryException(message)
