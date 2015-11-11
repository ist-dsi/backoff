package pt.ulisboa.tecnico.dsi

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.FiniteDuration
import scala.concurrent.duration.{DurationDouble,DurationLong}
import com.typesafe.config.{Config, ConfigFactory, ConfigList}

object Configs{
  val root = ConfigFactory.load("backoff")
}


/**
  * Created by ikea on 11/10/15.
  */
object Backoff {
  val root = Configs.root

  def constantFunction(offset: Long = root.getLong("constantFunction.offset"))
                      (iteration: Int): FiniteDuration = offset.seconds

  /*
    f(n) = a*n + b
   */
  def linearFunction(linearConstant: Double = root.getDouble("linearFunction.linearConstant"))
                    (linearOffset: Double = root.getDouble("linearFunction.offsetConstant"))
                    (iteration: Int): FiniteDuration = {
    val timeToWait = linearConstant * iteration + linearOffset
    timeToWait.seconds
  }

  def exponentialFunction(linearConstant: Double = root.getDouble("exponentialFunction.linearConstant"))
                         (exponentialConstant: Double = root.getDouble("exponentialFunction.exponentialConstant"))
                         (iteration: Int): FiniteDuration = {
    val timeToWait = linearConstant * math.exp(exponentialConstant * iteration)
    timeToWait.seconds
  }

  def goldenNumber(): Double ={
    1
  }
  def fibonacciFunction(constant: Double = root.getDouble("fibonacci.constant"))
                       (iteration: Int): FiniteDuration ={
    /*
        O(n) implementation http://stackoverflow.com/questions/16388982/algorithm-function-for-fibonacci-series
     */
    if(iteration <= 0)
      return 0.0.seconds
    if(iteration > 0 && iteration < 3)
      return 1.0.seconds

    var result = 0.0
    var preOldResult = 1.0
    var oldResult = 1.0
    for(n <- 2 to iteration){
      result = preOldResult + oldResult
      preOldResult = oldResult
      oldResult = result
    }
    val fibonacci = constant * result
    fibonacci.seconds
  }
}
