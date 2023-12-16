// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.requests.data.library;

import se.michaelthelin.spotify.requests.AbstractRequest;
import se.michaelthelin.spotify.requests.IRequest;
import com.neovisionaries.i18n.CountryCode;
import se.michaelthelin.spotify.requests.data.AbstractDataPagingRequest;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import java.io.IOException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.specification.SavedTrack;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.requests.data.AbstractDataRequest;

@JsonDeserialize(builder = Builder.class)
public class GetUsersSavedTracksRequest extends AbstractDataRequest<Paging<SavedTrack>>
{
    private GetUsersSavedTracksRequest(final Builder builder) {
        super(builder);
    }
    
    @Override
    public Paging<SavedTrack> execute() throws IOException, SpotifyWebApiException, ParseException {
        return new SavedTrack.JsonUtil().createModelObjectPaging(this.getJson());
    }
    
    public static final class Builder extends AbstractDataPagingRequest.Builder<SavedTrack, Builder>
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
        
        public Builder market(final CountryCode market) {
            assert market != null;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("market", market);
        }
        
        @Override
        public GetUsersSavedTracksRequest build() {
            this.setPath("/v1/me/tracks");
            return new GetUsersSavedTracksRequest(this, null);
        }
        
        @Override
        protected Builder self() {
            return this;
        }
    }
}
