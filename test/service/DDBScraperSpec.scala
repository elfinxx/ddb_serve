package service

import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.test.Injecting
import services.DDBScraper

class DDBScraperSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {


  "DDBScraper" should {

    "get doll info by id" in {
      val x = DDBScraper.getDollInfo(1)


      0 mustEqual 0
    }
  }

}
