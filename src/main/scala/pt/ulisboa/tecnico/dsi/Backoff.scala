package pt.ulisboa.tecnico.dsi

import java.time.Duration
import java.util.concurrent.TimeUnit
import scala.concurrent.duration.FiniteDuration
import scala.concurrent.duration.{DurationDouble, DurationLong}
import com.typesafe.config.{Config, ConfigFactory, ConfigList}

/**
  * Backoff is used to calculate the next duration to wait before
  * trying to resend a request using different functions, all of which,
  * receive an integer as the iteration number and a duration as the vertical
  * scaling factor.
  */
object Backoff {
  val root = ConfigFactory.load().getConfig("backoff")
  val goldenRatio = (1 - math.sqrt(5)) / 2
  val iterationExpected = "expected iteration greater or equal than 0"
  val durationExpected = "expected a duration greater or equal than 0 seconds"

  def getDurationConstant(name: String): FiniteDuration = {
    new FiniteDuration(root.getDuration(name, TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS)
  }

  /**
    * Return a backoff duration that is constant for all iterations.
    * For a duration of 5 seconds
    * <table>
    *   <tr><td>Iteration </td><td>Backoff</td></tr>
    *   <tr><td>0</td><td>5 seconds</td></tr>
    *   <tr><td>1</td><td>5 seconds</td></tr>
    *   <tr><td>2</td><td>5 seconds</td></tr>
    *   <tr><td>3</td><td>5 seconds</td></tr>
    *   <tr><td>4</td><td>5 seconds</td></tr>
    * </table>
    *
    * @param iteration  number of the iteration, starting at 0
    * @param duration   non-negative duration, returned by the function, optional argument which
    *                   defaults to a constant defined in the root config
    * @return   returns the @param duration
    */
  def constantFunction(iteration: Int)
                      (duration: FiniteDuration = getDurationConstant("constant-duration"))
  : FiniteDuration = {
    require(iteration >= 0, iterationExpected)
    require(duration >= 0.0.seconds, durationExpected)
    duration
  }

  /**
    * Returns a backoff duration that increases linearly for each iteration.
    * For a duration of 5 seconds
    * <table>
    *   <tr><td>Iteration </td><td>Backoff</td></tr>
    *   <tr><td>0</td><td>0 seconds</td></tr>
    *   <tr><td>1</td><td>5 seconds</td></tr>
    *   <tr><td>2</td><td>10 seconds</td></tr>
    *   <tr><td>3</td><td>15 seconds</td></tr>
    *   <tr><td>4</td><td>20 seconds</td></tr>
    * </table>
    *
    * @param iteration  number of the iteration, starting at 0
    * @param duration   non-negative duration. Defines the increase in time for each unit of duration,
    *                   optional argument which defaults to a constant defined in the root config
    * @return
    */
  def linearFunction(iteration: Int)
                    (duration: FiniteDuration = getDurationConstant("linear-constant"))
  : FiniteDuration = {
    require(iteration >= 0, iterationExpected)
    require(duration >= 0.0.seconds, durationExpected)
    duration * iteration
  }

  /**
    * Returns a backoff duration that increases exponentially for each iteration.
    *
    * For a duration of 5 seconds
    * <table>
    *   <tr><td>Iteration </td><td>Backoff</td></tr>
    *   <tr><td>0</td><td>5 seconds</td></tr>
    *   <tr><td>1</td><td>20 seconds</td></tr>
    *   <tr><td>2</td><td>40 seconds</td></tr>
    *   <tr><td>3</td><td>80 seconds</td></tr>
    *   <tr><td>4</td><td>160 seconds</td></tr>
    * </table>
    * @note If math.pow(2,iteration) is near Double.MaxValue overflow can happen
    *
    * @param iteration  number of the iteration, starting at 0
    * @param duration   non-negative duration. Scales the exponential function vertically.
    *                   For a duration of 2 seconds, the backoff is doubled
    * @return
    */
  def exponentialFunction(iteration: Int)
                         (duration: FiniteDuration = getDurationConstant("exponential-constant"))
  : FiniteDuration = {
    require(iteration >= 0, iterationExpected)
    require(duration >= 0.0.seconds, durationExpected)

    duration * math.pow(2,iteration).toLong
  }

  /**
    * Returns a backoff duration that increases in the same way as the fibonacci sequence does, using an approximation
    * for the concrete fibonacci sequence.
    *
    * For a duration of 5 seconds
    * <table>
    *   <tr><td>Iteration </td><td>Backoff</td></tr>
    *   <tr><td>0</td><td>5.00 seconds</td></tr>
    *   <tr><td>1</td><td>8.09 seconds</td></tr>
    *   <tr><td>2</td><td>13.09 seconds</td></tr>
    *   <tr><td>3</td><td>21.18 seconds</td></tr>
    *   <tr><td>4</td><td>34.27 seconds</td></tr>
    *   <tr><td>4</td><td>55.45 seconds</td></tr>
    * </table>
    * @note If math.pow(2,iteration) is near Double.MaxValue overflow can happen
    *
    * @param iteration  number of the iteration, starting at 0
    * @param duration   non-negative duration. Scales the fibonacci sequence vertically.
    * @return
    */
  def fibonacciFunction(iteration: Int)
                       (duration: FiniteDuration = getDurationConstant("fibonacci-duration"))
  : FiniteDuration = {
    require(iteration >= 0, iterationExpected)
    require(duration >= 0.0.seconds, durationExpected)
    val d = duration * math.pow(goldenRatio, iteration)
    d.asInstanceOf[FiniteDuration]
  }

}
