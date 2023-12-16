// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.model_objects.specification;

import com.google.gson.JsonObject;
import se.michaelthelin.spotify.model_objects.IModelObject;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.AbstractModelObject;

@JsonDeserialize(builder = Builder.class)
public class Error extends AbstractModelObject
{
    private final Integer status;
    private final String message;
    
    private Error(final Builder builder) {
        super(builder);
        this.status = builder.status;
        this.message = builder.message;
    }
    
    public Integer getStatus() {
        return this.status;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    @Override
    public String toString() {
        return invokedynamic(makeConcatWithConstants:(Ljava/lang/Integer;Ljava/lang/String;)Ljava/lang/String;, this.status, this.message);
    }
    
    @Override
    public Builder builder() {
        return new Builder();
    }
    
    public static final class Builder extends AbstractModelObject.Builder
    {
        private Integer status;
        private String message;
        
        public Builder setStatus(final Integer status) {
            this.status = status;
            return this;
        }
        
        public Builder setMessage(final String message) {
            this.message = message;
            return this;
        }
        
        @Override
        public Error build() {
            return new Error(this, null);
        }
    }
    
    public static final class JsonUtil extends AbstractModelObject.JsonUtil<Error>
    {
        @Override
        public Error createModelObject(final JsonObject jsonObject) {
            if (jsonObject == null || jsonObject.isJsonNull()) {
                return null;
            }
            return new Error.Builder().setStatus(this.hasAndNotNull(jsonObject, "status") ? Integer.valueOf(jsonObject.get("status").getAsInt()) : null).setMessage(this.hasAndNotNull(jsonObject, "message") ? jsonObject.get("message").getAsString() : null).build();
        }
    }
}
