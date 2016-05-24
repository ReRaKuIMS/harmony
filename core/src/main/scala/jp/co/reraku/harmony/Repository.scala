package jp.co.reraku.harmony

trait Repository[ID <: Identifier[_], E <: Entity[ID], X, M[+_]] {
  def resolve(id: ID)(implicit context: X): M[E]

  def store(entity: E)(implicit context: X): M[E]
}

class RepositoryException(message: String) extends Exception(message)

class EntityNotFoundException(message: String) extends RepositoryException(message)
