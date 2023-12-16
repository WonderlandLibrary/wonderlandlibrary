// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.model_objects.special;

import com.google.gson.JsonObject;
import se.michaelthelin.spotify.model_objects.IModelObject;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.model_objects.specification.ShowSimplified;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import se.michaelthelin.spotify.model_objects.specification.EpisodeSimplified;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.requests.data.search.interfaces.ISearchModelObject;
import se.michaelthelin.spotify.requests.data.personalization.interfaces.IArtistTrackModelObject;
import se.michaelthelin.spotify.model_objects.AbstractModelObject;

@JsonDeserialize(builder = Builder.class)
public class SearchResult extends AbstractModelObject implements IArtistTrackModelObject, ISearchModelObject
{
    private final Paging<AlbumSimplified> albums;
    private final Paging<Artist> artists;
    private final Paging<EpisodeSimplified> episodes;
    private final Paging<PlaylistSimplified> playlists;
    private final Paging<ShowSimplified> shows;
    private final Paging<Track> tracks;
    
    private SearchResult(final Builder builder) {
        super(builder);
        this.albums = builder.albums;
        this.artists = builder.artists;
        this.episodes = builder.episodes;
        this.playlists = builder.playlists;
        this.shows = builder.shows;
        this.tracks = builder.tracks;
    }
    
    public Paging<AlbumSimplified> getAlbums() {
        return this.albums;
    }
    
    public Paging<Artist> getArtists() {
        return this.artists;
    }
    
    public Paging<EpisodeSimplified> getEpisodes() {
        return this.episodes;
    }
    
    public Paging<PlaylistSimplified> getPlaylists() {
        return this.playlists;
    }
    
    public Paging<ShowSimplified> getShows() {
        return this.shows;
    }
    
    public Paging<Track> getTracks() {
        return this.tracks;
    }
    
    @Override
    public String toString() {
        return invokedynamic(makeConcatWithConstants:(Lse/michaelthelin/spotify/model_objects/specification/Paging;Lse/michaelthelin/spotify/model_objects/specification/Paging;Lse/michaelthelin/spotify/model_objects/specification/Paging;Lse/michaelthelin/spotify/model_objects/specification/Paging;Lse/michaelthelin/spotify/model_objects/specification/Paging;Lse/michaelthelin/spotify/model_objects/specification/Paging;)Ljava/lang/String;, this.albums, this.artists, this.episodes, this.playlists, this.shows, this.tracks);
    }
    
    @Override
    public Builder builder() {
        return new Builder();
    }
    
    public static final class Builder extends AbstractModelObject.Builder
    {
        private Paging<AlbumSimplified> albums;
        private Paging<Artist> artists;
        private Paging<EpisodeSimplified> episodes;
        private Paging<PlaylistSimplified> playlists;
        private Paging<ShowSimplified> shows;
        private Paging<Track> tracks;
        
        public Builder setAlbums(final Paging<AlbumSimplified> albums) {
            this.albums = albums;
            return this;
        }
        
        public Builder setArtists(final Paging<Artist> artists) {
            this.artists = artists;
            return this;
        }
        
        public Builder setEpisodes(final Paging<EpisodeSimplified> episodes) {
            this.episodes = episodes;
            return this;
        }
        
        public Builder setPlaylists(final Paging<PlaylistSimplified> playlists) {
            this.playlists = playlists;
            return this;
        }
        
        public Builder setShows(final Paging<ShowSimplified> shows) {
            this.shows = shows;
            return this;
        }
        
        public Builder setTracks(final Paging<Track> tracks) {
            this.tracks = tracks;
            return this;
        }
        
        @Override
        public SearchResult build() {
            return new SearchResult(this, null);
        }
    }
    
    public static final class JsonUtil extends AbstractModelObject.JsonUtil<SearchResult>
    {
        @Override
        public SearchResult createModelObject(final JsonObject jsonObject) {
            if (jsonObject == null || jsonObject.isJsonNull()) {
                return null;
            }
            return new SearchResult.Builder().setAlbums(this.hasAndNotNull(jsonObject, "albums") ? new AlbumSimplified.JsonUtil().createModelObjectPaging(jsonObject.getAsJsonObject("albums")) : null).setArtists(this.hasAndNotNull(jsonObject, "artists") ? new Artist.JsonUtil().createModelObjectPaging(jsonObject.getAsJsonObject("artists")) : null).setEpisodes(this.hasAndNotNull(jsonObject, "episodes") ? new EpisodeSimplified.JsonUtil().createModelObjectPaging(jsonObject.getAsJsonObject("episodes")) : null).setPlaylists(this.hasAndNotNull(jsonObject, "playlists") ? new PlaylistSimplified.JsonUtil().createModelObjectPaging(jsonObject.getAsJsonObject("playlists")) : null).setShows(this.hasAndNotNull(jsonObject, "shows") ? new ShowSimplified.JsonUtil().createModelObjectPaging(jsonObject.getAsJsonObject("shows")) : null).setTracks(this.hasAndNotNull(jsonObject, "tracks") ? new Track.JsonUtil().createModelObjectPaging(jsonObject.getAsJsonObject("tracks")) : null).build();
        }
    }
}
