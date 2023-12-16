// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.model_objects.specification;

import com.google.gson.JsonObject;
import se.michaelthelin.spotify.model_objects.IModelObject;
import se.michaelthelin.spotify.enums.CopyrightType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.AbstractModelObject;

@JsonDeserialize(builder = Builder.class)
public class Copyright extends AbstractModelObject
{
    private final String text;
    private final CopyrightType type;
    
    private Copyright(final Builder builder) {
        super(builder);
        this.text = builder.text;
        this.type = builder.type;
    }
    
    public String getText() {
        return this.text;
    }
    
    public CopyrightType getType() {
        return this.type;
    }
    
    @Override
    public String toString() {
        return invokedynamic(makeConcatWithConstants:(Ljava/lang/String;Lse/michaelthelin/spotify/enums/CopyrightType;)Ljava/lang/String;, this.text, this.type);
    }
    
    @Override
    public Builder builder() {
        return new Builder();
    }
    
    public static final class Builder extends AbstractModelObject.Builder
    {
        private String text;
        private CopyrightType type;
        
        public Builder setText(final String text) {
            this.text = text;
            return this;
        }
        
        public Builder setType(final CopyrightType type) {
            this.type = type;
            return this;
        }
        
        @Override
        public Copyright build() {
            return new Copyright(this, null);
        }
    }
    
    public static final class JsonUtil extends AbstractModelObject.JsonUtil<Copyright>
    {
        @Override
        public Copyright createModelObject(final JsonObject jsonObject) {
            if (jsonObject == null || jsonObject.isJsonNull()) {
                return null;
            }
            return new Copyright.Builder().setText(this.hasAndNotNull(jsonObject, "text") ? jsonObject.get("text").getAsString() : null).setType(this.hasAndNotNull(jsonObject, "type") ? CopyrightType.keyOf(jsonObject.get("type").getAsString().toLowerCase()) : null).build();
        }
    }
}
