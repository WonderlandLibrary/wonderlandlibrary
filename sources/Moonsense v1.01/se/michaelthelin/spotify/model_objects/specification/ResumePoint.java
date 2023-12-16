// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.model_objects.specification;

import com.google.gson.JsonObject;
import se.michaelthelin.spotify.model_objects.IModelObject;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.AbstractModelObject;

@JsonDeserialize(builder = Builder.class)
public class ResumePoint extends AbstractModelObject
{
    private final Boolean fullyPlayed;
    private final Integer resumePositionMs;
    
    private ResumePoint(final Builder builder) {
        super(builder);
        this.fullyPlayed = builder.fullyPlayed;
        this.resumePositionMs = builder.resumePositionMs;
    }
    
    public Boolean getFullyPlayed() {
        return this.fullyPlayed;
    }
    
    public Integer getResumePositionMs() {
        return this.resumePositionMs;
    }
    
    @Override
    public String toString() {
        return invokedynamic(makeConcatWithConstants:(Ljava/lang/Boolean;Ljava/lang/Integer;)Ljava/lang/String;, this.fullyPlayed, this.resumePositionMs);
    }
    
    @Override
    public Builder builder() {
        return new Builder();
    }
    
    public static final class Builder extends AbstractModelObject.Builder
    {
        private Boolean fullyPlayed;
        private Integer resumePositionMs;
        
        public Builder setFullyPlayed(final Boolean fullyPlayed) {
            this.fullyPlayed = fullyPlayed;
            return this;
        }
        
        public Builder setResumePositionMs(final Integer resumePositionMs) {
            this.resumePositionMs = resumePositionMs;
            return this;
        }
        
        @Override
        public ResumePoint build() {
            return new ResumePoint(this, null);
        }
    }
    
    public static final class JsonUtil extends AbstractModelObject.JsonUtil<ResumePoint>
    {
        @Override
        public ResumePoint createModelObject(final JsonObject jsonObject) {
            if (jsonObject == null || jsonObject.isJsonNull()) {
                return null;
            }
            return new ResumePoint.Builder().setFullyPlayed(this.hasAndNotNull(jsonObject, "fully_played") ? Boolean.valueOf(jsonObject.get("fully_played").getAsBoolean()) : null).setResumePositionMs(this.hasAndNotNull(jsonObject, "resume_position_ms") ? Integer.valueOf(jsonObject.get("resume_position_ms").getAsInt()) : null).build();
        }
    }
}
