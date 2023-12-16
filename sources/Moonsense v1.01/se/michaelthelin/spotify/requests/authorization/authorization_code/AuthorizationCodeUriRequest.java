// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.requests.authorization.authorization_code;

import se.michaelthelin.spotify.requests.IRequest;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import java.io.IOException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.net.URI;
import se.michaelthelin.spotify.requests.AbstractRequest;

@JsonDeserialize(builder = Builder.class)
public class AuthorizationCodeUriRequest extends AbstractRequest<URI>
{
    private AuthorizationCodeUriRequest(final Builder builder) {
        super(builder);
    }
    
    @Override
    public URI execute() {
        return this.getUri();
    }
    
    public static final class Builder extends AbstractRequest.Builder<URI, Builder>
    {
        public Builder client_id(final String client_id) {
            assert client_id != null;
            assert !client_id.equals("");
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("client_id", client_id);
        }
        
        public Builder response_type(final String response_type) {
            assert response_type != null;
            assert response_type.equals("code");
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("response_type", response_type);
        }
        
        public Builder redirect_uri(final URI redirect_uri) {
            assert redirect_uri != null;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("redirect_uri", redirect_uri.toString());
        }
        
        public Builder code_challenge_method(final String code_challenge_method) {
            assert code_challenge_method != null;
            assert code_challenge_method.equals("S256");
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("code_challenge_method", code_challenge_method);
        }
        
        public Builder code_challenge(final String code_challenge) {
            assert code_challenge != null;
            assert !code_challenge.equals("");
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("code_challenge", code_challenge);
        }
        
        public Builder state(final String state) {
            assert state != null;
            assert !state.equals("");
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("state", state);
        }
        
        public Builder scope(final String scope) {
            assert scope != null;
            assert !scope.equals("");
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("scope", scope);
        }
        
        public Builder show_dialog(final boolean show_dialog) {
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("show_dialog", Boolean.valueOf(show_dialog));
        }
        
        @Override
        public AuthorizationCodeUriRequest build() {
            this.setHost("accounts.spotify.com");
            this.setPort(Integer.valueOf(443));
            this.setScheme("https");
            this.setPath("/authorize");
            return new AuthorizationCodeUriRequest(this, null);
        }
        
        @Override
        protected Builder self() {
            return this;
        }
    }
}
