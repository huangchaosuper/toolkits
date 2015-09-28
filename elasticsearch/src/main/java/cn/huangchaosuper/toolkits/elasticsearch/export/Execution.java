package cn.huangchaosuper.toolkits.elasticsearch.export;

import cn.huangchaosuper.toolkits.Context;
import cn.huangchaosuper.toolkits.IStep;
import cn.huangchaosuper.toolkits.TaskStep;
import cn.huangchaosuper.toolkits.Utils;
import cn.huangchaosuper.toolkits.elasticsearch.serializer.ElasticSearchElement;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.internal.InternalSearchHit;
import org.omg.CORBA.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by I311579 on 9/28/2015.
 */
@TaskStep(active = true, step = 1)
public class Execution implements IStep {
    private static Logger logger = LoggerFactory.getLogger(Execution.class);
    Client client = null;

    @Override
    public boolean run(Context context) {
        Settings settings = ImmutableSettings.settingsBuilder()
                .put("cluster.name", Utils.getStringConfigVaule(context.getProperties(), "elasticsearch.cluster.name"))
                .build();

        Client client = new TransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(Utils.getStringConfigVaule(context.getProperties(), "elasticsearch.transport.address"), 9300));
        SearchResponse scrollResp = client.prepareSearch(Utils.getStringConfigVaule(context.getProperties(), "elasticsearch.index.name"))
                .setSearchType(SearchType.SCAN)
                .setScroll(new TimeValue(60000))
                .setSize(100).execute().actionGet();
        File file = new File(Utils.getStringConfigVaule(context.getProperties(), "elasticsearch.export.filename"));
        try {
            FileWriter fileWritter = new FileWriter(file, true);
            Integer length = scrollResp.getHits().getHits().length;
            while (true) {
                Arrays.asList(scrollResp.getHits().getHits()).stream().forEach((hit) -> {
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    gsonBuilder.registerTypeAdapter(InternalSearchHit.class, new ElasticSearchElement());
                    Gson gson = gsonBuilder.create();
                    try {
                        fileWritter.write(gson.toJson(hit) + System.getProperty("line.separator"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                fileWritter.flush();
                scrollResp = client.prepareSearchScroll(scrollResp.getScrollId()).setScroll(new TimeValue(600000)).execute().actionGet();
                length += scrollResp.getHits().getHits().length;
                logger.info("export size is {}",length);
                if (scrollResp.getHits().getHits().length == 0) {
                    break;
                }
            }
            fileWritter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void finalized() {
        if (client != null) {
            client.close();
        }
    }
}
