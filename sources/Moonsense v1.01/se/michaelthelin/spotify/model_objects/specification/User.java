// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.model_objects.specification;

import com.google.gson.JsonObject;
import se.michaelthelin.spotify.model_objects.IModelObject;
import java.util.Arrays;
import se.michaelthelin.spotify.enums.ModelObjectType;
import se.michaelthelin.spotify.enums.ProductType;
import com.neovisionaries.i18n.CountryCode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.AbstractModelObject;

@JsonDeserialize(builder = Builder.class)
public class User extends AbstractModelObject
{
    private final String birthdate;
    private final CountryCode country;
    private final String displayName;
    private final String email;
    private final ExternalUrl externalUrls;
    private final Followers followers;
    private final String href;
    private final String id;
    private final Image[] images;
    private final ProductType product;
    private final ModelObjectType type;
    private final String uri;
    
    private User(final Builder builder) {
        super(builder);
        this.birthdate = builder.birthdate;
        this.country = builder.country;
        this.displayName = builder.displayName;
        this.email = builder.email;
        this.externalUrls = builder.externalUrls;
        this.followers = builder.followers;
        this.href = builder.href;
        this.id = builder.id;
        this.images = builder.images;
        this.product = builder.product;
        this.type = builder.type;
        this.uri = builder.uri;
    }
    
    public String getBirthdate() {
        return this.birthdate;
    }
    
    public CountryCode getCountry() {
        return this.country;
    }
    
    public String getDisplayName() {
        return this.displayName;
    }
    
    public String getEmail() {
        return this.email;
    }
    
    public ExternalUrl getExternalUrls() {
        return this.externalUrls;
    }
    
    public Followers getFollowers() {
        return this.followers;
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
    
    public ProductType getProduct() {
        return this.product;
    }
    
    public ModelObjectType getType() {
        return this.type;
    }
    
    public String getUri() {
        return this.uri;
    }
    
    @Override
    public String toString() {
        return invokedynamic(makeConcatWithConstants:(Ljava/lang/String;Lcom/neovisionaries/i18n/CountryCode;Ljava/lang/String;Ljava/lang/String;Lse/michaelthelin/spotify/model_objects/specification/ExternalUrl;Lse/michaelthelin/spotify/model_objects/specification/Followers;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lse/michaelthelin/spotify/enums/ProductType;Lse/michaelthelin/spotify/enums/ModelObjectType;Ljava/lang/String;)Ljava/lang/String;, this.birthdate, this.country, this.displayName, this.email, this.externalUrls, this.followers, this.href, this.id, Arrays.toString(this.images), this.product, this.type, this.uri);
    }
    
    @Override
    public Builder builder() {
        return new Builder();
    }
    
    public static final class Builder extends AbstractModelObject.Builder
    {
        private String birthdate;
        private CountryCode country;
        private String displayName;
        private String email;
        private ExternalUrl externalUrls;
        private Followers followers;
        private String href;
        private String id;
        private Image[] images;
        private ProductType product;
        private ModelObjectType type;
        private String uri;
        
        public Builder setBirthdate(final String birthdate) {
            this.birthdate = birthdate;
            return this;
        }
        
        public Builder setCountry(final CountryCode country) {
            this.country = country;
            return this;
        }
        
        public Builder setDisplayName(final String displayName) {
            this.displayName = displayName;
            return this;
        }
        
        public Builder setEmail(final String email) {
            this.email = email;
            return this;
        }
        
        public Builder setExternalUrls(final ExternalUrl externalUrls) {
            this.externalUrls = externalUrls;
            return this;
        }
        
        public Builder setFollowers(final Followers followers) {
            this.followers = followers;
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
        
        public Builder setProduct(final ProductType product) {
            this.product = product;
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
        public User build() {
            return new User(this, null);
        }
    }
    
    public static final class JsonUtil extends AbstractModelObject.JsonUtil<User>
    {
        @Override
        public User createModelObject(final JsonObject jsonObject) {
            if (jsonObject == null || jsonObject.isJsonNull()) {
                return null;
            }
            return new User.Builder().setBirthdate(this.hasAndNotNull(jsonObject, "birthdate") ? jsonObject.get("birthdate").getAsString() : null).setCountry(this.hasAndNotNull(jsonObject, "country") ? CountryCode.getByCode(jsonObject.get("country").getAsString()) : null).setDisplayName(this.hasAndNotNull(jsonObject, "display_name") ? jsonObject.get("display_name").getAsString() : null).setEmail(this.hasAndNotNull(jsonObject, "email") ? jsonObject.get("email").getAsString() : null).setExternalUrls(this.hasAndNotNull(jsonObject, "external_urls") ? new ExternalUrl.JsonUtil().createModelObject(jsonObject.getAsJsonObject("external_urls")) : null).setFollowers(this.hasAndNotNull(jsonObject, "followers") ? new Followers.JsonUtil().createModelObject(jsonObject.getAsJsonObject("followers")) : null).setHref(this.hasAndNotNull(jsonObject, "href") ? jsonObject.get("href").getAsString() : null).setId(this.hasAndNotNull(jsonObject, "id") ? jsonObject.get("id").getAsString() : null).setImages((Image[])(this.hasAndNotNull(jsonObject, "images") ? ((Image[])new Image.JsonUtil().createModelObjectArray(jsonObject.getAsJsonArray("images"))) : null)).setProduct(this.hasAndNotNull(jsonObject, "product") ? ProductType.keyOf(jsonObject.get("product").getAsString().toLowerCase()) : null).setType(this.hasAndNotNull(jsonObject, "type") ? ModelObjectType.keyOf(jsonObject.get("type").getAsString().toLowerCase()) : null).setUri(this.hasAndNotNull(jsonObject, "uri") ? jsonObject.get("uri").getAsString() : null).build();
        }
    }
}
