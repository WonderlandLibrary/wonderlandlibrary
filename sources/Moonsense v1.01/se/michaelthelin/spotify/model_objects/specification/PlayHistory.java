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
public class PlayHistory extends AbstractModelObject
{
    private final TrackSimplified track;
    private final Date playedAt;
    private final Context context;
    
    private PlayHistory(final Builder builder) {
        super(builder);
        this.track = builder.track;
        this.playedAt = builder.playedAt;
        this.context = builder.context;
    }
    
    public TrackSimplified getTrack() {
        return this.track;
    }
    
    public Date getPlayedAt() {
        return this.playedAt;
    }
    
    public Context getContext() {
        return this.context;
    }
    
    @Override
    public String toString() {
        return invokedynamic(makeConcatWithConstants:(Lse/michaelthelin/spotify/model_objects/specification/TrackSimplified;Ljava/util/Date;Lse/michaelthelin/spotify/model_objects/specification/Context;)Ljava/lang/String;, this.track, this.playedAt, this.context);
    }
    
    @Override
    public Builder builder() {
        return new Builder();
    }
    
    public static final class Builder extends AbstractModelObject.Builder
    {
        private TrackSimplified track;
        private Date playedAt;
        private Context context;
        
        public Builder setTrack(final TrackSimplified track) {
            this.track = track;
            return this;
        }
        
        public Builder setPlayedAt(final Date playedAt) {
            this.playedAt = playedAt;
            return this;
        }
        
        public Builder setContext(final Context context) {
            this.context = context;
            return this;
        }
        
        @Override
        public PlayHistory build() {
            return new PlayHistory(this, null);
        }
    }
    
    public static final class JsonUtil extends AbstractModelObject.JsonUtil<PlayHistory>
    {
        @Override
        public PlayHistory createModelObject(final JsonObject jsonObject) {
            if (jsonObject == null || jsonObject.isJsonNull()) {
                return null;
            }
            try {
                return new PlayHistory.Builder().setTrack(this.hasAndNotNull(jsonObject, "track") ? new TrackSimplified.JsonUtil().createModelObject(jsonObject.getAsJsonObject("track")) : null).setPlayedAt(this.hasAndNotNull(jsonObject, "played_at") ? SpotifyApi.parseDefaultDate(jsonObject.get("played_at").getAsString()) : null).setContext(this.hasAndNotNull(jsonObject, "context") ? new Context.JsonUtil().createModelObject(jsonObject.getAsJsonObject("context")) : null).build();
            }
            catch (ParseException e) {
                SpotifyApi.LOGGER.log(Level.SEVERE, e.getMessage());
                return null;
            }
        }
    }
}
