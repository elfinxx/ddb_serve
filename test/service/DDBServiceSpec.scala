package service

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test.Injecting
import services.DDBService

class DDBServiceSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {
  "DDB Service " should {

    "get doll list from local csv" in {
      DDBService.dolls.size must be > 0
    }

    "get dolls by search query" in {
      val dolls = DDBService.findDollsByName("G11")
      dolls.map(d => println(d.name))

      dolls.size must be > 0

      val dolls2 = DDBService.findDollsByName("댕댕")
      dolls2.map(d => println(d.name))

      dolls2.size must be > 0
    }

  }
}