package dining.philosopher

import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.event.LoggingReceive

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration.DurationInt

object Philosopher {

  // message used by Philosopher to talk to itself

  case object Eat // Tell philosopher to try to eat
  case object Think // Tell philosopher to think

}

/**
 * A Philosopher can be in the following states:
 * - thinking: she is thinking and having an existential crisis.
 * - hungry: she is trying to eat and she is waiting to hear back from both the chopsticks.
 * - eating: she eating yum.
 * - hungryWithRightChopstick: she was hungry and she got the right chopstick and now she is waiting to hear back from the left chopstick.
 * - hungryWithLeftChopstick: she was hungry and she got the left chopstick and now she is waiting to hear back from the right chopstick.
 * - rejectedOnce: she was hungry and she got rejected by one of the chopstick and now is waiting to hear back from the other chopstick so that it can try again.
 *
 * A philosopher will start dying as soon as she is done with the existential crisis.
 * As dying by starvation is too painful, she will desperately search look at chopsticks by its side.
 * Like a typical philosopher, she is lazy as hell, and she would keep dying rather getting new chopsticks.
 *
 * @param chopstickLeft  the chopstick on the left side of the philosopher
 * @param chopstickRight the chopstick on the right side of the philosopher
 */
class Philosopher(private val chopstickLeft: ActorRef,
                  private val chopstickRight: ActorRef) extends Actor with ActorLogging {

  import ChopstickMessages._
  import Philosopher._

  def name = self.path.name

  // for setting timer
  implicit private val executionContext: ExecutionContextExecutor = context.dispatcher

  // timing configuration
  private val eatTime = 1000.millis
  private val thinkTime = 1500.millis
  private val retryTime = 10.millis

  override def receive: Receive = LoggingReceive {
    case Think =>
      tryAgainToEat()
  }

  /**
   * philosopher state
   * she is thinking and having an existential crisis.
   */
  def thinking: Receive = LoggingReceive {
    case Eat =>
      // "will try" to eat (or become hungry) and will stop thinking and

      // try picking up both the chopstick
      chopstickLeft ! PickUpTry
      chopstickRight ! PickUpTry

      // wait to hear back from the chopsticks in the hungry state
      context.become(hungry)
  }

  /**
   * philosopher state
   * she eating yum.
   */
  def eating: Receive = LoggingReceive {
    case Think =>
      // will start thinking now and will stop eating

      // i will put down both the chopstick
      chopstickLeft ! PutDown
      chopstickRight ! PutDown

      // change my state to think
      context.become(thinking)

      // and remind myself to eat after thinking for some time
      setEatingReminder()
  }

  /**
   * philosopher state
   * she is trying to eat and she is waiting to hear back from both the chopsticks.
   */
  def hungry: Receive = LoggingReceive {
    case PickedUp if sender equals chopstickLeft => {
      // i successfully picked up the left chopstick

      // wait to hear back from the right chopstick
      context.become(hungryWithLeftChopstick)
    }
    case PickedUp if sender equals chopstickRight => {
      // i successfully picked up the left chopstick

      // wait to hear back from the left chopstick
      context.become(hungryWithRightChopstick)
    }
    case Busy => {
      // i was not able to pick up one of the chopstick

      // wait to hear back from the other chopstick and retry to eat
      context.become(rejectedOnce)
    }
  }

  /**
   * philosopher state
   * she was hungry and she got the right chopstick and now she is waiting to hear back from the left chopstick.
   */
  def hungryWithRightChopstick: Receive = LoggingReceive {
    case PickedUp if sender equals chopstickLeft => {
      //i successfully picked up the left chopstick and now i've both of them

      // start eating
      context.become(eating)

      // also remind myself to think after sometime
      setThinkingReminder()
    }
    case Busy => {
      // unable to pickup left chopstick

      // free right chopstick so others can use it
      chopstickRight ! PutDown

      // retry to eat
      tryAgainToEat()
    }
  }

  /**
   * philosopher state
   * she was hungry and she got the left chopstick and now she is waiting to hear back from the right chopstick.
   */
  def hungryWithLeftChopstick: Receive = LoggingReceive {
    case PickedUp if sender equals chopstickRight =>
      // i successfully picked up the right chopstick and now i've both of them

      // start eating
      context.become(eating)

      // also remind myself to think after sometime
      setThinkingReminder()
    case Busy =>
      // unable to pickup right chopstick

      // free left chopstick so others can use it
      chopstickLeft ! PutDown

      // retry to eat
      tryAgainToEat()
  }

  /**
   * philosopher state
   * she was hungry and she got rejected by one of the chopstick and now is waiting to hear back from the other chopstick so that it can try again.
   */
  def rejectedOnce: Receive = LoggingReceive {
    case PickedUp =>
      // if picked up the other chopstick, put it down and try to eat again
      sender ! PutDown
      tryAgainToEat()
    case Busy =>
      // both chopsticks were busy; try again man
      tryAgainToEat()
  }

  private def tryAgainToEat(): Unit = {
    context.become(thinking)
    setRetryEatingReminder()
  }

  // philosopher will eat for eatTime and then ask herself to Think
  private def setThinkingReminder(): Unit = {
    log info "Philosopher %s is eating".format(name)
    context.system.scheduler.scheduleOnce(eatTime, self, Think)
  }

  // philosopher will think for thinkTime and then ask herself to Eat
  private def setEatingReminder(): Unit = {
    log info "Philosopher %s is thinking".format(name)
    context.system.scheduler.scheduleOnce(thinkTime, self, Eat)
  }

  // philosopher will think for retryTime(because she can do nothing else) and then reminder herself to Eat again
  private def setRetryEatingReminder(): Unit = {
    context.system.scheduler.scheduleOnce(retryTime, self, Eat)
  }

}
