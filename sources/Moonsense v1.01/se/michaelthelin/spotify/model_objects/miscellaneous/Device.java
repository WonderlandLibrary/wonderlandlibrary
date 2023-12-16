// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.model_objects.miscellaneous;

import com.google.gson.JsonObject;
import se.michaelthelin.spotify.model_objects.IModelObject;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.AbstractModelObject;

@JsonDeserialize(builder = Builder.class)
public class Device extends AbstractModelObject
{
    private final String id;
    private final Boolean is_active;
    private final Boolean is_restricted;
    private final String name;
    private final String type;
    private final Integer volume_percent;
    
    private Device(final Builder builder) {
        super(builder);
        this.id = builder.id;
        this.is_active = builder.is_active;
        this.is_restricted = builder.is_restricted;
        this.name = builder.name;
        this.type = builder.type;
        this.volume_percent = builder.volume_percent;
    }
    
    public String getId() {
        return this.id;
    }
    
    public Boolean getIs_active() {
        return this.is_active;
    }
    
    public Boolean getIs_restricted() {
        return this.is_restricted;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getType() {
        return this.type;
    }
    
    public Integer getVolume_percent() {
        return this.volume_percent;
    }
    
    @Override
    public String toString() {
        return invokedynamic(makeConcatWithConstants:(Ljava/lang/String;Ljava/lang/Boolean;Ljava/lang/Boolean;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;)Ljava/lang/String;, this.id, this.is_active, this.is_restricted, this.name, this.type, this.volume_percent);
    }
    
    @Override
    public Builder builder() {
        return new Builder();
    }
    
    public static final class Builder extends AbstractModelObject.Builder
    {
        private String id;
        private Boolean is_active;
        private Boolean is_restricted;
        private String name;
        private String type;
        private Integer volume_percent;
        
        public Builder setId(final String id) {
            this.id = id;
            return this;
        }
        
        public Builder setIs_active(final Boolean is_active) {
            this.is_active = is_active;
            return this;
        }
        
        public Builder setIs_restricted(final Boolean is_restricted) {
            this.is_restricted = is_restricted;
            return this;
        }
        
        public Builder setName(final String name) {
            this.name = name;
            return this;
        }
        
        public Builder setType(final String type) {
            this.type = type;
            return this;
        }
        
        public Builder setVolume_percent(final Integer volume_percent) {
            this.volume_percent = volume_percent;
            return this;
        }
        
        @Override
        public Device build() {
            return new Device(this, null);
        }
    }
    
    public static final class JsonUtil extends AbstractModelObject.JsonUtil<Device>
    {
        @Override
        public Device createModelObject(final JsonObject jsonObject) {
            if (jsonObject == null || jsonObject.isJsonNull()) {
                return null;
            }
            return new Device.Builder().setId(this.hasAndNotNull(jsonObject, "id") ? jsonObject.get("id").getAsString() : null).setIs_active(this.hasAndNotNull(jsonObject, "is_active") ? Boolean.valueOf(jsonObject.get("is_active").getAsBoolean()) : null).setIs_restricted(this.hasAndNotNull(jsonObject, "is_restricted") ? Boolean.valueOf(jsonObject.get("is_restricted").getAsBoolean()) : null).setName(this.hasAndNotNull(jsonObject, "name") ? jsonObject.get("name").getAsString() : null).setType(this.hasAndNotNull(jsonObject, "type") ? jsonObject.get("type").getAsString() : null).setVolume_percent(this.hasAndNotNull(jsonObject, "volume_percent") ? Integer.valueOf(jsonObject.get("volume_percent").getAsInt()) : null).build();
        }
    }
}
