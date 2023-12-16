// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.model_objects.specification;

import java.text.ParseException;
import java.util.logging.Level;
import se.michaelthelin.spotify.SpotifyApi;
import com.google.gson.JsonObject;
import se.michaelthelin.spotify.model_objects.IModelObject;
import java.util.Date;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.AbstractModelObject;

@JsonDeserialize(builder = Builder.class)
public class SavedShow extends AbstractModelObject
{
    private final Date addedAt;
    private final ShowSimplified show;
    
    private SavedShow(final Builder builder) {
        super(builder);
        this.addedAt = builder.addedAt;
        this.show = builder.show;
    }
    
    public Date getAddedAt() {
        return this.addedAt;
    }
    
    public ShowSimplified getShow() {
        return this.show;
    }
    
    @Override
    public String toString() {
        return invokedynamic(makeConcatWithConstants:(Ljava/util/Date;Lse/michaelthelin/spotify/model_objects/specification/ShowSimplified;)Ljava/lang/String;, this.addedAt, this.show);
    }
    
    @Override
    public Builder builder() {
        return new Builder();
    }
    
    public static final class Builder extends AbstractModelObject.Builder
    {
        private Date addedAt;
        private ShowSimplified show;
        
        public Builder setAddedAt(final Date addedAt) {
            this.addedAt = addedAt;
            return this;
        }
        
        public Builder setShow(final ShowSimplified show) {
            this.show = show;
            return this;
        }
        
        @Override
        public SavedShow build() {
            return new SavedShow(this, null);
        }
    }
    
    public static final class JsonUtil extends AbstractModelObject.JsonUtil<SavedShow>
    {
        @Override
        public SavedShow createModelObject(final JsonObject jsonObject) {
            if (jsonObject == null || jsonObject.isJsonNull()) {
                return null;
            }
            try {
                return new SavedShow.Builder().setAddedAt(this.hasAndNotNull(jsonObject, "added_at") ? SpotifyApi.parseDefaultDate(jsonObject.get("added_at").getAsString()) : null).setShow(this.hasAndNotNull(jsonObject, "show") ? new ShowSimplified.JsonUtil().createModelObject(jsonObject.getAsJsonObject("show")) : null).build();
            }
            catch (ParseException e) {
                SpotifyApi.LOGGER.log(Level.SEVERE, e.getMessage());
                return null;
            }
        }
    }
}
