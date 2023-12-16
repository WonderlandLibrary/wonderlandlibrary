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
public class Show extends AbstractModelObject
{
    private final CountryCode[] availableMarkets;
    private final Copyright[] copyrights;
    private final String description;
    private final Boolean explicit;
    private final Paging<EpisodeSimplified> episodes;
    private final ExternalUrl externalUrls;
    private final String href;
    private final String id;
    private final Image[] images;
    private final Boolean isExternallyHosted;
    private final String[] languages;
    private final String mediaType;
    private final String name;
    private final String publisher;
    private final ModelObjectType type;
    private final String uri;
    
    public Show(final Builder builder) {
        super(builder);
        this.availableMarkets = builder.availableMarkets;
        this.copyrights = builder.copyrights;
        this.description = builder.description;
        this.explicit = builder.explicit;
        this.episodes = builder.episodes;
        this.externalUrls = builder.externalUrls;
        this.href = builder.href;
        this.id = builder.id;
        this.images = builder.images;
        this.isExternallyHosted = builder.isExternallyHosted;
        this.languages = builder.languages;
        this.mediaType = builder.mediaType;
        this.name = builder.name;
        this.publisher = builder.publisher;
        this.type = builder.type;
        this.uri = builder.uri;
    }
    
    public CountryCode[] getAvailableMarkets() {
        return this.availableMarkets;
    }
    
    public Copyright[] getCopyrights() {
        return this.copyrights;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public Boolean getExplicit() {
        return this.explicit;
    }
    
    public Paging<EpisodeSimplified> getEpisodes() {
        return this.episodes;
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
    
    public Boolean getExternallyHosted() {
        return this.isExternallyHosted;
    }
    
    public String[] getLanguages() {
        return this.languages;
    }
    
    public String getMediaType() {
        return this.mediaType;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getPublisher() {
        return this.publisher;
    }
    
    public ModelObjectType getType() {
        return this.type;
    }
    
    public String getUri() {
        return this.uri;
    }
    
    @Override
    public String toString() {
        return invokedynamic(makeConcatWithConstants:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Boolean;Lse/michaelthelin/spotify/model_objects/specification/Paging;Lse/michaelthelin/spotify/model_objects/specification/ExternalUrl;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Boolean;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lse/michaelthelin/spotify/enums/ModelObjectType;Ljava/lang/String;)Ljava/lang/String;, Arrays.toString(this.availableMarkets), Arrays.toString(this.copyrights), this.description, this.explicit, this.episodes, this.externalUrls, this.href, this.id, Arrays.toString(this.images), this.isExternallyHosted, Arrays.toString(this.languages), this.mediaType, this.name, this.publisher, this.type, this.uri);
    }
    
    @Override
    public Builder builder() {
        return new Builder();
    }
    
    public static final class Builder extends AbstractModelObject.Builder
    {
        private CountryCode[] availableMarkets;
        private Copyright[] copyrights;
        private String description;
        private Boolean explicit;
        private Paging<EpisodeSimplified> episodes;
        private ExternalUrl externalUrls;
        private String href;
        private String id;
        private Image[] images;
        private Boolean isExternallyHosted;
        private String[] languages;
        private String mediaType;
        private String name;
        private String publisher;
        private ModelObjectType type;
        private String uri;
        
        public Builder setAvailableMarkets(final CountryCode... availableMarkets) {
            this.availableMarkets = availableMarkets;
            return this;
        }
        
        public Builder setCopyrights(final Copyright... copyrights) {
            this.copyrights = copyrights;
            return this;
        }
        
        public Builder setDescription(final String description) {
            this.description = description;
            return this;
        }
        
        public Builder setExplicit(final Boolean explicit) {
            this.explicit = explicit;
            return this;
        }
        
        public Builder setEpisodes(final Paging<EpisodeSimplified> episodes) {
            this.episodes = episodes;
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
        
        public Builder setExternallyHosted(final Boolean externallyHosted) {
            this.isExternallyHosted = externallyHosted;
            return this;
        }
        
        public Builder setLanguages(final String[] languages) {
            this.languages = languages;
            return this;
        }
        
        public Builder setMediaType(final String mediaType) {
            this.mediaType = mediaType;
            return this;
        }
        
        public Builder setName(final String name) {
            this.name = name;
            return this;
        }
        
        public Builder setPublisher(final String publisher) {
            this.publisher = publisher;
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
        public Show build() {
            return new Show(this);
        }
    }
    
    public static final class JsonUtil extends AbstractModelObject.JsonUtil<Show>
    {
        @Override
        public Show createModelObject(final JsonObject jsonObject) {
            if (jsonObject == null || jsonObject.isJsonNull()) {
                return null;
            }
            return new Show.Builder().setAvailableMarkets((CountryCode[])(this.hasAndNotNull(jsonObject, "available_markets") ? ((CountryCode[])new Gson().fromJson(jsonObject.getAsJsonArray("available_markets"), CountryCode[].class)) : null)).setCopyrights((Copyright[])(this.hasAndNotNull(jsonObject, "copyrights") ? ((Copyright[])new Gson().fromJson(jsonObject.getAsJsonArray("copyrights"), Copyright[].class)) : null)).setDescription(this.hasAndNotNull(jsonObject, "description") ? jsonObject.get("description").getAsString() : null).setExplicit(this.hasAndNotNull(jsonObject, "explicit") ? Boolean.valueOf(jsonObject.get("explicit").getAsBoolean()) : null).setEpisodes(this.hasAndNotNull(jsonObject, "episodes") ? new EpisodeSimplified.JsonUtil().createModelObjectPaging(jsonObject.getAsJsonObject("episodes")) : null).setExternalUrls(this.hasAndNotNull(jsonObject, "external_urls") ? new ExternalUrl.JsonUtil().createModelObject(jsonObject.getAsJsonObject("external_urls")) : null).setHref(this.hasAndNotNull(jsonObject, "href") ? jsonObject.get("href").getAsString() : null).setId(this.hasAndNotNull(jsonObject, "id") ? jsonObject.get("id").getAsString() : null).setImages((Image[])(this.hasAndNotNull(jsonObject, "images") ? ((Image[])new Image.JsonUtil().createModelObjectArray(jsonObject.getAsJsonArray("images"))) : null)).setExternallyHosted(this.hasAndNotNull(jsonObject, "is_externally_hosted") ? Boolean.valueOf(jsonObject.get("is_externally_hosted").getAsBoolean()) : null).setLanguages((String[])(this.hasAndNotNull(jsonObject, "languages") ? ((String[])new Gson().fromJson(jsonObject.getAsJsonArray("languages"), String[].class)) : null)).setMediaType(this.hasAndNotNull(jsonObject, "media_type") ? jsonObject.get("media_type").getAsString() : null).setName(this.hasAndNotNull(jsonObject, "name") ? jsonObject.get("name").getAsString() : null).setPublisher(this.hasAndNotNull(jsonObject, "publisher") ? jsonObject.get("publisher").getAsString() : null).setType(this.hasAndNotNull(jsonObject, "type") ? ModelObjectType.keyOf(jsonObject.get("type").getAsString().toLowerCase()) : null).setUri(this.hasAndNotNull(jsonObject, "uri") ? jsonObject.get("uri").getAsString() : null).build();
        }
    }
}
