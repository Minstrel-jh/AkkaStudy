package router;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.japi.pf.FI;
import akka.routing.RoundRobinPool;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Router;

import java.util.ArrayList;
import java.util.List;

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
            actorRef.tell("Insert",ActorRef.noSender());
        }
    }
}

class ControlActor3 extends AbstractActor {

    @Override
    public Receive createReceive() {
        receiveBuilder()
            .match(StartCommand.class, new FI.UnitApply() {

                @Override
                public void apply(Object o) {
                    return;
                }
            });

        return null;
    }
}
