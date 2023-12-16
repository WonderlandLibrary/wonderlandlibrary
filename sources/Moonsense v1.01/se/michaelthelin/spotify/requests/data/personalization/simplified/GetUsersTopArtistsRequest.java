// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.requests.data.personalization.simplified;

import se.michaelthelin.spotify.requests.AbstractRequest;
import se.michaelthelin.spotify.requests.IRequest;
import se.michaelthelin.spotify.requests.data.AbstractDataPagingRequest;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import java.io.IOException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.requests.data.AbstractDataRequest;

@JsonDeserialize(builder = Builder.class)
public class GetUsersTopArtistsRequest extends AbstractDataRequest<Paging<Artist>>
{
    private GetUsersTopArtistsRequest(final Builder builder) {
        super(builder);
    }
    
    @Override
    public Paging<Artist> execute() throws IOException, SpotifyWebApiException, ParseException {
        return new Artist.JsonUtil().createModelObjectPaging(this.getJson());
    }
    
    public static final class Builder extends AbstractDataPagingRequest.Builder<Artist, Builder>
    {
        public Builder(final String accessToken) {
            super(accessToken);
        }
        
        @Override
        public Builder limit(final Integer limit) {
            assert limit != null;
            assert 1 <= limit && limit <= 50;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("limit", limit);
        }
        
        @Override
        public Builder offset(final Integer offset) {
            assert offset >= 0;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("offset", offset);
        }
        
        public Builder time_range(final String time_range) {
            assert time_range != null;
            assert time_range.equals("long_term") || time_range.equals("short_term");
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("time_range", time_range);
        }
        
        @Override
        public GetUsersTopArtistsRequest build() {
            this.setPath("/v1/me/top/artists");
            return new GetUsersTopArtistsRequest(this, null);
        }
        
        @Override
        protected Builder self() {
            return this;
        }
    }
}
