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
public class GetUsersProfileRequest extends AbstractDataRequest<User>
{
    private GetUsersProfileRequest(final Builder builder) {
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
        
        public Builder user_id(final String user_id) {
            assert user_id != null;
            assert !user_id.equals("");
            return ((AbstractRequest.Builder<T, Builder>)this).setPathParameter("user_id", user_id);
        }
        
        @Override
        public GetUsersProfileRequest build() {
            this.setPath("/v1/users/{user_id}");
            return new GetUsersProfileRequest(this, null);
        }
        
        @Override
        protected Builder self() {
            return this;
        }
    }
}
