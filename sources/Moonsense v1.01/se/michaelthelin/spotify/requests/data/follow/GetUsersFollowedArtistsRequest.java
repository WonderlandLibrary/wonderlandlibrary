// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.requests.data.follow;

import se.michaelthelin.spotify.requests.AbstractRequest;
import se.michaelthelin.spotify.requests.IRequest;
import se.michaelthelin.spotify.enums.ModelObjectType;
import se.michaelthelin.spotify.requests.data.AbstractDataPagingCursorbasedRequest;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import java.io.IOException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.PagingCursorbased;
import se.michaelthelin.spotify.requests.data.AbstractDataRequest;

@JsonDeserialize(builder = Builder.class)
public class GetUsersFollowedArtistsRequest extends AbstractDataRequest<PagingCursorbased<Artist>>
{
    private GetUsersFollowedArtistsRequest(final Builder builder) {
        super(builder);
    }
    
    @Override
    public PagingCursorbased<Artist> execute() throws IOException, SpotifyWebApiException, ParseException {
        return new Artist.JsonUtil().createModelObjectPagingCursorbased(this.getJson(), "artists");
    }
    
    public static final class Builder extends AbstractDataPagingCursorbasedRequest.Builder<Artist, String, Builder>
    {
        public Builder(final String accessToken) {
            super(accessToken);
        }
        
        public Builder type(final ModelObjectType type) {
            assert type != null;
            assert type.getType().equals("artist");
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("type", type);
        }
        
        @Override
        public Builder limit(final Integer limit) {
            assert limit != null;
            assert 1 <= limit && limit <= 50;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("limit", limit);
        }
        
        @Override
        public Builder after(final String after) {
            assert after != null;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("after", after);
        }
        
        @Override
        public GetUsersFollowedArtistsRequest build() {
            this.setPath("/v1/me/following");
            return new GetUsersFollowedArtistsRequest(this, null);
        }
        
        @Override
        protected Builder self() {
            return this;
        }
    }
}
