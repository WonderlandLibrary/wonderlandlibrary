// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.model_objects.specification;

import com.google.gson.JsonElement;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import se.michaelthelin.spotify.model_objects.IModelObject;
import java.util.Map;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.AbstractModelObject;

@JsonDeserialize(builder = Builder.class)
public class ExternalUrl extends AbstractModelObject
{
    private final Map<String, String> externalUrls;
    
    private ExternalUrl(final Builder builder) {
        super(builder);
        this.externalUrls = builder.externalUrls;
    }
    
    public String get(final String key) {
        return this.externalUrls.get(key);
    }
    
    public Map<String, String> getExternalUrls() {
        return this.externalUrls;
    }
    
    @Override
    public String toString() {
        return invokedynamic(makeConcatWithConstants:(Ljava/util/Map;)Ljava/lang/String;, this.externalUrls);
    }
    
    @Override
    public Builder builder() {
        return new Builder();
    }
    
    public static final class Builder extends AbstractModelObject.Builder
    {
        private Map<String, String> externalUrls;
        
        public Builder setExternalUrls(final Map<String, String> externalUrls) {
            this.externalUrls = externalUrls;
            return this;
        }
        
        @Override
        public ExternalUrl build() {
            return new ExternalUrl(this, null);
        }
    }
    
    public static final class JsonUtil extends AbstractModelObject.JsonUtil<ExternalUrl>
    {
        @Override
        public ExternalUrl createModelObject(final JsonObject jsonObject) {
            if (jsonObject == null || jsonObject.isJsonNull()) {
                return null;
            }
            final Map<String, String> map = new Gson().fromJson(jsonObject, (Class<Map<String, String>>)Map.class);
            return new ExternalUrl.Builder().setExternalUrls(map).build();
        }
    }
}
