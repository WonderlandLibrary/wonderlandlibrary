// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.requests.data.library;

import se.michaelthelin.spotify.requests.IRequest;
import se.michaelthelin.spotify.requests.AbstractRequest;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import java.io.IOException;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.Gson;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.requests.data.AbstractDataRequest;

@JsonDeserialize(builder = Builder.class)
public class CheckUsersSavedTracksRequest extends AbstractDataRequest<Boolean[]>
{
    private CheckUsersSavedTracksRequest(final Builder builder) {
        super(builder);
    }
    
    @Override
    public Boolean[] execute() throws IOException, SpotifyWebApiException, ParseException {
        return new Gson().fromJson(JsonParser.parseString(this.getJson()).getAsJsonArray(), Boolean[].class);
    }
    
    public static final class Builder extends AbstractDataRequest.Builder<Boolean[], Builder>
    {
        public Builder(final String accessToken) {
            super(accessToken);
        }
        
        public Builder ids(final String ids) {
            assert ids != null;
            assert ids.split(",").length <= 50;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("ids", ids);
        }
        
        @Override
        public CheckUsersSavedTracksRequest build() {
            this.setPath("/v1/me/tracks/contains");
            return new CheckUsersSavedTracksRequest(this, null);
        }
        
        @Override
        protected Builder self() {
            return this;
        }
    }
}
