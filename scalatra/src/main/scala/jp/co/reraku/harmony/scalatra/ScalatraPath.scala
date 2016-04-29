package jp.co.reraku.harmony.scalatra

import jp.co.reraku.harmony._

import org.scalatra.{ ScalatraBase, RouteTransformer, ActionResult }

import scala.util.{ Try, Success, Failure }

class ScalatraPath[S <: ScalatraBase, X <: RequestContext](scalatraContext: S, transformers: Seq[RouteTransformer], context: => X) {
  def handleWith[C <: Command[C], P](commandHandler: CommandHandler[X, C, P])(
    implicit
    inbound:  ScalatraInbound[S, C],
    outbound: ScalatraOutbound[C, P]
  ) {
    scalatraContext.post(transformers: _*)(run(commandHandler))
  }

  def handleWith[Q <: Query[Q], P](queryHandler: QueryHandler[X, Q, P])(
    implicit
    inbound:  ScalatraInbound[S, Q],
    outbound: ScalatraOutbound[Q, P]
  ) {
    scalatraContext.get(transformers: _*)(run(queryHandler))
  }

  private def run[R <: Request[R], P](requestHandler: RequestHandler[X, R, P])(
    implicit
    inbound:  ScalatraInbound[S, R],
    outbound: ScalatraOutbound[R, P]
  ): ActionResult = {
    val request = try {
      inbound(new ScalatraContextWrapper(scalatraContext))
    }
    catch {
      case e: Exception => throw new InboundException(e.getMessage)
    }

    val result = Try(requestHandler(context, request))

    try {
      result match {
        case Success(product)              => outbound(request, product)
        case Failure(exception: Exception) => outbound.recover(request, exception)
        case Failure(throwable: Throwable) => throw throwable
      }
    }
    catch {
      case e: Exception => throw new OutboundException(e.getMessage)
    }
  }
}
