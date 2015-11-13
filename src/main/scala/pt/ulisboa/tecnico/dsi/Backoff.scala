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
  val goldenRatio = (1 - math.sqrt(5)) / 2
  val iterationExpected = "expected iteration greater or equal than 0"

  def getDurationConstant(name: String): FiniteDuration = {
    new FiniteDuration(root.getDuration(name, TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS)
  }

  /**
    * Return a backoff duration equal to @param duration for all iterations
    * @param iteration
    * @param duration
    * @return
    */
  def constantFunction(iteration: Int)
                      (duration: FiniteDuration = getDurationConstant("constant-duration"))
  : FiniteDuration = {
    require(iteration >= 0, iterationExpected)
    duration
  }

  /**
    * returns a backoff linear duration on the iteration
    * @param iteration
    * @param duration
    * @return
    */
  def linearFunction(iteration: Int)
                    (duration: FiniteDuration = getDurationConstant("linear-constant"))
  : FiniteDuration = {
    require(iteration >= 0, iterationExpected)
    duration * iteration
  }

  /**
    * Returns a backoff duration that increases linearly for each iteration
    * @param iteration
    * @param duration
    * @return
    */
  def exponentialFunction(iteration: Int)
                         (duration: FiniteDuration = getDurationConstant("exponential-constant"))
  : FiniteDuration = {
    require(iteration >= 0, iterationExpected)
    val d = duration * math.exp(iteration)
    d.asInstanceOf[FiniteDuration]
  }

  /**
    * Return a backoff duration aproximately equal to the fibonacci sequence for each iteration
    * @param iteration
    * @param duration
    * @return
    */
  def fibonacciFunction(iteration: Int)
                       (duration: FiniteDuration = getDurationConstant("fibonacci-duration"))
  : FiniteDuration = {
    require(iteration >= 0, iterationExpected)
    val d = duration * math.pow(goldenRatio, iteration)
    d.asInstanceOf[FiniteDuration]
  }

}
