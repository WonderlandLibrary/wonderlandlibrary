// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.requests.data.artists;

import se.michaelthelin.spotify.requests.AbstractRequest;
import se.michaelthelin.spotify.requests.IRequest;
import com.neovisionaries.i18n.CountryCode;
import se.michaelthelin.spotify.requests.data.AbstractDataPagingRequest;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import java.io.IOException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.requests.data.AbstractDataRequest;

@JsonDeserialize(builder = Builder.class)
public class GetArtistsAlbumsRequest extends AbstractDataRequest<Paging<AlbumSimplified>>
{
    private GetArtistsAlbumsRequest(final Builder builder) {
        super(builder);
    }
    
    @Override
    public Paging<AlbumSimplified> execute() throws IOException, SpotifyWebApiException, ParseException {
        return new AlbumSimplified.JsonUtil().createModelObjectPaging(this.getJson());
    }
    
    public static final class Builder extends AbstractDataPagingRequest.Builder<AlbumSimplified, Builder>
    {
        public Builder(final String accessToken) {
            super(accessToken);
        }
        
        public Builder id(final String id) {
            assert id != null;
            assert !id.equals("");
            return ((AbstractRequest.Builder<T, Builder>)this).setPathParameter("id", id);
        }
        
        public Builder album_type(final String album_type) {
            assert album_type != null;
            assert album_type.matches("((^|,)(single|album|appears_on|compilation))+$");
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("album_type", album_type);
        }
        
        public Builder market(final CountryCode market) {
            assert market != null;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("market", market);
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
        public GetArtistsAlbumsRequest build() {
            this.setPath("/v1/artists/{id}/albums");
            return new GetArtistsAlbumsRequest(this, null);
        }
        
        @Override
        protected Builder self() {
            return this;
        }
    }
}
