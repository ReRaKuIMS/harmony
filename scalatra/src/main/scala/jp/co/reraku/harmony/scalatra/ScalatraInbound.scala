package jp.co.reraku.harmony.scalatra

import jp.co.reraku.harmony.{ Inbound, Request }

import org.scalatra.{ ScalatraContext, ScalatraParamsImplicits }
import org.scalatra.util.conversion.DefaultImplicitConversions

trait ScalatraInbound[S <: ScalatraContext, R <: Request[R]]
  extends Inbound[ScalatraContextWrapper[S], R]
  with DefaultImplicitConversions
  with ScalatraParamsImplicits
