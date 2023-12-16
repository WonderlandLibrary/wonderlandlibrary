// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.requests.data.search;

import se.michaelthelin.spotify.requests.IRequest;
import se.michaelthelin.spotify.requests.AbstractRequest;
import com.neovisionaries.i18n.CountryCode;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import java.io.IOException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.special.SearchResult;
import se.michaelthelin.spotify.requests.data.AbstractDataRequest;

@JsonDeserialize(builder = Builder.class)
public class SearchItemRequest extends AbstractDataRequest<SearchResult>
{
    private SearchItemRequest(final Builder builder) {
        super(builder);
    }
    
    @Override
    public SearchResult execute() throws IOException, SpotifyWebApiException, ParseException {
        return new SearchResult.JsonUtil().createModelObject(this.getJson());
    }
    
    public static final class Builder extends AbstractDataRequest.Builder<SearchResult, Builder>
    {
        public Builder(final String accessToken) {
            super(accessToken);
        }
        
        public Builder q(final String q) {
            assert q != null;
            assert !q.equals("");
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("q", q);
        }
        
        public Builder type(final String type) {
            assert type != null;
            assert type.matches("((^|,)(album|artist|episode|playlist|show|track))+$");
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("type", type);
        }
        
        public Builder market(final CountryCode market) {
            assert market != null;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("market", market);
        }
        
        public Builder limit(final Integer limit) {
            assert limit != null;
            assert 1 <= limit && limit <= 50;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("limit", limit);
        }
        
        public Builder offset(final Integer offset) {
            assert offset != null;
            assert 0 <= offset && offset <= 100000;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("offset", offset);
        }
        
        public Builder includeExternal(final String includeExternal) {
            assert includeExternal != null;
            assert includeExternal.matches("audio");
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("include_external", includeExternal);
        }
        
        @Override
        public SearchItemRequest build() {
            this.setPath("/v1/search");
            return new SearchItemRequest(this, null);
        }
        
        @Override
        protected Builder self() {
            return this;
        }
    }
}
