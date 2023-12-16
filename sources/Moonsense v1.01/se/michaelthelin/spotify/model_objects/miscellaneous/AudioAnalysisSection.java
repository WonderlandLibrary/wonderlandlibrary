// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.model_objects.miscellaneous;

import com.google.gson.JsonObject;
import se.michaelthelin.spotify.model_objects.IModelObject;
import se.michaelthelin.spotify.enums.Modality;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.AbstractModelObject;

@JsonDeserialize(builder = Builder.class)
public class AudioAnalysisSection extends AbstractModelObject
{
    private final AudioAnalysisMeasure measure;
    private final Float loudness;
    private final Float tempo;
    private final Float tempoConfidence;
    private final Integer key;
    private final Float keyConfidence;
    private final Modality mode;
    private final Float modeConfidence;
    private final Integer timeSignature;
    private final Float timeSignatureConfidence;
    
    private AudioAnalysisSection(final Builder builder) {
        super(builder);
        this.measure = builder.measure;
        this.loudness = builder.loudness;
        this.tempo = builder.tempo;
        this.tempoConfidence = builder.tempoConfidence;
        this.key = builder.key;
        this.keyConfidence = builder.keyConfidence;
        this.mode = builder.mode;
        this.modeConfidence = builder.modeConfidence;
        this.timeSignature = builder.timeSignature;
        this.timeSignatureConfidence = builder.timeSignatureConfidence;
    }
    
    public AudioAnalysisMeasure getMeasure() {
        return this.measure;
    }
    
    public Float getLoudness() {
        return this.loudness;
    }
    
    public Float getTempo() {
        return this.tempo;
    }
    
    public Float getTempoConfidence() {
        return this.tempoConfidence;
    }
    
    public Integer getKey() {
        return this.key;
    }
    
    public Float getKeyConfidence() {
        return this.keyConfidence;
    }
    
    public Modality getMode() {
        return this.mode;
    }
    
    public Float getModeConfidence() {
        return this.modeConfidence;
    }
    
    public Integer getTimeSignature() {
        return this.timeSignature;
    }
    
    public Float getTimeSignatureConfidence() {
        return this.timeSignatureConfidence;
    }
    
    @Override
    public String toString() {
        return invokedynamic(makeConcatWithConstants:(Lse/michaelthelin/spotify/model_objects/miscellaneous/AudioAnalysisMeasure;Ljava/lang/Float;Ljava/lang/Float;Ljava/lang/Float;Ljava/lang/Integer;Ljava/lang/Float;Lse/michaelthelin/spotify/enums/Modality;Ljava/lang/Float;Ljava/lang/Integer;Ljava/lang/Float;)Ljava/lang/String;, this.measure, this.loudness, this.tempo, this.tempoConfidence, this.key, this.keyConfidence, this.mode, this.modeConfidence, this.timeSignature, this.timeSignatureConfidence);
    }
    
    @Override
    public Builder builder() {
        return new Builder();
    }
    
    public static final class Builder extends AbstractModelObject.Builder
    {
        private AudioAnalysisMeasure measure;
        private Float loudness;
        private Float tempo;
        private Float tempoConfidence;
        private Integer key;
        private Float keyConfidence;
        private Modality mode;
        private Float modeConfidence;
        private Integer timeSignature;
        private Float timeSignatureConfidence;
        
        public Builder setMeasure(final AudioAnalysisMeasure measure) {
            this.measure = measure;
            return this;
        }
        
        public Builder setLoudness(final Float loudness) {
            this.loudness = loudness;
            return this;
        }
        
        public Builder setTempo(final Float tempo) {
            this.tempo = tempo;
            return this;
        }
        
        public Builder setTempoConfidence(final Float tempoConfidence) {
            this.tempoConfidence = tempoConfidence;
            return this;
        }
        
        public Builder setKey(final Integer key) {
            this.key = key;
            return this;
        }
        
        public Builder setKeyConfidence(final Float keyConfidence) {
            this.keyConfidence = keyConfidence;
            return this;
        }
        
        public Builder setMode(final Modality mode) {
            this.mode = mode;
            return this;
        }
        
        public Builder setModeConfidence(final Float modeConfidence) {
            this.modeConfidence = modeConfidence;
            return this;
        }
        
        public Builder setTimeSignature(final Integer timeSignature) {
            this.timeSignature = timeSignature;
            return this;
        }
        
        public Builder setTimeSignatureConfidence(final Float timeSignatureConfidence) {
            this.timeSignatureConfidence = timeSignatureConfidence;
            return this;
        }
        
        @Override
        public AudioAnalysisSection build() {
            return new AudioAnalysisSection(this, null);
        }
    }
    
    public static final class JsonUtil extends AbstractModelObject.JsonUtil<AudioAnalysisSection>
    {
        @Override
        public AudioAnalysisSection createModelObject(final JsonObject jsonObject) {
            if (jsonObject == null || jsonObject.isJsonNull()) {
                return null;
            }
            return new AudioAnalysisSection.Builder().setKey(this.hasAndNotNull(jsonObject, "key") ? Integer.valueOf(jsonObject.get("key").getAsInt()) : null).setKeyConfidence(this.hasAndNotNull(jsonObject, "key_confidence") ? Float.valueOf(jsonObject.get("key_confidence").getAsFloat()) : null).setLoudness(this.hasAndNotNull(jsonObject, "loudness") ? Float.valueOf(jsonObject.get("loudness").getAsFloat()) : null).setMeasure(new AudioAnalysisMeasure.JsonUtil().createModelObject(jsonObject)).setMode(this.hasAndNotNull(jsonObject, "type") ? Modality.keyOf(jsonObject.get("mode").getAsInt()) : null).setModeConfidence(this.hasAndNotNull(jsonObject, "mode_confidence") ? Float.valueOf(jsonObject.get("mode_confidence").getAsFloat()) : null).setTempo(this.hasAndNotNull(jsonObject, "tempo") ? Float.valueOf(jsonObject.get("tempo").getAsFloat()) : null).setTempoConfidence(this.hasAndNotNull(jsonObject, "tempo_confidence") ? Float.valueOf(jsonObject.get("tempo_confidence").getAsFloat()) : null).setTimeSignature(this.hasAndNotNull(jsonObject, "time_signature") ? Integer.valueOf(jsonObject.get("time_signature").getAsInt()) : null).setTimeSignatureConfidence(this.hasAndNotNull(jsonObject, "time_signature_confidence") ? Float.valueOf(jsonObject.get("time_signature_confidence").getAsFloat()) : null).build();
        }
    }
}
