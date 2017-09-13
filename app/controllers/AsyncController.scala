package controllers

import javax.inject._

import akka.actor.ActorSystem
import play.api.libs.json.{JsSuccess, JsValue, Json}
import play.api.mvc._
import services.DDBService
import aa.mango.json.toJson
import model.Doll

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future, Promise}

/**
 * This controller creates an `Action` that demonstrates how to write
 * simple asynchronous code in a controller. It uses a timer to
 * asynchronously delay sending a response for 1 second.
 *
 * @param cc standard controller components
 * @param actorSystem We need the `ActorSystem`'s `Scheduler` to
 * run code after a delay.
 * @param exec We need an `ExecutionContext` to execute our
 * asynchronous code.  When rendering content, you should use Play's
 * default execution context, which is dependency injected.  If you are
 * using blocking operations, such as database or network access, then you should
 * use a different custom execution context that has a thread pool configured for
 * a blocking API.
 */
@Singleton
class AsyncController @Inject()(cc: ControllerComponents, actorSystem: ActorSystem)(implicit exec: ExecutionContext) extends AbstractController(cc) {

  val JsonContentType = "application/json; charset=utf-8"

  /**
   * Creates an Action that returns a plain text message after a delay
   * of 1 second.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/message`.
   */
  def list() = Action.async { r =>
    val convertedBody: JsValue = r.body match {
      case b: AnyContentAsRaw =>
        Json.parse(b.asRaw.get.asBytes().get.decodeString("UTF-8"))
      case b: AnyContentAsJson  =>
        b.asJson.get
    }

    (convertedBody \ "action" \ "params" \ "name").validate[String] match {
      case JsSuccess(name, _) =>
        getDollList(name, 0.second).map { msg =>
          Ok(msg)
        }

      case _ =>
        Future.successful(Ok(Json.parse(
          """
            |{
            | "text":"json structure error"
            |}
          """.stripMargin)))
    }
  }

  def findByName() =  Action.async(parse.anyContent) { r: Request[AnyContent] =>

    val convertedBody: JsValue = r.body match {
      case b: AnyContentAsRaw =>
        Json.parse(b.asRaw.get.asBytes().get.decodeString("UTF-8"))
      case b: AnyContentAsJson  =>
        b.asJson.get
    }

    println("REQUIEST -> " + convertedBody)
    (convertedBody \ "action" \ "params" \ "name").validate[String] match {
//    (convertedBody \ "name").validate[String] match {
      case JsSuccess(name, _) =>
        println("pRAMANAME -> " + name)
        getDollByName(name, 0.second).map { msg =>
          println("MSG -> " + msg)
          Ok(msg)
        }

      case _ =>
        Future.successful(Ok(Json.parse(
          """
            |{
            | "text":"json structure error"
            |}
          """.stripMargin)))
    }
  }

  def findByTime(time: String) =  Action.async {
    getDollListByTime(time, 0.second).map { msg =>
      Ok(msg)
    }
  }

  def simpleResponse() = Action.async {
    Future.successful(Ok(Json.parse(
      """
        |{
        | "text":"댕댕잉!",
        | "imageUrl":"http://k.kakaocdn.net/dn/FZJ1b//btqhbJhe4yF//s3KEdDbB7LKJeUa5YTOaVK/original.jpg"
        |}
      """.stripMargin)))
  }

  private def getDollListByTime(time: String, delayTime: FiniteDuration): Future[JsValue] = {
    val dolls = DDBService.findDollsByMakingTime(time)

    val promise: Promise[JsValue] = Promise[JsValue]()
    actorSystem.scheduler.scheduleOnce(delayTime) {

      promise.success(Json.parse(toJson(dolls.headOption.getOrElse(Doll.empty()))))

    }(actorSystem.dispatcher) // run scheduled tasks using the actor system's dispatcher
    promise.future
  }

  private def getDollList(name: String, delayTime: FiniteDuration): Future[JsValue] = {
    val dolls = DDBService.findDollsByName(name)

    val promise: Promise[JsValue] = Promise[JsValue]()
    actorSystem.scheduler.scheduleOnce(delayTime) {

      if (dolls.length > 9) {
        promise.success(Json.parse(toJson(Map("result_data" -> dolls.slice(0, 9)))))
      } else {
        promise.success(Json.parse(toJson(Map("result_data" -> dolls))))
      }

    }(actorSystem.dispatcher) // run scheduled tasks using the actor system's dispatcher
    promise.future
  }

  private def getDollByName(name: String, delayTime: FiniteDuration): Future[JsValue] = {
    val doll = DDBService.findDollByName(name)

    val promise: Promise[JsValue] = Promise[JsValue]()
    actorSystem.scheduler.scheduleOnce(delayTime) {

      promise.success(Json.parse(toJson(doll)))

    }(actorSystem.dispatcher) // run scheduled tasks using the actor system's dispatcher
    promise.future
  }
}
