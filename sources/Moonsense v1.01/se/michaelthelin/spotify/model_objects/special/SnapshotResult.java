// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.model_objects.special;

import com.google.gson.JsonObject;
import se.michaelthelin.spotify.model_objects.IModelObject;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.AbstractModelObject;

@JsonDeserialize(builder = Builder.class)
public class SnapshotResult extends AbstractModelObject
{
    private final String snapshotId;
    
    private SnapshotResult(final Builder builder) {
        super(builder);
        this.snapshotId = builder.snapshotId;
    }
    
    public String getSnapshotId() {
        return this.snapshotId;
    }
    
    @Override
    public String toString() {
        return invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, this.snapshotId);
    }
    
    @Override
    public Builder builder() {
        return new Builder();
    }
    
    public static final class Builder extends AbstractModelObject.Builder
    {
        public String snapshotId;
        
        public Builder setSnapshotId(final String snapshotId) {
            this.snapshotId = snapshotId;
            return this;
        }
        
        @Override
        public SnapshotResult build() {
            return new SnapshotResult(this, null);
        }
    }
    
    public static final class JsonUtil extends AbstractModelObject.JsonUtil<SnapshotResult>
    {
        @Override
        public SnapshotResult createModelObject(final JsonObject jsonObject) {
            if (jsonObject == null || jsonObject.isJsonNull()) {
                return null;
            }
            return new SnapshotResult.Builder().setSnapshotId(this.hasAndNotNull(jsonObject, "snapshot_id") ? jsonObject.get("snapshot_id").getAsString() : null).build();
        }
    }
}
