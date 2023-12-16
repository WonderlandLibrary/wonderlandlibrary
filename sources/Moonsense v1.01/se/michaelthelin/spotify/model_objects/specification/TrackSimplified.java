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
import com.neovisionaries.i18n.CountryCode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.AbstractModelObject;

@JsonDeserialize(builder = Builder.class)
public class TrackSimplified extends AbstractModelObject
{
    private final ArtistSimplified[] artists;
    private final CountryCode[] availableMarkets;
    private final Integer discNumber;
    private final Integer durationMs;
    private final Boolean explicit;
    private final ExternalUrl externalUrls;
    private final String href;
    private final String id;
    private final Boolean isPlayable;
    private final TrackLink linkedFrom;
    private final String name;
    private final String previewUrl;
    private final Integer trackNumber;
    private final ModelObjectType type;
    private final String uri;
    
    private TrackSimplified(final Builder builder) {
        super(builder);
        this.artists = builder.artists;
        this.availableMarkets = builder.availableMarkets;
        this.discNumber = builder.discNumber;
        this.durationMs = builder.durationMs;
        this.explicit = builder.explicit;
        this.externalUrls = builder.externalUrls;
        this.href = builder.href;
        this.id = builder.id;
        this.isPlayable = builder.isPlayable;
        this.linkedFrom = builder.linkedFrom;
        this.name = builder.name;
        this.previewUrl = builder.previewUrl;
        this.trackNumber = builder.trackNumber;
        this.type = builder.type;
        this.uri = builder.uri;
    }
    
    public ArtistSimplified[] getArtists() {
        return this.artists;
    }
    
    public CountryCode[] getAvailableMarkets() {
        return this.availableMarkets;
    }
    
    public Integer getDiscNumber() {
        return this.discNumber;
    }
    
    public Integer getDurationMs() {
        return this.durationMs;
    }
    
    public Boolean getIsExplicit() {
        return this.explicit;
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
    
    public Boolean getIsPlayable() {
        return this.isPlayable;
    }
    
    public TrackLink getLinkedFrom() {
        return this.linkedFrom;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getPreviewUrl() {
        return this.previewUrl;
    }
    
    public Integer getTrackNumber() {
        return this.trackNumber;
    }
    
    public ModelObjectType getType() {
        return this.type;
    }
    
    public String getUri() {
        return this.uri;
    }
    
    @Override
    public String toString() {
        return invokedynamic(makeConcatWithConstants:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/Integer;Ljava/lang/Boolean;Lse/michaelthelin/spotify/model_objects/specification/ExternalUrl;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Boolean;Lse/michaelthelin/spotify/model_objects/specification/TrackLink;Ljava/lang/String;Ljava/lang/Integer;Lse/michaelthelin/spotify/enums/ModelObjectType;Ljava/lang/String;)Ljava/lang/String;, this.name, Arrays.toString(this.artists), Arrays.toString(this.availableMarkets), this.discNumber, this.durationMs, this.explicit, this.externalUrls, this.href, this.id, this.isPlayable, this.linkedFrom, this.previewUrl, this.trackNumber, this.type, this.uri);
    }
    
    @Override
    public Builder builder() {
        return new Builder();
    }
    
    public static final class Builder extends AbstractModelObject.Builder
    {
        private ArtistSimplified[] artists;
        private CountryCode[] availableMarkets;
        private Integer discNumber;
        private Integer durationMs;
        private Boolean explicit;
        private ExternalUrl externalUrls;
        private String href;
        private String id;
        private Boolean isPlayable;
        private TrackLink linkedFrom;
        private String name;
        private String previewUrl;
        private Integer trackNumber;
        private ModelObjectType type;
        private String uri;
        
        public Builder setArtists(final ArtistSimplified... artists) {
            this.artists = artists;
            return this;
        }
        
        public Builder setAvailableMarkets(final CountryCode... availableMarkets) {
            this.availableMarkets = availableMarkets;
            return this;
        }
        
        public Builder setDiscNumber(final Integer discNumber) {
            this.discNumber = discNumber;
            return this;
        }
        
        public Builder setDurationMs(final Integer durationMs) {
            this.durationMs = durationMs;
            return this;
        }
        
        public Builder setExplicit(final Boolean explicit) {
            this.explicit = explicit;
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
        
        public Builder setIsPlayable(final Boolean isPlayable) {
            this.isPlayable = isPlayable;
            return this;
        }
        
        public Builder setLinkedFrom(final TrackLink linkedFrom) {
            this.linkedFrom = linkedFrom;
            return this;
        }
        
        public Builder setName(final String name) {
            this.name = name;
            return this;
        }
        
        public Builder setPreviewUrl(final String previewUrl) {
            this.previewUrl = previewUrl;
            return this;
        }
        
        public Builder setTrackNumber(final Integer trackNumber) {
            this.trackNumber = trackNumber;
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
        public TrackSimplified build() {
            return new TrackSimplified(this, null);
        }
    }
    
    public static final class JsonUtil extends AbstractModelObject.JsonUtil<TrackSimplified>
    {
        @Override
        public TrackSimplified createModelObject(final JsonObject jsonObject) {
            if (jsonObject == null || jsonObject.isJsonNull()) {
                return null;
            }
            return new TrackSimplified.Builder().setArtists((ArtistSimplified[])(this.hasAndNotNull(jsonObject, "artists") ? ((ArtistSimplified[])new ArtistSimplified.JsonUtil().createModelObjectArray(jsonObject.getAsJsonArray("artists"))) : null)).setAvailableMarkets((CountryCode[])(this.hasAndNotNull(jsonObject, "available_markets") ? ((CountryCode[])new Gson().fromJson(jsonObject.getAsJsonArray("available_markets"), CountryCode[].class)) : null)).setDiscNumber(this.hasAndNotNull(jsonObject, "disc_number") ? Integer.valueOf(jsonObject.get("disc_number").getAsInt()) : null).setDurationMs(this.hasAndNotNull(jsonObject, "duration_ms") ? Integer.valueOf(jsonObject.get("duration_ms").getAsInt()) : null).setExplicit(this.hasAndNotNull(jsonObject, "explicit") ? Boolean.valueOf(jsonObject.get("explicit").getAsBoolean()) : null).setExternalUrls(this.hasAndNotNull(jsonObject, "external_urls") ? new ExternalUrl.JsonUtil().createModelObject(jsonObject.getAsJsonObject("external_urls")) : null).setHref(this.hasAndNotNull(jsonObject, "href") ? jsonObject.get("href").getAsString() : null).setId(this.hasAndNotNull(jsonObject, "id") ? jsonObject.get("id").getAsString() : null).setIsPlayable(this.hasAndNotNull(jsonObject, "is_playable") ? Boolean.valueOf(jsonObject.get("is_playable").getAsBoolean()) : null).setLinkedFrom(this.hasAndNotNull(jsonObject, "linked_from") ? new TrackLink.JsonUtil().createModelObject(jsonObject.get("linked_from").getAsJsonObject()) : null).setName(this.hasAndNotNull(jsonObject, "name") ? jsonObject.get("name").getAsString() : null).setPreviewUrl(this.hasAndNotNull(jsonObject, "preview_url") ? jsonObject.get("preview_url").getAsString() : null).setTrackNumber(this.hasAndNotNull(jsonObject, "track_number") ? Integer.valueOf(jsonObject.get("track_number").getAsInt()) : null).setType(this.hasAndNotNull(jsonObject, "type") ? ModelObjectType.keyOf(jsonObject.get("type").getAsString().toLowerCase()) : null).setUri(this.hasAndNotNull(jsonObject, "uri") ? jsonObject.get("uri").getAsString() : null).build();
        }
    }
}
