// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.model_objects.specification;

import com.google.gson.JsonObject;
import se.michaelthelin.spotify.model_objects.IModelObject;
import se.michaelthelin.spotify.enums.ModelObjectType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.AbstractModelObject;

@JsonDeserialize(builder = Builder.class)
public class RecommendationsSeed extends AbstractModelObject
{
    private final Integer afterFilteringSize;
    private final Integer afterRelinkingSize;
    private final String href;
    private final String id;
    private final Integer initialPoolSize;
    private final ModelObjectType type;
    
    private RecommendationsSeed(final Builder builder) {
        super(builder);
        this.afterFilteringSize = builder.afterFilteringSize;
        this.afterRelinkingSize = builder.afterRelinkingSize;
        this.href = builder.href;
        this.id = builder.id;
        this.initialPoolSize = builder.initialPoolSize;
        this.type = builder.type;
    }
    
    public Integer getAfterFilteringSize() {
        return this.afterFilteringSize;
    }
    
    public Integer getAfterRelinkingSize() {
        return this.afterRelinkingSize;
    }
    
    public String getHref() {
        return this.href;
    }
    
    public String getId() {
        return this.id;
    }
    
    public Integer getInitialPoolSize() {
        return this.initialPoolSize;
    }
    
    public ModelObjectType getType() {
        return this.type;
    }
    
    @Override
    public String toString() {
        return invokedynamic(makeConcatWithConstants:(Ljava/lang/Integer;Ljava/lang/Integer;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;Lse/michaelthelin/spotify/enums/ModelObjectType;)Ljava/lang/String;, this.afterFilteringSize, this.afterRelinkingSize, this.href, this.id, this.initialPoolSize, this.type);
    }
    
    @Override
    public Builder builder() {
        return new Builder();
    }
    
    public static final class Builder extends AbstractModelObject.Builder
    {
        private Integer afterFilteringSize;
        private Integer afterRelinkingSize;
        private String href;
        private String id;
        private Integer initialPoolSize;
        private ModelObjectType type;
        
        public Builder setAfterFilteringSize(final Integer afterFilteringSize) {
            this.afterFilteringSize = afterFilteringSize;
            return this;
        }
        
        public Builder setAfterRelinkingSize(final Integer afterRelinkingSize) {
            this.afterRelinkingSize = afterRelinkingSize;
            return this;
        }
        
        public Builder setHref(final String href) {
            this.href = href;
            return this;
        }
        
        public Builder setId(final String id) {
            this.id = id;
            return this;
        }
        
        public Builder setInitialPoolSize(final Integer initialPoolSize) {
            this.initialPoolSize = initialPoolSize;
            return this;
        }
        
        public Builder setType(final ModelObjectType type) {
            this.type = type;
            return this;
        }
        
        @Override
        public RecommendationsSeed build() {
            return new RecommendationsSeed(this, null);
        }
    }
    
    public static final class JsonUtil extends AbstractModelObject.JsonUtil<RecommendationsSeed>
    {
        @Override
        public RecommendationsSeed createModelObject(final JsonObject jsonObject) {
            if (jsonObject == null || jsonObject.isJsonNull()) {
                return null;
            }
            return new RecommendationsSeed.Builder().setAfterFilteringSize(this.hasAndNotNull(jsonObject, "afterFilteringSize") ? Integer.valueOf(jsonObject.get("afterFilteringSize").getAsInt()) : null).setAfterRelinkingSize(this.hasAndNotNull(jsonObject, "afterRelinkingSize") ? Integer.valueOf(jsonObject.get("afterRelinkingSize").getAsInt()) : null).setHref(this.hasAndNotNull(jsonObject, "href") ? jsonObject.get("href").getAsString() : null).setId(this.hasAndNotNull(jsonObject, "id") ? jsonObject.get("id").getAsString() : null).setInitialPoolSize(this.hasAndNotNull(jsonObject, "initialPoolSize") ? Integer.valueOf(jsonObject.get("initialPoolSize").getAsInt()) : null).setType(this.hasAndNotNull(jsonObject, "type") ? ModelObjectType.keyOf(jsonObject.get("type").getAsString().toLowerCase()) : null).build();
        }
    }
}
