// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.requests.data.artists;

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
public class GetArtistsTopTracksRequest extends AbstractDataRequest<Track[]>
{
    private GetArtistsTopTracksRequest(final Builder builder) {
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
        
        public Builder id(final String id) {
            assert id != null;
            assert !id.equals("");
            return ((AbstractRequest.Builder<T, Builder>)this).setPathParameter("id", id);
        }
        
        public Builder country(final CountryCode country) {
            assert country != null;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("country", country);
        }
        
        @Override
        public GetArtistsTopTracksRequest build() {
            this.setPath("/v1/artists/{id}/top-tracks");
            return new GetArtistsTopTracksRequest(this, null);
        }
        
        @Override
        protected Builder self() {
            return this;
        }
    }
}
