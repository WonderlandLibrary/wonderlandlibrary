// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.model_objects.specification;

import com.google.gson.JsonElement;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import se.michaelthelin.spotify.model_objects.IModelObject;
import java.util.Arrays;
import se.michaelthelin.spotify.enums.ModelObjectType;
import se.michaelthelin.spotify.enums.ReleaseDatePrecision;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.IPlaylistItem;
import se.michaelthelin.spotify.model_objects.AbstractModelObject;

@JsonDeserialize(builder = Builder.class)
public class Episode extends AbstractModelObject implements IPlaylistItem
{
    private final String audioPreviewUrl;
    private final String description;
    private final Integer durationMs;
    private final Boolean explicit;
    private final ExternalUrl externalUrls;
    private final String href;
    private final String id;
    private final Image[] images;
    private final Boolean isExternallyHosted;
    private final Boolean isPlayable;
    private final String[] languages;
    private final String name;
    private final String releaseDate;
    private final ReleaseDatePrecision releaseDatePrecision;
    private final ResumePoint resumePoint;
    private final ShowSimplified show;
    private final ModelObjectType type;
    private final String uri;
    
    private Episode(final Builder builder) {
        super(builder);
        this.audioPreviewUrl = builder.audioPreviewUrl;
        this.description = builder.description;
        this.durationMs = builder.durationMs;
        this.explicit = builder.explicit;
        this.externalUrls = builder.externalUrls;
        this.href = builder.href;
        this.id = builder.id;
        this.images = builder.images;
        this.isExternallyHosted = builder.isExternallyHosted;
        this.isPlayable = builder.isPlayable;
        this.languages = builder.languages;
        this.name = builder.name;
        this.releaseDate = builder.releaseDate;
        this.releaseDatePrecision = builder.releaseDatePrecision;
        this.resumePoint = builder.resumePoint;
        this.show = builder.show;
        this.type = builder.type;
        this.uri = builder.uri;
    }
    
    public String getAudioPreviewUrl() {
        return this.audioPreviewUrl;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    @Override
    public Integer getDurationMs() {
        return this.durationMs;
    }
    
    public Boolean getExplicit() {
        return this.explicit;
    }
    
    @Override
    public ExternalUrl getExternalUrls() {
        return this.externalUrls;
    }
    
    @Override
    public String getHref() {
        return this.href;
    }
    
    @Override
    public String getId() {
        return this.id;
    }
    
    public Image[] getImages() {
        return this.images;
    }
    
    public Boolean getExternallyHosted() {
        return this.isExternallyHosted;
    }
    
    public Boolean getPlayable() {
        return this.isPlayable;
    }
    
    public String[] getLanguages() {
        return this.languages;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    public String getReleaseDate() {
        return this.releaseDate;
    }
    
    public ReleaseDatePrecision getReleaseDatePrecision() {
        return this.releaseDatePrecision;
    }
    
    public ResumePoint getResumePoint() {
        return this.resumePoint;
    }
    
    public ShowSimplified getShow() {
        return this.show;
    }
    
    @Override
    public ModelObjectType getType() {
        return this.type;
    }
    
    @Override
    public String getUri() {
        return this.uri;
    }
    
    @Override
    public String toString() {
        return invokedynamic(makeConcatWithConstants:(Ljava/lang/String;Ljava/lang/String;Lse/michaelthelin/spotify/model_objects/specification/ShowSimplified;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/Boolean;Lse/michaelthelin/spotify/model_objects/specification/ExternalUrl;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Boolean;Ljava/lang/Boolean;Ljava/lang/String;Ljava/lang/String;Lse/michaelthelin/spotify/enums/ReleaseDatePrecision;Lse/michaelthelin/spotify/model_objects/specification/ResumePoint;Lse/michaelthelin/spotify/enums/ModelObjectType;Ljava/lang/String;)Ljava/lang/String;, this.name, this.description, this.show, this.audioPreviewUrl, this.durationMs, this.explicit, this.externalUrls, this.href, this.id, Arrays.toString(this.images), this.isExternallyHosted, this.isPlayable, Arrays.toString(this.languages), this.releaseDate, this.releaseDatePrecision, this.resumePoint, this.type, this.uri);
    }
    
    @Override
    public Builder builder() {
        return new Builder();
    }
    
    public static final class Builder extends AbstractModelObject.Builder
    {
        private String audioPreviewUrl;
        private String description;
        private Integer durationMs;
        private Boolean explicit;
        private ExternalUrl externalUrls;
        private String href;
        private String id;
        private Image[] images;
        private Boolean isExternallyHosted;
        private Boolean isPlayable;
        private String[] languages;
        private String name;
        private String releaseDate;
        private ReleaseDatePrecision releaseDatePrecision;
        private ResumePoint resumePoint;
        private ShowSimplified show;
        private ModelObjectType type;
        private String uri;
        
        public Builder setAudioPreviewUrl(final String audioPreviewUrl) {
            this.audioPreviewUrl = audioPreviewUrl;
            return this;
        }
        
        public Builder setDescription(final String description) {
            this.description = description;
            return this;
        }
        
        public Builder setDurationMs(final Integer durationMs) {
            this.durationMs = durationMs;
            return this;
        }
        
        public Builder setExplicit(final Boolean explicit) {
            this.explicit = explicit;
            return this;
        }
        
        public Builder setExternalUrls(final ExternalUrl externalUrls) {
            this.externalUrls = externalUrls;
            return this;
        }
        
        public Builder setHref(final String href) {
            this.href = href;
            return this;
        }
        
        public Builder setId(final String id) {
            this.id = id;
            return this;
        }
        
        public Builder setImages(final Image... images) {
            this.images = images;
            return this;
        }
        
        public Builder setExternallyHosted(final Boolean externallyHosted) {
            this.isExternallyHosted = externallyHosted;
            return this;
        }
        
        public Builder setPlayable(final Boolean playable) {
            this.isPlayable = playable;
            return this;
        }
        
        public Builder setLanguages(final String... languages) {
            this.languages = languages;
            return this;
        }
        
        public Builder setName(final String name) {
            this.name = name;
            return this;
        }
        
        public Builder setReleaseDate(final String releaseDate) {
            this.releaseDate = releaseDate;
            return this;
        }
        
        public Builder setReleaseDatePrecision(final ReleaseDatePrecision releaseDatePrecision) {
            this.releaseDatePrecision = releaseDatePrecision;
            return this;
        }
        
        public Builder setResumePoint(final ResumePoint resumePoint) {
            this.resumePoint = resumePoint;
            return this;
        }
        
        public Builder setShow(final ShowSimplified show) {
            this.show = show;
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
        
        @Override
        public Episode build() {
            return new Episode(this, null);
        }
    }
    
    public static final class JsonUtil extends AbstractModelObject.JsonUtil<Episode>
    {
        @Override
        public Episode createModelObject(final JsonObject jsonObject) {
            if (jsonObject == null || jsonObject.isJsonNull()) {
                return null;
            }
            return new Episode.Builder().setAudioPreviewUrl(this.hasAndNotNull(jsonObject, "audio_preview_url") ? jsonObject.get("audio_preview_url").getAsString() : null).setDescription(this.hasAndNotNull(jsonObject, "description") ? jsonObject.get("description").getAsString() : null).setDurationMs(this.hasAndNotNull(jsonObject, "duration_ms") ? Integer.valueOf(jsonObject.get("duration_ms").getAsInt()) : null).setExplicit(this.hasAndNotNull(jsonObject, "explicit") ? Boolean.valueOf(jsonObject.get("explicit").getAsBoolean()) : null).setExternalUrls(this.hasAndNotNull(jsonObject, "external_urls") ? new ExternalUrl.JsonUtil().createModelObject(jsonObject.getAsJsonObject("external_urls")) : null).setHref(this.hasAndNotNull(jsonObject, "href") ? jsonObject.get("href").getAsString() : null).setId(this.hasAndNotNull(jsonObject, "id") ? jsonObject.get("id").getAsString() : null).setImages((Image[])(this.hasAndNotNull(jsonObject, "images") ? ((Image[])new Image.JsonUtil().createModelObjectArray(jsonObject.getAsJsonArray("images"))) : null)).setExternallyHosted(this.hasAndNotNull(jsonObject, "is_externally_hosted") ? Boolean.valueOf(jsonObject.get("is_externally_hosted").getAsBoolean()) : null).setPlayable(this.hasAndNotNull(jsonObject, "is_playable") ? Boolean.valueOf(jsonObject.get("is_playable").getAsBoolean()) : null).setLanguages((String[])(this.hasAndNotNull(jsonObject, "languages") ? ((String[])new Gson().fromJson(jsonObject.getAsJsonArray("languages"), String[].class)) : null)).setName(this.hasAndNotNull(jsonObject, "name") ? jsonObject.get("name").getAsString() : null).setReleaseDate(this.hasAndNotNull(jsonObject, "release_date") ? jsonObject.get("release_date").getAsString() : null).setReleaseDatePrecision(this.hasAndNotNull(jsonObject, "release_date_precision") ? ReleaseDatePrecision.keyOf(jsonObject.get("release_date_precision").getAsString().toLowerCase()) : null).setResumePoint(this.hasAndNotNull(jsonObject, "resume_point") ? new ResumePoint.JsonUtil().createModelObject(jsonObject.getAsJsonObject("resume_point")) : null).setShow(this.hasAndNotNull(jsonObject, "show") ? new ShowSimplified.JsonUtil().createModelObject(jsonObject.getAsJsonObject("show")) : null).setType(this.hasAndNotNull(jsonObject, "type") ? ModelObjectType.keyOf(jsonObject.get("type").getAsString().toLowerCase()) : null).setUri(this.hasAndNotNull(jsonObject, "uri") ? jsonObject.get("uri").getAsString() : null).build();
        }
    }
}
