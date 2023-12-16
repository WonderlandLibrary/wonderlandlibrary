// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.model_objects.specification;

import com.google.gson.JsonObject;
import se.michaelthelin.spotify.model_objects.IModelObject;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.AbstractModelObject;

@JsonDeserialize(builder = Builder.class)
public class Image extends AbstractModelObject
{
    private final Integer height;
    private final String url;
    private final Integer width;
    
    private Image(final Builder builder) {
        super(builder);
        this.height = builder.height;
        this.url = builder.url;
        this.width = builder.width;
    }
    
    public Integer getHeight() {
        return this.height;
    }
    
    public String getUrl() {
        return this.url;
    }
    
    public Integer getWidth() {
        return this.width;
    }
    
    @Override
    public String toString() {
        return invokedynamic(makeConcatWithConstants:(Ljava/lang/Integer;Ljava/lang/String;Ljava/lang/Integer;)Ljava/lang/String;, this.height, this.url, this.width);
    }
    
    @Override
    public Builder builder() {
        return new Builder();
    }
    
    public static final class Builder extends AbstractModelObject.Builder
    {
        private Integer height;
        private String url;
        private Integer width;
        
        public Builder setHeight(final Integer height) {
            this.height = height;
            return this;
        }
        
        public Builder setUrl(final String url) {
            this.url = url;
            return this;
        }
        
        public Builder setWidth(final Integer width) {
            this.width = width;
            return this;
        }
        
        @Override
        public Image build() {
            return new Image(this, null);
        }
    }
    
    public static final class JsonUtil extends AbstractModelObject.JsonUtil<Image>
    {
        @Override
        public Image createModelObject(final JsonObject jsonObject) {
            if (jsonObject == null || jsonObject.isJsonNull()) {
                return null;
            }
            return new Image.Builder().setHeight(this.hasAndNotNull(jsonObject, "height") ? Integer.valueOf(jsonObject.get("height").getAsInt()) : null).setUrl(this.hasAndNotNull(jsonObject, "url") ? jsonObject.get("url").getAsString() : null).setWidth(this.hasAndNotNull(jsonObject, "width") ? Integer.valueOf(jsonObject.get("width").getAsInt()) : null).build();
        }
    }
}
