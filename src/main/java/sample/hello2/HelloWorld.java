package sample.hello2;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.alibaba.fastjson.JSONObject;

import java.util.Arrays;

public class HelloWorld extends UntypedActor {

    private ActorRef greeter;

    @Override
    public void preStart() {
        // create the greeter actor
        greeter = getContext().actorOf(Props.create(Greeter.class), "greeter");
        System.out.println("Greeter actor path: " + greeter.path());

        // tell it to perform the greeting
        greeter.tell(new Message(2, Arrays.asList("2", "jh")), getSelf());
    }

    @Override
    public void onReceive(Object msg) {
        System.out.println("HelloWorld收到的数据为：" + JSONObject.toJSONString(msg));
    }
}
