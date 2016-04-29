package jp.co.reraku.harmony.scalatra

import jp.co.reraku.harmony.{ Outbound, Request }

import org.scalatra.ActionResult

trait ScalatraOutbound[R <: Request[R], P] extends Outbound[R, P, ActionResult]
