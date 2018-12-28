package router.demo1;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Router;

import java.util.ArrayList;
import java.util.List;

public class ControlActor extends UntypedActor {
    @Override
    public void onReceive(Object msg) throws Exception {
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
