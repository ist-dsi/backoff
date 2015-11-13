package pt.ulisboa.tecnico.dsi

import org.scalatest._
import scala.concurrent.duration._

abstract class UnitSpec extends FlatSpec with Matchers
with OptionValues with Inside with Inspectors

/**
  * Created by ikea on 11/12/15.
  */
class BackoffSpec extends FlatSpec with Matchers{
  "A Backoff" should "always return the same value using the constant function" in {
    val value1 = Backoff.constantFunction(0)()
    Backoff.constantFunction(1)() should be (value1)
  }
  it should "return increments using a linear function" in {
    Backoff.linearFunction(0)(5.seconds) should be (0.seconds)
    Backoff.linearFunction(1)(5.seconds) should be (5.seconds)
    Backoff.linearFunction(10)(5.seconds) should be (50.seconds)
   }
  it should "return exponential increments using a exponential function" in {
    Backoff.exponentialFunction(0)(1.seconds) should be (1.seconds)
    Backoff.exponentialFunction(1)(1.seconds) should be (math.exp(1).seconds)
    Backoff.exponentialFunction(10)(1.seconds) should be (math.exp(10).seconds)
  }
  it should "should follow the fibonacci sequence for each iteration" in {
    val goldenRatio = (1 - math.sqrt(5)) / 2
    Backoff.fibonacciFunction(0)(1.seconds) should be (math.pow(goldenRatio,0).seconds)
    Backoff.fibonacciFunction(1)(1.seconds) should be (math.pow(goldenRatio,1).seconds)
    Backoff.fibonacciFunction(5)(1.seconds) should be (math.pow(goldenRatio,5).seconds)
  }
}
