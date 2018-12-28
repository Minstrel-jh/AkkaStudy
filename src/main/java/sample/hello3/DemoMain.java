package sample.hello3;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Inbox;
import akka.actor.Props;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class DemoMain {
    public static void main(String[] args) throws TimeoutException {
        ActorSystem system = ActorSystem.create("helloakka");

        ActorRef greeter = system.actorOf(Props.create(Greeter.class), "greeter");

        Inbox inbox = Inbox.create(system);

        greeter.tell(new Integer(1), ActorRef.noSender());
        inbox.send(greeter, new Greet());

        Greeting greeting1 = (Greeting) inbox.receive(Duration.create(5, TimeUnit.SECONDS));
        System.out.println("Greeting:" + greeting1.message);

        greeter.tell(new WhoToGreet("typesafe"), ActorRef.noSender());
        inbox.send(greeter, new Greet());

        Greeting greeting2 = (Greeting) inbox.receive(Duration.create(5, TimeUnit.SECONDS));
        System.out.println("Greeting:" + greeting2.message);

        ActorRef greetPrinter = system.actorOf(Props.create(GreetPrinter.class));
        system.scheduler().schedule(
            Duration.Zero(),
            Duration.create(1, TimeUnit.SECONDS),
            greeter,
            new Greet(),
            system.dispatcher(),
            greetPrinter
        );
    }
}
