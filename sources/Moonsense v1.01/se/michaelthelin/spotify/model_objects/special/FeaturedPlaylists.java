// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.model_objects.special;

import com.google.gson.JsonObject;
import se.michaelthelin.spotify.model_objects.IModelObject;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.AbstractModelObject;

@JsonDeserialize(builder = Builder.class)
public class FeaturedPlaylists extends AbstractModelObject
{
    private final String message;
    private final Paging<PlaylistSimplified> playlists;
    
    private FeaturedPlaylists(final Builder builder) {
        super(builder);
        this.message = builder.message;
        this.playlists = builder.playlists;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public Paging<PlaylistSimplified> getPlaylists() {
        return this.playlists;
    }
    
    @Override
    public String toString() {
        return invokedynamic(makeConcatWithConstants:(Ljava/lang/String;Lse/michaelthelin/spotify/model_objects/specification/Paging;)Ljava/lang/String;, this.message, this.playlists);
    }
    
    @Override
    public Builder builder() {
        return new Builder();
    }
    
    public static final class Builder extends AbstractModelObject.Builder
    {
        private String message;
        private Paging<PlaylistSimplified> playlists;
        
        public Builder setMessage(final String message) {
            this.message = message;
            return this;
        }
        
        public Builder setPlaylists(final Paging<PlaylistSimplified> playlists) {
            this.playlists = playlists;
            return this;
        }
        
        @Override
        public FeaturedPlaylists build() {
            return new FeaturedPlaylists(this, null);
        }
    }
    
    public static final class JsonUtil extends AbstractModelObject.JsonUtil<FeaturedPlaylists>
    {
        @Override
        public FeaturedPlaylists createModelObject(final JsonObject jsonObject) {
            if (jsonObject == null || jsonObject.isJsonNull()) {
                return null;
            }
            return new FeaturedPlaylists.Builder().setMessage(this.hasAndNotNull(jsonObject, "message") ? jsonObject.get("message").getAsString() : null).setPlaylists(this.hasAndNotNull(jsonObject, "playlists") ? new PlaylistSimplified.JsonUtil().createModelObjectPaging(jsonObject.getAsJsonObject("playlists")) : null).build();
        }
    }
}
