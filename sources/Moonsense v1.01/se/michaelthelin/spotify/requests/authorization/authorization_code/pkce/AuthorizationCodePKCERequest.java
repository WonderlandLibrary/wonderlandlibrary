// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.requests.authorization.authorization_code.pkce;

import se.michaelthelin.spotify.requests.IRequest;
import org.apache.hc.core5.http.ContentType;
import java.net.URI;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import java.io.IOException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.requests.AbstractRequest;

@JsonDeserialize(builder = Builder.class)
public class AuthorizationCodePKCERequest extends AbstractRequest<AuthorizationCodeCredentials>
{
    private AuthorizationCodePKCERequest(final Builder builder) {
        super(builder);
    }
    
    @Override
    public AuthorizationCodeCredentials execute() throws IOException, SpotifyWebApiException, ParseException {
        return new AuthorizationCodeCredentials.JsonUtil().createModelObject(this.postJson());
    }
    
    public static final class Builder extends AbstractRequest.Builder<AuthorizationCodeCredentials, Builder>
    {
        public Builder client_id(final String client_id) {
            assert client_id != null;
            assert !client_id.equals("");
            return ((AbstractRequest.Builder<T, Builder>)this).setBodyParameter("client_id", client_id);
        }
        
        public Builder grant_type(final String grant_type) {
            assert grant_type != null;
            assert grant_type.equals("authorization_code");
            return ((AbstractRequest.Builder<T, Builder>)this).setBodyParameter("grant_type", grant_type);
        }
        
        public Builder code(final String code) {
            assert code != null;
            assert !code.equals("");
            return ((AbstractRequest.Builder<T, Builder>)this).setBodyParameter("code", code);
        }
        
        public Builder redirect_uri(final URI redirect_uri) {
            assert redirect_uri != null;
            return ((AbstractRequest.Builder<T, Builder>)this).setBodyParameter("redirect_uri", redirect_uri.toString());
        }
        
        public Builder code_verifier(final String code_verifier) {
            assert code_verifier != null;
            assert !code_verifier.equals("");
            return ((AbstractRequest.Builder<T, Builder>)this).setBodyParameter("code_verifier", code_verifier);
        }
        
        @Override
        public AuthorizationCodePKCERequest build() {
            this.setContentType(ContentType.APPLICATION_FORM_URLENCODED);
            this.setHost("accounts.spotify.com");
            this.setPort(Integer.valueOf(443));
            this.setScheme("https");
            this.setPath("/api/token");
            return new AuthorizationCodePKCERequest(this, null);
        }
        
        @Override
        protected Builder self() {
            return this;
        }
    }
}
