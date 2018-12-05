package sample.hello3;

import akka.actor.UntypedActor;

public class Greeter extends UntypedActor {

    String greeting = "";

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof WhoToGreet) {
            greeting = "hello," + ((WhoToGreet) message).who;
        } else if (message instanceof Greet) {
            getSender().tell(new Greeting(greeting), getSelf());
        } else {
            System.out.println("message未注册");
            unhandled(message);
        }
    }
}
