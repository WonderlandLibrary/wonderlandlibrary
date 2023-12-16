// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.model_objects.specification;

import com.google.gson.JsonObject;
import se.michaelthelin.spotify.model_objects.IModelObject;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.AbstractModelObject;

@JsonDeserialize(builder = Builder.class)
public class Followers extends AbstractModelObject
{
    private final String href;
    private final Integer total;
    
    private Followers(final Builder builder) {
        super(builder);
        this.href = builder.href;
        this.total = builder.total;
    }
    
    public String getHref() {
        return this.href;
    }
    
    public Integer getTotal() {
        return this.total;
    }
    
    @Override
    public String toString() {
        return invokedynamic(makeConcatWithConstants:(Ljava/lang/String;Ljava/lang/Integer;)Ljava/lang/String;, this.href, this.total);
    }
    
    @Override
    public Builder builder() {
        return new Builder();
    }
    
    public static final class Builder extends AbstractModelObject.Builder
    {
        private String href;
        private Integer total;
        
        public Builder setHref(final String href) {
            this.href = href;
            return this;
        }
        
        public Builder setTotal(final Integer total) {
            this.total = total;
            return this;
        }
        
        @Override
        public Followers build() {
            return new Followers(this, null);
        }
    }
    
    public static final class JsonUtil extends AbstractModelObject.JsonUtil<Followers>
    {
        @Override
        public Followers createModelObject(final JsonObject jsonObject) {
            if (jsonObject == null || jsonObject.isJsonNull()) {
                return null;
            }
            return new Followers.Builder().setHref(this.hasAndNotNull(jsonObject, "href") ? jsonObject.get("href").getAsString() : null).setTotal(this.hasAndNotNull(jsonObject, "total") ? Integer.valueOf(jsonObject.get("total").getAsInt()) : null).build();
        }
    }
}
