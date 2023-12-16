// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.requests.data.player;

import se.michaelthelin.spotify.requests.IRequest;
import se.michaelthelin.spotify.requests.AbstractRequest;
import com.neovisionaries.i18n.CountryCode;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import java.io.IOException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.miscellaneous.CurrentlyPlaying;
import se.michaelthelin.spotify.requests.data.AbstractDataRequest;

@JsonDeserialize(builder = Builder.class)
public class GetUsersCurrentlyPlayingTrackRequest extends AbstractDataRequest<CurrentlyPlaying>
{
    private GetUsersCurrentlyPlayingTrackRequest(final Builder builder) {
        super(builder);
    }
    
    @Override
    public CurrentlyPlaying execute() throws IOException, SpotifyWebApiException, ParseException {
        return new CurrentlyPlaying.JsonUtil().createModelObject(this.getJson());
    }
    
    public static final class Builder extends AbstractDataRequest.Builder<CurrentlyPlaying, Builder>
    {
        public Builder(final String accessToken) {
            super(accessToken);
        }
        
        public Builder market(final CountryCode market) {
            assert market != null;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("market", market);
        }
        
        public Builder additionalTypes(final String additionalTypes) {
            assert additionalTypes != null;
            assert additionalTypes.matches("((^|,)(episode|track))+$");
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("additional_types", additionalTypes);
        }
        
        @Override
        public GetUsersCurrentlyPlayingTrackRequest build() {
            this.setPath("/v1/me/player/currently-playing");
            return new GetUsersCurrentlyPlayingTrackRequest(this, null);
        }
        
        @Override
        protected Builder self() {
            return this;
        }
    }
}
