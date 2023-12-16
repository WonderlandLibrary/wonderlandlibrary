// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.requests.data.playlists;

import se.michaelthelin.spotify.requests.AbstractRequest;
import se.michaelthelin.spotify.requests.IRequest;
import com.neovisionaries.i18n.CountryCode;
import se.michaelthelin.spotify.requests.data.AbstractDataPagingRequest;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import java.io.IOException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.requests.data.AbstractDataRequest;

@JsonDeserialize(builder = Builder.class)
public class GetPlaylistsItemsRequest extends AbstractDataRequest<Paging<PlaylistTrack>>
{
    private GetPlaylistsItemsRequest(final Builder builder) {
        super(builder);
    }
    
    @Override
    public Paging<PlaylistTrack> execute() throws IOException, SpotifyWebApiException, ParseException {
        return new PlaylistTrack.JsonUtil().createModelObjectPaging(this.getJson());
    }
    
    public static final class Builder extends AbstractDataPagingRequest.Builder<PlaylistTrack, Builder>
    {
        public Builder(final String accessToken) {
            super(accessToken);
        }
        
        public Builder playlist_id(final String playlist_id) {
            assert playlist_id != null;
            assert !playlist_id.equals("");
            return ((AbstractRequest.Builder<T, Builder>)this).setPathParameter("playlist_id", playlist_id);
        }
        
        public Builder fields(final String fields) {
            assert fields != null;
            assert !fields.equals("");
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("fields", fields);
        }
        
        @Override
        public Builder limit(final Integer limit) {
            assert 1 <= limit && limit <= 100;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("limit", limit);
        }
        
        @Override
        public Builder offset(final Integer offset) {
            assert offset >= 0;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("offset", offset);
        }
        
        public Builder market(final CountryCode market) {
            assert market != null;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("market", market);
        }
        
        public Builder additionalTypes(final String additionalTypes) {
            assert additionalTypes != null;
            assert additionalTypes.matches("((^|,)(episode|track))+$");
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("additional_types", additionalTypes);
        }
        
        @Override
        public GetPlaylistsItemsRequest build() {
            this.setPath("/v1/playlists/{playlist_id}/tracks");
            return new GetPlaylistsItemsRequest(this, null);
        }
        
        @Override
        protected Builder self() {
            return this;
        }
    }
}
