// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.requests.data.player;

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
public class TransferUsersPlaybackRequest extends AbstractDataRequest<String>
{
    private TransferUsersPlaybackRequest(final Builder builder) {
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
        
        public Builder device_ids(final JsonArray device_ids) {
            assert device_ids != null;
            assert !device_ids.isJsonNull();
            assert device_ids.size() == 1;
            return ((AbstractRequest.Builder<T, Builder>)this).setBodyParameter("device_ids", device_ids);
        }
        
        public Builder play(final Boolean play) {
            return ((AbstractRequest.Builder<T, Builder>)this).setBodyParameter("play", play);
        }
        
        @Override
        public TransferUsersPlaybackRequest build() {
            this.setContentType(ContentType.APPLICATION_JSON);
            this.setPath("/v1/me/player");
            return new TransferUsersPlaybackRequest(this, null);
        }
        
        @Override
        protected Builder self() {
            return this;
        }
    }
}
