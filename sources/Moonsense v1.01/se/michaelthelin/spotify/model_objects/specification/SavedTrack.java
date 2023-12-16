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
public class SavedTrack extends AbstractModelObject
{
    private final Date addedAt;
    private final Track track;
    
    private SavedTrack(final Builder builder) {
        super(builder);
        this.addedAt = builder.addedAt;
        this.track = builder.track;
    }
    
    public Date getAddedAt() {
        return this.addedAt;
    }
    
    public Track getTrack() {
        return this.track;
    }
    
    @Override
    public String toString() {
        return invokedynamic(makeConcatWithConstants:(Ljava/util/Date;Lse/michaelthelin/spotify/model_objects/specification/Track;)Ljava/lang/String;, this.addedAt, this.track);
    }
    
    @Override
    public Builder builder() {
        return new Builder();
    }
    
    public static final class Builder extends AbstractModelObject.Builder
    {
        private Date addedAt;
        private Track track;
        
        public Builder setAddedAt(final Date addedAt) {
            this.addedAt = addedAt;
            return this;
        }
        
        public Builder setTrack(final Track track) {
            this.track = track;
            return this;
        }
        
        @Override
        public SavedTrack build() {
            return new SavedTrack(this, null);
        }
    }
    
    public static final class JsonUtil extends AbstractModelObject.JsonUtil<SavedTrack>
    {
        @Override
        public SavedTrack createModelObject(final JsonObject jsonObject) {
            if (jsonObject == null || jsonObject.isJsonNull()) {
                return null;
            }
            try {
                return new SavedTrack.Builder().setAddedAt(this.hasAndNotNull(jsonObject, "added_at") ? SpotifyApi.parseDefaultDate(jsonObject.get("added_at").getAsString()) : null).setTrack(this.hasAndNotNull(jsonObject, "track") ? new Track.JsonUtil().createModelObject(jsonObject.getAsJsonObject("track")) : null).build();
            }
            catch (ParseException e) {
                SpotifyApi.LOGGER.log(Level.SEVERE, e.getMessage());
                return null;
            }
        }
    }
}
