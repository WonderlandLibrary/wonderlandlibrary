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
import se.michaelthelin.spotify.model_objects.specification.Playlist;
import se.michaelthelin.spotify.requests.data.AbstractDataRequest;

@JsonDeserialize(builder = Builder.class)
public class CreatePlaylistRequest extends AbstractDataRequest<Playlist>
{
    private CreatePlaylistRequest(final Builder builder) {
        super(builder);
    }
    
    @Override
    public Playlist execute() throws IOException, SpotifyWebApiException, ParseException {
        return new Playlist.JsonUtil().createModelObject(this.postJson());
    }
    
    public static final class Builder extends AbstractDataRequest.Builder<Playlist, Builder>
    {
        public Builder(final String accessToken) {
            super(accessToken);
        }
        
        public Builder user_id(final String user_id) {
            assert user_id != null;
            assert !user_id.equals("");
            return ((AbstractRequest.Builder<T, Builder>)this).setPathParameter("user_id", user_id);
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
        public CreatePlaylistRequest build() {
            this.setContentType(ContentType.APPLICATION_JSON);
            this.setPath("/v1/users/{user_id}/playlists");
            return new CreatePlaylistRequest(this, null);
        }
        
        @Override
        protected Builder self() {
            return this;
        }
    }
}
