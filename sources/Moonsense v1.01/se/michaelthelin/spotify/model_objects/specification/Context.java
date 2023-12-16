// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.model_objects.specification;

import com.google.gson.JsonObject;
import se.michaelthelin.spotify.model_objects.IModelObject;
import se.michaelthelin.spotify.enums.ModelObjectType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.AbstractModelObject;

@JsonDeserialize(builder = Builder.class)
public class Context extends AbstractModelObject
{
    private final ModelObjectType type;
    private final String href;
    private final ExternalUrl externalUrls;
    private final String uri;
    
    private Context(final Builder builder) {
        super(builder);
        this.type = builder.type;
        this.href = builder.href;
        this.externalUrls = builder.externalUrls;
        this.uri = builder.uri;
    }
    
    public ModelObjectType getType() {
        return this.type;
    }
    
    public String getHref() {
        return this.href;
    }
    
    public ExternalUrl getExternalUrls() {
        return this.externalUrls;
    }
    
    public String getUri() {
        return this.uri;
    }
    
    @Override
    public String toString() {
        return invokedynamic(makeConcatWithConstants:(Lse/michaelthelin/spotify/enums/ModelObjectType;Ljava/lang/String;Lse/michaelthelin/spotify/model_objects/specification/ExternalUrl;Ljava/lang/String;)Ljava/lang/String;, this.type, this.href, this.externalUrls, this.uri);
    }
    
    @Override
    public Builder builder() {
        return new Builder();
    }
    
    public static final class Builder extends AbstractModelObject.Builder
    {
        private ModelObjectType type;
        private String href;
        private ExternalUrl externalUrls;
        private String uri;
        
        public Builder setType(final ModelObjectType type) {
            this.type = type;
            return this;
        }
        
        public Builder setHref(final String href) {
            this.href = href;
            return this;
        }
        
        public Builder setExternalUrls(final ExternalUrl externalUrls) {
            this.externalUrls = externalUrls;
            return this;
        }
        
        public Builder setUri(final String uri) {
            this.uri = uri;
            return this;
        }
        
        @Override
        public Context build() {
            return new Context(this, null);
        }
    }
    
    public static final class JsonUtil extends AbstractModelObject.JsonUtil<Context>
    {
        @Override
        public Context createModelObject(final JsonObject jsonObject) {
            if (jsonObject == null || jsonObject.isJsonNull()) {
                return null;
            }
            return new Context.Builder().setType(this.hasAndNotNull(jsonObject, "type") ? ModelObjectType.keyOf(jsonObject.get("type").getAsString().toLowerCase()) : null).setHref(this.hasAndNotNull(jsonObject, "href") ? jsonObject.get("href").getAsString() : null).setExternalUrls(this.hasAndNotNull(jsonObject, "external_urls") ? new ExternalUrl.JsonUtil().createModelObject(jsonObject.getAsJsonObject("external_urls")) : null).setUri(this.hasAndNotNull(jsonObject, "uri") ? jsonObject.get("uri").getAsString() : null).build();
        }
    }
}
