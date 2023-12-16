// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.model_objects.credentials.error;

import com.google.gson.JsonObject;
import se.michaelthelin.spotify.model_objects.IModelObject;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.AbstractModelObject;

@JsonDeserialize(builder = Builder.class)
public class AuthenticationError extends AbstractModelObject
{
    private final String error;
    private final String error_description;
    
    private AuthenticationError(final Builder builder) {
        super(builder);
        this.error = builder.error;
        this.error_description = builder.error_description;
    }
    
    public String getError() {
        return this.error;
    }
    
    public String getError_description() {
        return this.error_description;
    }
    
    @Override
    public String toString() {
        return invokedynamic(makeConcatWithConstants:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;, this.error, this.error_description);
    }
    
    @Override
    public Builder builder() {
        return new Builder();
    }
    
    public static final class Builder extends AbstractModelObject.Builder
    {
        private String error;
        private String error_description;
        
        public Builder setError(final String error) {
            this.error = error;
            return this;
        }
        
        public Builder setError_description(final String error_description) {
            this.error_description = error_description;
            return this;
        }
        
        @Override
        public AuthenticationError build() {
            return new AuthenticationError(this, null);
        }
    }
    
    public static final class JsonUtil extends AbstractModelObject.JsonUtil<AuthenticationError>
    {
        @Override
        public AuthenticationError createModelObject(final JsonObject jsonObject) {
            if (jsonObject == null || jsonObject.isJsonNull()) {
                return null;
            }
            return new AuthenticationError.Builder().setError(this.hasAndNotNull(jsonObject, "error") ? jsonObject.get("error").getAsString() : null).setError_description(this.hasAndNotNull(jsonObject, "error_description") ? jsonObject.get("error_description").getAsString() : null).build();
        }
    }
}
