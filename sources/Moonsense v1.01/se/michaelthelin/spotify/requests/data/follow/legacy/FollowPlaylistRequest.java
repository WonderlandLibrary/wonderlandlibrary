// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.requests.data.follow.legacy;

import se.michaelthelin.spotify.requests.IRequest;
import se.michaelthelin.spotify.requests.AbstractRequest;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import java.io.IOException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.requests.data.AbstractDataRequest;

@JsonDeserialize(builder = Builder.class)
public class FollowPlaylistRequest extends AbstractDataRequest<String>
{
    private FollowPlaylistRequest(final Builder builder) {
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
        
        public Builder public_(final Boolean public_) {
            assert public_ != null;
            return ((AbstractRequest.Builder<T, Builder>)this).setBodyParameter("public", public_);
        }
        
        @Override
        public FollowPlaylistRequest build() {
            this.setContentType(ContentType.APPLICATION_JSON);
            this.setPath("/v1/users/{owner_id}/playlists/{playlist_id}/followers");
            return new FollowPlaylistRequest(this, null);
        }
        
        @Override
        protected Builder self() {
            return this;
        }
    }
}
