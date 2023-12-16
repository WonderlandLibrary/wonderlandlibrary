// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.model_objects.specification;

import com.google.gson.JsonObject;
import se.michaelthelin.spotify.model_objects.IModelObject;
import java.util.Arrays;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.AbstractModelObject;

@JsonDeserialize(builder = Builder.class)
public class Category extends AbstractModelObject
{
    private final String href;
    private final Image[] icons;
    private final String id;
    private final String name;
    
    private Category(final Builder builder) {
        super(builder);
        this.href = builder.href;
        this.icons = builder.icons;
        this.id = builder.id;
        this.name = builder.name;
    }
    
    public String getHref() {
        return this.href;
    }
    
    public Image[] getIcons() {
        return this.icons;
    }
    
    public String getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    @Override
    public String toString() {
        return invokedynamic(makeConcatWithConstants:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;, this.href, Arrays.toString(this.icons), this.id, this.name);
    }
    
    @Override
    public Builder builder() {
        return new Builder();
    }
    
    public static final class Builder extends AbstractModelObject.Builder
    {
        private String href;
        private Image[] icons;
        private String id;
        private String name;
        
        public Builder setHref(final String href) {
            this.href = href;
            return this;
        }
        
        public Builder setIcons(final Image... icons) {
            this.icons = icons;
            return this;
        }
        
        public Builder setId(final String id) {
            this.id = id;
            return this;
        }
        
        public Builder setName(final String name) {
            this.name = name;
            return this;
        }
        
        @Override
        public Category build() {
            return new Category(this, null);
        }
    }
    
    public static final class JsonUtil extends AbstractModelObject.JsonUtil<Category>
    {
        @Override
        public Category createModelObject(final JsonObject jsonObject) {
            if (jsonObject == null || jsonObject.isJsonNull()) {
                return null;
            }
            return new Category.Builder().setHref(this.hasAndNotNull(jsonObject, "href") ? jsonObject.get("href").getAsString() : null).setIcons((Image[])(this.hasAndNotNull(jsonObject, "icons") ? ((Image[])new Image.JsonUtil().createModelObjectArray(jsonObject.getAsJsonArray("icons"))) : null)).setId(this.hasAndNotNull(jsonObject, "id") ? jsonObject.get("id").getAsString() : null).setName(this.hasAndNotNull(jsonObject, "name") ? jsonObject.get("name").getAsString() : null).build();
        }
    }
}
