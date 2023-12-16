// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.requests.data.follow;

import se.michaelthelin.spotify.requests.IRequest;
import se.michaelthelin.spotify.requests.AbstractRequest;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import java.io.IOException;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.Gson;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.requests.data.AbstractDataRequest;

@JsonDeserialize(builder = Builder.class)
public class CheckUsersFollowPlaylistRequest extends AbstractDataRequest<Boolean[]>
{
    private CheckUsersFollowPlaylistRequest(final Builder builder) {
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
        
        public Builder owner_id(final String owner_id) {
            assert owner_id != null;
            assert !owner_id.equals("");
            return ((AbstractRequest.Builder<T, Builder>)this).setPathParameter("owner_id", owner_id);
        }
        
        public Builder playlist_id(final String playlist_id) {
            assert playlist_id != null;
            assert !playlist_id.equals("");
            return ((AbstractRequest.Builder<T, Builder>)this).setPathParameter("playlist_id", playlist_id);
        }
        
        public Builder ids(final String ids) {
            assert ids != null;
            assert ids.split(",").length <= 5;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("ids", ids);
        }
        
        @Override
        public CheckUsersFollowPlaylistRequest build() {
            this.setPath("/v1/users/{owner_id}/playlists/{playlist_id}/followers/contains");
            return new CheckUsersFollowPlaylistRequest(this, null);
        }
        
        @Override
        protected Builder self() {
            return this;
        }
    }
}
