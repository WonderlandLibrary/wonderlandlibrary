// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_15to1_14_4.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.protocols.protocol1_15to1_14_4.ClientboundPackets1_15;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import java.util.List;
import com.viaversion.viaversion.api.type.types.version.Types1_14;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.rewriter.EntityRewriter;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_15Types;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.ClientboundPackets1_14;
import com.viaversion.viaversion.protocols.protocol1_15to1_14_4.metadata.MetadataRewriter1_15To1_14_4;
import com.viaversion.viaversion.protocols.protocol1_15to1_14_4.Protocol1_15To1_14_4;

public class EntityPackets
{
    public static void register(final Protocol1_15To1_14_4 protocol) {
        final MetadataRewriter1_15To1_14_4 metadataRewriter = protocol.get(MetadataRewriter1_15To1_14_4.class);
        metadataRewriter.registerTrackerWithData(ClientboundPackets1_14.SPAWN_ENTITY, Entity1_15Types.FALLING_BLOCK);
        ((AbstractProtocol<ClientboundPackets1_14, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_14.SPAWN_MOB, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.UUID);
                this.map(Type.VAR_INT);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.SHORT);
                this.map(Type.SHORT);
                this.map(Type.SHORT);
                this.handler(metadataRewriter.trackerHandler());
                this.handler(wrapper -> sendMetadataPacket(wrapper, wrapper.get((Type<Integer>)Type.VAR_INT, 0), metadataRewriter));
            }
        });
        ((AbstractProtocol<ClientboundPackets1_14, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_14.SPAWN_PLAYER, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.UUID);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                final EntityRewriter val$metadataRewriter;
                final int entityId;
                this.handler(wrapper -> {
                    val$metadataRewriter = metadataRewriter;
                    entityId = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    wrapper.user().getEntityTracker(Protocol1_15To1_14_4.class).addEntity(entityId, Entity1_15Types.PLAYER);
                    sendMetadataPacket(wrapper, entityId, val$metadataRewriter);
                });
            }
        });
        metadataRewriter.registerMetadataRewriter(ClientboundPackets1_14.ENTITY_METADATA, Types1_14.METADATA_LIST);
        metadataRewriter.registerRemoveEntities(ClientboundPackets1_14.DESTROY_ENTITIES);
    }
    
    private static void sendMetadataPacket(final PacketWrapper wrapper, final int entityId, final EntityRewriter rewriter) throws Exception {
        final List<Metadata> metadata = wrapper.read(Types1_14.METADATA_LIST);
        if (metadata.isEmpty()) {
            return;
        }
        wrapper.send(Protocol1_15To1_14_4.class);
        wrapper.cancel();
        rewriter.handleMetadata(entityId, metadata, wrapper.user());
        final PacketWrapper metadataPacket = PacketWrapper.create(ClientboundPackets1_15.ENTITY_METADATA, wrapper.user());
        metadataPacket.write(Type.VAR_INT, entityId);
        metadataPacket.write(Types1_14.METADATA_LIST, metadata);
        metadataPacket.send(Protocol1_15To1_14_4.class);
    }
    
    public static int getNewEntityId(final int oldId) {
        return (oldId >= 4) ? (oldId + 1) : oldId;
    }
}
