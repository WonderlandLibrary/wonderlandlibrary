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
import se.michaelthelin.spotify.model_objects.miscellaneous.CurrentlyPlayingContext;
import se.michaelthelin.spotify.requests.data.AbstractDataRequest;

@JsonDeserialize(builder = Builder.class)
public class GetInformationAboutUsersCurrentPlaybackRequest extends AbstractDataRequest<CurrentlyPlayingContext>
{
    private GetInformationAboutUsersCurrentPlaybackRequest(final Builder builder) {
        super(builder);
    }
    
    @Override
    public CurrentlyPlayingContext execute() throws IOException, SpotifyWebApiException, ParseException {
        return new CurrentlyPlayingContext.JsonUtil().createModelObject(this.getJson());
    }
    
    public static final class Builder extends AbstractDataRequest.Builder<CurrentlyPlayingContext, Builder>
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
        public GetInformationAboutUsersCurrentPlaybackRequest build() {
            this.setPath("/v1/me/player");
            return new GetInformationAboutUsersCurrentPlaybackRequest(this, null);
        }
        
        @Override
        protected Builder self() {
            return this;
        }
    }
}
