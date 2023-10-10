// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.rewriter.meta;

import java.util.List;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.api.connection.UserConnection;

public interface MetaHandlerEvent
{
    UserConnection user();
    
    int entityId();
    
    EntityType entityType();
    
    default int index() {
        return this.meta().id();
    }
    
    default void setIndex(final int index) {
        this.meta().setId(index);
    }
    
    Metadata meta();
    
    void cancel();
    
    boolean cancelled();
    
    Metadata metaAtIndex(final int p0);
    
    List<Metadata> metadataList();
    
    List<Metadata> extraMeta();
    
    void createExtraMeta(final Metadata p0);
}
