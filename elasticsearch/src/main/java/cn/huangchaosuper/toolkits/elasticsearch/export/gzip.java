package cn.huangchaosuper.toolkits.elasticsearch.export;

import cn.huangchaosuper.toolkits.Context;
import cn.huangchaosuper.toolkits.IStep;
import cn.huangchaosuper.toolkits.TaskStep;
import cn.huangchaosuper.toolkits.Utils;

import java.io.*;
import java.util.zip.GZIPOutputStream;

/**
 * Created by I311579 on 9/28/2015.
 */
@TaskStep(active = true, step = 2)
public class Gzip implements IStep {
    @Override
    public boolean run(Context context) {
        if (!Utils.getBooleanConfigVaule(context.getProperties(), "elasticsearch.cluster.name")) {
            return true;
        }
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(Utils.getStringConfigVaule(context.getProperties(), "elasticsearch.export.filename"));
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new GZIPOutputStream(new FileOutputStream(Utils.getStringConfigVaule(context.getProperties(), "elasticsearch.export.filename") + ".gz")));
            int c = 0;
            while ((c = bufferedReader.read()) != -1) {
                bufferedOutputStream.write(c);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public void finalized() {

    }
}
