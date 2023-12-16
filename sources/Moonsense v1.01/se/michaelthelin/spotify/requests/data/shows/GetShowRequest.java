// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.requests.data.shows;

import se.michaelthelin.spotify.requests.IRequest;
import se.michaelthelin.spotify.requests.AbstractRequest;
import com.neovisionaries.i18n.CountryCode;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import java.io.IOException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.specification.Show;
import se.michaelthelin.spotify.requests.data.AbstractDataRequest;

@JsonDeserialize(builder = Builder.class)
public class GetShowRequest extends AbstractDataRequest<Show>
{
    private GetShowRequest(final Builder builder) {
        super(builder);
    }
    
    @Override
    public Show execute() throws IOException, SpotifyWebApiException, ParseException {
        return new Show.JsonUtil().createModelObject(this.getJson());
    }
    
    public static final class Builder extends AbstractDataRequest.Builder<Show, Builder>
    {
        public Builder(final String accessToken) {
            super(accessToken);
        }
        
        public Builder id(final String id) {
            assert id != null;
            assert !id.equals("");
            return ((AbstractRequest.Builder<T, Builder>)this).setPathParameter("id", id);
        }
        
        public Builder market(final CountryCode market) {
            assert market != null;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("market", market);
        }
        
        @Override
        public GetShowRequest build() {
            this.setPath("/v1/shows/{id}");
            return new GetShowRequest(this, null);
        }
        
        @Override
        protected Builder self() {
            return this;
        }
    }
}
