package sample.hello2;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class Main {
    public static void main(String[] args) {
        bootstrap2();
    }

    private static void bootstrap1() {
        akka.Main.main(new String[] { HelloWorld.class.getName() });
    }

    private static void bootstrap2() {
        ActorSystem system = ActorSystem.create("Hello");
        ActorRef a = system.actorOf(Props.create(HelloWorld.class), "HelloWorld");
        System.out.println(a.path());
    }
}
