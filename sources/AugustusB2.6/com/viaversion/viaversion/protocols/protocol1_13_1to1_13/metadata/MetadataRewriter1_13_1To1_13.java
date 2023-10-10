// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_13_1to1_13.metadata;

import com.viaversion.viaversion.api.minecraft.entities.Entity1_13Types;
import com.viaversion.viaversion.api.type.types.Particle;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.type.types.version.Types1_13;
import com.viaversion.viaversion.api.connection.UserConnection;
import java.util.List;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.protocols.protocol1_13_1to1_13.Protocol1_13_1To1_13;
import com.viaversion.viaversion.rewriter.EntityRewriter;

public class MetadataRewriter1_13_1To1_13 extends EntityRewriter<Protocol1_13_1To1_13>
{
    public MetadataRewriter1_13_1To1_13(final Protocol1_13_1To1_13 protocol) {
        super(protocol);
    }
    
    @Override
    protected void handleMetadata(final int entityId, final EntityType type, final Metadata metadata, final List<Metadata> metadatas, final UserConnection connection) {
        if (metadata.metaType() == Types1_13.META_TYPES.itemType) {
            ((Protocol1_13_1To1_13)this.protocol).getItemRewriter().handleItemToClient((Item)metadata.getValue());
        }
        else if (metadata.metaType() == Types1_13.META_TYPES.blockStateType) {
            final int data = (int)metadata.getValue();
            metadata.setValue(((Protocol1_13_1To1_13)this.protocol).getMappingData().getNewBlockStateId(data));
        }
        else if (metadata.metaType() == Types1_13.META_TYPES.particleType) {
            this.rewriteParticle((Particle)metadata.getValue());
        }
        if (type == null) {
            return;
        }
        if (type.isOrHasParent(Entity1_13Types.EntityType.MINECART_ABSTRACT) && metadata.id() == 9) {
            final int data = (int)metadata.getValue();
            metadata.setValue(((Protocol1_13_1To1_13)this.protocol).getMappingData().getNewBlockStateId(data));
        }
        else if (type.isOrHasParent(Entity1_13Types.EntityType.ABSTRACT_ARROW) && metadata.id() >= 7) {
            metadata.setId(metadata.id() + 1);
        }
    }
    
    @Override
    public EntityType typeFromId(final int type) {
        return Entity1_13Types.getTypeFromId(type, false);
    }
    
    @Override
    public EntityType objectTypeFromId(final int type) {
        return Entity1_13Types.getTypeFromId(type, true);
    }
}
