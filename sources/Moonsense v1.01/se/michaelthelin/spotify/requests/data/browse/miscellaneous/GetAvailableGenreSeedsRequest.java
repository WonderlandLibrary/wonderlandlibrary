// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.requests.data.browse.miscellaneous;

import se.michaelthelin.spotify.requests.IRequest;
import se.michaelthelin.spotify.requests.AbstractRequest;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import java.io.IOException;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.google.gson.JsonParser;
import com.google.gson.Gson;
import java.util.List;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.requests.data.AbstractDataRequest;

@JsonDeserialize(builder = Builder.class)
public class GetAvailableGenreSeedsRequest extends AbstractDataRequest<String[]>
{
    private GetAvailableGenreSeedsRequest(final Builder builder) {
        super(builder);
    }
    
    @Override
    public String[] execute() throws IOException, SpotifyWebApiException, ParseException {
        final List<String> genres = new Gson().fromJson(JsonParser.parseString(this.getJson()).getAsJsonObject().get("genres").getAsJsonArray(), new TypeToken<List<String>>() {}.getType());
        return genres.toArray(new String[0]);
    }
    
    public static final class Builder extends AbstractDataRequest.Builder<String[], Builder>
    {
        public Builder(final String accessToken) {
            super(accessToken);
        }
        
        @Override
        public GetAvailableGenreSeedsRequest build() {
            this.setPath("/v1/recommendations/available-genre-seeds");
            return new GetAvailableGenreSeedsRequest(this, null);
        }
        
        @Override
        protected Builder self() {
            return this;
        }
    }
}
