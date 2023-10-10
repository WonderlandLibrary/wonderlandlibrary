// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.rewriter.meta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import java.util.List;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.api.connection.UserConnection;

public class MetaHandlerEventImpl implements MetaHandlerEvent
{
    private final UserConnection connection;
    private final EntityType entityType;
    private final int entityId;
    private final List<Metadata> metadataList;
    private final Metadata meta;
    private List<Metadata> extraData;
    private boolean cancel;
    
    public MetaHandlerEventImpl(final UserConnection connection, final EntityType entityType, final int entityId, final Metadata meta, final List<Metadata> metadataList) {
        this.connection = connection;
        this.entityType = entityType;
        this.entityId = entityId;
        this.meta = meta;
        this.metadataList = metadataList;
    }
    
    @Override
    public UserConnection user() {
        return this.connection;
    }
    
    @Override
    public int entityId() {
        return this.entityId;
    }
    
    @Override
    public EntityType entityType() {
        return this.entityType;
    }
    
    @Override
    public Metadata meta() {
        return this.meta;
    }
    
    @Override
    public void cancel() {
        this.cancel = true;
    }
    
    @Override
    public boolean cancelled() {
        return this.cancel;
    }
    
    @Override
    public Metadata metaAtIndex(final int index) {
        for (final Metadata meta : this.metadataList) {
            if (index == meta.id()) {
                return meta;
            }
        }
        return null;
    }
    
    @Override
    public List<Metadata> metadataList() {
        return Collections.unmodifiableList((List<? extends Metadata>)this.metadataList);
    }
    
    @Override
    public List<Metadata> extraMeta() {
        return this.extraData;
    }
    
    @Override
    public void createExtraMeta(final Metadata metadata) {
        ((this.extraData != null) ? this.extraData : (this.extraData = new ArrayList<Metadata>())).add(metadata);
    }
}
