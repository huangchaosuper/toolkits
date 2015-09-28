package cn.huangchaosuper.toolkits.elasticsearch;

import cn.huangchaosuper.toolkits.Context;
import cn.huangchaosuper.toolkits.elasticsearch.export.Execution;
import org.junit.Test;

/**
 * Created by I311579 on 9/28/2015.
 */
public class ExportTest {

    Context context = new Context();
    @org.junit.Before
    public void setUp() throws Exception {
        context.setProperties(System.getProperties());
        System.getProperties().setProperty("step.open", "0");
        System.getProperties().setProperty("step.close", "9999");
        System.getProperties().setProperty("elasticsearch.cluster.name", "anywhere");
        System.getProperties().setProperty("elasticsearch.transport.address", "10.97.16.139");
        System.getProperties().setProperty("elasticsearch.index.name", "logstash-cn-2015.09.24");
        System.getProperties().setProperty("elasticsearch.export.filename","c:\\temp\\logstash-cn-2015.09.24.json");
    }
    @Test
    public void export() throws Exception{
        Execution execution = new Execution();
        execution.run(context);
        execution.finalized();
    }
}