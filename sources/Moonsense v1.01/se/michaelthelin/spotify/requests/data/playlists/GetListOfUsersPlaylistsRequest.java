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
public class GetListOfUsersPlaylistsRequest extends AbstractDataRequest<Paging<PlaylistSimplified>>
{
    private GetListOfUsersPlaylistsRequest(final Builder builder) {
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
        
        public Builder user_id(final String user_id) {
            assert user_id != null;
            assert !user_id.equals("");
            return ((AbstractRequest.Builder<T, Builder>)this).setPathParameter("user_id", user_id);
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
        public GetListOfUsersPlaylistsRequest build() {
            this.setPath("/v1/users/{user_id}/playlists");
            return new GetListOfUsersPlaylistsRequest(this, null);
        }
        
        @Override
        protected Builder self() {
            return this;
        }
    }
}
