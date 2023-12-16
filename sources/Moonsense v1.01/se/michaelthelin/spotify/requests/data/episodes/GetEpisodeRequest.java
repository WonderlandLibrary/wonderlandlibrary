// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.requests.data.episodes;

import se.michaelthelin.spotify.requests.IRequest;
import se.michaelthelin.spotify.requests.AbstractRequest;
import com.neovisionaries.i18n.CountryCode;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import java.io.IOException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.specification.Episode;
import se.michaelthelin.spotify.requests.data.AbstractDataRequest;

@JsonDeserialize(builder = Builder.class)
public class GetEpisodeRequest extends AbstractDataRequest<Episode>
{
    private GetEpisodeRequest(final Builder builder) {
        super(builder);
    }
    
    @Override
    public Episode execute() throws IOException, SpotifyWebApiException, ParseException {
        return new Episode.JsonUtil().createModelObject(this.getJson());
    }
    
    public static final class Builder extends AbstractDataRequest.Builder<Episode, Builder>
    {
        public Builder(final String accessToken) {
            super(accessToken);
        }
        
        public Builder id(final String id) {
            assert id != null;
            assert !id.equals("");
            return ((AbstractRequest.Builder<T, Builder>)this).setPathParameter("id", id);
        }
        
        public Builder market(final CountryCode market) {
            assert market != null;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("market", market);
        }
        
        @Override
        public GetEpisodeRequest build() {
            this.setPath("/v1/episodes/{id}");
            return new GetEpisodeRequest(this, null);
        }
        
        @Override
        protected Builder self() {
            return this;
        }
    }
}
