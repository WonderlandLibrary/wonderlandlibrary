// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.requests.data.player;

import se.michaelthelin.spotify.requests.IRequest;
import se.michaelthelin.spotify.requests.AbstractRequest;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import java.io.IOException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.requests.data.AbstractDataRequest;

@JsonDeserialize(builder = Builder.class)
public class AddItemToUsersPlaybackQueueRequest extends AbstractDataRequest<String>
{
    private AddItemToUsersPlaybackQueueRequest(final Builder builder) {
        super(builder);
    }
    
    @Override
    public String execute() throws IOException, SpotifyWebApiException, ParseException {
        return this.postJson();
    }
    
    public static final class Builder extends AbstractDataRequest.Builder<String, Builder>
    {
        public Builder(final String accessToken) {
            super(accessToken);
        }
        
        public Builder device_id(final String device_id) {
            assert device_id != null;
            assert !device_id.equals("");
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("device_id", device_id);
        }
        
        public Builder uri(final String uri) {
            assert uri != null;
            assert !uri.equals("");
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("uri", uri);
        }
        
        @Override
        public AddItemToUsersPlaybackQueueRequest build() {
            this.setContentType(ContentType.APPLICATION_JSON);
            this.setPath("/v1/me/player/queue");
            return new AddItemToUsersPlaybackQueueRequest(this, null);
        }
        
        @Override
        protected Builder self() {
            return this;
        }
    }
}
