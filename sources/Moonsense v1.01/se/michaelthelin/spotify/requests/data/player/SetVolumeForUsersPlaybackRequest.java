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
public class SetVolumeForUsersPlaybackRequest extends AbstractDataRequest<String>
{
    private SetVolumeForUsersPlaybackRequest(final Builder builder) {
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
        
        public Builder volume_percent(final Integer volume_percent) {
            assert volume_percent != null;
            assert 0 <= volume_percent && volume_percent <= 100;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("volume_percent", volume_percent);
        }
        
        public Builder device_id(final String device_id) {
            assert device_id != null;
            assert !device_id.equals("");
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("device_id", device_id);
        }
        
        @Override
        public SetVolumeForUsersPlaybackRequest build() {
            this.setContentType(ContentType.APPLICATION_JSON);
            this.setPath("/v1/me/player/volume");
            return new SetVolumeForUsersPlaybackRequest(this, null);
        }
        
        @Override
        protected Builder self() {
            return this;
        }
    }
}
