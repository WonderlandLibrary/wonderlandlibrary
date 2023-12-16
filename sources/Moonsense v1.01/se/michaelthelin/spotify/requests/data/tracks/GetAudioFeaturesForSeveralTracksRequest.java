// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.requests.data.tracks;

import se.michaelthelin.spotify.requests.IRequest;
import se.michaelthelin.spotify.requests.AbstractRequest;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import java.io.IOException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.specification.AudioFeatures;
import se.michaelthelin.spotify.requests.data.AbstractDataRequest;

@JsonDeserialize(builder = Builder.class)
public class GetAudioFeaturesForSeveralTracksRequest extends AbstractDataRequest<AudioFeatures[]>
{
    private GetAudioFeaturesForSeveralTracksRequest(final Builder builder) {
        super(builder);
    }
    
    @Override
    public AudioFeatures[] execute() throws IOException, SpotifyWebApiException, ParseException {
        return new AudioFeatures.JsonUtil().createModelObjectArray(this.getJson(), "audio_features");
    }
    
    public static final class Builder extends AbstractDataRequest.Builder<AudioFeatures[], Builder>
    {
        public Builder(final String accessToken) {
            super(accessToken);
        }
        
        public Builder ids(final String ids) {
            assert ids != null;
            assert ids.split(",").length <= 100;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("ids", ids);
        }
        
        @Override
        public GetAudioFeaturesForSeveralTracksRequest build() {
            this.setPath("/v1/audio-features");
            return new GetAudioFeaturesForSeveralTracksRequest(this, null);
        }
        
        @Override
        protected Builder self() {
            return this;
        }
    }
}
