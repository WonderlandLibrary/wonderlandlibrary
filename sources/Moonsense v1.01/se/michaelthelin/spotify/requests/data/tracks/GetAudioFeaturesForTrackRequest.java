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
public class GetAudioFeaturesForTrackRequest extends AbstractDataRequest<AudioFeatures>
{
    private GetAudioFeaturesForTrackRequest(final Builder builder) {
        super(builder);
    }
    
    @Override
    public AudioFeatures execute() throws IOException, SpotifyWebApiException, ParseException {
        return new AudioFeatures.JsonUtil().createModelObject(this.getJson());
    }
    
    public static final class Builder extends AbstractDataRequest.Builder<AudioFeatures, Builder>
    {
        public Builder(final String accessToken) {
            super(accessToken);
        }
        
        public Builder id(final String id) {
            assert id != null;
            assert !id.equals("");
            return ((AbstractRequest.Builder<T, Builder>)this).setPathParameter("id", id);
        }
        
        @Override
        public GetAudioFeaturesForTrackRequest build() {
            this.setPath("/v1/audio-features/{id}");
            return new GetAudioFeaturesForTrackRequest(this, null);
        }
        
        @Override
        protected Builder self() {
            return this;
        }
    }
}
