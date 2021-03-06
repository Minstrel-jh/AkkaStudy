package dispatcher;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.typesafe.config.ConfigFactory;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        final ActorSystem system = ActorSystem.create(
            "main",
            ConfigFactory.load("dispatcher")
        );

        final ActorRef control = system.actorOf(Props.create(ControlActor.class), "control");
        control.tell(new StartCommand(100), ActorRef.noSender());

    }
}

class ControlActor extends UntypedActor {
    @Override
    public void onReceive(Object msg) throws Exception {
        if (msg instanceof StartCommand) {
            List<ActorRef> actors = createActors(((StartCommand) msg).getActorCount());

            /**
             * 这里使用了JDK1.8中的StreamAPI
             * parallel() 转变为并行流
             *
             * 并行调用列表中的actor发消息，writerActor会打印当前的线程名
             */
            actors.stream().parallel().forEach(actorRef -> actorRef.tell("Insert", ActorRef.noSender()));
        }
    }

    /**
     * 根据StartCommand中的actorCount创建了这么多个Actor的一个列表
     */
    private List<ActorRef> createActors(int actorCount) {
        /**
         * actorSystem创建是可以指定配置文件
         * 这里的writer-dispatcher配置了一个fork-join-executor
         */
        Props props = Props.create(WriterActor.class).withDispatcher("writer-dispatcher");
        ArrayList<ActorRef> actors = new ArrayList<>(actorCount);
        for (int i = 0; i < actorCount; i++) {
            actors.add(getContext().actorOf(props, "writer_" + i));
        }

        return actors;
    }
}

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

class WriterActor extends UntypedActor {
    @Override
    public void onReceive(Object o) {
        System.out.println(Thread.currentThread().getName() + ":" + getSelf().path());
    }
}