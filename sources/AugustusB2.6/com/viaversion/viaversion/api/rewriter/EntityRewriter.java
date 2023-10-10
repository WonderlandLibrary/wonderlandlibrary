// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.rewriter;

import com.viaversion.viaversion.api.data.entity.EntityTracker;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import java.util.List;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.api.protocol.Protocol;

public interface EntityRewriter<T extends Protocol> extends Rewriter<T>
{
    EntityType typeFromId(final int p0);
    
    default EntityType objectTypeFromId(final int type) {
        return this.typeFromId(type);
    }
    
    int newEntityId(final int p0);
    
    void handleMetadata(final int p0, final List<Metadata> p1, final UserConnection p2);
    
    default <E extends EntityTracker> E tracker(final UserConnection connection) {
        return connection.getEntityTracker((Class<? extends Protocol>)this.protocol().getClass());
    }
}
