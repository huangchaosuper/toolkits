package cn.huangchaosuper.toolkits;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created by I311579 on 9/28/2015.
 */
public class SimpleTask implements ITask {
    private static Logger logger = LoggerFactory.getLogger(SimpleTask.class);
    private String stepPackage = null;
    protected Properties properties = null;
    protected Context context = new Context();

    public SimpleTask() {
    }

    @Override
    public boolean initialize(Properties properties) {
        this.properties = properties;
        if (!checkConfig()) return false;
        this.stepPackage = this.properties.getProperty(Utils.SIMPLETASKPACKAGE);
        context.setOpenStep(Integer.parseInt(this.properties.getProperty(Utils.OPENSTEP)));
        context.setCloseStep(Integer.parseInt(this.properties.getProperty(Utils.CLOSESTEP)));
        context.setProperties(this.properties);
        return true;
    }

    protected boolean checkConfig() {
        String configPath = System.getProperty(Utils.CONFIGPATH);
        if (configPath == null || configPath.equals("")) {
            logger.error("config.path not define, example: -Dconfig.path=/opt/path/config.properties");
            return false;
        }
        try {
            properties.load(new FileInputStream(configPath));
        } catch (IOException e) {
            logger.error("config file not exist :{0}", configPath);
            return false;
        }
        logger.debug(Utils.SIMPLETASKPACKAGE + ":" + properties.getProperty(Utils.SIMPLETASKPACKAGE));
        if (!Utils.checkConfigValue(properties, Utils.SIMPLETASKPACKAGE)) {
            logger.error(Utils.SIMPLETASKPACKAGE + " not define, example: -D" + Utils.SIMPLETASKPACKAGE + "=cn.huangchaosuper.steps");
            return false;
        }
        logger.debug(Utils.OPENSTEP + ":" + properties.getProperty(Utils.OPENSTEP));
        if (!Utils.checkConfigValue(properties, Utils.OPENSTEP, "0")) {
            return false;
        }
        logger.debug(Utils.CLOSESTEP + ":" + properties.getProperty(Utils.CLOSESTEP));
        if (!Utils.checkConfigValue(properties, Utils.CLOSESTEP, "9999")) {
            return false;
        }
        return true;
    }

    @Override
    public void run() {
        Set<Class<?>> classes = Utils.getClasses(this.stepPackage);
        SortedSet<Class<?>> stepClasses = new TreeSet<Class<?>>(new Comparator<Class<?>>() {
            @Override
            public int compare(Class<?> o1, Class<?> o2) {
                return o1.getAnnotation(TaskStep.class).step() < o2.getAnnotation(TaskStep.class).step() ? -1 : 1;
            }
        });
        for (Class<?> item : classes) {
            if (item.getAnnotation(TaskStep.class) == null) continue;
            if (item.getAnnotation(TaskStep.class).step() < context.getOpenStep() || item.getAnnotation(TaskStep.class).step() > context.getCloseStep())
                continue;
            if (item.getAnnotation(TaskStep.class).active()) {
                logger.info(item.getName());
                stepClasses.add((Class<?>) item);
            }
        }
        logger.info("Start!");
        IStep step = null;
        try {
            for (Class<?> item : stepClasses) {
                step = (IStep) item.newInstance();
                logger.info("start {}-{}", item.getAnnotation(TaskStep.class).step(), item.getName());
                if (!step.run(context)) {
                    step.finalized();
                    logger.error("break action on {}-{}", item.getAnnotation(TaskStep.class).step(), item.getName());
                    break;
                }
                if (step != null) {
                    step.finalized();
                    logger.info("complete {}-{}", item.getAnnotation(TaskStep.class).step(), item.getName());
                }
            }
        } catch (InstantiationException e) {
            logger.error("task class not implement ITask Interface");
        } catch (IllegalAccessException e) {
            logger.error("task class implement ITask Interface by private");
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("Done!");
    }

    @Override
    public void unInitialize() {
        return;
    }
}
