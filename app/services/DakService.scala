package services

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.model._
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._

class DakService  {

}

object DakService {
  def getUserStat: String = {
    val browser = JsoupBrowser()
    val doc = browser.get("https://dak.gg/profile/nanok-pony")

    val matchItems: Seq[MatchItem] = doc >?> elementList("div.matchHistory > div.item") match {
      case Some(v) =>
        v.map { x =>
          MatchItem(
            x.select("div.summary").head.text,
            x.select("div.modeName").head.text,
            x.select("dl.rating").head.text,
            x.select("dl.kills").head.text,
            x.select("dl.assists").head.text,
            x.select("dl.damage").head.text,
            x.select("dl.move").head.text,
            x.select("dl.survived").head.text
          )
        }

      case None =>
        Seq(MatchItem.empty)
    }

    matchItems.head.modeName
  }
}

case class MatchItem(
  summary: String,
  modeName: String,
  rating: String,
  kills: String,
  assists: String,
  damage: String,
  move: String,
  survived: String
)

object MatchItem {
  def empty = {
    MatchItem("", "", "", "", "", "", "", "")
  }
}