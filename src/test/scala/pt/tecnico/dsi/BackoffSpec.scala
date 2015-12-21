package pt.tecnico.dsi

import org.scalacheck.Gen
import org.scalatest._
import org.scalatest.prop.GeneratorDrivenPropertyChecks

import scala.concurrent.duration._

abstract class UnitSpec extends FlatSpec with Matchers with OptionValues with Inside with Inspectors

class BackoffSpec extends PropSpec with GeneratorDrivenPropertyChecks with  ShouldMatchers {
  private val duration = 1.seconds
  private val maxIteration = math.floor(math.log(Long.MaxValue / duration.toNanos) / math.log(2)).toInt
  private val generator = for (n <- Gen.choose(0, maxIteration - 1)) yield n

  property("Using the constant function should always return the same value") {
    forAll { iteration: Int =>
      whenever(iteration >= 0 && iteration < Int.MaxValue) {
        Backoff.constant(iteration, duration) shouldBe duration
      }
    }
  }

  property("Using the linear function should return a constant difference between two iterations") {
    forAll { iteration: Int =>
      whenever(iteration >= 0 && iteration < Int.MaxValue) {
        Backoff.linear(iteration, duration) + duration shouldBe Backoff.linear(iteration + 1, duration)
      }
    }
  }


  property("Using the exponential function should return a constant ratio between two iterations") {
    forAll(generator) { iteration =>
      Backoff.exponential(iteration, duration) * 2 shouldBe Backoff.exponential(iteration + 1, duration)

    }
  }

  property("Using the fibonacci function should return a constant ratio(golden ratio) between two iterations") {
    forAll { iteration: Int =>
      whenever(iteration >= 0 && iteration < Int.MaxValue) {
        Backoff.fibonacci(iteration, duration) * Backoff.goldenRatio shouldBe Backoff.fibonacci(iteration + 1, duration)
      }
    }
  }
}
