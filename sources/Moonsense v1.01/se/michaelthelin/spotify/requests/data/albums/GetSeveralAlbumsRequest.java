// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.requests.data.albums;

import se.michaelthelin.spotify.requests.IRequest;
import se.michaelthelin.spotify.requests.AbstractRequest;
import com.neovisionaries.i18n.CountryCode;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import java.io.IOException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.specification.Album;
import se.michaelthelin.spotify.requests.data.AbstractDataRequest;

@JsonDeserialize(builder = Builder.class)
public class GetSeveralAlbumsRequest extends AbstractDataRequest<Album[]>
{
    private GetSeveralAlbumsRequest(final Builder builder) {
        super(builder);
    }
    
    @Override
    public Album[] execute() throws IOException, SpotifyWebApiException, ParseException {
        return new Album.JsonUtil().createModelObjectArray(this.getJson(), "albums");
    }
    
    public static final class Builder extends AbstractDataRequest.Builder<Album[], Builder>
    {
        public Builder(final String accessToken) {
            super(accessToken);
        }
        
        public Builder ids(final String ids) {
            assert ids != null;
            assert ids.split(",").length <= 20;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("ids", ids);
        }
        
        public Builder market(final CountryCode market) {
            assert market != null;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("market", market);
        }
        
        @Override
        public GetSeveralAlbumsRequest build() {
            this.setPath("/v1/albums");
            return new GetSeveralAlbumsRequest(this, null);
        }
        
        @Override
        protected Builder self() {
            return this;
        }
    }
}
