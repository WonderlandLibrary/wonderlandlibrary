// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.requests.data.player;

import se.michaelthelin.spotify.requests.IRequest;
import se.michaelthelin.spotify.requests.AbstractRequest;
import org.apache.hc.core5.http.ContentType;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import java.io.IOException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.requests.data.AbstractDataRequest;

@JsonDeserialize(builder = Builder.class)
public class StartResumeUsersPlaybackRequest extends AbstractDataRequest<String>
{
    private StartResumeUsersPlaybackRequest(final Builder builder) {
        super(builder);
    }
    
    @Override
    public String execute() throws IOException, SpotifyWebApiException, ParseException {
        return this.putJson();
    }
    
    public static final class Builder extends AbstractDataRequest.Builder<String, Builder>
    {
        public Builder(final String accessToken) {
            super(accessToken);
        }
        
        public Builder device_id(final String device_id) {
            assert device_id != null;
            assert !device_id.equals("");
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("device_id", device_id);
        }
        
        public Builder context_uri(final String context_uri) {
            assert context_uri != null;
            assert !context_uri.equals("");
            return ((AbstractRequest.Builder<T, Builder>)this).setBodyParameter("context_uri", context_uri);
        }
        
        public Builder uris(final JsonArray uris) {
            assert uris != null;
            assert !uris.isJsonNull();
            return ((AbstractRequest.Builder<T, Builder>)this).setBodyParameter("uris", uris);
        }
        
        public Builder offset(final JsonObject offset) {
            assert offset != null;
            assert !offset.isJsonNull();
            return ((AbstractRequest.Builder<T, Builder>)this).setBodyParameter("offset", offset);
        }
        
        public Builder position_ms(final Integer position_ms) {
            assert position_ms != null;
            assert position_ms >= 0;
            return ((AbstractRequest.Builder<T, Builder>)this).setBodyParameter("position_ms", position_ms);
        }
        
        @Override
        public StartResumeUsersPlaybackRequest build() {
            this.setContentType(ContentType.APPLICATION_JSON);
            this.setPath("/v1/me/player/play");
            return new StartResumeUsersPlaybackRequest(this, null);
        }
        
        @Override
        protected Builder self() {
            return this;
        }
    }
}
