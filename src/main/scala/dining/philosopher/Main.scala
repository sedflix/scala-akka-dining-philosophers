package dining.philosopher

import akka.actor.{ActorSystem, Props, ActorRef}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt


object Main {

  import Philosopher._

  val system: ActorSystem = ActorSystem("dinner")

  def main(args: Array[String]): Unit = {

    // a list of chopsticks
    val chopsticks: Seq[ActorRef] = for (x <- 1 to 5) yield system.actorOf(Props[Chopstick], s"c$x")

    // a list of philosophers
    val philosophers: Seq[ActorRef] = for ((name, i) <- List("1", "2", "3", "4", "5").zipWithIndex)
      yield system.actorOf(Props(
        classOf[Philosopher], // Actor of which kind
        chopsticks(i), // right chopstick ActorRef
        chopsticks((i + 1) % 5)), // left chopstick ActorRef
        name // name
      )

    // send think to philosopher to kick-start the real game
    philosophers.foreach(_ ! Think)

    // terminate everything after 15 seconds
    system.scheduler.scheduleOnce(15.seconds)(system.terminate())
  }


}
