// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_16_2to1_16_1.metadata;

import com.viaversion.viaversion.api.type.types.Particle;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.type.types.version.Types1_16;
import com.viaversion.viaversion.api.connection.UserConnection;
import java.util.List;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_16_2Types;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_16Types;
import com.viaversion.viaversion.protocols.protocol1_16_2to1_16_1.Protocol1_16_2To1_16_1;
import com.viaversion.viaversion.rewriter.EntityRewriter;

public class MetadataRewriter1_16_2To1_16_1 extends EntityRewriter<Protocol1_16_2To1_16_1>
{
    public MetadataRewriter1_16_2To1_16_1(final Protocol1_16_2To1_16_1 protocol) {
        super(protocol);
        this.mapTypes(Entity1_16Types.values(), Entity1_16_2Types.class);
    }
    
    public void handleMetadata(final int entityId, final EntityType type, final Metadata metadata, final List<Metadata> metadatas, final UserConnection connection) throws Exception {
        if (metadata.metaType() == Types1_16.META_TYPES.itemType) {
            ((Protocol1_16_2To1_16_1)this.protocol).getItemRewriter().handleItemToClient((Item)metadata.getValue());
        }
        else if (metadata.metaType() == Types1_16.META_TYPES.blockStateType) {
            final int data = (int)metadata.getValue();
            metadata.setValue(((Protocol1_16_2To1_16_1)this.protocol).getMappingData().getNewBlockStateId(data));
        }
        else if (metadata.metaType() == Types1_16.META_TYPES.particleType) {
            this.rewriteParticle((Particle)metadata.getValue());
        }
        if (type == null) {
            return;
        }
        if (type.isOrHasParent(Entity1_16_2Types.MINECART_ABSTRACT) && metadata.id() == 10) {
            final int data = (int)metadata.getValue();
            metadata.setValue(((Protocol1_16_2To1_16_1)this.protocol).getMappingData().getNewBlockStateId(data));
        }
        if (type.isOrHasParent(Entity1_16_2Types.ABSTRACT_PIGLIN)) {
            if (metadata.id() == 15) {
                metadata.setId(16);
            }
            else if (metadata.id() == 16) {
                metadata.setId(15);
            }
        }
    }
    
    @Override
    public EntityType typeFromId(final int type) {
        return Entity1_16_2Types.getTypeFromId(type);
    }
}
