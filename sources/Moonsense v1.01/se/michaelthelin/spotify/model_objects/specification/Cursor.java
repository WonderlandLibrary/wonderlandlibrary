// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.model_objects.specification;

import com.google.gson.JsonObject;
import se.michaelthelin.spotify.model_objects.IModelObject;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.AbstractModelObject;

@JsonDeserialize(builder = Builder.class)
public class Cursor extends AbstractModelObject
{
    private final String after;
    
    private Cursor(final Builder builder) {
        super(builder);
        this.after = builder.after;
    }
    
    public String getAfter() {
        return this.after;
    }
    
    @Override
    public String toString() {
        return invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, this.after);
    }
    
    @Override
    public Builder builder() {
        return new Builder();
    }
    
    public static final class Builder extends AbstractModelObject.Builder
    {
        private String after;
        
        public Builder setAfter(final String after) {
            this.after = after;
            return this;
        }
        
        @Override
        public Cursor build() {
            return new Cursor(this, null);
        }
    }
    
    public static final class JsonUtil extends AbstractModelObject.JsonUtil<Cursor>
    {
        @Override
        public Cursor createModelObject(final JsonObject jsonObject) {
            if (jsonObject == null || jsonObject.isJsonNull()) {
                return null;
            }
            return new Cursor.Builder().setAfter(this.hasAndNotNull(jsonObject, "after") ? jsonObject.get("after").getAsString() : null).build();
        }
    }
}
