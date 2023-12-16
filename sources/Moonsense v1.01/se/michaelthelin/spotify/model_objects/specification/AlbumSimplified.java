// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.model_objects.specification;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import se.michaelthelin.spotify.model_objects.IModelObject;
import java.util.Arrays;
import se.michaelthelin.spotify.enums.ModelObjectType;
import se.michaelthelin.spotify.model_objects.miscellaneous.Restrictions;
import se.michaelthelin.spotify.enums.ReleaseDatePrecision;
import com.neovisionaries.i18n.CountryCode;
import se.michaelthelin.spotify.enums.AlbumType;
import se.michaelthelin.spotify.enums.AlbumGroup;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.requests.data.search.interfaces.ISearchModelObject;
import se.michaelthelin.spotify.model_objects.AbstractModelObject;

@JsonDeserialize(builder = Builder.class)
public class AlbumSimplified extends AbstractModelObject implements ISearchModelObject
{
    private final AlbumGroup albumGroup;
    private final AlbumType albumType;
    private final ArtistSimplified[] artists;
    private final CountryCode[] availableMarkets;
    private final ExternalUrl externalUrls;
    private final String href;
    private final String id;
    private final Image[] images;
    private final String name;
    private final String releaseDate;
    private final ReleaseDatePrecision releaseDatePrecision;
    private final Restrictions restrictions;
    private final ModelObjectType type;
    private final String uri;
    
    private AlbumSimplified(final Builder builder) {
        super(builder);
        this.albumGroup = builder.albumGroup;
        this.albumType = builder.albumType;
        this.artists = builder.artists;
        this.availableMarkets = builder.availableMarkets;
        this.externalUrls = builder.externalUrls;
        this.href = builder.href;
        this.id = builder.id;
        this.images = builder.images;
        this.name = builder.name;
        this.releaseDate = builder.releaseDate;
        this.releaseDatePrecision = builder.releaseDatePrecision;
        this.restrictions = builder.restrictions;
        this.type = builder.type;
        this.uri = builder.uri;
    }
    
    public AlbumGroup getAlbumGroup() {
        return this.albumGroup;
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
    
    public ExternalUrl getExternalUrls() {
        return this.externalUrls;
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
    
    public String getName() {
        return this.name;
    }
    
    public String getReleaseDate() {
        return this.releaseDate;
    }
    
    public ReleaseDatePrecision getReleaseDatePrecision() {
        return this.releaseDatePrecision;
    }
    
    public Restrictions getRestrictions() {
        return this.restrictions;
    }
    
    public ModelObjectType getType() {
        return this.type;
    }
    
    public String getUri() {
        return this.uri;
    }
    
    @Override
    public String toString() {
        return invokedynamic(makeConcatWithConstants:(Ljava/lang/String;Ljava/lang/String;Lse/michaelthelin/spotify/enums/AlbumGroup;Lse/michaelthelin/spotify/enums/AlbumType;Ljava/lang/String;Lse/michaelthelin/spotify/model_objects/specification/ExternalUrl;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lse/michaelthelin/spotify/enums/ReleaseDatePrecision;Lse/michaelthelin/spotify/model_objects/miscellaneous/Restrictions;Lse/michaelthelin/spotify/enums/ModelObjectType;Ljava/lang/String;)Ljava/lang/String;, Arrays.toString(this.artists), this.name, this.albumGroup, this.albumType, Arrays.toString(this.availableMarkets), this.externalUrls, this.href, this.id, Arrays.toString(this.images), this.releaseDate, this.releaseDatePrecision, this.restrictions, this.type, this.uri);
    }
    
    @Override
    public Builder builder() {
        return new Builder();
    }
    
    public static final class Builder extends AbstractModelObject.Builder
    {
        private AlbumGroup albumGroup;
        private AlbumType albumType;
        private ArtistSimplified[] artists;
        private CountryCode[] availableMarkets;
        private ExternalUrl externalUrls;
        private String href;
        private String id;
        private Image[] images;
        private String name;
        private String releaseDate;
        private ReleaseDatePrecision releaseDatePrecision;
        private Restrictions restrictions;
        private ModelObjectType type;
        private String uri;
        
        public Builder setAlbumGroup(final AlbumGroup albumGroup) {
            this.albumGroup = albumGroup;
            return this;
        }
        
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
        
        public Builder setExternalUrls(final ExternalUrl externalUrls) {
            this.externalUrls = externalUrls;
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
        
        public Builder setName(final String name) {
            this.name = name;
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
        
        public Builder setRestrictions(final Restrictions restrictions) {
            this.restrictions = restrictions;
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
        public AlbumSimplified build() {
            return new AlbumSimplified(this, null);
        }
    }
    
    public static final class JsonUtil extends AbstractModelObject.JsonUtil<AlbumSimplified>
    {
        @Override
        public AlbumSimplified createModelObject(final JsonObject jsonObject) {
            if (jsonObject == null || jsonObject.isJsonNull()) {
                return null;
            }
            return new AlbumSimplified.Builder().setAlbumGroup(this.hasAndNotNull(jsonObject, "album_group") ? AlbumGroup.keyOf(jsonObject.get("album_group").getAsString().toLowerCase()) : null).setAlbumType(this.hasAndNotNull(jsonObject, "album_type") ? AlbumType.keyOf(jsonObject.get("album_type").getAsString().toLowerCase()) : null).setArtists((ArtistSimplified[])(this.hasAndNotNull(jsonObject, "artists") ? ((ArtistSimplified[])new ArtistSimplified.JsonUtil().createModelObjectArray(jsonObject.getAsJsonArray("artists"))) : null)).setAvailableMarkets((CountryCode[])(this.hasAndNotNull(jsonObject, "available_markets") ? ((CountryCode[])new Gson().fromJson(jsonObject.get("available_markets"), CountryCode[].class)) : null)).setExternalUrls(this.hasAndNotNull(jsonObject, "external_urls") ? new ExternalUrl.JsonUtil().createModelObject(jsonObject.getAsJsonObject("external_urls")) : null).setHref(this.hasAndNotNull(jsonObject, "href") ? jsonObject.get("href").getAsString() : null).setId(this.hasAndNotNull(jsonObject, "id") ? jsonObject.get("id").getAsString() : null).setImages((Image[])(this.hasAndNotNull(jsonObject, "images") ? ((Image[])new Image.JsonUtil().createModelObjectArray(jsonObject.getAsJsonArray("images"))) : null)).setName(this.hasAndNotNull(jsonObject, "name") ? jsonObject.get("name").getAsString() : null).setReleaseDate(this.hasAndNotNull(jsonObject, "release_date") ? jsonObject.get("release_date").getAsString() : null).setReleaseDatePrecision(this.hasAndNotNull(jsonObject, "release_date_precision") ? ReleaseDatePrecision.keyOf(jsonObject.get("release_date_precision").getAsString().toLowerCase()) : null).setRestrictions(this.hasAndNotNull(jsonObject, "restrictions") ? new Restrictions.JsonUtil().createModelObject(jsonObject.getAsJsonObject("restrictions")) : null).setType(this.hasAndNotNull(jsonObject, "type") ? ModelObjectType.keyOf(jsonObject.get("type").getAsString().toLowerCase()) : null).setUri(this.hasAndNotNull(jsonObject, "uri") ? jsonObject.get("uri").getAsString() : null).build();
        }
    }
}
