package typedactor;

import akka.actor.ActorSystem;
import akka.actor.TypedActor;
import akka.actor.TypedProps;
import akka.japi.Creator;
import akka.japi.Option;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws Exception {
        final ActorSystem system = ActorSystem.create("helloakka");

        Squarer mySquarer = TypedActor.get(system).typedActorOf(new TypedProps<>(Squarer.class, SquarerImpl.class));

        Option<Integer> oSquare = mySquarer.squareNow(3);
        System.out.println("阻塞调用");
        System.out.println("sum = " + oSquare.get());

        Future<Integer> fSquare = mySquarer.square(3);
        System.out.println("非阻塞调用");
        System.out.println(Await.result(fSquare, Duration.apply(12, TimeUnit.SECONDS)));
    }
}
