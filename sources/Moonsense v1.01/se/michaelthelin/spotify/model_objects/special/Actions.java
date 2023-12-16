// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.model_objects.special;

import com.google.gson.JsonObject;
import se.michaelthelin.spotify.model_objects.IModelObject;
import se.michaelthelin.spotify.model_objects.specification.Disallows;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.AbstractModelObject;

@JsonDeserialize(builder = Builder.class)
public class Actions extends AbstractModelObject
{
    private final Disallows disallows;
    
    public Actions(final Builder builder) {
        super(builder);
        this.disallows = builder.disallows;
    }
    
    public Disallows getDisallows() {
        return this.disallows;
    }
    
    @Override
    public String toString() {
        return invokedynamic(makeConcatWithConstants:(Lse/michaelthelin/spotify/model_objects/specification/Disallows;)Ljava/lang/String;, this.disallows);
    }
    
    @Override
    public Builder builder() {
        return new Builder();
    }
    
    public static final class Builder extends AbstractModelObject.Builder
    {
        private Disallows disallows;
        
        public Builder setDisallows(final Disallows disallows) {
            this.disallows = disallows;
            return this;
        }
        
        @Override
        public Actions build() {
            return new Actions(this);
        }
    }
    
    public static final class JsonUtil extends AbstractModelObject.JsonUtil<Actions>
    {
        @Override
        public Actions createModelObject(final JsonObject jsonObject) {
            if (jsonObject == null || jsonObject.isJsonNull()) {
                return null;
            }
            return new Actions.Builder().setDisallows(this.hasAndNotNull(jsonObject, "disallows") ? new Disallows.JsonUtil().createModelObject(jsonObject.getAsJsonObject("disallows")) : null).build();
        }
    }
}
