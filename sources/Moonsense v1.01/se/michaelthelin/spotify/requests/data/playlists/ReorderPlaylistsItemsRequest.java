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
import se.michaelthelin.spotify.model_objects.special.SnapshotResult;
import se.michaelthelin.spotify.requests.data.AbstractDataRequest;

@JsonDeserialize(builder = Builder.class)
public class ReorderPlaylistsItemsRequest extends AbstractDataRequest<SnapshotResult>
{
    private ReorderPlaylistsItemsRequest(final Builder builder) {
        super(builder);
    }
    
    @Override
    public SnapshotResult execute() throws IOException, SpotifyWebApiException, ParseException {
        return new SnapshotResult.JsonUtil().createModelObject(this.putJson());
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
        
        public Builder range_start(final Integer range_start) {
            assert range_start != null;
            assert range_start >= 0;
            return ((AbstractRequest.Builder<T, Builder>)this).setBodyParameter("range_start", range_start);
        }
        
        public Builder range_length(final Integer range_length) {
            assert range_length != null;
            assert range_length >= 1;
            return ((AbstractRequest.Builder<T, Builder>)this).setBodyParameter("range_length", range_length);
        }
        
        public Builder insert_before(final Integer insert_before) {
            assert insert_before != null;
            assert insert_before >= 0;
            return ((AbstractRequest.Builder<T, Builder>)this).setBodyParameter("insert_before", insert_before);
        }
        
        public Builder snapshot_id(final String snapshot_id) {
            assert snapshot_id != null;
            assert !snapshot_id.equals("");
            return ((AbstractRequest.Builder<T, Builder>)this).setBodyParameter("snapshot_id", snapshot_id);
        }
        
        @Override
        public ReorderPlaylistsItemsRequest build() {
            this.setContentType(ContentType.APPLICATION_JSON);
            this.setPath("/v1/playlists/{playlist_id}/tracks");
            return new ReorderPlaylistsItemsRequest(this, null);
        }
        
        @Override
        protected Builder self() {
            return this;
        }
    }
}
