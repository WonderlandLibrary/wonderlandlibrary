// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.model_objects.special;

import com.google.gson.JsonElement;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import se.michaelthelin.spotify.model_objects.IModelObject;
import java.util.Arrays;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.AbstractModelObject;

@JsonDeserialize(builder = Builder.class)
public class PlaylistTrackPosition extends AbstractModelObject
{
    private final String uri;
    private final int[] positions;
    
    public PlaylistTrackPosition(final Builder builder) {
        super(builder);
        this.uri = builder.uri;
        this.positions = builder.positions;
    }
    
    public String getUri() {
        return this.uri;
    }
    
    public int[] getPositions() {
        return this.positions;
    }
    
    @Override
    public String toString() {
        return invokedynamic(makeConcatWithConstants:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;, this.uri, Arrays.toString(this.positions));
    }
    
    @Override
    public Builder builder() {
        return new Builder();
    }
    
    public static final class Builder extends AbstractModelObject.Builder
    {
        private String uri;
        private int[] positions;
        
        public Builder setUri(final String uri) {
            this.uri = uri;
            return this;
        }
        
        public Builder setPositions(final int... positions) {
            this.positions = positions;
            return this;
        }
        
        @Override
        public PlaylistTrackPosition build() {
            return new PlaylistTrackPosition(this);
        }
    }
    
    public static final class JsonUtil extends AbstractModelObject.JsonUtil<PlaylistTrackPosition>
    {
        @Override
        public PlaylistTrackPosition createModelObject(final JsonObject jsonObject) {
            if (jsonObject == null || jsonObject.isJsonNull()) {
                return null;
            }
            return new PlaylistTrackPosition.Builder().setPositions((int[])(this.hasAndNotNull(jsonObject, "positions") ? ((int[])new Gson().fromJson(jsonObject.getAsJsonArray("positions"), int[].class)) : null)).setUri(this.hasAndNotNull(jsonObject, "uri") ? jsonObject.get("uri").getAsString() : null).build();
        }
    }
}
