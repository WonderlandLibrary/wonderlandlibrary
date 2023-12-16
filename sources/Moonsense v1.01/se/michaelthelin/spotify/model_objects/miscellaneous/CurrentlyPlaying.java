// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.model_objects.miscellaneous;

import se.michaelthelin.spotify.model_objects.specification.Episode;
import se.michaelthelin.spotify.model_objects.specification.Track;
import com.google.gson.JsonObject;
import se.michaelthelin.spotify.model_objects.IModelObject;
import se.michaelthelin.spotify.model_objects.special.Actions;
import se.michaelthelin.spotify.enums.CurrentlyPlayingType;
import se.michaelthelin.spotify.model_objects.IPlaylistItem;
import se.michaelthelin.spotify.model_objects.specification.Context;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.AbstractModelObject;

@JsonDeserialize(builder = Builder.class)
public class CurrentlyPlaying extends AbstractModelObject
{
    private final Context context;
    private final Long timestamp;
    private final Integer progress_ms;
    private final Boolean is_playing;
    private final IPlaylistItem item;
    private final CurrentlyPlayingType currentlyPlayingType;
    private final Actions actions;
    
    private CurrentlyPlaying(final Builder builder) {
        super(builder);
        this.context = builder.context;
        this.timestamp = builder.timestamp;
        this.progress_ms = builder.progress_ms;
        this.is_playing = builder.is_playing;
        this.item = builder.item;
        this.currentlyPlayingType = builder.currentlyPlayingType;
        this.actions = builder.actions;
    }
    
    public Context getContext() {
        return this.context;
    }
    
    public Long getTimestamp() {
        return this.timestamp;
    }
    
    public Integer getProgress_ms() {
        return this.progress_ms;
    }
    
    public Boolean getIs_playing() {
        return this.is_playing;
    }
    
    public IPlaylistItem getItem() {
        return this.item;
    }
    
    public CurrentlyPlayingType getCurrentlyPlayingType() {
        return this.currentlyPlayingType;
    }
    
    public Actions getActions() {
        return this.actions;
    }
    
    @Override
    public String toString() {
        return invokedynamic(makeConcatWithConstants:(Lse/michaelthelin/spotify/model_objects/specification/Context;Ljava/lang/Long;Ljava/lang/Integer;Ljava/lang/Boolean;Lse/michaelthelin/spotify/model_objects/IPlaylistItem;Lse/michaelthelin/spotify/enums/CurrentlyPlayingType;Lse/michaelthelin/spotify/model_objects/special/Actions;)Ljava/lang/String;, this.context, this.timestamp, this.progress_ms, this.is_playing, this.item, this.currentlyPlayingType, this.actions);
    }
    
    @Override
    public Builder builder() {
        return new Builder();
    }
    
    public static final class Builder extends AbstractModelObject.Builder
    {
        private Context context;
        private Long timestamp;
        private Integer progress_ms;
        private Boolean is_playing;
        private IPlaylistItem item;
        private CurrentlyPlayingType currentlyPlayingType;
        private Actions actions;
        
        public Builder setContext(final Context context) {
            this.context = context;
            return this;
        }
        
        public Builder setTimestamp(final Long timestamp) {
            this.timestamp = timestamp;
            return this;
        }
        
        public Builder setProgress_ms(final Integer progress_ms) {
            this.progress_ms = progress_ms;
            return this;
        }
        
        public Builder setIs_playing(final Boolean is_playing) {
            this.is_playing = is_playing;
            return this;
        }
        
        public Builder setItem(final IPlaylistItem item) {
            this.item = item;
            return this;
        }
        
        public Builder setCurrentlyPlayingType(final CurrentlyPlayingType currentlyPlayingType) {
            this.currentlyPlayingType = currentlyPlayingType;
            return this;
        }
        
        public Builder setActions(final Actions actions) {
            this.actions = actions;
            return this;
        }
        
        @Override
        public CurrentlyPlaying build() {
            return new CurrentlyPlaying(this, null);
        }
    }
    
    public static final class JsonUtil extends AbstractModelObject.JsonUtil<CurrentlyPlaying>
    {
        @Override
        public CurrentlyPlaying createModelObject(final JsonObject jsonObject) {
            if (jsonObject == null || jsonObject.isJsonNull()) {
                return null;
            }
            return new CurrentlyPlaying.Builder().setContext(this.hasAndNotNull(jsonObject, "context") ? new Context.JsonUtil().createModelObject(jsonObject.getAsJsonObject("context")) : null).setTimestamp(this.hasAndNotNull(jsonObject, "timestamp") ? Long.valueOf(jsonObject.get("timestamp").getAsLong()) : null).setProgress_ms(this.hasAndNotNull(jsonObject, "progress_ms") ? Integer.valueOf(jsonObject.get("progress_ms").getAsInt()) : null).setIs_playing(this.hasAndNotNull(jsonObject, "is_playing") ? Boolean.valueOf(jsonObject.get("is_playing").getAsBoolean()) : null).setItem((this.hasAndNotNull(jsonObject, "item") && this.hasAndNotNull(jsonObject, "currently_playing_type")) ? (jsonObject.get("currently_playing_type").getAsString().equals("track") ? new Track.JsonUtil().createModelObject(jsonObject.getAsJsonObject("item")) : (jsonObject.get("currently_playing_type").getAsString().equals("episode") ? new Episode.JsonUtil().createModelObject(jsonObject.getAsJsonObject("item")) : null)) : null).setCurrentlyPlayingType(this.hasAndNotNull(jsonObject, "currently_playing_type") ? CurrentlyPlayingType.keyOf(jsonObject.get("currently_playing_type").getAsString().toLowerCase()) : null).setActions(this.hasAndNotNull(jsonObject, "actions") ? new Actions.JsonUtil().createModelObject(jsonObject.getAsJsonObject("actions")) : null).build();
        }
    }
}
