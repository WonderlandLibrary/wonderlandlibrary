// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.requests.data.playlists;

import se.michaelthelin.spotify.requests.AbstractRequest;
import se.michaelthelin.spotify.requests.IRequest;
import se.michaelthelin.spotify.requests.data.AbstractDataPagingRequest;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import java.io.IOException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.requests.data.AbstractDataRequest;

@JsonDeserialize(builder = Builder.class)
public class GetListOfCurrentUsersPlaylistsRequest extends AbstractDataRequest<Paging<PlaylistSimplified>>
{
    private GetListOfCurrentUsersPlaylistsRequest(final Builder builder) {
        super(builder);
    }
    
    @Override
    public Paging<PlaylistSimplified> execute() throws IOException, SpotifyWebApiException, ParseException {
        return new PlaylistSimplified.JsonUtil().createModelObjectPaging(this.getJson());
    }
    
    public static final class Builder extends AbstractDataPagingRequest.Builder<PlaylistSimplified, Builder>
    {
        public Builder(final String accessToken) {
            super(accessToken);
        }
        
        @Override
        public Builder limit(final Integer limit) {
            assert 1 <= limit && limit <= 50;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("limit", limit);
        }
        
        @Override
        public Builder offset(final Integer offset) {
            assert 0 <= offset && offset <= 100000;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("offset", offset);
        }
        
        @Override
        public GetListOfCurrentUsersPlaylistsRequest build() {
            this.setPath("/v1/me/playlists");
            return new GetListOfCurrentUsersPlaylistsRequest(this, null);
        }
        
        @Override
        protected Builder self() {
            return this;
        }
    }
}
