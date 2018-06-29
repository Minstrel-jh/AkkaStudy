package sample.hello;

import akka.actor.UntypedActor;

public class Greeter extends UntypedActor {

    public enum Msg {
        GREET,
        DONE
    }

    @Override
    public void onReceive(Object msg) throws Exception {
        if (msg == Msg.GREET) {
            System.out.println("Hello World!");
            Thread.sleep(1000);
            getSender().tell(Msg.DONE, getSelf());
        } else {
            unhandled(msg);
        }
    }

}
