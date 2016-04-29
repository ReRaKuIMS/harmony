package jp.co.reraku.harmony.scalatra

import org.scalatra.{ ScalatraContext, ScalatraBase, Params }
import org.scalatra.json.JsonSupport
import org.json4s.JValue

class ScalatraContextWrapper[S <: ScalatraContext](scalatraContext: S) {
  implicit def request = scalatraContext.request

  def params(implicit ev: S <:< ScalatraBase): Params =
    scalatraContext.params

  def parsedBody(implicit ev: S <:< JsonSupport[_]): JValue =
    scalatraContext.parsedBody
}
