// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.model_objects.specification;

import java.text.ParseException;
import java.util.logging.Level;
import se.michaelthelin.spotify.SpotifyApi;
import com.google.gson.JsonObject;
import se.michaelthelin.spotify.model_objects.IModelObject;
import se.michaelthelin.spotify.model_objects.IPlaylistItem;
import java.util.Date;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.AbstractModelObject;

@JsonDeserialize(builder = Builder.class)
public class PlaylistTrack extends AbstractModelObject
{
    private final Date addedAt;
    private final User addedBy;
    private final Boolean isLocal;
    private final IPlaylistItem track;
    
    private PlaylistTrack(final Builder builder) {
        super(builder);
        this.addedAt = builder.addedAt;
        this.addedBy = builder.addedBy;
        this.isLocal = builder.isLocal;
        this.track = builder.track;
    }
    
    public Date getAddedAt() {
        return this.addedAt;
    }
    
    public User getAddedBy() {
        return this.addedBy;
    }
    
    public Boolean getIsLocal() {
        return this.isLocal;
    }
    
    public IPlaylistItem getTrack() {
        return this.track;
    }
    
    @Override
    public String toString() {
        return invokedynamic(makeConcatWithConstants:(Lse/michaelthelin/spotify/model_objects/IPlaylistItem;Ljava/util/Date;Lse/michaelthelin/spotify/model_objects/specification/User;Ljava/lang/Boolean;)Ljava/lang/String;, this.track, this.addedAt, this.addedBy, this.isLocal);
    }
    
    @Override
    public Builder builder() {
        return new Builder();
    }
    
    public static final class Builder extends AbstractModelObject.Builder
    {
        private Date addedAt;
        private User addedBy;
        private Boolean isLocal;
        private IPlaylistItem track;
        
        public Builder setAddedAt(final Date addedAt) {
            this.addedAt = addedAt;
            return this;
        }
        
        public Builder setAddedBy(final User addedBy) {
            this.addedBy = addedBy;
            return this;
        }
        
        public Builder setIsLocal(final Boolean isLocal) {
            this.isLocal = isLocal;
            return this;
        }
        
        public Builder setTrack(final IPlaylistItem track) {
            this.track = track;
            return this;
        }
        
        @Override
        public PlaylistTrack build() {
            return new PlaylistTrack(this, null);
        }
    }
    
    public static final class JsonUtil extends AbstractModelObject.JsonUtil<PlaylistTrack>
    {
        @Override
        public PlaylistTrack createModelObject(final JsonObject jsonObject) {
            if (jsonObject == null || jsonObject.isJsonNull()) {
                return null;
            }
            try {
                IPlaylistItem track = null;
                if (this.hasAndNotNull(jsonObject, "track")) {
                    final JsonObject trackObj = jsonObject.getAsJsonObject("track");
                    if (this.hasAndNotNull(trackObj, "type")) {
                        final String type = trackObj.get("type").getAsString().toLowerCase();
                        if (type.equals("track")) {
                            track = new Track.JsonUtil().createModelObject(trackObj);
                        }
                        else if (type.equals("episode")) {
                            track = new Episode.JsonUtil().createModelObject(trackObj);
                        }
                    }
                }
                return new PlaylistTrack.Builder().setAddedAt(this.hasAndNotNull(jsonObject, "added_at") ? SpotifyApi.parseDefaultDate(jsonObject.get("added_at").getAsString()) : null).setAddedBy(this.hasAndNotNull(jsonObject, "added_by") ? new User.JsonUtil().createModelObject(jsonObject.get("added_by").getAsJsonObject()) : null).setIsLocal(this.hasAndNotNull(jsonObject, "is_local") ? Boolean.valueOf(jsonObject.get("is_local").getAsBoolean()) : null).setTrack(track).build();
            }
            catch (ParseException e) {
                SpotifyApi.LOGGER.log(Level.SEVERE, e.getMessage());
                return null;
            }
        }
    }
}
