// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.model_objects.credentials;

import com.google.gson.JsonObject;
import se.michaelthelin.spotify.model_objects.IModelObject;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.AbstractModelObject;

@JsonDeserialize(builder = Builder.class)
public class ClientCredentials extends AbstractModelObject
{
    private final String accessToken;
    private final String tokenType;
    private final Integer expiresIn;
    
    private ClientCredentials(final Builder builder) {
        super(builder);
        this.accessToken = builder.accessToken;
        this.tokenType = builder.tokenType;
        this.expiresIn = builder.expiresIn;
    }
    
    public String getAccessToken() {
        return this.accessToken;
    }
    
    public String getTokenType() {
        return this.tokenType;
    }
    
    public Integer getExpiresIn() {
        return this.expiresIn;
    }
    
    @Override
    public String toString() {
        return invokedynamic(makeConcatWithConstants:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;)Ljava/lang/String;, this.accessToken, this.tokenType, this.expiresIn);
    }
    
    @Override
    public Builder builder() {
        return new Builder();
    }
    
    public static final class Builder extends AbstractModelObject.Builder
    {
        private String accessToken;
        private String tokenType;
        private Integer expiresIn;
        
        public Builder setAccessToken(final String accessToken) {
            this.accessToken = accessToken;
            return this;
        }
        
        public Builder setTokenType(final String tokenType) {
            this.tokenType = tokenType;
            return this;
        }
        
        public Builder setExpiresIn(final Integer expiresIn) {
            this.expiresIn = expiresIn;
            return this;
        }
        
        @Override
        public ClientCredentials build() {
            return new ClientCredentials(this, null);
        }
    }
    
    public static final class JsonUtil extends AbstractModelObject.JsonUtil<ClientCredentials>
    {
        @Override
        public ClientCredentials createModelObject(final JsonObject jsonObject) {
            if (jsonObject == null || jsonObject.isJsonNull()) {
                return null;
            }
            return new ClientCredentials.Builder().setAccessToken(this.hasAndNotNull(jsonObject, "access_token") ? jsonObject.get("access_token").getAsString() : null).setTokenType(this.hasAndNotNull(jsonObject, "token_type") ? jsonObject.get("token_type").getAsString() : null).setExpiresIn(this.hasAndNotNull(jsonObject, "expires_in") ? Integer.valueOf(jsonObject.get("expires_in").getAsInt()) : null).build();
        }
    }
}
