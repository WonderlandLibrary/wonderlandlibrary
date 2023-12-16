// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.requests.data.browse;

import se.michaelthelin.spotify.requests.IRequest;
import se.michaelthelin.spotify.requests.AbstractRequest;
import com.neovisionaries.i18n.LanguageCode;
import com.neovisionaries.i18n.CountryCode;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import java.io.IOException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.specification.Category;
import se.michaelthelin.spotify.requests.data.AbstractDataRequest;

@JsonDeserialize(builder = Builder.class)
public class GetCategoryRequest extends AbstractDataRequest<Category>
{
    private GetCategoryRequest(final Builder builder) {
        super(builder);
    }
    
    @Override
    public Category execute() throws IOException, SpotifyWebApiException, ParseException {
        return new Category.JsonUtil().createModelObject(this.getJson());
    }
    
    public static final class Builder extends AbstractDataRequest.Builder<Category, Builder>
    {
        public Builder(final String accessToken) {
            super(accessToken);
        }
        
        public Builder category_id(final String category_id) {
            assert category_id != null;
            assert category_id.matches("^[a-z]+$");
            return ((AbstractRequest.Builder<T, Builder>)this).setPathParameter("category_id", category_id);
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
        public GetCategoryRequest build() {
            this.setPath("/v1/browse/categories/{category_id}");
            return new GetCategoryRequest(this, null);
        }
        
        @Override
        protected Builder self() {
            return this;
        }
    }
}
