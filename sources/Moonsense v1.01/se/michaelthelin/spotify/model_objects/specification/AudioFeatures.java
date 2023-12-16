// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.model_objects.specification;

import com.google.gson.JsonObject;
import se.michaelthelin.spotify.model_objects.IModelObject;
import se.michaelthelin.spotify.enums.ModelObjectType;
import se.michaelthelin.spotify.enums.Modality;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.AbstractModelObject;

@JsonDeserialize(builder = Builder.class)
public class AudioFeatures extends AbstractModelObject
{
    private final Float acousticness;
    private final String analysisUrl;
    private final Float danceability;
    private final Integer durationMs;
    private final Float energy;
    private final String id;
    private final Float instrumentalness;
    private final Integer key;
    private final Float liveness;
    private final Float loudness;
    private final Modality mode;
    private final Float speechiness;
    private final Float tempo;
    private final Integer timeSignature;
    private final String trackHref;
    private final ModelObjectType type;
    private final String uri;
    private final Float valence;
    
    private AudioFeatures(final Builder builder) {
        super(builder);
        this.acousticness = builder.acousticness;
        this.analysisUrl = builder.analysisUrl;
        this.danceability = builder.danceability;
        this.durationMs = builder.durationMs;
        this.energy = builder.energy;
        this.id = builder.id;
        this.instrumentalness = builder.instrumentalness;
        this.key = builder.key;
        this.liveness = builder.liveness;
        this.loudness = builder.loudness;
        this.mode = builder.mode;
        this.speechiness = builder.speechiness;
        this.tempo = builder.tempo;
        this.timeSignature = builder.timeSignature;
        this.trackHref = builder.trackHref;
        this.type = builder.type;
        this.uri = builder.uri;
        this.valence = builder.valence;
    }
    
    public Float getAcousticness() {
        return this.acousticness;
    }
    
    public String getAnalysisUrl() {
        return this.analysisUrl;
    }
    
    public Float getDanceability() {
        return this.danceability;
    }
    
    public Integer getDurationMs() {
        return this.durationMs;
    }
    
    public Float getEnergy() {
        return this.energy;
    }
    
    public String getId() {
        return this.id;
    }
    
    public Float getInstrumentalness() {
        return this.instrumentalness;
    }
    
    public Integer getKey() {
        return this.key;
    }
    
    public Float getLiveness() {
        return this.liveness;
    }
    
    public Float getLoudness() {
        return this.loudness;
    }
    
    public Modality getMode() {
        return this.mode;
    }
    
    public Float getSpeechiness() {
        return this.speechiness;
    }
    
    public Float getTempo() {
        return this.tempo;
    }
    
    public Integer getTimeSignature() {
        return this.timeSignature;
    }
    
    public String getTrackHref() {
        return this.trackHref;
    }
    
    public ModelObjectType getType() {
        return this.type;
    }
    
    public String getUri() {
        return this.uri;
    }
    
    public Float getValence() {
        return this.valence;
    }
    
    @Override
    public String toString() {
        return invokedynamic(makeConcatWithConstants:(Ljava/lang/Float;Ljava/lang/String;Ljava/lang/Float;Ljava/lang/Integer;Ljava/lang/Float;Ljava/lang/String;Ljava/lang/Float;Ljava/lang/Integer;Ljava/lang/Float;Ljava/lang/Float;Lse/michaelthelin/spotify/enums/Modality;Ljava/lang/Float;Ljava/lang/Float;Ljava/lang/Integer;Ljava/lang/String;Lse/michaelthelin/spotify/enums/ModelObjectType;Ljava/lang/String;Ljava/lang/Float;)Ljava/lang/String;, this.acousticness, this.analysisUrl, this.danceability, this.durationMs, this.energy, this.id, this.instrumentalness, this.key, this.liveness, this.loudness, this.mode, this.speechiness, this.tempo, this.timeSignature, this.trackHref, this.type, this.uri, this.valence);
    }
    
    @Override
    public Builder builder() {
        return new Builder();
    }
    
    public static final class Builder extends AbstractModelObject.Builder
    {
        private Float acousticness;
        private String analysisUrl;
        private Float danceability;
        private Integer durationMs;
        private Float energy;
        private String id;
        private Float instrumentalness;
        private Integer key;
        private Float liveness;
        private Float loudness;
        private Modality mode;
        private Float speechiness;
        private Float tempo;
        private Integer timeSignature;
        private String trackHref;
        private ModelObjectType type;
        private String uri;
        private Float valence;
        
        public Builder setAcousticness(final Float acousticness) {
            this.acousticness = acousticness;
            return this;
        }
        
        public Builder setAnalysisUrl(final String analysisUrl) {
            this.analysisUrl = analysisUrl;
            return this;
        }
        
        public Builder setDanceability(final Float danceability) {
            this.danceability = danceability;
            return this;
        }
        
        public Builder setDurationMs(final Integer durationMs) {
            this.durationMs = durationMs;
            return this;
        }
        
        public Builder setEnergy(final Float energy) {
            this.energy = energy;
            return this;
        }
        
        public Builder setId(final String id) {
            this.id = id;
            return this;
        }
        
        public Builder setInstrumentalness(final Float instrumentalness) {
            this.instrumentalness = instrumentalness;
            return this;
        }
        
        public Builder setKey(final Integer key) {
            this.key = key;
            return this;
        }
        
        public Builder setLiveness(final Float liveness) {
            this.liveness = liveness;
            return this;
        }
        
        public Builder setLoudness(final Float loudness) {
            this.loudness = loudness;
            return this;
        }
        
        public Builder setMode(final Modality mode) {
            this.mode = mode;
            return this;
        }
        
        public Builder setSpeechiness(final Float speechiness) {
            this.speechiness = speechiness;
            return this;
        }
        
        public Builder setTempo(final Float tempo) {
            this.tempo = tempo;
            return this;
        }
        
        public Builder setTimeSignature(final Integer timeSignature) {
            this.timeSignature = timeSignature;
            return this;
        }
        
        public Builder setTrackHref(final String trackHref) {
            this.trackHref = trackHref;
            return this;
        }
        
        public Builder setType(final ModelObjectType type) {
            this.type = type;
            return this;
        }
        
        public Builder setUri(final String uri) {
            this.uri = uri;
            return this;
        }
        
        public Builder setValence(final Float valence) {
            this.valence = valence;
            return this;
        }
        
        @Override
        public AudioFeatures build() {
            return new AudioFeatures(this, null);
        }
    }
    
    public static final class JsonUtil extends AbstractModelObject.JsonUtil<AudioFeatures>
    {
        @Override
        public AudioFeatures createModelObject(final JsonObject jsonObject) {
            if (jsonObject == null || jsonObject.isJsonNull()) {
                return null;
            }
            return new AudioFeatures.Builder().setAcousticness(this.hasAndNotNull(jsonObject, "acousticness") ? Float.valueOf(jsonObject.get("acousticness").getAsFloat()) : null).setAnalysisUrl(this.hasAndNotNull(jsonObject, "analysis_url") ? jsonObject.get("analysis_url").getAsString() : null).setDanceability(this.hasAndNotNull(jsonObject, "danceability") ? Float.valueOf(jsonObject.get("danceability").getAsFloat()) : null).setDurationMs(this.hasAndNotNull(jsonObject, "duration_ms") ? Integer.valueOf(jsonObject.get("duration_ms").getAsInt()) : null).setEnergy(this.hasAndNotNull(jsonObject, "energy") ? Float.valueOf(jsonObject.get("energy").getAsFloat()) : null).setId(this.hasAndNotNull(jsonObject, "id") ? jsonObject.get("id").getAsString() : null).setInstrumentalness(this.hasAndNotNull(jsonObject, "instrumentalness") ? Float.valueOf(jsonObject.get("instrumentalness").getAsFloat()) : null).setKey(this.hasAndNotNull(jsonObject, "key") ? Integer.valueOf(jsonObject.get("key").getAsInt()) : null).setLiveness(this.hasAndNotNull(jsonObject, "liveness") ? Float.valueOf(jsonObject.get("liveness").getAsFloat()) : null).setLoudness(this.hasAndNotNull(jsonObject, "loudness") ? Float.valueOf(jsonObject.get("loudness").getAsFloat()) : null).setMode(this.hasAndNotNull(jsonObject, "mode") ? Modality.keyOf(jsonObject.get("mode").getAsInt()) : null).setSpeechiness(this.hasAndNotNull(jsonObject, "speechiness") ? Float.valueOf(jsonObject.get("speechiness").getAsFloat()) : null).setTempo(this.hasAndNotNull(jsonObject, "tempo") ? Float.valueOf(jsonObject.get("tempo").getAsFloat()) : null).setTimeSignature(this.hasAndNotNull(jsonObject, "time_signature") ? Integer.valueOf(jsonObject.get("time_signature").getAsInt()) : null).setTrackHref(this.hasAndNotNull(jsonObject, "track_href") ? jsonObject.get("track_href").getAsString() : null).setType(this.hasAndNotNull(jsonObject, "type") ? ModelObjectType.keyOf(jsonObject.get("type").getAsString().toLowerCase()) : null).setUri(this.hasAndNotNull(jsonObject, "uri") ? jsonObject.get("uri").getAsString() : null).setValence(this.hasAndNotNull(jsonObject, "valence") ? Float.valueOf(jsonObject.get("valence").getAsFloat()) : null).build();
        }
    }
}
