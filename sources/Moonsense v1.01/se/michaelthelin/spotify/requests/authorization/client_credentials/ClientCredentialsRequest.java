// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.requests.authorization.client_credentials;

import se.michaelthelin.spotify.requests.IRequest;
import se.michaelthelin.spotify.requests.AbstractRequest;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import java.io.IOException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.requests.authorization.AbstractAuthorizationRequest;

@JsonDeserialize(builder = Builder.class)
public class ClientCredentialsRequest extends AbstractAuthorizationRequest<ClientCredentials>
{
    public ClientCredentialsRequest(final Builder builder) {
        super(builder);
    }
    
    @Override
    public ClientCredentials execute() throws IOException, SpotifyWebApiException, ParseException {
        return new ClientCredentials.JsonUtil().createModelObject(this.postJson());
    }
    
    public static final class Builder extends AbstractAuthorizationRequest.Builder<ClientCredentials, Builder>
    {
        public Builder(final String clientId, final String clientSecret) {
            super(clientId, clientSecret);
        }
        
        public Builder grant_type(final String grant_type) {
            assert grant_type != null;
            assert grant_type.equals("client_credentials");
            return ((AbstractRequest.Builder<T, Builder>)this).setBodyParameter("grant_type", grant_type);
        }
        
        @Override
        public ClientCredentialsRequest build() {
            this.setContentType(ContentType.APPLICATION_FORM_URLENCODED);
            this.setHost("accounts.spotify.com");
            this.setPort(Integer.valueOf(443));
            this.setScheme("https");
            this.setPath("/api/token");
            return new ClientCredentialsRequest(this);
        }
        
        @Override
        protected Builder self() {
            return this;
        }
    }
}
