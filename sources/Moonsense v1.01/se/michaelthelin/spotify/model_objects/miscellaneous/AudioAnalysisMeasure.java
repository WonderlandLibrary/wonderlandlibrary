// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.model_objects.miscellaneous;

import com.google.gson.JsonObject;
import se.michaelthelin.spotify.model_objects.IModelObject;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.AbstractModelObject;

@JsonDeserialize(builder = Builder.class)
public class AudioAnalysisMeasure extends AbstractModelObject
{
    private final Float confidence;
    private final Float duration;
    private final Float start;
    
    private AudioAnalysisMeasure(final Builder builder) {
        super(builder);
        this.confidence = builder.confidence;
        this.duration = builder.duration;
        this.start = builder.start;
    }
    
    public Float getConfidence() {
        return this.confidence;
    }
    
    public Float getDuration() {
        return this.duration;
    }
    
    public Float getStart() {
        return this.start;
    }
    
    @Override
    public String toString() {
        return invokedynamic(makeConcatWithConstants:(Ljava/lang/Float;Ljava/lang/Float;Ljava/lang/Float;)Ljava/lang/String;, this.confidence, this.duration, this.start);
    }
    
    @Override
    public Builder builder() {
        return new Builder();
    }
    
    public static final class Builder extends AbstractModelObject.Builder
    {
        private Float confidence;
        private Float duration;
        private Float start;
        
        public Builder setConfidence(final Float confidence) {
            this.confidence = confidence;
            return this;
        }
        
        public Builder setDuration(final Float duration) {
            this.duration = duration;
            return this;
        }
        
        public Builder setStart(final Float start) {
            this.start = start;
            return this;
        }
        
        @Override
        public AudioAnalysisMeasure build() {
            return new AudioAnalysisMeasure(this, null);
        }
    }
    
    public static final class JsonUtil extends AbstractModelObject.JsonUtil<AudioAnalysisMeasure>
    {
        @Override
        public AudioAnalysisMeasure createModelObject(final JsonObject jsonObject) {
            if (jsonObject == null || jsonObject.isJsonNull()) {
                return null;
            }
            return new AudioAnalysisMeasure.Builder().setConfidence(this.hasAndNotNull(jsonObject, "confidence") ? Float.valueOf(jsonObject.get("confidence").getAsFloat()) : null).setDuration(this.hasAndNotNull(jsonObject, "duration") ? Float.valueOf(jsonObject.get("duration").getAsFloat()) : null).setStart(this.hasAndNotNull(jsonObject, "start") ? Float.valueOf(jsonObject.get("start").getAsFloat()) : null).build();
        }
    }
}
