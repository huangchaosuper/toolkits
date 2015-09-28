package cn.huangchaosuper.toolkits;

import java.util.Calendar;
import java.util.Properties;

/**
 * Created by I311579 on 9/28/2015.
 */
public class Context {
    protected Calendar openTime = Calendar.getInstance();
    protected Calendar closeTime = Calendar.getInstance();
    protected int openStep;
    protected int closeStep;
    protected Properties properties;

    public Calendar getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Calendar openTime) {
        this.openTime = openTime;
    }

    public Calendar getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Calendar closeTime) {
        this.closeTime = closeTime;
    }

    public int getOpenStep() {
        return openStep;
    }

    public void setOpenStep(int openStep) {
        this.openStep = openStep;
    }

    public int getCloseStep() {
        return closeStep;
    }

    public void setCloseStep(int closeStep) {
        this.closeStep = closeStep;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
