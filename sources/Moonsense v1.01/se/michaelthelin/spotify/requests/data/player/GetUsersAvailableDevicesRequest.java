// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.requests.data.player;

import se.michaelthelin.spotify.requests.IRequest;
import se.michaelthelin.spotify.requests.AbstractRequest;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import java.io.IOException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.miscellaneous.Device;
import se.michaelthelin.spotify.requests.data.AbstractDataRequest;

@JsonDeserialize(builder = Builder.class)
public class GetUsersAvailableDevicesRequest extends AbstractDataRequest<Device[]>
{
    private GetUsersAvailableDevicesRequest(final Builder builder) {
        super(builder);
    }
    
    @Override
    public Device[] execute() throws IOException, SpotifyWebApiException, ParseException {
        return new Device.JsonUtil().createModelObjectArray(this.getJson(), "devices");
    }
    
    public static final class Builder extends AbstractDataRequest.Builder<Device[], Builder>
    {
        public Builder(final String accessToken) {
            super(accessToken);
        }
        
        @Override
        public GetUsersAvailableDevicesRequest build() {
            this.setPath("/v1/me/player/devices");
            return new GetUsersAvailableDevicesRequest(this, null);
        }
        
        @Override
        protected Builder self() {
            return this;
        }
    }
}
