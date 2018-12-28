package router;

import akka.actor.*;
import akka.routing.FromConfig;
import akka.routing.RoundRobinPool;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Router;
import com.typesafe.config.ConfigFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        final ActorSystem system = ActorSystem.create("router", ConfigFactory.load("router"));

        final ActorRef control = system.actorOf(Props.create(ControlActor3.class), "control");
        control.tell(new StartCommand(100), ActorRef.noSender());
    }
}

/**
 * 这种方式是通过AKKA提供的API,手动的创建Router对象,然后调用addRoutee方法手动的添加Actor
 */
class ControlActor1 extends UntypedActor {
    @Override
    public void onReceive(Object msg) {
        if (msg instanceof StartCommand) {
            List<ActorRef> actors = createActors(((StartCommand) msg).getActorCount());

            /**
             * 手动的创建Router对象,然后调用addRoutee方法手动的添加Actor
             */
            Router router = new Router(new RoundRobinRoutingLogic());
            for (ActorRef actor : actors) {
                // 需要接收addRoutee的返回
                router = router.addRoutee(actor);
            }

            /**
             * AKKA会把这个消息按照路由策略分发给某一个Actor中执行
             */
            router.route("Insert", ActorRef.noSender());
        }
    }

    private List<ActorRef> createActors(int actorCount) {
        ArrayList<ActorRef> actors = new ArrayList<>(actorCount);
        for (int i = 0; i < actorCount; i++) {
            actors.add(getContext().actorOf(Props.create(WriterActor.class), "writer_" + i));
        }

        return actors;
    }
}

/**
 * 这种方式是通过创建一个RouterActor来使用路由
 */
class ControlActor2 extends UntypedActor {
    @Override
    public void onReceive(Object msg) {
        if (msg instanceof StartCommand) {
            int actorCount = ((StartCommand) msg).getActorCount();
            Props props = Props.create(WriterActor.class).withRouter(new RoundRobinPool(actorCount));
            ActorRef actorRef = getContext().actorOf(props);
            actorRef.tell("Insert", ActorRef.noSender());
        }
    }
}

class ControlActor3 extends UntypedActor {

    @Override
    public void onReceive(Object msg) {
        if (msg instanceof StartCommand) {
            System.out.println("收到 command");
            Props props = Props.create(WriterActor.class).withRouter(new FromConfig());
            ActorRef actorRef = getContext().actorOf(props, "broadCastRouter"); // 这里的name需要是配置的router的路径名
            actorRef.tell("Insert", ActorRef.noSender());
        }
    }
}

/**
 * 定义消息类
 */
class StartCommand implements Serializable {

    private int actorCount = 0;

    public StartCommand() {}

    public StartCommand(int actorCount) {
        this.actorCount = actorCount;
    }

    public int getActorCount() {
        return actorCount;
    }

    public void setActorCount(int actorCount) {
        this.actorCount = actorCount;
    }
}

/**
 * 定义一个WriterActor，用来接收ControlActor的消息
 */
class WriterActor extends UntypedActor {
    @Override
    public void onReceive(Object o) throws Exception {
        System.out.println(Thread.currentThread().getName() + ":" + getSelf().path());
    }
}
