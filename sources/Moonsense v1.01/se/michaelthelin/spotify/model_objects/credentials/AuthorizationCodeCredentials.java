// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.model_objects.credentials;

import com.google.gson.JsonObject;
import se.michaelthelin.spotify.model_objects.IModelObject;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.AbstractModelObject;

@JsonDeserialize(builder = Builder.class)
public class AuthorizationCodeCredentials extends AbstractModelObject
{
    private final String accessToken;
    private final String tokenType;
    private final String scope;
    private final Integer expiresIn;
    private final String refreshToken;
    
    private AuthorizationCodeCredentials(final Builder builder) {
        super(builder);
        this.accessToken = builder.accessToken;
        this.tokenType = builder.tokenType;
        this.scope = builder.scope;
        this.expiresIn = builder.expiresIn;
        this.refreshToken = builder.refreshToken;
    }
    
    public String getAccessToken() {
        return this.accessToken;
    }
    
    public String getTokenType() {
        return this.tokenType;
    }
    
    public String getScope() {
        return this.scope;
    }
    
    public Integer getExpiresIn() {
        return this.expiresIn;
    }
    
    public String getRefreshToken() {
        return this.refreshToken;
    }
    
    @Override
    public String toString() {
        return invokedynamic(makeConcatWithConstants:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/String;)Ljava/lang/String;, this.accessToken, this.tokenType, this.scope, this.expiresIn, this.refreshToken);
    }
    
    @Override
    public Builder builder() {
        return new Builder();
    }
    
    public static final class Builder extends AbstractModelObject.Builder
    {
        private String accessToken;
        private String tokenType;
        private String scope;
        private Integer expiresIn;
        private String refreshToken;
        
        public Builder setAccessToken(final String accessToken) {
            this.accessToken = accessToken;
            return this;
        }
        
        public Builder setTokenType(final String tokenType) {
            this.tokenType = tokenType;
            return this;
        }
        
        public Builder setScope(final String scope) {
            this.scope = scope;
            return this;
        }
        
        public Builder setExpiresIn(final Integer expiresIn) {
            this.expiresIn = expiresIn;
            return this;
        }
        
        public Builder setRefreshToken(final String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }
        
        @Override
        public AuthorizationCodeCredentials build() {
            return new AuthorizationCodeCredentials(this, null);
        }
    }
    
    public static final class JsonUtil extends AbstractModelObject.JsonUtil<AuthorizationCodeCredentials>
    {
        @Override
        public AuthorizationCodeCredentials createModelObject(final JsonObject jsonObject) {
            if (jsonObject == null || jsonObject.isJsonNull()) {
                return null;
            }
            return new AuthorizationCodeCredentials.Builder().setAccessToken(this.hasAndNotNull(jsonObject, "access_token") ? jsonObject.get("access_token").getAsString() : null).setTokenType(this.hasAndNotNull(jsonObject, "token_type") ? jsonObject.get("token_type").getAsString() : null).setScope(this.hasAndNotNull(jsonObject, "scope") ? jsonObject.get("scope").getAsString() : null).setExpiresIn(this.hasAndNotNull(jsonObject, "expires_in") ? Integer.valueOf(jsonObject.get("expires_in").getAsInt()) : null).setRefreshToken(this.hasAndNotNull(jsonObject, "refresh_token") ? jsonObject.get("refresh_token").getAsString() : null).build();
        }
    }
}
