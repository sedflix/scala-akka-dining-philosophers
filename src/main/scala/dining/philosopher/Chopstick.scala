package dining.philosopher

import akka.actor.{Actor, ActorRef}
import akka.event.LoggingReceive

/**
 * Describe messages that are sent and received by Chopstick
 */
object ChopstickMessages {

  case object PickUpTry // a message received to ask for the chopstick
  case object PutDown // a message received to ask to put down the chopstick

  case object Busy // a message sent to show that the chopstick is in use right now
  case object PickedUp // as message sent to show that the chopstick was picked up successfully
}

/**
 * Chopstick can be in the following two states:
 * - idle
 * - busy with a philosopher
 */
class Chopstick extends Actor {

  import ChopstickMessages._

  /**
   * we start with a idle state
   */
  override def receive() = idle

  def idle: Receive = LoggingReceive {

    case PickUpTry =>
      // sender(a philosopher) asks to PickUp this chopstick

      // inform philosopher that they successfully picked up this chopstick
      sender ! PickedUp

      // now chopstick becomes busy
      context.become(busy(sender))
  }

  /**
   * @param philosopherInCharge the chopstick is currently being used by philosopherInCharge
   */
  def busy(philosopherInCharge: ActorRef): Receive = LoggingReceive {

    case PutDown if sender equals philosopherInCharge =>
      // the philosopher using the the chopstick(i.e. philosopherInCharge) puts it down

      // now chopstick becomes free
      context.become(idle)

    case PickUpTry =>
      // sender, a philosopher, asks for this chopstick

      // tell the philosopher(the sender) that chopstick is busy
      sender ! Busy
      
      
  }

}
