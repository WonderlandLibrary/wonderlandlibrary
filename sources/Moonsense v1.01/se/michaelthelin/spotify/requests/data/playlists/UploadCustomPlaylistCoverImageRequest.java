// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.requests.data.playlists;

import se.michaelthelin.spotify.requests.IRequest;
import se.michaelthelin.spotify.requests.AbstractRequest;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import java.io.IOException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.requests.data.AbstractDataRequest;

@JsonDeserialize(builder = Builder.class)
public class UploadCustomPlaylistCoverImageRequest extends AbstractDataRequest<String>
{
    private UploadCustomPlaylistCoverImageRequest(final Builder builder) {
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
        
        public Builder image_data(final String image_data) {
            assert image_data != null;
            assert !image_data.equals("");
            assert image_data.getBytes().length <= 256000;
            return ((AbstractRequest.Builder<T, Builder>)this).setBody((HttpEntity)new StringEntity(image_data, ContentType.IMAGE_JPEG));
        }
        
        @Override
        public UploadCustomPlaylistCoverImageRequest build() {
            this.setContentType(ContentType.IMAGE_JPEG);
            this.setPath("/v1/playlists/{playlist_id}/images");
            return new UploadCustomPlaylistCoverImageRequest(this, null);
        }
        
        @Override
        protected Builder self() {
            return this;
        }
    }
}
