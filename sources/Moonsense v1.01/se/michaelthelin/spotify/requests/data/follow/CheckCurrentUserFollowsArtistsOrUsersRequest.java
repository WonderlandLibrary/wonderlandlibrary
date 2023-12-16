// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.requests.data.follow;

import se.michaelthelin.spotify.requests.IRequest;
import se.michaelthelin.spotify.requests.AbstractRequest;
import se.michaelthelin.spotify.enums.ModelObjectType;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import java.io.IOException;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.Gson;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.requests.data.AbstractDataRequest;

@JsonDeserialize(builder = Builder.class)
public class CheckCurrentUserFollowsArtistsOrUsersRequest extends AbstractDataRequest<Boolean[]>
{
    private CheckCurrentUserFollowsArtistsOrUsersRequest(final Builder builder) {
        super(builder);
    }
    
    @Override
    public Boolean[] execute() throws IOException, SpotifyWebApiException, ParseException {
        return new Gson().fromJson(JsonParser.parseString(this.getJson()).getAsJsonArray(), Boolean[].class);
    }
    
    public static final class Builder extends AbstractDataRequest.Builder<Boolean[], Builder>
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
        
        @Override
        public CheckCurrentUserFollowsArtistsOrUsersRequest build() {
            this.setPath("/v1/me/following/contains");
            return new CheckCurrentUserFollowsArtistsOrUsersRequest(this, null);
        }
        
        @Override
        protected Builder self() {
            return this;
        }
    }
}
