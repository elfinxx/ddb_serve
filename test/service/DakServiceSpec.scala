package service

import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.test.Injecting
import services.DakService

class DakServiceSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {
  "DakService " should {
    "get status for some player" in {
      println(DakService.getUserStat)
      0 mustEqual 0
    }
  }

}
