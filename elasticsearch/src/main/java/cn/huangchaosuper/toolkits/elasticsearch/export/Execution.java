package cn.huangchaosuper.toolkits.elasticsearch.export;

import cn.huangchaosuper.toolkits.Context;
import cn.huangchaosuper.toolkits.IStep;
import cn.huangchaosuper.toolkits.TaskStep;

/**
 * Created by I311579 on 9/28/2015.
 */
@TaskStep(active = true, step = 1)
public class Execution implements IStep {

    @Override
    public boolean run(Context context) {
        return false;
    }

    @Override
    public void finalized() {

    }
}
