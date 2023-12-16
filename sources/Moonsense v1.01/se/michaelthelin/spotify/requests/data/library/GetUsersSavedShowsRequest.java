// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.requests.data.library;

import se.michaelthelin.spotify.requests.AbstractRequest;
import se.michaelthelin.spotify.requests.IRequest;
import se.michaelthelin.spotify.requests.data.AbstractDataPagingRequest;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import java.io.IOException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.specification.SavedShow;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.requests.data.AbstractDataRequest;

@JsonDeserialize(builder = Builder.class)
public class GetUsersSavedShowsRequest extends AbstractDataRequest<Paging<SavedShow>>
{
    private GetUsersSavedShowsRequest(final Builder builder) {
        super(builder);
    }
    
    @Override
    public Paging<SavedShow> execute() throws IOException, SpotifyWebApiException, ParseException {
        return new SavedShow.JsonUtil().createModelObjectPaging(this.getJson());
    }
    
    public static final class Builder extends AbstractDataPagingRequest.Builder<SavedShow, Builder>
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
            assert offset >= 0;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("offset", offset);
        }
        
        @Override
        public GetUsersSavedShowsRequest build() {
            this.setPath("/v1/me/shows");
            return new GetUsersSavedShowsRequest(this, null);
        }
        
        @Override
        protected Builder self() {
            return this;
        }
    }
}
