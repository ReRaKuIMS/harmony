package jp.co.reraku.harmony

import scala.util.Try

trait RequestHandler[X <: RequestContext, R <: Request[R], P] {
  def apply(context: X, request: R): P

  def unwrap[T](result: => Try[T]): T = result.get
}

trait CommandHandler[X <: RequestContext, C <: Command[C], P] extends RequestHandler[X, C, P]
trait QueryHandler[X <: RequestContext, Q <: Query[Q], P] extends RequestHandler[X, Q, P]

class RequestHandlingException[R <: Request[R]](request: R, message: String) extends Exception(message)
class CommandHandlingException[C <: Command[C]](command: C, message: String) extends RequestHandlingException(command, message)
class QueryHandlingException[Q <: Query[Q]](query: Q, message: String) extends RequestHandlingException(query, message)
