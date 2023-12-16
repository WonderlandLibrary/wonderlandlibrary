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
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.requests.data.search.interfaces.ISearchModelObject;
import se.michaelthelin.spotify.requests.data.personalization.interfaces.IArtistTrackModelObject;
import se.michaelthelin.spotify.model_objects.AbstractModelObject;

@JsonDeserialize(builder = Builder.class)
public class Artist extends AbstractModelObject implements IArtistTrackModelObject, ISearchModelObject
{
    private final ExternalUrl externalUrls;
    private final Followers followers;
    private final String[] genres;
    private final String href;
    private final String id;
    private final Image[] images;
    private final String name;
    private final Integer popularity;
    private final ModelObjectType type;
    private final String uri;
    
    private Artist(final Builder builder) {
        super(builder);
        this.externalUrls = builder.externalUrls;
        this.followers = builder.followers;
        this.genres = builder.genres;
        this.href = builder.href;
        this.id = builder.id;
        this.images = builder.images;
        this.name = builder.name;
        this.popularity = builder.popularity;
        this.type = builder.type;
        this.uri = builder.uri;
    }
    
    public ExternalUrl getExternalUrls() {
        return this.externalUrls;
    }
    
    public Followers getFollowers() {
        return this.followers;
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
    
    public String getName() {
        return this.name;
    }
    
    public Integer getPopularity() {
        return this.popularity;
    }
    
    public ModelObjectType getType() {
        return this.type;
    }
    
    public String getUri() {
        return this.uri;
    }
    
    @Override
    public String toString() {
        return invokedynamic(makeConcatWithConstants:(Ljava/lang/String;Lse/michaelthelin/spotify/model_objects/specification/ExternalUrl;Lse/michaelthelin/spotify/model_objects/specification/Followers;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;Lse/michaelthelin/spotify/enums/ModelObjectType;Ljava/lang/String;)Ljava/lang/String;, this.name, this.externalUrls, this.followers, Arrays.toString(this.genres), this.href, this.id, Arrays.toString(this.images), this.popularity, this.type, this.uri);
    }
    
    @Override
    public Builder builder() {
        return new Builder();
    }
    
    public static final class Builder extends AbstractModelObject.Builder
    {
        private ExternalUrl externalUrls;
        private Followers followers;
        private String[] genres;
        private String href;
        private String id;
        private Image[] images;
        private String name;
        private Integer popularity;
        private ModelObjectType type;
        private String uri;
        
        public Builder setExternalUrls(final ExternalUrl externalUrls) {
            this.externalUrls = externalUrls;
            return this;
        }
        
        public Builder setFollowers(final Followers followers) {
            this.followers = followers;
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
        
        public Builder setName(final String name) {
            this.name = name;
            return this;
        }
        
        public Builder setPopularity(final Integer popularity) {
            this.popularity = popularity;
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
        public Artist build() {
            return new Artist(this, null);
        }
    }
    
    public static final class JsonUtil extends AbstractModelObject.JsonUtil<Artist>
    {
        @Override
        public Artist createModelObject(final JsonObject jsonObject) {
            if (jsonObject == null || jsonObject.isJsonNull()) {
                return null;
            }
            return new Artist.Builder().setExternalUrls(this.hasAndNotNull(jsonObject, "external_urls") ? new ExternalUrl.JsonUtil().createModelObject(jsonObject.getAsJsonObject("external_urls")) : null).setFollowers(this.hasAndNotNull(jsonObject, "followers") ? new Followers.JsonUtil().createModelObject(jsonObject.getAsJsonObject("followers")) : null).setGenres((String[])(this.hasAndNotNull(jsonObject, "genres") ? ((String[])new Gson().fromJson(jsonObject.getAsJsonArray("genres"), String[].class)) : null)).setHref(this.hasAndNotNull(jsonObject, "href") ? jsonObject.get("href").getAsString() : null).setId(this.hasAndNotNull(jsonObject, "id") ? jsonObject.get("id").getAsString() : null).setImages((Image[])(this.hasAndNotNull(jsonObject, "images") ? ((Image[])new Image.JsonUtil().createModelObjectArray(jsonObject.getAsJsonArray("images"))) : null)).setName(this.hasAndNotNull(jsonObject, "name") ? jsonObject.get("name").getAsString() : null).setPopularity(this.hasAndNotNull(jsonObject, "popularity") ? Integer.valueOf(jsonObject.get("popularity").getAsInt()) : null).setType(this.hasAndNotNull(jsonObject, "type") ? ModelObjectType.keyOf(jsonObject.get("type").getAsString().toLowerCase()) : null).setUri(this.hasAndNotNull(jsonObject, "uri") ? jsonObject.get("uri").getAsString() : null).build();
        }
    }
}
