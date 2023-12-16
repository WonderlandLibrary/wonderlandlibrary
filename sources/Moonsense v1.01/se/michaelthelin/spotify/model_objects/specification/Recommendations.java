// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.model_objects.specification;

import com.google.gson.JsonObject;
import se.michaelthelin.spotify.model_objects.IModelObject;
import java.util.Arrays;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.AbstractModelObject;

@JsonDeserialize(builder = Builder.class)
public class Recommendations extends AbstractModelObject
{
    private final RecommendationsSeed[] seeds;
    private final TrackSimplified[] tracks;
    
    private Recommendations(final Builder builder) {
        super(builder);
        this.seeds = builder.seeds;
        this.tracks = builder.tracks;
    }
    
    public RecommendationsSeed[] getSeeds() {
        return this.seeds;
    }
    
    public TrackSimplified[] getTracks() {
        return this.tracks;
    }
    
    @Override
    public String toString() {
        return invokedynamic(makeConcatWithConstants:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;, Arrays.toString(this.seeds), Arrays.toString(this.tracks));
    }
    
    @Override
    public Builder builder() {
        return new Builder();
    }
    
    public static final class Builder extends AbstractModelObject.Builder
    {
        private RecommendationsSeed[] seeds;
        private TrackSimplified[] tracks;
        
        public Builder setSeeds(final RecommendationsSeed... seeds) {
            this.seeds = seeds;
            return this;
        }
        
        public Builder setTracks(final TrackSimplified... tracks) {
            this.tracks = tracks;
            return this;
        }
        
        @Override
        public Recommendations build() {
            return new Recommendations(this, null);
        }
    }
    
    public static final class JsonUtil extends AbstractModelObject.JsonUtil<Recommendations>
    {
        @Override
        public Recommendations createModelObject(final JsonObject jsonObject) {
            if (jsonObject == null || jsonObject.isJsonNull()) {
                return null;
            }
            return new Recommendations.Builder().setSeeds((RecommendationsSeed[])(this.hasAndNotNull(jsonObject, "seeds") ? ((RecommendationsSeed[])new RecommendationsSeed.JsonUtil().createModelObjectArray(jsonObject.getAsJsonArray("seeds"))) : null)).setTracks((TrackSimplified[])(this.hasAndNotNull(jsonObject, "tracks") ? ((TrackSimplified[])new TrackSimplified.JsonUtil().createModelObjectArray(jsonObject.getAsJsonArray("tracks"))) : null)).build();
        }
    }
}
