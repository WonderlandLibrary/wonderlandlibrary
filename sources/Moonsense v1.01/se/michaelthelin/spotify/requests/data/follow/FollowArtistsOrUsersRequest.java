// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.requests.data.follow;

import se.michaelthelin.spotify.requests.IRequest;
import se.michaelthelin.spotify.requests.AbstractRequest;
import org.apache.hc.core5.http.ContentType;
import com.google.gson.JsonArray;
import se.michaelthelin.spotify.enums.ModelObjectType;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import java.io.IOException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.requests.data.AbstractDataRequest;

@JsonDeserialize(builder = Builder.class)
public class FollowArtistsOrUsersRequest extends AbstractDataRequest<String>
{
    private FollowArtistsOrUsersRequest(final Builder builder) {
        super(builder);
    }
    
    @Override
    public String execute() throws IOException, SpotifyWebApiException, ParseException {
        return this.putJson();
    }
    
    public static final class Builder extends AbstractDataRequest.Builder<String, Builder>
    {
        public Builder(final String accessToken) {
            super(accessToken);
        }
        
        public Builder type(final ModelObjectType type) {
            assert type != null;
            assert type.getType().equals("artist") || type.getType().equals("user");
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("type", type);
        }
        
        public Builder ids(final String ids) {
            assert ids != null;
            assert ids.split(",").length <= 50;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("ids", ids);
        }
        
        public Builder ids(final JsonArray ids) {
            assert ids != null;
            assert !ids.isJsonNull();
            assert ids.size() <= 50;
            return ((AbstractRequest.Builder<T, Builder>)this).setBodyParameter("ids", ids);
        }
        
        @Override
        public FollowArtistsOrUsersRequest build() {
            this.setContentType(ContentType.APPLICATION_JSON);
            this.setPath("/v1/me/following");
            return new FollowArtistsOrUsersRequest(this, null);
        }
        
        @Override
        protected Builder self() {
            return this;
        }
    }
}
