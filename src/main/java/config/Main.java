package config;

import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        main.getConfig();
    }

    public void getConfig() {
        InputStream rs = this.getClass().getResourceAsStream("application.conf");
        Properties props = new Properties();
        try {
            props.load(rs);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String prop = props.getProperty("spark.master");
        System.out.println(prop);
    }

    public static void tmp() {
        Config sparkConf = ConfigFactory.load("application.conf").getConfig("spark");
        String master = sparkConf.getString("master");
        String duration = sparkConf.getString("streaming.batch.duration");
        System.out.println(master + "; " + duration);

        //String executor = demo1.getConfig("writer-dispatcher").getString("executor");
        //System.out.println(executor);
//        ActorSystem.create("demo1", );
    }
}
