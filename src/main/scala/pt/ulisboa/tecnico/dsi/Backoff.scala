package pt.ulisboa.tecnico.dsi

import java.time.Duration
import java.util.concurrent.TimeUnit
import scala.concurrent.duration.FiniteDuration
import scala.concurrent.duration.{DurationDouble, DurationLong}
import com.typesafe.config.{Config, ConfigFactory, ConfigList}

/**
  * Created by ikea on 11/10/15.
  */
object Backoff {
  val root = ConfigFactory.load().getConfig("backoff")
  val goldenRation = (1 - math.sqrt(5)) / 2
  val iterationExpected = "expected iteration greater or equal than 0"

  def getDurationConstant(name: String): FiniteDuration = {
    new FiniteDuration(root.getDuration(name, TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS)
  }

  def constantFunction(duration: FiniteDuration = getDurationConstant("constant-duration"))
                      (iteration: Int): FiniteDuration = {
    require(iteration >= 0, iterationExpected)
    duration
  }

  def linearFunction(duration: FiniteDuration =getDurationConstant("linear-constant"))
                    (iteration: Int): FiniteDuration = {
    require(iteration >= 0, iterationExpected)
    duration * iteration
  }

  def exponentialFunction(duration: FiniteDuration = getDurationConstant("exponential-constant"))
                         (iteration: Int): FiniteDuration = {
    require(iteration >= 0, iterationExpected)
    val d = duration * math.exp(iteration)
    d.asInstanceOf[FiniteDuration]
  }

  def fibonacciFunction(duration: FiniteDuration = getDurationConstant("fibonacci-duration"))
                       (iteration: Int): FiniteDuration = {
    require(iteration >= 0, iterationExpected)
    val d = duration * math.pow(goldenRation, iteration)
    d.asInstanceOf[FiniteDuration]
  }

}
