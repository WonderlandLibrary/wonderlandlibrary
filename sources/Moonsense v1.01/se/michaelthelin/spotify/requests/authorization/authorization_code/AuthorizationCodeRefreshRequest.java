// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.requests.authorization.authorization_code;

import se.michaelthelin.spotify.requests.IRequest;
import se.michaelthelin.spotify.requests.AbstractRequest;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import java.io.IOException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.requests.authorization.AbstractAuthorizationRequest;

@JsonDeserialize(builder = Builder.class)
public class AuthorizationCodeRefreshRequest extends AbstractAuthorizationRequest<AuthorizationCodeCredentials>
{
    private AuthorizationCodeRefreshRequest(final Builder builder) {
        super(builder);
    }
    
    @Override
    public AuthorizationCodeCredentials execute() throws IOException, SpotifyWebApiException, ParseException {
        return new AuthorizationCodeCredentials.JsonUtil().createModelObject(this.postJson());
    }
    
    public static final class Builder extends AbstractAuthorizationRequest.Builder<AuthorizationCodeCredentials, Builder>
    {
        public Builder(final String clientId, final String clientSecret) {
            super(clientId, clientSecret);
        }
        
        public Builder grant_type(final String grant_type) {
            assert grant_type != null;
            assert grant_type.equals("refresh_token");
            return ((AbstractRequest.Builder<T, Builder>)this).setBodyParameter("grant_type", grant_type);
        }
        
        public Builder refresh_token(final String refresh_token) {
            assert refresh_token != null;
            assert !refresh_token.equals("");
            return ((AbstractRequest.Builder<T, Builder>)this).setBodyParameter("refresh_token", refresh_token);
        }
        
        @Override
        public AuthorizationCodeRefreshRequest build() {
            this.setContentType(ContentType.APPLICATION_FORM_URLENCODED);
            this.setHost("accounts.spotify.com");
            this.setPort(Integer.valueOf(443));
            this.setScheme("https");
            this.setPath("/api/token");
            return new AuthorizationCodeRefreshRequest(this, null);
        }
        
        @Override
        protected Builder self() {
            return this;
        }
    }
}
