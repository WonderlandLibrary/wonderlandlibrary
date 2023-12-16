// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.model_objects.specification;

import java.text.ParseException;
import java.util.logging.Level;
import se.michaelthelin.spotify.SpotifyApi;
import com.google.gson.JsonObject;
import se.michaelthelin.spotify.model_objects.IModelObject;
import java.util.Date;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.AbstractModelObject;

@JsonDeserialize(builder = Builder.class)
public class SavedAlbum extends AbstractModelObject
{
    private final Date addedAt;
    private final Album album;
    
    private SavedAlbum(final Builder builder) {
        super(builder);
        this.addedAt = builder.addedAt;
        this.album = builder.album;
    }
    
    public Date getAddedAt() {
        return this.addedAt;
    }
    
    public Album getAlbum() {
        return this.album;
    }
    
    @Override
    public String toString() {
        return invokedynamic(makeConcatWithConstants:(Ljava/util/Date;Lse/michaelthelin/spotify/model_objects/specification/Album;)Ljava/lang/String;, this.addedAt, this.album);
    }
    
    @Override
    public Builder builder() {
        return new Builder();
    }
    
    public static final class Builder extends AbstractModelObject.Builder
    {
        private Date addedAt;
        private Album album;
        
        public Builder setAddedAt(final Date addedAt) {
            this.addedAt = addedAt;
            return this;
        }
        
        public Builder setAlbum(final Album album) {
            this.album = album;
            return this;
        }
        
        @Override
        public SavedAlbum build() {
            return new SavedAlbum(this, null);
        }
    }
    
    public static final class JsonUtil extends AbstractModelObject.JsonUtil<SavedAlbum>
    {
        @Override
        public SavedAlbum createModelObject(final JsonObject jsonObject) {
            if (jsonObject == null || jsonObject.isJsonNull()) {
                return null;
            }
            try {
                return new SavedAlbum.Builder().setAddedAt(this.hasAndNotNull(jsonObject, "added_at") ? SpotifyApi.parseDefaultDate(jsonObject.get("added_at").getAsString()) : null).setAlbum(this.hasAndNotNull(jsonObject, "album") ? new Album.JsonUtil().createModelObject(jsonObject.getAsJsonObject("album")) : null).build();
            }
            catch (ParseException e) {
                SpotifyApi.LOGGER.log(Level.SEVERE, e.getMessage());
                return null;
            }
        }
    }
}
