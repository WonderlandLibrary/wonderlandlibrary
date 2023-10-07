// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.protocol.protocol1_19to1_19_1.packets;

import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_19Types;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viaversion.api.type.types.version.Types1_19;
import com.viaversion.viaversion.protocols.protocol1_19_1to1_19.ClientboundPackets1_19_1;
import com.viaversion.viabackwards.protocol.protocol1_19to1_19_1.Protocol1_19To1_19_1;
import com.viaversion.viabackwards.api.rewriters.EntityRewriter;

public final class EntityPackets1_19_1 extends EntityRewriter<Protocol1_19To1_19_1>
{
    public EntityPackets1_19_1(final Protocol1_19To1_19_1 protocol) {
        super(protocol);
    }
    
    @Override
    protected void registerPackets() {
        this.registerMetadataRewriter(ClientboundPackets1_19_1.ENTITY_METADATA, Types1_19.METADATA_LIST);
        this.registerRemoveEntities(ClientboundPackets1_19_1.REMOVE_ENTITIES);
        this.registerSpawnTracker(ClientboundPackets1_19_1.SPAWN_ENTITY);
    }
    
    public void registerRewrites() {
        this.filter().type(Entity1_19Types.ALLAY).cancel(16);
        this.filter().type(Entity1_19Types.ALLAY).cancel(17);
    }
    
    @Override
    public EntityType typeFromId(final int typeId) {
        return Entity1_19Types.getTypeFromId(typeId);
    }
}
