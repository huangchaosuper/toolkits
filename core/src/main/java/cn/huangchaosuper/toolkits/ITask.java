package cn.huangchaosuper.toolkits;

import java.util.Properties;

/**
 * Created by I311579 on 9/28/2015.
 */
public interface ITask {
    boolean initialize(Properties properties);
    void run();
    void unInitialize();
}
