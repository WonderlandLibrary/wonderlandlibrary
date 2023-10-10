// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_14_1to1_14.metadata;

import com.viaversion.viaversion.api.minecraft.entities.Entity1_14Types;
import com.viaversion.viaversion.api.connection.UserConnection;
import java.util.List;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.protocols.protocol1_14_1to1_14.Protocol1_14_1To1_14;
import com.viaversion.viaversion.rewriter.EntityRewriter;

public class MetadataRewriter1_14_1To1_14 extends EntityRewriter<Protocol1_14_1To1_14>
{
    public MetadataRewriter1_14_1To1_14(final Protocol1_14_1To1_14 protocol) {
        super(protocol);
    }
    
    public void handleMetadata(final int entityId, final EntityType type, final Metadata metadata, final List<Metadata> metadatas, final UserConnection connection) {
        if (type == null) {
            return;
        }
        if ((type == Entity1_14Types.VILLAGER || type == Entity1_14Types.WANDERING_TRADER) && metadata.id() >= 15) {
            metadata.setId(metadata.id() + 1);
        }
    }
    
    @Override
    public EntityType typeFromId(final int type) {
        return Entity1_14Types.getTypeFromId(type);
    }
}
