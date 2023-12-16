// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.requests.data.artists;

import se.michaelthelin.spotify.requests.IRequest;
import se.michaelthelin.spotify.requests.AbstractRequest;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import java.io.IOException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.requests.data.AbstractDataRequest;

@JsonDeserialize(builder = Builder.class)
public class GetArtistRequest extends AbstractDataRequest<Artist>
{
    private GetArtistRequest(final Builder builder) {
        super(builder);
    }
    
    @Override
    public Artist execute() throws IOException, SpotifyWebApiException, ParseException {
        return new Artist.JsonUtil().createModelObject(this.getJson());
    }
    
    public static final class Builder extends AbstractDataRequest.Builder<Artist, Builder>
    {
        public Builder(final String accessToken) {
            super(accessToken);
        }
        
        public Builder id(final String id) {
            assert id != null;
            assert !id.equals("");
            return ((AbstractRequest.Builder<T, Builder>)this).setPathParameter("id", id);
        }
        
        @Override
        public GetArtistRequest build() {
            this.setPath("/v1/artists/{id}");
            return new GetArtistRequest(this, null);
        }
        
        @Override
        protected Builder self() {
            return this;
        }
    }
}
