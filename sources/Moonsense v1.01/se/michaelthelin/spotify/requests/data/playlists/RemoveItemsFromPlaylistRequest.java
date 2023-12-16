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
public class RemoveItemsFromPlaylistRequest extends AbstractDataRequest<SnapshotResult>
{
    private RemoveItemsFromPlaylistRequest(final Builder builder) {
        super(builder);
    }
    
    @Override
    public SnapshotResult execute() throws IOException, SpotifyWebApiException, ParseException {
        return new SnapshotResult.JsonUtil().createModelObject(this.deleteJson());
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
        
        public Builder tracks(final JsonArray tracks) {
            assert tracks != null;
            assert !tracks.isJsonNull();
            assert tracks.size() <= 100;
            return ((AbstractRequest.Builder<T, Builder>)this).setBodyParameter("tracks", tracks);
        }
        
        public Builder snapshotId(final String snapshotId) {
            assert snapshotId != null;
            assert !snapshotId.equals("");
            return ((AbstractRequest.Builder<T, Builder>)this).setBodyParameter("snapshot_id", snapshotId);
        }
        
        @Override
        public RemoveItemsFromPlaylistRequest build() {
            this.setContentType(ContentType.APPLICATION_JSON);
            this.setPath("/v1/playlists/{playlist_id}/tracks");
            return new RemoveItemsFromPlaylistRequest(this, null);
        }
        
        @Override
        protected Builder self() {
            return this;
        }
    }
}
