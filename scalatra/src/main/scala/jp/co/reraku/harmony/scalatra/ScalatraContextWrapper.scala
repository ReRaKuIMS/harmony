package jp.co.reraku.harmony.scalatra

import org.scalatra.{ ScalatraContext, ScalatraBase }
import org.scalatra.util.{ MultiMapHeadView, MultiMap }
import org.scalatra.servlet.{ FileUploadSupport, FileItem, FileMultiParams }
import org.scalatra.json.JsonSupport
import org.json4s.JValue

class ScalatraContextWrapper[S <: ScalatraContext](scalatraContext: S) {
  implicit def request = scalatraContext.request

  def params(implicit ev: S <:< ScalatraBase): MultiMapHeadView[String, String] =
    scalatraContext.params

  def multiParams(implicit ev: S <:< ScalatraBase): MultiMap =
    scalatraContext.multiParams

  def fileParams(implicit ev: S <:< FileUploadSupport): MultiMapHeadView[String, FileItem] =
    scalatraContext.fileParams

  def fileMultiParams(implicit ev: S <:< FileUploadSupport): FileMultiParams =
    scalatraContext.fileMultiParams

  def parsedBody(implicit ev: S <:< JsonSupport[_]): JValue =
    scalatraContext.parsedBody
}
