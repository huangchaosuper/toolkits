package cn.huangchaosuper.toolkits.elasticsearch.serializer;

import com.google.gson.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.internal.InternalSearchHit;

import java.lang.reflect.Type;

/**
 * Created by I311579 on 9/28/2015.
 */
public class ElasticSearchElement implements JsonSerializer<InternalSearchHit> {
    @Override
    public JsonElement serialize(InternalSearchHit src, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject hit = new JsonObject();
        hit.addProperty("_index", src.getIndex());
        hit.addProperty("_id", src.getId());
        hit.addProperty("_type", src.getType());
        hit.addProperty("_version", src.getVersion());
        JsonParser jsonParser = new JsonParser();
        hit.add("_source", jsonParser.parse((new GsonBuilder()).create().toJson(src.getSource())));
        return hit;
    }
}
