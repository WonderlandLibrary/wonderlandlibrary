// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.model_objects.specification;

import com.google.gson.JsonElement;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import se.michaelthelin.spotify.model_objects.IModelObject;
import java.util.Arrays;
import se.michaelthelin.spotify.enums.ModelObjectType;
import se.michaelthelin.spotify.enums.ReleaseDatePrecision;
import com.neovisionaries.i18n.CountryCode;
import se.michaelthelin.spotify.enums.AlbumType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.AbstractModelObject;

@JsonDeserialize(builder = Builder.class)
public class Album extends AbstractModelObject
{
    private final AlbumType albumType;
    private final ArtistSimplified[] artists;
    private final CountryCode[] availableMarkets;
    private final Copyright[] copyrights;
    private final ExternalId externalIds;
    private final ExternalUrl externalUrls;
    private final String[] genres;
    private final String href;
    private final String id;
    private final Image[] images;
    private final String label;
    private final String name;
    private final Integer popularity;
    private final String releaseDate;
    private final ReleaseDatePrecision releaseDatePrecision;
    private final Paging<TrackSimplified> tracks;
    private final ModelObjectType type;
    private final String uri;
    
    private Album(final Builder builder) {
        super(builder);
        this.albumType = builder.albumType;
        this.artists = builder.artists;
        this.availableMarkets = builder.availableMarkets;
        this.copyrights = builder.copyrights;
        this.externalIds = builder.externalIds;
        this.externalUrls = builder.externalUrls;
        this.genres = builder.genres;
        this.href = builder.href;
        this.id = builder.id;
        this.images = builder.images;
        this.label = builder.label;
        this.name = builder.name;
        this.popularity = builder.popularity;
        this.releaseDate = builder.releaseDate;
        this.releaseDatePrecision = builder.releaseDatePrecision;
        this.tracks = builder.tracks;
        this.type = builder.type;
        this.uri = builder.uri;
    }
    
    public AlbumType getAlbumType() {
        return this.albumType;
    }
    
    public ArtistSimplified[] getArtists() {
        return this.artists;
    }
    
    public CountryCode[] getAvailableMarkets() {
        return this.availableMarkets;
    }
    
    public Copyright[] getCopyrights() {
        return this.copyrights;
    }
    
    public ExternalId getExternalIds() {
        return this.externalIds;
    }
    
    public ExternalUrl getExternalUrls() {
        return this.externalUrls;
    }
    
    public String[] getGenres() {
        return this.genres;
    }
    
    public String getHref() {
        return this.href;
    }
    
    public String getId() {
        return this.id;
    }
    
    public Image[] getImages() {
        return this.images;
    }
    
    public String getLabel() {
        return this.label;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Integer getPopularity() {
        return this.popularity;
    }
    
    public String getReleaseDate() {
        return this.releaseDate;
    }
    
    public ReleaseDatePrecision getReleaseDatePrecision() {
        return this.releaseDatePrecision;
    }
    
    public Paging<TrackSimplified> getTracks() {
        return this.tracks;
    }
    
    public ModelObjectType getType() {
        return this.type;
    }
    
    public String getUri() {
        return this.uri;
    }
    
    @Override
    public String toString() {
        return invokedynamic(makeConcatWithConstants:(Ljava/lang/String;Ljava/lang/String;Lse/michaelthelin/spotify/enums/AlbumType;Ljava/lang/String;Ljava/lang/String;Lse/michaelthelin/spotify/model_objects/specification/ExternalId;Lse/michaelthelin/spotify/model_objects/specification/ExternalUrl;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/String;Lse/michaelthelin/spotify/enums/ReleaseDatePrecision;Lse/michaelthelin/spotify/model_objects/specification/Paging;Lse/michaelthelin/spotify/enums/ModelObjectType;Ljava/lang/String;)Ljava/lang/String;, Arrays.toString(this.artists), this.name, this.albumType, Arrays.toString(this.availableMarkets), Arrays.toString(this.copyrights), this.externalIds, this.externalUrls, Arrays.toString(this.genres), this.href, this.id, Arrays.toString(this.images), this.label, this.popularity, this.releaseDate, this.releaseDatePrecision, this.tracks, this.type, this.uri);
    }
    
    @Override
    public Builder builder() {
        return new Builder();
    }
    
    public static final class Builder extends AbstractModelObject.Builder
    {
        private AlbumType albumType;
        private ArtistSimplified[] artists;
        private CountryCode[] availableMarkets;
        private Copyright[] copyrights;
        private ExternalId externalIds;
        private ExternalUrl externalUrls;
        private String[] genres;
        private String href;
        private String id;
        private Image[] images;
        private String label;
        private String name;
        private Integer popularity;
        private String releaseDate;
        private ReleaseDatePrecision releaseDatePrecision;
        private Paging<TrackSimplified> tracks;
        private ModelObjectType type;
        private String uri;
        
        public Builder setAlbumType(final AlbumType albumType) {
            this.albumType = albumType;
            return this;
        }
        
        public Builder setArtists(final ArtistSimplified... artists) {
            this.artists = artists;
            return this;
        }
        
        public Builder setAvailableMarkets(final CountryCode... availableMarkets) {
            this.availableMarkets = availableMarkets;
            return this;
        }
        
        public Builder setCopyrights(final Copyright... copyrights) {
            this.copyrights = copyrights;
            return this;
        }
        
        public Builder setExternalIds(final ExternalId externalIds) {
            this.externalIds = externalIds;
            return this;
        }
        
        public Builder setExternalUrls(final ExternalUrl externalUrls) {
            this.externalUrls = externalUrls;
            return this;
        }
        
        public Builder setGenres(final String... genres) {
            this.genres = genres;
            return this;
        }
        
        public Builder setHref(final String href) {
            this.href = href;
            return this;
        }
        
        public Builder setId(final String id) {
            this.id = id;
            return this;
        }
        
        public Builder setImages(final Image... images) {
            this.images = images;
            return this;
        }
        
        public Builder setLabel(final String label) {
            this.label = label;
            return this;
        }
        
        public Builder setName(final String name) {
            this.name = name;
            return this;
        }
        
        public Builder setPopularity(final Integer popularity) {
            this.popularity = popularity;
            return this;
        }
        
        public Builder setReleaseDate(final String releaseDate) {
            this.releaseDate = releaseDate;
            return this;
        }
        
        public Builder setReleaseDatePrecision(final ReleaseDatePrecision releaseDatePrecision) {
            this.releaseDatePrecision = releaseDatePrecision;
            return this;
        }
        
        public Builder setTracks(final Paging<TrackSimplified> tracks) {
            this.tracks = tracks;
            return this;
        }
        
        public Builder setType(final ModelObjectType type) {
            this.type = type;
            return this;
        }
        
        public Builder setUri(final String uri) {
            this.uri = uri;
            return this;
        }
        
        @Override
        public Album build() {
            return new Album(this, null);
        }
    }
    
    public static final class JsonUtil extends AbstractModelObject.JsonUtil<Album>
    {
        @Override
        public Album createModelObject(final JsonObject jsonObject) {
            if (jsonObject == null || jsonObject.isJsonNull()) {
                return null;
            }
            return new Album.Builder().setAlbumType(this.hasAndNotNull(jsonObject, "album_type") ? AlbumType.keyOf(jsonObject.get("album_type").getAsString().toLowerCase()) : null).setArtists((ArtistSimplified[])(this.hasAndNotNull(jsonObject, "artists") ? ((ArtistSimplified[])new ArtistSimplified.JsonUtil().createModelObjectArray(jsonObject.getAsJsonArray("artists"))) : null)).setAvailableMarkets((CountryCode[])(this.hasAndNotNull(jsonObject, "available_markets") ? ((CountryCode[])new Gson().fromJson(jsonObject.getAsJsonArray("available_markets"), CountryCode[].class)) : null)).setCopyrights((Copyright[])(this.hasAndNotNull(jsonObject, "copyrights") ? ((Copyright[])new Copyright.JsonUtil().createModelObjectArray(jsonObject.getAsJsonArray("copyrights"))) : null)).setExternalIds(this.hasAndNotNull(jsonObject, "external_ids") ? new ExternalId.JsonUtil().createModelObject(jsonObject.getAsJsonObject("external_ids")) : null).setExternalUrls(this.hasAndNotNull(jsonObject, "external_urls") ? new ExternalUrl.JsonUtil().createModelObject(jsonObject.getAsJsonObject("external_urls")) : null).setGenres((String[])(this.hasAndNotNull(jsonObject, "genres") ? ((String[])new Gson().fromJson(jsonObject.getAsJsonArray("genres"), String[].class)) : null)).setHref(this.hasAndNotNull(jsonObject, "href") ? jsonObject.get("href").getAsString() : null).setId(this.hasAndNotNull(jsonObject, "id") ? jsonObject.get("id").getAsString() : null).setImages((Image[])(this.hasAndNotNull(jsonObject, "images") ? ((Image[])new Image.JsonUtil().createModelObjectArray(jsonObject.getAsJsonArray("images"))) : null)).setLabel(this.hasAndNotNull(jsonObject, "label") ? jsonObject.get("label").getAsString() : null).setName(this.hasAndNotNull(jsonObject, "name") ? jsonObject.get("name").getAsString() : null).setPopularity(this.hasAndNotNull(jsonObject, "popularity") ? Integer.valueOf(jsonObject.get("popularity").getAsInt()) : null).setReleaseDate(this.hasAndNotNull(jsonObject, "release_date") ? jsonObject.get("release_date").getAsString() : null).setReleaseDatePrecision(this.hasAndNotNull(jsonObject, "release_date_precision") ? ReleaseDatePrecision.keyOf(jsonObject.get("release_date_precision").getAsString().toLowerCase()) : null).setTracks(this.hasAndNotNull(jsonObject, "tracks") ? new TrackSimplified.JsonUtil().createModelObjectPaging(jsonObject.getAsJsonObject("tracks")) : null).setType(this.hasAndNotNull(jsonObject, "type") ? ModelObjectType.keyOf(jsonObject.get("type").getAsString().toLowerCase()) : null).setUri(this.hasAndNotNull(jsonObject, "uri") ? jsonObject.get("uri").getAsString() : null).build();
        }
    }
}
