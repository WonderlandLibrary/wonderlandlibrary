// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.requests.data.playlists;

import se.michaelthelin.spotify.requests.IRequest;
import se.michaelthelin.spotify.requests.AbstractRequest;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import java.io.IOException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.specification.Image;
import se.michaelthelin.spotify.requests.data.AbstractDataRequest;

@JsonDeserialize(builder = Builder.class)
public class GetPlaylistCoverImageRequest extends AbstractDataRequest<Image[]>
{
    private GetPlaylistCoverImageRequest(final Builder builder) {
        super(builder);
    }
    
    @Override
    public Image[] execute() throws IOException, SpotifyWebApiException, ParseException {
        return new Image.JsonUtil().createModelObjectArray(this.getJson());
    }
    
    public static final class Builder extends AbstractDataRequest.Builder<Image[], Builder>
    {
        public Builder(final String accessToken) {
            super(accessToken);
        }
        
        public Builder playlist_id(final String playlist_id) {
            assert playlist_id != null;
            assert !playlist_id.equals("");
            return ((AbstractRequest.Builder<T, Builder>)this).setPathParameter("playlist_id", playlist_id);
        }
        
        @Override
        public GetPlaylistCoverImageRequest build() {
            this.setPath("/v1/playlists/{playlist_id}/images");
            return new GetPlaylistCoverImageRequest(this, null);
        }
        
        @Override
        protected Builder self() {
            return this;
        }
    }
}
