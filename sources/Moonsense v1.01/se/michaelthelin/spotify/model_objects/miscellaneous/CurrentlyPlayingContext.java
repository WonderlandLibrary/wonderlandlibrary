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
public class CurrentlyPlayingContext extends AbstractModelObject
{
    private final Device device;
    private final String repeat_state;
    private final Boolean shuffle_state;
    private final Context context;
    private final Long timestamp;
    private final Integer progress_ms;
    private final Boolean is_playing;
    private final IPlaylistItem item;
    private final CurrentlyPlayingType currentlyPlayingType;
    private final Actions actions;
    
    private CurrentlyPlayingContext(final Builder builder) {
        super(builder);
        this.device = builder.device;
        this.repeat_state = builder.repeat_state;
        this.shuffle_state = builder.shuffle_state;
        this.context = builder.context;
        this.timestamp = builder.timestamp;
        this.progress_ms = builder.progress_ms;
        this.is_playing = builder.is_playing;
        this.item = builder.item;
        this.currentlyPlayingType = builder.currentlyPlayingType;
        this.actions = builder.actions;
    }
    
    public Device getDevice() {
        return this.device;
    }
    
    public String getRepeat_state() {
        return this.repeat_state;
    }
    
    public Boolean getShuffle_state() {
        return this.shuffle_state;
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
        return invokedynamic(makeConcatWithConstants:(Lse/michaelthelin/spotify/model_objects/miscellaneous/Device;Ljava/lang/String;Ljava/lang/Boolean;Lse/michaelthelin/spotify/model_objects/specification/Context;Ljava/lang/Long;Ljava/lang/Integer;Ljava/lang/Boolean;Lse/michaelthelin/spotify/model_objects/IPlaylistItem;Lse/michaelthelin/spotify/enums/CurrentlyPlayingType;Lse/michaelthelin/spotify/model_objects/special/Actions;)Ljava/lang/String;, this.device, this.repeat_state, this.shuffle_state, this.context, this.timestamp, this.progress_ms, this.is_playing, this.item, this.currentlyPlayingType, this.actions);
    }
    
    @Override
    public Builder builder() {
        return new Builder();
    }
    
    public static final class Builder extends AbstractModelObject.Builder
    {
        private Device device;
        private String repeat_state;
        private Boolean shuffle_state;
        private Context context;
        private Long timestamp;
        private Integer progress_ms;
        private Boolean is_playing;
        private IPlaylistItem item;
        private CurrentlyPlayingType currentlyPlayingType;
        private Actions actions;
        
        public Builder setDevice(final Device device) {
            this.device = device;
            return this;
        }
        
        public Builder setRepeat_state(final String repeat_state) {
            this.repeat_state = repeat_state;
            return this;
        }
        
        public Builder setShuffle_state(final Boolean shuffle_state) {
            this.shuffle_state = shuffle_state;
            return this;
        }
        
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
        public CurrentlyPlayingContext build() {
            return new CurrentlyPlayingContext(this, null);
        }
    }
    
    public static final class JsonUtil extends AbstractModelObject.JsonUtil<CurrentlyPlayingContext>
    {
        @Override
        public CurrentlyPlayingContext createModelObject(final JsonObject jsonObject) {
            if (jsonObject == null || jsonObject.isJsonNull()) {
                return null;
            }
            return new CurrentlyPlayingContext.Builder().setDevice(this.hasAndNotNull(jsonObject, "device") ? new Device.JsonUtil().createModelObject(jsonObject.getAsJsonObject("device")) : null).setRepeat_state(this.hasAndNotNull(jsonObject, "repeat_state") ? jsonObject.get("repeat_state").getAsString() : null).setShuffle_state(this.hasAndNotNull(jsonObject, "shuffle_state") ? Boolean.valueOf(jsonObject.get("shuffle_state").getAsBoolean()) : null).setContext(this.hasAndNotNull(jsonObject, "context") ? new Context.JsonUtil().createModelObject(jsonObject.getAsJsonObject("context")) : null).setTimestamp(this.hasAndNotNull(jsonObject, "timestamp") ? Long.valueOf(jsonObject.get("timestamp").getAsLong()) : null).setProgress_ms(this.hasAndNotNull(jsonObject, "progress_ms") ? Integer.valueOf(jsonObject.get("progress_ms").getAsInt()) : null).setIs_playing(this.hasAndNotNull(jsonObject, "is_playing") ? Boolean.valueOf(jsonObject.get("is_playing").getAsBoolean()) : null).setItem((this.hasAndNotNull(jsonObject, "item") && this.hasAndNotNull(jsonObject, "currently_playing_type")) ? (jsonObject.get("currently_playing_type").getAsString().equals("track") ? new Track.JsonUtil().createModelObject(jsonObject.getAsJsonObject("item")) : (jsonObject.get("currently_playing_type").getAsString().equals("episode") ? new Episode.JsonUtil().createModelObject(jsonObject.getAsJsonObject("item")) : null)) : null).setCurrentlyPlayingType(this.hasAndNotNull(jsonObject, "currently_playing_type") ? CurrentlyPlayingType.keyOf(jsonObject.get("currently_playing_type").getAsString().toLowerCase()) : null).setActions(this.hasAndNotNull(jsonObject, "actions") ? new Actions.JsonUtil().createModelObject(jsonObject.getAsJsonObject("actions")) : null).build();
        }
    }
}
