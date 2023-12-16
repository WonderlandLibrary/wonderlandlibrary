// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.model_objects.miscellaneous;

import com.google.gson.JsonObject;
import se.michaelthelin.spotify.model_objects.IModelObject;
import java.util.Arrays;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.AbstractModelObject;

@JsonDeserialize(builder = Builder.class)
public class AudioAnalysis extends AbstractModelObject
{
    private final AudioAnalysisMeasure[] bars;
    private final AudioAnalysisMeasure[] beats;
    private final AudioAnalysisMeta meta;
    private final AudioAnalysisSection[] sections;
    private final AudioAnalysisSegment[] segments;
    private final AudioAnalysisMeasure[] tatums;
    private final AudioAnalysisTrack track;
    
    private AudioAnalysis(final Builder builder) {
        super(builder);
        this.bars = builder.bars;
        this.beats = builder.beats;
        this.meta = builder.meta;
        this.sections = builder.sections;
        this.segments = builder.segments;
        this.tatums = builder.tatums;
        this.track = builder.track;
    }
    
    public AudioAnalysisMeasure[] getBars() {
        return this.bars;
    }
    
    public AudioAnalysisMeasure[] getBeats() {
        return this.beats;
    }
    
    public AudioAnalysisMeta getMeta() {
        return this.meta;
    }
    
    public AudioAnalysisSection[] getSections() {
        return this.sections;
    }
    
    public AudioAnalysisSegment[] getSegments() {
        return this.segments;
    }
    
    public AudioAnalysisMeasure[] getTatums() {
        return this.tatums;
    }
    
    public AudioAnalysisTrack getTrack() {
        return this.track;
    }
    
    @Override
    public String toString() {
        return invokedynamic(makeConcatWithConstants:(Ljava/lang/String;Ljava/lang/String;Lse/michaelthelin/spotify/model_objects/miscellaneous/AudioAnalysisMeta;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lse/michaelthelin/spotify/model_objects/miscellaneous/AudioAnalysisTrack;)Ljava/lang/String;, Arrays.toString(this.bars), Arrays.toString(this.beats), this.meta, Arrays.toString(this.sections), Arrays.toString(this.segments), Arrays.toString(this.tatums), this.track);
    }
    
    @Override
    public Builder builder() {
        return new Builder();
    }
    
    public static final class Builder extends AbstractModelObject.Builder
    {
        private AudioAnalysisMeasure[] bars;
        private AudioAnalysisMeasure[] beats;
        private AudioAnalysisMeta meta;
        private AudioAnalysisSection[] sections;
        private AudioAnalysisSegment[] segments;
        private AudioAnalysisMeasure[] tatums;
        private AudioAnalysisTrack track;
        
        public Builder setBars(final AudioAnalysisMeasure[] bars) {
            this.bars = bars;
            return this;
        }
        
        public Builder setBeats(final AudioAnalysisMeasure[] beats) {
            this.beats = beats;
            return this;
        }
        
        public Builder setMeta(final AudioAnalysisMeta meta) {
            this.meta = meta;
            return this;
        }
        
        public Builder setSections(final AudioAnalysisSection[] sections) {
            this.sections = sections;
            return this;
        }
        
        public Builder setSegments(final AudioAnalysisSegment[] segments) {
            this.segments = segments;
            return this;
        }
        
        public Builder setTatums(final AudioAnalysisMeasure[] tatums) {
            this.tatums = tatums;
            return this;
        }
        
        public Builder setTrack(final AudioAnalysisTrack track) {
            this.track = track;
            return this;
        }
        
        @Override
        public AudioAnalysis build() {
            return new AudioAnalysis(this, null);
        }
    }
    
    public static final class JsonUtil extends AbstractModelObject.JsonUtil<AudioAnalysis>
    {
        @Override
        public AudioAnalysis createModelObject(final JsonObject jsonObject) {
            if (jsonObject == null || jsonObject.isJsonNull()) {
                return null;
            }
            return new AudioAnalysis.Builder().setBars((AudioAnalysisMeasure[])(this.hasAndNotNull(jsonObject, "bars") ? ((AudioAnalysisMeasure[])new AudioAnalysisMeasure.JsonUtil().createModelObjectArray(jsonObject.getAsJsonArray("bars"))) : null)).setBeats((AudioAnalysisMeasure[])(this.hasAndNotNull(jsonObject, "beats") ? ((AudioAnalysisMeasure[])new AudioAnalysisMeasure.JsonUtil().createModelObjectArray(jsonObject.getAsJsonArray("beats"))) : null)).setMeta(this.hasAndNotNull(jsonObject, "meta") ? new AudioAnalysisMeta.JsonUtil().createModelObject(jsonObject.getAsJsonObject("meta")) : null).setSections((AudioAnalysisSection[])(this.hasAndNotNull(jsonObject, "sections") ? ((AudioAnalysisSection[])new AudioAnalysisSection.JsonUtil().createModelObjectArray(jsonObject.getAsJsonArray("sections"))) : null)).setSegments((AudioAnalysisSegment[])(this.hasAndNotNull(jsonObject, "segments") ? ((AudioAnalysisSegment[])new AudioAnalysisSegment.JsonUtil().createModelObjectArray(jsonObject.getAsJsonArray("segments"))) : null)).setTatums((AudioAnalysisMeasure[])(this.hasAndNotNull(jsonObject, "tatums") ? ((AudioAnalysisMeasure[])new AudioAnalysisMeasure.JsonUtil().createModelObjectArray(jsonObject.getAsJsonArray("tatums"))) : null)).setTrack(this.hasAndNotNull(jsonObject, "track") ? new AudioAnalysisTrack.JsonUtil().createModelObject(jsonObject.getAsJsonObject("track")) : null).build();
        }
    }
}
