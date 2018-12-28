package dispatcher;

import akka.actor.UntypedActor;

public class WriterActor extends UntypedActor {
    @Override
    public void onReceive(Object o) throws Exception {
        System.out.println(Thread.currentThread().getName() + ":" + getSelf().path());
    }
}
