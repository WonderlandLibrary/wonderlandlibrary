// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.requests.data.browse;

import se.michaelthelin.spotify.requests.AbstractRequest;
import se.michaelthelin.spotify.requests.IRequest;
import com.neovisionaries.i18n.CountryCode;
import se.michaelthelin.spotify.requests.data.AbstractDataPagingRequest;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import java.io.IOException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.requests.data.AbstractDataRequest;

@JsonDeserialize(builder = Builder.class)
public class GetCategorysPlaylistsRequest extends AbstractDataRequest<Paging<PlaylistSimplified>>
{
    private GetCategorysPlaylistsRequest(final Builder builder) {
        super(builder);
    }
    
    @Override
    public Paging<PlaylistSimplified> execute() throws IOException, SpotifyWebApiException, ParseException {
        return new PlaylistSimplified.JsonUtil().createModelObjectPaging(this.getJson(), "playlists");
    }
    
    public static final class Builder extends AbstractDataPagingRequest.Builder<PlaylistSimplified, Builder>
    {
        public Builder(final String accessToken) {
            super(accessToken);
        }
        
        public Builder category_id(final String category_id) {
            assert category_id != null;
            assert category_id.matches("^[a-z]+$");
            return ((AbstractRequest.Builder<T, Builder>)this).setPathParameter("category_id", category_id);
        }
        
        public Builder country(final CountryCode country) {
            assert country != null;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("country", country);
        }
        
        @Override
        public Builder limit(final Integer limit) {
            assert 1 <= limit && limit <= 50;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("limit", limit);
        }
        
        @Override
        public Builder offset(final Integer offset) {
            assert offset >= 0;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("offset", offset);
        }
        
        @Override
        public GetCategorysPlaylistsRequest build() {
            this.setPath("/v1/browse/categories/{category_id}/playlists");
            return new GetCategorysPlaylistsRequest(this, null);
        }
        
        @Override
        protected Builder self() {
            return this;
        }
    }
}
