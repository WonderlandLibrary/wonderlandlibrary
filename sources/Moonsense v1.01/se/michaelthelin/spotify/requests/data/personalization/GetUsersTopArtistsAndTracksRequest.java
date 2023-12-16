// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.requests.data.personalization;

import se.michaelthelin.spotify.requests.AbstractRequest;
import se.michaelthelin.spotify.requests.IRequest;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.enums.ModelObjectType;
import se.michaelthelin.spotify.requests.data.AbstractDataPagingRequest;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import java.io.IOException;
import se.michaelthelin.spotify.model_objects.AbstractModelObject;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.requests.data.AbstractDataRequest;
import se.michaelthelin.spotify.requests.data.personalization.interfaces.IArtistTrackModelObject;

@JsonDeserialize(builder = Builder.class)
public class GetUsersTopArtistsAndTracksRequest<T extends IArtistTrackModelObject> extends AbstractDataRequest<Paging<T>>
{
    private final AbstractModelObject.JsonUtil<T> tClass;
    
    private GetUsersTopArtistsAndTracksRequest(final Builder<T> builder, final AbstractModelObject.JsonUtil<T> tClass) {
        super(builder);
        this.tClass = tClass;
    }
    
    @Override
    public Paging<T> execute() throws IOException, SpotifyWebApiException, ParseException {
        return this.tClass.createModelObjectPaging(this.getJson());
    }
    
    public static final class Builder<T extends IArtistTrackModelObject> extends AbstractDataPagingRequest.Builder<T, Builder<T>>
    {
        private AbstractModelObject.JsonUtil<T> tClass;
        
        public Builder(final String accessToken) {
            super(accessToken);
        }
        
        public Builder<T> type(final ModelObjectType type) {
            assert type != null;
            assert type.getType().equals("artists") || type.getType().equals("tracks");
            final String type2 = type.getType();
            switch (type2) {
                case "artists": {
                    this.tClass = (AbstractModelObject.JsonUtil<T>)new Artist.JsonUtil();
                    break;
                }
                case "tracks": {
                    this.tClass = (AbstractModelObject.JsonUtil<T>)new Track.JsonUtil();
                    break;
                }
            }
            return this.setPathParameter("type", type.getType());
        }
        
        @Override
        public Builder<T> limit(final Integer limit) {
            assert limit != null;
            assert 1 <= limit && limit <= 50;
            return this.setQueryParameter("limit", limit);
        }
        
        @Override
        public Builder<T> offset(final Integer offset) {
            assert offset >= 0;
            return this.setQueryParameter("offset", offset);
        }
        
        public Builder<T> time_range(final String time_range) {
            assert time_range != null;
            assert time_range.equals("long_term") || time_range.equals("short_term");
            return this.setQueryParameter("time_range", time_range);
        }
        
        @Override
        public GetUsersTopArtistsAndTracksRequest<T> build() {
            this.setPath("/v1/me/top/{type}");
            return new GetUsersTopArtistsAndTracksRequest<T>(this, this.tClass, null);
        }
        
        @Override
        protected Builder<T> self() {
            return this;
        }
    }
}
