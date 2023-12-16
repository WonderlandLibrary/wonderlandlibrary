// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.requests.data.tracks;

import se.michaelthelin.spotify.requests.IRequest;
import se.michaelthelin.spotify.requests.AbstractRequest;
import com.neovisionaries.i18n.CountryCode;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import java.io.IOException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.data.AbstractDataRequest;

@JsonDeserialize(builder = Builder.class)
public class GetSeveralTracksRequest extends AbstractDataRequest<Track[]>
{
    private GetSeveralTracksRequest(final Builder builder) {
        super(builder);
    }
    
    @Override
    public Track[] execute() throws IOException, SpotifyWebApiException, ParseException {
        return new Track.JsonUtil().createModelObjectArray(this.getJson(), "tracks");
    }
    
    public static final class Builder extends AbstractDataRequest.Builder<Track[], Builder>
    {
        public Builder(final String accessToken) {
            super(accessToken);
        }
        
        public Builder ids(final String ids) {
            assert ids != null;
            assert ids.split(",").length <= 50;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("ids", ids);
        }
        
        public Builder market(final CountryCode market) {
            assert market != null;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("market", market);
        }
        
        @Override
        public GetSeveralTracksRequest build() {
            this.setPath("/v1/tracks");
            return new GetSeveralTracksRequest(this, null);
        }
        
        @Override
        protected Builder self() {
            return this;
        }
    }
}
