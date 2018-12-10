package dispatcher.demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;

public class Main {

    public static void main(String[] args) {
        final ActorSystem system = ActorSystem.create(
            "main",
            ConfigFactory.load("dispatcher").getConfig("demo1")
        );

        final ActorRef control = system.actorOf(Props.create(ControlActor.class), "control");
        control.tell(new StartCommand(100), ActorRef.noSender());

    }
}
