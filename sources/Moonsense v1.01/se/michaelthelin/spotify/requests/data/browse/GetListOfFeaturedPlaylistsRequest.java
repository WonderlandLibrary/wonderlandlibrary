// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.requests.data.browse;

import se.michaelthelin.spotify.requests.IRequest;
import se.michaelthelin.spotify.requests.AbstractRequest;
import se.michaelthelin.spotify.SpotifyApi;
import java.util.Date;
import com.neovisionaries.i18n.CountryCode;
import com.neovisionaries.i18n.LanguageCode;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import java.io.IOException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.special.FeaturedPlaylists;
import se.michaelthelin.spotify.requests.data.AbstractDataRequest;

@JsonDeserialize(builder = Builder.class)
public class GetListOfFeaturedPlaylistsRequest extends AbstractDataRequest<FeaturedPlaylists>
{
    private GetListOfFeaturedPlaylistsRequest(final Builder builder) {
        super(builder);
    }
    
    @Override
    public FeaturedPlaylists execute() throws IOException, SpotifyWebApiException, ParseException {
        return new FeaturedPlaylists.JsonUtil().createModelObject(this.getJson());
    }
    
    public static final class Builder extends AbstractDataRequest.Builder<FeaturedPlaylists, Builder>
    {
        public Builder(final String accessToken) {
            super(accessToken);
        }
        
        public Builder locale(final String locale) {
            assert locale != null;
            assert locale.contains("_");
            final String[] localeParts = locale.split("_");
            assert localeParts.length == 2;
            assert LanguageCode.getByCode(localeParts[0]) != null;
            assert CountryCode.getByCode(localeParts[1]) != null;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("locale", locale);
        }
        
        public Builder country(final CountryCode country) {
            assert country != null;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("country", country);
        }
        
        public Builder timestamp(final Date timestamp) {
            assert timestamp != null;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("timestamp", SpotifyApi.formatDefaultDate(timestamp));
        }
        
        public Builder limit(final Integer limit) {
            assert 1 <= limit && limit <= 50;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("limit", limit);
        }
        
        public Builder offset(final Integer offset) {
            assert offset >= 0;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("offset", offset);
        }
        
        @Override
        public GetListOfFeaturedPlaylistsRequest build() {
            this.setPath("/v1/browse/featured-playlists");
            return new GetListOfFeaturedPlaylistsRequest(this, null);
        }
        
        @Override
        protected Builder self() {
            return this;
        }
    }
}
