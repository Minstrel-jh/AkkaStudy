package config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigUtil {

    public static void main(String[] args) {
        testGetProperties();

        testGetConfig();
    }

    /**
     * jdk提供的配置类Properties
     * 支持.properties后缀的文件
     */
    public Properties getProperties(String path) {
        InputStream rs = ConfigUtil.class.getClassLoader().getResourceAsStream(path);
        Properties props = new Properties();
        try {
            props.load(rs);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return props;
    }

    public static void testGetProperties() {
        System.out.println("=====testGetProperties=====");
        ConfigUtil configUtil = new ConfigUtil();
        Properties prop = configUtil.getProperties("application.properties");
        System.out.println(prop.getProperty("test"));
    }

    /**
     * 由typesafe.config提供的配置类
     * 支持.conf后缀的文件
     * 默认加载application.conf的配置，也可以指定文件名
     */
    public Config getConfig() {
        return ConfigFactory.load();
    }

    public Config getConfig(String path) {
        return ConfigFactory.load(path);
    }

    public static void testGetConfig() {
        System.out.println("=====testGetConfig=====");
        ConfigUtil configUtil = new ConfigUtil();

        Config defaultConfig = configUtil.getConfig();
        String master = defaultConfig.getString("spark.master");
        System.out.println(master);

        Config namedConfig = configUtil.getConfig("another.conf");
        Config firstSubConf = namedConfig.getConfig("mysql");
        Config secondSubConf = firstSubConf.getConfig("dataSource");
        String maxLifetime = secondSubConf.getString("maxLifetime");
        System.out.println(maxLifetime);

    }
}
