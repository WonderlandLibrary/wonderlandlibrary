// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.model_objects.miscellaneous;

import com.google.gson.JsonObject;
import se.michaelthelin.spotify.model_objects.IModelObject;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.AbstractModelObject;

@JsonDeserialize(builder = Builder.class)
public class Restrictions extends AbstractModelObject
{
    private final String reason;
    
    private Restrictions(final Builder builder) {
        super(builder);
        this.reason = builder.reason;
    }
    
    public String getReason() {
        return this.reason;
    }
    
    @Override
    public String toString() {
        return invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, this.reason);
    }
    
    @Override
    public Builder builder() {
        return new Builder();
    }
    
    public static final class Builder extends AbstractModelObject.Builder
    {
        private String reason;
        
        public Builder setReason(final String reason) {
            this.reason = reason;
            return this;
        }
        
        @Override
        public Restrictions build() {
            return new Restrictions(this, null);
        }
    }
    
    public static final class JsonUtil extends AbstractModelObject.JsonUtil<Restrictions>
    {
        @Override
        public Restrictions createModelObject(final JsonObject jsonObject) {
            if (jsonObject == null || jsonObject.isJsonNull()) {
                return null;
            }
            return new Restrictions.Builder().setReason(this.hasAndNotNull(jsonObject, "reason") ? jsonObject.get("reason").getAsString() : null).build();
        }
    }
}
