// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.requests.data.users_profile;

import se.michaelthelin.spotify.requests.IRequest;
import se.michaelthelin.spotify.requests.AbstractRequest;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import java.io.IOException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.specification.User;
import se.michaelthelin.spotify.requests.data.AbstractDataRequest;

@JsonDeserialize(builder = Builder.class)
public class GetCurrentUsersProfileRequest extends AbstractDataRequest<User>
{
    private GetCurrentUsersProfileRequest(final Builder builder) {
        super(builder);
    }
    
    @Override
    public User execute() throws IOException, SpotifyWebApiException, ParseException {
        return new User.JsonUtil().createModelObject(this.getJson());
    }
    
    public static final class Builder extends AbstractDataRequest.Builder<User, Builder>
    {
        public Builder(final String accessToken) {
            super(accessToken);
        }
        
        @Override
        public GetCurrentUsersProfileRequest build() {
            this.setPath("/v1/me");
            return new GetCurrentUsersProfileRequest(this, null);
        }
        
        @Override
        protected Builder self() {
            return this;
        }
    }
}
