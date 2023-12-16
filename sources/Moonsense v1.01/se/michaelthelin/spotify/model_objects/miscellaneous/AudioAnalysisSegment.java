// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.model_objects.miscellaneous;

import com.google.gson.JsonElement;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import se.michaelthelin.spotify.model_objects.IModelObject;
import java.util.Arrays;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.AbstractModelObject;

@JsonDeserialize(builder = Builder.class)
public class AudioAnalysisSegment extends AbstractModelObject
{
    private final AudioAnalysisMeasure measure;
    private final Float loudnessStart;
    private final Float loudnessMaxTime;
    private final Float loudnessMax;
    private final Float loudnessEnd;
    private final float[] pitches;
    private final float[] timbre;
    
    private AudioAnalysisSegment(final Builder builder) {
        super(builder);
        this.measure = builder.measure;
        this.loudnessStart = builder.loudnessStart;
        this.loudnessMaxTime = builder.loudnessMaxTime;
        this.loudnessMax = builder.loudnessMax;
        this.loudnessEnd = builder.loudnessEnd;
        this.pitches = builder.pitches;
        this.timbre = builder.timbre;
    }
    
    public AudioAnalysisMeasure getMeasure() {
        return this.measure;
    }
    
    public Float getLoudnessStart() {
        return this.loudnessStart;
    }
    
    public Float getLoudnessMaxTime() {
        return this.loudnessMaxTime;
    }
    
    public Float getLoudnessMax() {
        return this.loudnessMax;
    }
    
    public Float getLoudnessEnd() {
        return this.loudnessEnd;
    }
    
    public float[] getPitches() {
        return this.pitches;
    }
    
    public float[] getTimbre() {
        return this.timbre;
    }
    
    @Override
    public String toString() {
        return invokedynamic(makeConcatWithConstants:(Lse/michaelthelin/spotify/model_objects/miscellaneous/AudioAnalysisMeasure;Ljava/lang/Float;Ljava/lang/Float;Ljava/lang/Float;Ljava/lang/Float;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;, this.measure, this.loudnessStart, this.loudnessMaxTime, this.loudnessMax, this.loudnessEnd, Arrays.toString(this.pitches), Arrays.toString(this.timbre));
    }
    
    @Override
    public Builder builder() {
        return new Builder();
    }
    
    public static final class Builder extends AbstractModelObject.Builder
    {
        private AudioAnalysisMeasure measure;
        private Float loudnessStart;
        private Float loudnessMaxTime;
        private Float loudnessMax;
        private Float loudnessEnd;
        private float[] pitches;
        private float[] timbre;
        
        public Builder setMeasure(final AudioAnalysisMeasure measure) {
            this.measure = measure;
            return this;
        }
        
        public Builder setLoudnessStart(final Float loudnessStart) {
            this.loudnessStart = loudnessStart;
            return this;
        }
        
        public Builder setLoudnessMaxTime(final Float loudnessMaxTime) {
            this.loudnessMaxTime = loudnessMaxTime;
            return this;
        }
        
        public Builder setLoudnessMax(final Float loudnessMax) {
            this.loudnessMax = loudnessMax;
            return this;
        }
        
        public Builder setLoudnessEnd(final Float loudnessEnd) {
            this.loudnessEnd = loudnessEnd;
            return this;
        }
        
        public Builder setPitches(final float[] pitches) {
            this.pitches = pitches;
            return this;
        }
        
        public Builder setTimbre(final float[] timbre) {
            this.timbre = timbre;
            return this;
        }
        
        @Override
        public AudioAnalysisSegment build() {
            return new AudioAnalysisSegment(this, null);
        }
    }
    
    public static final class JsonUtil extends AbstractModelObject.JsonUtil<AudioAnalysisSegment>
    {
        @Override
        public AudioAnalysisSegment createModelObject(final JsonObject jsonObject) {
            if (jsonObject == null || jsonObject.isJsonNull()) {
                return null;
            }
            return new AudioAnalysisSegment.Builder().setLoudnessEnd(this.hasAndNotNull(jsonObject, "loudness_end") ? Float.valueOf(jsonObject.get("loudness_end").getAsFloat()) : null).setLoudnessMax(this.hasAndNotNull(jsonObject, "loudness_max") ? Float.valueOf(jsonObject.get("loudness_max").getAsFloat()) : null).setLoudnessMaxTime(this.hasAndNotNull(jsonObject, "loudness_max_time") ? Float.valueOf(jsonObject.get("loudness_max_time").getAsFloat()) : null).setLoudnessStart(this.hasAndNotNull(jsonObject, "loudness_start") ? Float.valueOf(jsonObject.get("loudness_start").getAsFloat()) : null).setMeasure(new AudioAnalysisMeasure.JsonUtil().createModelObject(jsonObject)).setPitches((float[])(this.hasAndNotNull(jsonObject, "pitches") ? ((float[])new Gson().fromJson(jsonObject.getAsJsonArray("pitches"), float[].class)) : null)).setTimbre((float[])(this.hasAndNotNull(jsonObject, "timbre") ? ((float[])new Gson().fromJson(jsonObject.getAsJsonArray("timbre"), float[].class)) : null)).build();
        }
    }
}
