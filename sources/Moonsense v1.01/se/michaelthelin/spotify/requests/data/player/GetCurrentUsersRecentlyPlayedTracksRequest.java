// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.requests.data.player;

import se.michaelthelin.spotify.requests.AbstractRequest;
import se.michaelthelin.spotify.requests.IRequest;
import se.michaelthelin.spotify.SpotifyApi;
import java.util.Date;
import se.michaelthelin.spotify.requests.data.AbstractDataPagingCursorbasedRequest;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import java.io.IOException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.specification.PlayHistory;
import se.michaelthelin.spotify.model_objects.specification.PagingCursorbased;
import se.michaelthelin.spotify.requests.data.AbstractDataRequest;

@JsonDeserialize(builder = Builder.class)
public class GetCurrentUsersRecentlyPlayedTracksRequest extends AbstractDataRequest<PagingCursorbased<PlayHistory>>
{
    private GetCurrentUsersRecentlyPlayedTracksRequest(final Builder builder) {
        super(builder);
    }
    
    @Override
    public PagingCursorbased<PlayHistory> execute() throws IOException, SpotifyWebApiException, ParseException {
        return new PlayHistory.JsonUtil().createModelObjectPagingCursorbased(this.getJson());
    }
    
    public static final class Builder extends AbstractDataPagingCursorbasedRequest.Builder<PlayHistory, Date, Builder>
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
        public Builder after(final Date after) {
            assert after != null;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("after", SpotifyApi.formatDefaultDate(after));
        }
        
        public Builder before(final Date before) {
            assert before != null;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("before", SpotifyApi.formatDefaultDate(before));
        }
        
        @Override
        public GetCurrentUsersRecentlyPlayedTracksRequest build() {
            this.setPath("/v1/me/player/recently-played");
            return new GetCurrentUsersRecentlyPlayedTracksRequest(this, null);
        }
        
        @Override
        protected Builder self() {
            return this;
        }
    }
}
