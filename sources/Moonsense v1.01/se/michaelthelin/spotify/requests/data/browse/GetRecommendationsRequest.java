// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.requests.data.browse;

import se.michaelthelin.spotify.requests.IRequest;
import se.michaelthelin.spotify.requests.AbstractRequest;
import com.neovisionaries.i18n.CountryCode;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import java.io.IOException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.specification.Recommendations;
import se.michaelthelin.spotify.requests.data.AbstractDataRequest;

@JsonDeserialize(builder = Builder.class)
public class GetRecommendationsRequest extends AbstractDataRequest<Recommendations>
{
    private GetRecommendationsRequest(final Builder builder) {
        super(builder);
    }
    
    @Override
    public Recommendations execute() throws IOException, SpotifyWebApiException, ParseException {
        return new Recommendations.JsonUtil().createModelObject(this.getJson());
    }
    
    public static final class Builder extends AbstractDataRequest.Builder<Recommendations, Builder>
    {
        public Builder(final String accessToken) {
            super(accessToken);
        }
        
        public Builder limit(final Integer limit) {
            assert 1 <= limit && limit <= 100;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("limit", limit);
        }
        
        public Builder market(final CountryCode market) {
            assert market != null;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("market", market);
        }
        
        public Builder max_acousticness(final Float max_acousticness) {
            assert max_acousticness != null;
            assert 0.0 <= max_acousticness && max_acousticness <= 1.0;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("max_acousticness", max_acousticness);
        }
        
        public Builder max_danceability(final Float max_danceability) {
            assert max_danceability != null;
            assert 0.0 <= max_danceability && max_danceability <= 1.0;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("max_danceability", max_danceability);
        }
        
        public Builder max_duration_ms(final Integer max_duration_ms) {
            assert max_duration_ms != null;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("max_duration_ms", max_duration_ms);
        }
        
        public Builder max_energy(final Float max_energy) {
            assert max_energy != null;
            assert 0.0 <= max_energy && max_energy <= 1.0;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("max_energy", max_energy);
        }
        
        public Builder max_instrumentalness(final Float max_instrumentalness) {
            assert max_instrumentalness != null;
            assert 0.0 <= max_instrumentalness && max_instrumentalness <= 1.0;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("max_instrumentalness", max_instrumentalness);
        }
        
        public Builder max_key(final Integer max_key) {
            assert max_key != null;
            assert 0 <= max_key && max_key <= 11;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("max_key", max_key);
        }
        
        public Builder max_liveness(final Float max_liveness) {
            assert max_liveness != null;
            assert 0.0 <= max_liveness && max_liveness <= 1.0;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("max_liveness", max_liveness);
        }
        
        public Builder max_loudness(final Float max_loudness) {
            assert max_loudness != null;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("max_loudness", max_loudness);
        }
        
        public Builder max_mode(final Integer max_mode) {
            assert max_mode != null;
            assert max_mode == 1;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("max_mode", max_mode);
        }
        
        public Builder max_popularity(final Integer max_popularity) {
            assert max_popularity != null;
            assert 0 <= max_popularity && max_popularity <= 100;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("max_popularity", max_popularity);
        }
        
        public Builder max_speechiness(final Float max_speechiness) {
            assert max_speechiness != null;
            assert 0.0 <= max_speechiness && max_speechiness <= 1.0;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("max_speechiness", max_speechiness);
        }
        
        public Builder max_tempo(final Float max_tempo) {
            assert max_tempo != null;
            assert max_tempo >= 0.0f;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("max_tempo", max_tempo);
        }
        
        public Builder max_time_signature(final Integer max_time_signature) {
            assert max_time_signature != null;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("max_time_signature", max_time_signature);
        }
        
        public Builder max_valence(final Float max_valence) {
            assert max_valence != null;
            assert 0.0 <= max_valence && max_valence <= 1.0;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("max_valence", max_valence);
        }
        
        public Builder min_acousticness(final Float min_acousticness) {
            assert min_acousticness != null;
            assert 0.0 <= min_acousticness && min_acousticness <= 1.0;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("min_acousticness", min_acousticness);
        }
        
        public Builder min_danceability(final Float min_danceability) {
            assert min_danceability != null;
            assert 0.0 <= min_danceability && min_danceability <= 1.0;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("min_danceability", min_danceability);
        }
        
        public Builder min_duration_ms(final Integer min_duration_ms) {
            assert min_duration_ms != null;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("min_duration_ms", min_duration_ms);
        }
        
        public Builder min_energy(final Float min_energy) {
            assert min_energy != null;
            assert 0.0 <= min_energy && min_energy <= 1.0;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("min_energy", min_energy);
        }
        
        public Builder min_instrumentalness(final Float min_instrumentalness) {
            assert min_instrumentalness != null;
            assert 0.0 <= min_instrumentalness && min_instrumentalness <= 1.0;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("min_instrumentalness", min_instrumentalness);
        }
        
        public Builder min_key(final Integer min_key) {
            assert min_key != null;
            assert 0 <= min_key && min_key <= 11;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("min_key", min_key);
        }
        
        public Builder min_liveness(final Float min_liveness) {
            assert min_liveness != null;
            assert 0.0 <= min_liveness && min_liveness <= 1.0;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("min_liveness", min_liveness);
        }
        
        public Builder min_loudness(final Float min_loudness) {
            assert min_loudness != null;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("min_loudness", min_loudness);
        }
        
        public Builder min_mode(final Integer min_mode) {
            assert min_mode != null;
            assert min_mode == 1;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("min_mode", min_mode);
        }
        
        public Builder min_popularity(final Integer min_popularity) {
            assert min_popularity != null;
            assert 0 <= min_popularity && min_popularity <= 100;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("min_popularity", min_popularity);
        }
        
        public Builder min_speechiness(final Float min_speechiness) {
            assert min_speechiness != null;
            assert 0.0 <= min_speechiness && min_speechiness <= 1.0;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("min_speechiness", min_speechiness);
        }
        
        public Builder min_tempo(final Float min_tempo) {
            assert min_tempo != null;
            assert min_tempo >= 0.0f;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("min_tempo", min_tempo);
        }
        
        public Builder min_time_signature(final Integer min_time_signature) {
            assert min_time_signature != null;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("min_time_signature", min_time_signature);
        }
        
        public Builder min_valence(final Float min_valence) {
            assert min_valence != null;
            assert 0.0 <= min_valence && min_valence <= 1.0;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("min_valence", min_valence);
        }
        
        public Builder seed_artists(final String seed_artists) {
            assert seed_artists != null;
            assert seed_artists.split(",").length <= 5;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("seed_artists", seed_artists);
        }
        
        public Builder seed_genres(final String seed_genres) {
            assert seed_genres != null;
            assert seed_genres.split(",").length <= 5;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("seed_genres", seed_genres);
        }
        
        public Builder seed_tracks(final String seed_tracks) {
            assert seed_tracks != null;
            assert seed_tracks.split(",").length <= 5;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("seed_tracks", seed_tracks);
        }
        
        public Builder target_acousticness(final Float target_acousticness) {
            assert target_acousticness != null;
            assert 0.0 <= target_acousticness && target_acousticness <= 1.0;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("target_acousticness", target_acousticness);
        }
        
        public Builder target_danceability(final Float target_danceability) {
            assert target_danceability != null;
            assert 0.0 <= target_danceability && target_danceability <= 1.0;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("target_danceability", target_danceability);
        }
        
        public Builder target_duration_ms(final Integer target_duration_ms) {
            assert target_duration_ms != null;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("target_duration_ms", target_duration_ms);
        }
        
        public Builder target_energy(final Float target_energy) {
            assert target_energy != null;
            assert 0.0 <= target_energy && target_energy <= 1.0;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("target_energy", target_energy);
        }
        
        public Builder target_instrumentalness(final Float target_instrumentalness) {
            assert target_instrumentalness != null;
            assert 0.0 <= target_instrumentalness && target_instrumentalness <= 1.0;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("target_instrumentalness", target_instrumentalness);
        }
        
        public Builder target_key(final Integer target_key) {
            assert target_key != null;
            assert 0 <= target_key && target_key <= 11;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("target_key", target_key);
        }
        
        public Builder target_liveness(final Float target_liveness) {
            assert target_liveness != null;
            assert 0.0 <= target_liveness && target_liveness <= 1.0;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("target_liveness", target_liveness);
        }
        
        public Builder target_loudness(final Float target_loudness) {
            assert target_loudness != null;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("target_loudness", target_loudness);
        }
        
        public Builder target_mode(final Integer target_mode) {
            assert target_mode != null;
            assert target_mode == 1;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("target_mode", target_mode);
        }
        
        public Builder target_popularity(final Integer target_popularity) {
            assert target_popularity != null;
            assert 0 <= target_popularity && target_popularity <= 100;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("target_popularity", target_popularity);
        }
        
        public Builder target_speechiness(final Float target_speechiness) {
            assert target_speechiness != null;
            assert 0.0 <= target_speechiness && target_speechiness <= 1.0;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("target_speechiness", target_speechiness);
        }
        
        public Builder target_tempo(final Float target_tempo) {
            assert target_tempo != null;
            assert target_tempo >= 0.0f;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("target_tempo", target_tempo);
        }
        
        public Builder target_time_signature(final Integer target_time_signature) {
            assert target_time_signature != null;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("target_time_signature", target_time_signature);
        }
        
        public Builder target_valence(final Float target_valence) {
            assert target_valence != null;
            assert 0.0 <= target_valence && target_valence <= 1.0;
            return ((AbstractRequest.Builder<T, Builder>)this).setQueryParameter("target_valence", target_valence);
        }
        
        @Override
        public GetRecommendationsRequest build() {
            this.setPath("/v1/recommendations");
            return new GetRecommendationsRequest(this, null);
        }
        
        @Override
        protected Builder self() {
            return this;
        }
    }
}
