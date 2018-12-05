package dispatcher;

import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class Main {
    public static void main(String[] args) {
        Config sparkConf = ConfigFactory.load("application.conf").getConfig("spark");
        String master = sparkConf.getString("master");
        String duration = sparkConf.getString("streaming.batch.duration");
        System.out.println(master + "; " + duration);

        //String executor = demo1.getConfig("writer-dispatcher").getString("executor");
        //System.out.println(executor);
//        ActorSystem.create("demo1", );
    }
}
