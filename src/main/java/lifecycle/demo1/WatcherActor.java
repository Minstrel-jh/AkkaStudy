package lifecycle.demo1;

import akka.actor.ActorRef;
import akka.actor.Terminated;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class WatcherActor extends UntypedActor {

    private LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    public WatcherActor(ActorRef actorRef) {
        getContext().watch(actorRef);
    }

    @Override
    public void onReceive(Object msg) {
        if (msg instanceof Terminated) {
            logger.error(((Terminated)msg).getActor().path() + "has terminated. now shutdown the system");
            getContext().system().terminate();
        } else {
            unhandled(msg);
        }
    }
}
