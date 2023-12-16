// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.requests.data.playlists;

import se.michaelthelin.spotify.requests.IRequest;
import se.michaelthelin.spotify.requests.AbstractRequest;
import org.apache.hc.core5.http.ContentType;
import com.google.gson.JsonArray;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import java.io.IOException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.requests.data.AbstractDataRequest;

@JsonDeserialize(builder = Builder.class)
public class ReplacePlaylistsItemsRequest extends AbstractDataRequest<String>
{
    private ReplacePlaylistsItemsRequest(final Builder builder) {
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
        
        public Builder uris(final String uris) {
            assert uris != null;
            assert !uris.equals("");
            assert uris.split(",").length <= 100;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("uris", uris);
        }
        
        public Builder uris(final JsonArray uris) {
            assert uris != null;
            assert !uris.isJsonNull();
            assert uris.size() <= 100;
            return ((AbstractRequest.Builder<T, Builder>)this).setBodyParameter("uris", uris);
        }
        
        @Override
        public ReplacePlaylistsItemsRequest build() {
            this.setContentType(ContentType.APPLICATION_JSON);
            this.setPath("/v1/playlists/{playlist_id}/tracks");
            return new ReplacePlaylistsItemsRequest(this, null);
        }
        
        @Override
        protected Builder self() {
            return this;
        }
    }
}
