// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.model_objects.miscellaneous;

import com.google.gson.JsonObject;
import se.michaelthelin.spotify.model_objects.IModelObject;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.AbstractModelObject;

@JsonDeserialize(builder = Builder.class)
public class AudioAnalysisMeta extends AbstractModelObject
{
    private final String analyzerVersion;
    private final String platform;
    private final String detailedStatus;
    private final Integer statusCode;
    private final Long timestamp;
    private final Float analysisTime;
    private final String inputProcess;
    
    private AudioAnalysisMeta(final Builder builder) {
        super(builder);
        this.analyzerVersion = builder.analyzerVersion;
        this.platform = builder.platform;
        this.detailedStatus = builder.detailedStatus;
        this.statusCode = builder.statusCode;
        this.timestamp = builder.timestamp;
        this.analysisTime = builder.analysisTime;
        this.inputProcess = builder.inputProcess;
    }
    
    public String getAnalyzerVersion() {
        return this.analyzerVersion;
    }
    
    public String getPlatform() {
        return this.platform;
    }
    
    public String getDetailedStatus() {
        return this.detailedStatus;
    }
    
    public Integer getStatusCode() {
        return this.statusCode;
    }
    
    public Long getTimestamp() {
        return this.timestamp;
    }
    
    public Float getAnalysisTime() {
        return this.analysisTime;
    }
    
    public String getInputProcess() {
        return this.inputProcess;
    }
    
    @Override
    public String toString() {
        return invokedynamic(makeConcatWithConstants:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/Long;Ljava/lang/Float;Ljava/lang/String;)Ljava/lang/String;, this.analyzerVersion, this.platform, this.detailedStatus, this.statusCode, this.timestamp, this.analysisTime, this.inputProcess);
    }
    
    @Override
    public Builder builder() {
        return new Builder();
    }
    
    public static final class Builder extends AbstractModelObject.Builder
    {
        private String analyzerVersion;
        private String platform;
        private String detailedStatus;
        private Integer statusCode;
        private Long timestamp;
        private Float analysisTime;
        private String inputProcess;
        
        public Builder setAnalyzerVersion(final String analyzerVersion) {
            this.analyzerVersion = analyzerVersion;
            return this;
        }
        
        public Builder setPlatform(final String platform) {
            this.platform = platform;
            return this;
        }
        
        public Builder setDetailedStatus(final String detailedStatus) {
            this.detailedStatus = detailedStatus;
            return this;
        }
        
        public Builder setStatusCode(final Integer statusCode) {
            this.statusCode = statusCode;
            return this;
        }
        
        public Builder setTimestamp(final Long timestamp) {
            this.timestamp = timestamp;
            return this;
        }
        
        public Builder setAnalysisTime(final Float analysisTime) {
            this.analysisTime = analysisTime;
            return this;
        }
        
        public Builder setInputProcess(final String inputProcess) {
            this.inputProcess = inputProcess;
            return this;
        }
        
        @Override
        public AudioAnalysisMeta build() {
            return new AudioAnalysisMeta(this, null);
        }
    }
    
    public static final class JsonUtil extends AbstractModelObject.JsonUtil<AudioAnalysisMeta>
    {
        @Override
        public AudioAnalysisMeta createModelObject(final JsonObject jsonObject) {
            if (jsonObject == null || jsonObject.isJsonNull()) {
                return null;
            }
            return new AudioAnalysisMeta.Builder().setAnalysisTime(this.hasAndNotNull(jsonObject, "analysis_time") ? Float.valueOf(jsonObject.get("analysis_time").getAsFloat()) : null).setAnalyzerVersion(this.hasAndNotNull(jsonObject, "analyzer_version") ? jsonObject.get("analyzer_version").getAsString() : null).setDetailedStatus(this.hasAndNotNull(jsonObject, "detailed_status") ? jsonObject.get("detailed_status").getAsString() : null).setInputProcess(this.hasAndNotNull(jsonObject, "input_process") ? jsonObject.get("input_process").getAsString() : null).setPlatform(this.hasAndNotNull(jsonObject, "platform") ? jsonObject.get("platform").getAsString() : null).setStatusCode(this.hasAndNotNull(jsonObject, "status_code") ? Integer.valueOf(jsonObject.get("status_code").getAsInt()) : null).setTimestamp(this.hasAndNotNull(jsonObject, "timestamp") ? Long.valueOf(jsonObject.get("timestamp").getAsLong()) : null).build();
        }
    }
}
