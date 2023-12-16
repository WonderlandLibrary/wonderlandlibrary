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
public class ExternalId extends AbstractModelObject
{
    private final Map<String, String> externalIds;
    
    private ExternalId(final Builder builder) {
        super(builder);
        this.externalIds = builder.externalIds;
    }
    
    public Map<String, String> getExternalIds() {
        return this.externalIds;
    }
    
    @Override
    public String toString() {
        return invokedynamic(makeConcatWithConstants:(Ljava/util/Map;)Ljava/lang/String;, this.externalIds);
    }
    
    @Override
    public Builder builder() {
        return new Builder();
    }
    
    public static final class Builder extends AbstractModelObject.Builder
    {
        private Map<String, String> externalIds;
        
        public Builder setExternalIds(final Map<String, String> externalIds) {
            this.externalIds = externalIds;
            return this;
        }
        
        @Override
        public ExternalId build() {
            return new ExternalId(this, null);
        }
    }
    
    public static final class JsonUtil extends AbstractModelObject.JsonUtil<ExternalId>
    {
        @Override
        public ExternalId createModelObject(final JsonObject jsonObject) {
            if (jsonObject == null || jsonObject.isJsonNull()) {
                return null;
            }
            final Map<String, String> map = new Gson().fromJson(jsonObject, (Class<Map<String, String>>)Map.class);
            return new ExternalId.Builder().setExternalIds(map).build();
        }
    }
}
