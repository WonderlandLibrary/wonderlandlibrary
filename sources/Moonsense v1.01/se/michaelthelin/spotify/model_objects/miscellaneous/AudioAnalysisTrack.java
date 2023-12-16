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
public class AudioAnalysisTrack extends AbstractModelObject
{
    private final Long numSamples;
    private final Float duration;
    private final String sampleMd5;
    private final Integer offsetSeconds;
    private final Integer windowSeconds;
    private final Long analysisSampleRate;
    private final Integer analysisChannels;
    private final Float endOfFadeIn;
    private final Float startOfFadeOut;
    private final Float loudness;
    private final Float tempo;
    private final Float tempoConfidence;
    private final Integer timeSignature;
    private final Float timeSignatureConfidence;
    private final Integer key;
    private final Float keyConfidence;
    private final Modality mode;
    private final Float modeConfidence;
    private final String codeString;
    private final Float codeVersion;
    private final String echoprintString;
    private final Float echoprintVersion;
    private final String synchString;
    private final Float synchVersion;
    private final String rhythmString;
    private final Float rhythmVersion;
    
    private AudioAnalysisTrack(final Builder builder) {
        super(builder);
        this.numSamples = builder.numSamples;
        this.duration = builder.duration;
        this.sampleMd5 = builder.sampleMd5;
        this.offsetSeconds = builder.offsetSeconds;
        this.windowSeconds = builder.windowSeconds;
        this.analysisSampleRate = builder.analysisSampleRate;
        this.analysisChannels = builder.analysisChannels;
        this.endOfFadeIn = builder.endOfFadeIn;
        this.startOfFadeOut = builder.startOfFadeOut;
        this.loudness = builder.loudness;
        this.tempo = builder.tempo;
        this.tempoConfidence = builder.tempoConfidence;
        this.timeSignature = builder.timeSignature;
        this.timeSignatureConfidence = builder.timeSignatureConfidence;
        this.key = builder.key;
        this.keyConfidence = builder.keyConfidence;
        this.mode = builder.mode;
        this.modeConfidence = builder.modeConfidence;
        this.codeString = builder.codeString;
        this.codeVersion = builder.codeVersion;
        this.echoprintString = builder.echoprintString;
        this.echoprintVersion = builder.echoprintVersion;
        this.synchString = builder.synchString;
        this.synchVersion = builder.synchVersion;
        this.rhythmString = builder.rhythmString;
        this.rhythmVersion = builder.rhythmVersion;
    }
    
    public Long getNumSamples() {
        return this.numSamples;
    }
    
    public Float getDuration() {
        return this.duration;
    }
    
    public String getSampleMd5() {
        return this.sampleMd5;
    }
    
    public Integer getOffsetSeconds() {
        return this.offsetSeconds;
    }
    
    public Integer getWindowSeconds() {
        return this.windowSeconds;
    }
    
    public Long getAnalysisSampleRate() {
        return this.analysisSampleRate;
    }
    
    public Integer getAnalysisChannels() {
        return this.analysisChannels;
    }
    
    public Float getEndOfFadeIn() {
        return this.endOfFadeIn;
    }
    
    public Float getStartOfFadeOut() {
        return this.startOfFadeOut;
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
    
    public Integer getTimeSignature() {
        return this.timeSignature;
    }
    
    public Float getTimeSignatureConfidence() {
        return this.timeSignatureConfidence;
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
    
    public String getCodeString() {
        return this.codeString;
    }
    
    public Float getCodeVersion() {
        return this.codeVersion;
    }
    
    public String getEchoprintString() {
        return this.echoprintString;
    }
    
    public Float getEchoprintVersion() {
        return this.echoprintVersion;
    }
    
    public String getSynchString() {
        return this.synchString;
    }
    
    public Float getSynchVersion() {
        return this.synchVersion;
    }
    
    public String getRhythmString() {
        return this.rhythmString;
    }
    
    public Float getRhythmVersion() {
        return this.rhythmVersion;
    }
    
    @Override
    public String toString() {
        return invokedynamic(makeConcatWithConstants:(Ljava/lang/Long;Ljava/lang/Float;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/Integer;Ljava/lang/Long;Ljava/lang/Integer;Ljava/lang/Float;Ljava/lang/Float;Ljava/lang/Float;Ljava/lang/Float;Ljava/lang/Float;Ljava/lang/Integer;Ljava/lang/Float;Ljava/lang/Integer;Ljava/lang/Float;Lse/michaelthelin/spotify/enums/Modality;Ljava/lang/Float;Ljava/lang/String;Ljava/lang/Float;Ljava/lang/String;Ljava/lang/Float;Ljava/lang/String;Ljava/lang/Float;Ljava/lang/String;Ljava/lang/Float;)Ljava/lang/String;, this.numSamples, this.duration, this.sampleMd5, this.offsetSeconds, this.windowSeconds, this.analysisSampleRate, this.analysisChannels, this.endOfFadeIn, this.startOfFadeOut, this.loudness, this.tempo, this.tempoConfidence, this.timeSignature, this.timeSignatureConfidence, this.key, this.keyConfidence, this.mode, this.modeConfidence, this.codeString, this.codeVersion, this.echoprintString, this.echoprintVersion, this.synchString, this.synchVersion, this.rhythmString, this.rhythmVersion);
    }
    
    @Override
    public Builder builder() {
        return new Builder();
    }
    
    public static final class Builder extends AbstractModelObject.Builder
    {
        private Long numSamples;
        private Float duration;
        private String sampleMd5;
        private Integer offsetSeconds;
        private Integer windowSeconds;
        private Long analysisSampleRate;
        private Integer analysisChannels;
        private Float endOfFadeIn;
        private Float startOfFadeOut;
        private Float loudness;
        private Float tempo;
        private Float tempoConfidence;
        private Integer timeSignature;
        private Float timeSignatureConfidence;
        private Integer key;
        private Float keyConfidence;
        private Modality mode;
        private Float modeConfidence;
        private String codeString;
        private Float codeVersion;
        private String echoprintString;
        private Float echoprintVersion;
        private String synchString;
        private Float synchVersion;
        private String rhythmString;
        private Float rhythmVersion;
        
        public Builder setNumSamples(final Long numSamples) {
            this.numSamples = numSamples;
            return this;
        }
        
        public Builder setDuration(final Float duration) {
            this.duration = duration;
            return this;
        }
        
        public Builder setSampleMd5(final String sampleMd5) {
            this.sampleMd5 = sampleMd5;
            return this;
        }
        
        public Builder setOffsetSeconds(final Integer offsetSeconds) {
            this.offsetSeconds = offsetSeconds;
            return this;
        }
        
        public Builder setWindowSeconds(final Integer windowSeconds) {
            this.windowSeconds = windowSeconds;
            return this;
        }
        
        public Builder setAnalysisSampleRate(final Long analysisSampleRate) {
            this.analysisSampleRate = analysisSampleRate;
            return this;
        }
        
        public Builder setAnalysisChannels(final Integer analysisChannels) {
            this.analysisChannels = analysisChannels;
            return this;
        }
        
        public Builder setEndOfFadeIn(final Float endOfFadeIn) {
            this.endOfFadeIn = endOfFadeIn;
            return this;
        }
        
        public Builder setStartOfFadeOut(final Float startOfFadeOut) {
            this.startOfFadeOut = startOfFadeOut;
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
        
        public Builder setTimeSignature(final Integer timeSignature) {
            this.timeSignature = timeSignature;
            return this;
        }
        
        public Builder setTimeSignatureConfidence(final Float timeSignatureConfidence) {
            this.timeSignatureConfidence = timeSignatureConfidence;
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
        
        public Builder setCodeString(final String codeString) {
            this.codeString = codeString;
            return this;
        }
        
        public Builder setCodeVersion(final Float codeVersion) {
            this.codeVersion = codeVersion;
            return this;
        }
        
        public Builder setEchoprintString(final String echoprintString) {
            this.echoprintString = echoprintString;
            return this;
        }
        
        public Builder setEchoprintVersion(final Float echoprintVersion) {
            this.echoprintVersion = echoprintVersion;
            return this;
        }
        
        public Builder setSynchString(final String synchString) {
            this.synchString = synchString;
            return this;
        }
        
        public Builder setSynchVersion(final Float synchVersion) {
            this.synchVersion = synchVersion;
            return this;
        }
        
        public Builder setRhythmString(final String rhythmString) {
            this.rhythmString = rhythmString;
            return this;
        }
        
        public Builder setRhythmVersion(final Float rhythmVersion) {
            this.rhythmVersion = rhythmVersion;
            return this;
        }
        
        @Override
        public AudioAnalysisTrack build() {
            return new AudioAnalysisTrack(this, null);
        }
    }
    
    public static final class JsonUtil extends AbstractModelObject.JsonUtil<AudioAnalysisTrack>
    {
        @Override
        public AudioAnalysisTrack createModelObject(final JsonObject jsonObject) {
            if (jsonObject == null || jsonObject.isJsonNull()) {
                return null;
            }
            return new AudioAnalysisTrack.Builder().setAnalysisChannels(this.hasAndNotNull(jsonObject, "analysis_channels") ? Integer.valueOf(jsonObject.get("analysis_channels").getAsInt()) : null).setAnalysisSampleRate(this.hasAndNotNull(jsonObject, "analysis_sample_rate") ? Long.valueOf(jsonObject.get("analysis_sample_rate").getAsLong()) : null).setCodeString(this.hasAndNotNull(jsonObject, "code_string") ? jsonObject.get("code_string").getAsString() : null).setCodeVersion(this.hasAndNotNull(jsonObject, "code_version") ? Float.valueOf(jsonObject.get("code_version").getAsFloat()) : null).setDuration(this.hasAndNotNull(jsonObject, "duration") ? Float.valueOf(jsonObject.get("duration").getAsFloat()) : null).setEchoprintString(this.hasAndNotNull(jsonObject, "echoprintstring") ? jsonObject.get("echoprintstring").getAsString() : null).setEchoprintVersion(this.hasAndNotNull(jsonObject, "echoprint_version") ? Float.valueOf(jsonObject.get("echoprint_version").getAsFloat()) : null).setEndOfFadeIn(this.hasAndNotNull(jsonObject, "end_of_face_in") ? Float.valueOf(jsonObject.get("end_of_face_in").getAsFloat()) : null).setKey(this.hasAndNotNull(jsonObject, "key") ? Integer.valueOf(jsonObject.get("key").getAsInt()) : null).setKeyConfidence(this.hasAndNotNull(jsonObject, "key_confidence") ? Float.valueOf(jsonObject.get("key_confidence").getAsFloat()) : null).setLoudness(this.hasAndNotNull(jsonObject, "loudness") ? Float.valueOf(jsonObject.get("loudness").getAsFloat()) : null).setMode(this.hasAndNotNull(jsonObject, "type") ? Modality.keyOf(jsonObject.get("mode").getAsInt()) : null).setModeConfidence(this.hasAndNotNull(jsonObject, "mode_confidence") ? Float.valueOf(jsonObject.get("mode_confidence").getAsFloat()) : null).setNumSamples(this.hasAndNotNull(jsonObject, "num_samples") ? Long.valueOf(jsonObject.get("num_samples").getAsLong()) : null).setOffsetSeconds(this.hasAndNotNull(jsonObject, "offset_seconds") ? Integer.valueOf(jsonObject.get("offset_seconds").getAsInt()) : null).setRhythmString(this.hasAndNotNull(jsonObject, "rhythmstring") ? jsonObject.get("rhythmstring").getAsString() : null).setRhythmVersion(this.hasAndNotNull(jsonObject, "rhythm_version") ? Float.valueOf(jsonObject.get("rhythm_version").getAsFloat()) : null).setSampleMd5(this.hasAndNotNull(jsonObject, "sample_md5") ? jsonObject.get("sample_md5").getAsString() : null).setStartOfFadeOut(this.hasAndNotNull(jsonObject, "start_of_fade_out") ? Float.valueOf(jsonObject.get("start_of_fade_out").getAsFloat()) : null).setSynchString(this.hasAndNotNull(jsonObject, "synchstring") ? jsonObject.get("synchstring").getAsString() : null).setSynchVersion(this.hasAndNotNull(jsonObject, "synch_version") ? Float.valueOf(jsonObject.get("synch_version").getAsFloat()) : null).setTempo(this.hasAndNotNull(jsonObject, "tempo") ? Float.valueOf(jsonObject.get("tempo").getAsFloat()) : null).setTempoConfidence(this.hasAndNotNull(jsonObject, "tempo_confidence") ? Float.valueOf(jsonObject.get("tempo_confidence").getAsFloat()) : null).setTimeSignature(this.hasAndNotNull(jsonObject, "time_signature") ? Integer.valueOf(jsonObject.get("time_signature").getAsInt()) : null).setTimeSignatureConfidence(this.hasAndNotNull(jsonObject, "time_signature_confidence") ? Float.valueOf(jsonObject.get("time_signature_confidence").getAsFloat()) : null).setWindowSeconds(this.hasAndNotNull(jsonObject, "windows_seconds") ? Integer.valueOf(jsonObject.get("windows_seconds").getAsInt()) : null).build();
        }
    }
}
