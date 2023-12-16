// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.requests.data.browse;

import se.michaelthelin.spotify.requests.AbstractRequest;
import se.michaelthelin.spotify.requests.IRequest;
import com.neovisionaries.i18n.LanguageCode;
import com.neovisionaries.i18n.CountryCode;
import se.michaelthelin.spotify.requests.data.AbstractDataPagingRequest;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import java.io.IOException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.specification.Category;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.requests.data.AbstractDataRequest;

@JsonDeserialize(builder = Builder.class)
public class GetListOfCategoriesRequest extends AbstractDataRequest<Paging<Category>>
{
    private GetListOfCategoriesRequest(final Builder builder) {
        super(builder);
    }
    
    @Override
    public Paging<Category> execute() throws IOException, SpotifyWebApiException, ParseException {
        return new Category.JsonUtil().createModelObjectPaging(this.getJson(), "categories");
    }
    
    public static final class Builder extends AbstractDataPagingRequest.Builder<Category, Builder>
    {
        public Builder(final String accessToken) {
            super(accessToken);
        }
        
        public Builder country(final CountryCode country) {
            assert country != null;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("country", country);
        }
        
        public Builder locale(final String locale) {
            assert locale != null;
            assert locale.contains("_");
            final String[] localeParts = locale.split("_");
            assert localeParts.length == 2;
            assert LanguageCode.getByCode(localeParts[0]) != null;
            assert CountryCode.getByCode(localeParts[1]) != null;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("locale", locale);
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
        public GetListOfCategoriesRequest build() {
            this.setPath("/v1/browse/categories");
            return new GetListOfCategoriesRequest(this, null);
        }
        
        @Override
        protected Builder self() {
            return this;
        }
    }
}
