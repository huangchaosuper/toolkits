package cn.huangchaosuper.toolkits;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by I311579 on 9/28/2015.
 */
public class Main {
    private static Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {
        String configPath = System.getProperty("config.path");
        if (configPath == null || configPath.equals("")) {
            logger.error("config.path not define, example: -DconfigPath=/opt/path/config.properties");
            return;
        }
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(configPath));
        } catch (IOException e) {
            logger.error("config file not exist :{}", configPath);
            return;
        }
        String main = properties.getProperty("main");
        if (main == null || main.equals("")) {
            logger.error("main class must be define in config file");
            return;
        }
        ITask application = null;
        try {
            Class<?> mainClass = Class.forName(main);
            application = (ITask) mainClass.newInstance();
            if(application.initialize(properties)) {
                application.run();
            }
        } catch (ClassNotFoundException e) {
            logger.error("main class not exist :{}",main);
        } catch (InstantiationException e) {
            logger.error("main class not implement ITask Interface");
        } catch (IllegalAccessException e) {
            logger.error("main class implement ITask Interface by private");
        } catch(Exception e){
            e.printStackTrace();
        }finally {
            if(application != null) {
                application.unInitialize();
            }
        }
    }
}