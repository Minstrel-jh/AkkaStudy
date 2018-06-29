package lifecycle.demo1;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class MyWork extends UntypedActor {

    private LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    public enum Msg{
        WORKING,
        CLOSE
    }

    @Override
    public void preStart() {
        logger.info("myWork starting.");
    }

    @Override
    public void postStop() {
        logger.info("myWork stopping..");
    }

    @Override
    public void onReceive(Object msg) {
        if (msg == Msg.WORKING) {
            logger.info("receive WORKING msg, now i am working....");
        } else if (msg == Msg.CLOSE) {
            logger.info("receive STOP msg, prepare to shutdown....");
            getContext().stop(getSelf());
        } else {
            logger.info("未注册的消息");
            unhandled(msg);
        }
    }
}
