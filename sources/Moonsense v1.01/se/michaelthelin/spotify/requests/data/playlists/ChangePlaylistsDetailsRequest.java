// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.requests.data.playlists;

import se.michaelthelin.spotify.requests.IRequest;
import se.michaelthelin.spotify.requests.AbstractRequest;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import java.io.IOException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.requests.data.AbstractDataRequest;

@JsonDeserialize(builder = Builder.class)
public class ChangePlaylistsDetailsRequest extends AbstractDataRequest<String>
{
    private ChangePlaylistsDetailsRequest(final Builder builder) {
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
        
        public Builder playlist_id(final String playlist_id) {
            assert playlist_id != null;
            assert !playlist_id.equals("");
            return ((AbstractRequest.Builder<T, Builder>)this).setPathParameter("playlist_id", playlist_id);
        }
        
        public Builder name(final String name) {
            assert name != null;
            assert !name.equals("");
            return ((AbstractRequest.Builder<T, Builder>)this).setBodyParameter("name", name);
        }
        
        public Builder public_(final Boolean public_) {
            return ((AbstractRequest.Builder<T, Builder>)this).setBodyParameter("public", public_);
        }
        
        public Builder collaborative(final Boolean collaborative) {
            return ((AbstractRequest.Builder<T, Builder>)this).setBodyParameter("collaborative", collaborative);
        }
        
        public Builder description(final String description) {
            assert description != null;
            assert !description.equals("");
            return ((AbstractRequest.Builder<T, Builder>)this).setBodyParameter("description", description);
        }
        
        @Override
        public ChangePlaylistsDetailsRequest build() {
            this.setContentType(ContentType.APPLICATION_JSON);
            this.setPath("/v1/playlists/{playlist_id}");
            return new ChangePlaylistsDetailsRequest(this, null);
        }
        
        @Override
        protected Builder self() {
            return this;
        }
    }
}
