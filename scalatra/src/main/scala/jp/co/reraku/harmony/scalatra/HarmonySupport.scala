package jp.co.reraku.harmony.scalatra

import jp.co.reraku.harmony.RequestContext

import org.scalatra.{ ScalatraBase, RouteTransformer }

trait HarmonySupport[S <: ScalatraBase, X <: RequestContext] { self: S =>
  def context: X

  def arrive(transformers: RouteTransformer*): ScalatraPath[S, X] =
    new ScalatraPath(this, transformers, context)
}
