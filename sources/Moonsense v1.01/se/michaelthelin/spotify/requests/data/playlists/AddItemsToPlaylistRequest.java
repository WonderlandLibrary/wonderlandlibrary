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
import se.michaelthelin.spotify.model_objects.special.SnapshotResult;
import se.michaelthelin.spotify.requests.data.AbstractDataRequest;

@JsonDeserialize(builder = Builder.class)
public class AddItemsToPlaylistRequest extends AbstractDataRequest<SnapshotResult>
{
    private AddItemsToPlaylistRequest(final Builder builder) {
        super(builder);
    }
    
    @Override
    public SnapshotResult execute() throws IOException, SpotifyWebApiException, ParseException {
        return new SnapshotResult.JsonUtil().createModelObject(this.postJson());
    }
    
    public static final class Builder extends AbstractDataRequest.Builder<SnapshotResult, Builder>
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
        
        public Builder position(final Integer position) {
            return this.position(position, false);
        }
        
        public Builder uris(final JsonArray uris) {
            assert uris != null;
            assert !uris.isJsonNull();
            assert uris.size() <= 100;
            return ((AbstractRequest.Builder<T, Builder>)this).setBodyParameter("uris", uris);
        }
        
        public Builder position(final Integer position, final Boolean use_body) {
            assert position >= 0;
            if (use_body) {
                return ((AbstractRequest.Builder<T, Builder>)this).setBodyParameter("position", position);
            }
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("position", position);
        }
        
        @Override
        public AddItemsToPlaylistRequest build() {
            this.setContentType(ContentType.APPLICATION_JSON);
            this.setPath("/v1/playlists/{playlist_id}/tracks");
            return new AddItemsToPlaylistRequest(this, null);
        }
        
        @Override
        protected Builder self() {
            return this;
        }
    }
}
