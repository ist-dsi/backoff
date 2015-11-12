package pt.ulisboa.tecnico.dsi

import org.scalatest._
import scala.concurrent.duration._

abstract class UnitSpec extends FlatSpec with Matchers
with OptionValues with Inside with Inspectors

/**
  * Created by ikea on 11/12/15.
  */
class BackoffSpec extends FlatSpec with Matchers{
  "A Backoff" should "using constant function should always return same value" in {
    val value1 = Backoff.constantFunction()(1)
    Backoff.constantFunction()(1) should be (value1)
  }
}
