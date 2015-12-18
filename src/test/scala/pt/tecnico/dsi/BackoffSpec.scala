package pt.tecnico.dsi

import org.scalacheck.Gen
import org.scalatest._
import org.scalatest.prop.{TableDrivenPropertyChecks, GeneratorDrivenPropertyChecks}

import scala.concurrent.duration._

abstract class UnitSpec extends FlatSpec with Matchers
with OptionValues with Inside with Inspectors

class BackoffSpec extends PropSpec with
  GeneratorDrivenPropertyChecks with
  ShouldMatchers {

  property("Using the constant function should always return the same value") {
    forAll { iteration: Int =>
      val duration = 1.seconds
      whenever(iteration >= 0 && iteration < Int.MaxValue) {
        Backoff.constant(iteration, duration) shouldBe duration
      }
    }
  }

  property("Using the linear function should return a constant difference between two iterations") {
    forAll { iteration: Int =>
      val duration = 1.seconds
      whenever(iteration >= 0 && iteration < Int.MaxValue) {
        Backoff.linear(iteration, duration) + duration shouldBe Backoff.linear(iteration + 1, duration)
      }
    }
  }

  val duration = 1.seconds
  val maxIteration = math.floor(math.log(Long.MaxValue / duration.toNanos) / math.log(2)).toInt
  val generator = for(n <- Gen.choose(0,maxIteration-1)) yield n
  property("Using the exponential function should return a constant ratio between two iterations") {
    forAll(generator) { iteration =>
      val x = Backoff.exponential(iteration, duration) * 2
      val y = Backoff.exponential(iteration + 1, duration)
      println(s"it:$iteration x:$x y:$y max:$maxIteration")
      x shouldBe y
    }
  }

  property("Using the fibonacci function should return a constant ratio(golden ratio) between two iterations") {
    forAll { iteration: Int =>
      val duration = 1.seconds
      whenever(iteration >= 0 && iteration < Int.MaxValue) {
        Backoff.fibonacci(iteration, duration) * Backoff.goldenRatio shouldBe Backoff.fibonacci(iteration + 1, duration)
      }
    }
  }
}
