// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.intent.cloud;

import com.google.gson.JsonObject;
import java.util.function.Consumer;
import java.util.HashMap;
import java.util.Map;

public class Request
{
    private final Map<String, String> getMap;
    private final Map<String, String> postMap;
    private final String url;
    
    public Request(final String url) {
        this.getMap = new HashMap<String, String>();
        this.postMap = new HashMap<String, String>();
        this.url = url;
    }
    
    public Request modifyGetMap(final Consumer<Map<String, String>> map) {
        map.accept(this.getMap);
        return this;
    }
    
    public Request modifyPostMap(final Consumer<Map<String, String>> map) {
        map.accept(this.postMap);
        return this;
    }
    
    public JsonObject post() {
        final String apiKey = Cloud.getApiKey();
        this.getMap.put("key", apiKey);
        this.getMap.put("product_code", Cloud.getProductCode());
        final JsonObject response = Cloud.request(this.url, this.getMap, this.postMap);
        if (response != null && response.has("response")) {
            return response.isJsonObject() ? response.getAsJsonObject() : response;
        }
        return null;
    }
}
