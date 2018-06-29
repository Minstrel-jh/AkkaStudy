package lifecycle.demo1;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;

public class Main {

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("LifeCycle", ConfigFactory.load("akka.config"));

        // 创建MyWork
        ActorRef myWork = system.actorOf(Props.create(MyWork.class), "MyWork");

        // 创建WatcherActor
        system.actorOf(Props.create(WatcherActor.class, myWork), "WatcherActor");

        myWork.tell(MyWork.Msg.WORKING, ActorRef.noSender());
        myWork.tell(PoisonPill.getInstance(), ActorRef.noSender());

        myWork.tell(MyWork.Msg.CLOSE, ActorRef.noSender()); //由于MyWork已经停止，消息会废弃

    }
}
