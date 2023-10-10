// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_17to1_16_4.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.viaversion.viaversion.rewriter.meta.MetaHandlerEvent;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.data.entity.EntityTracker;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.protocols.protocol1_17to1_16_4.ClientboundPackets1_17;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.type.types.version.Types1_17;
import com.viaversion.viaversion.api.type.types.version.Types1_16;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viaversion.protocols.protocol1_16_2to1_16_1.ClientboundPackets1_16_2;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_17Types;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_16_2Types;
import com.viaversion.viaversion.protocols.protocol1_17to1_16_4.Protocol1_17To1_16_4;
import com.viaversion.viaversion.rewriter.EntityRewriter;

public final class EntityPackets extends EntityRewriter<Protocol1_17To1_16_4>
{
    public EntityPackets(final Protocol1_17To1_16_4 protocol) {
        super(protocol);
        this.mapTypes(Entity1_16_2Types.values(), Entity1_17Types.class);
    }
    
    public void registerPackets() {
        this.registerTrackerWithData(ClientboundPackets1_16_2.SPAWN_ENTITY, Entity1_17Types.FALLING_BLOCK);
        this.registerTracker(ClientboundPackets1_16_2.SPAWN_MOB);
        this.registerTracker(ClientboundPackets1_16_2.SPAWN_PLAYER, Entity1_17Types.PLAYER);
        this.registerMetadataRewriter(ClientboundPackets1_16_2.ENTITY_METADATA, Types1_16.METADATA_LIST, Types1_17.METADATA_LIST);
        ((Protocol<ClientboundPackets1_16_2, ClientboundPackets1_17, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_16_2.DESTROY_ENTITIES, null, new PacketRemapper() {
            @Override
            public void registerMap() {
                final int[] entityIds;
                final EntityTracker entityTracker;
                final int[] array;
                int length;
                int i = 0;
                int entityId;
                PacketWrapper newPacket;
                this.handler(wrapper -> {
                    entityIds = wrapper.read(Type.VAR_INT_ARRAY_PRIMITIVE);
                    wrapper.cancel();
                    entityTracker = wrapper.user().getEntityTracker(Protocol1_17To1_16_4.class);
                    for (length = array.length; i < length; ++i) {
                        entityId = array[i];
                        entityTracker.removeEntity(entityId);
                        newPacket = wrapper.create(ClientboundPackets1_17.REMOVE_ENTITY);
                        newPacket.write(Type.VAR_INT, entityId);
                        newPacket.send(Protocol1_17To1_16_4.class);
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_16_2, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_16_2.ENTITY_PROPERTIES, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.handler(wrapper -> wrapper.write(Type.VAR_INT, (Integer)wrapper.read((Type<T>)Type.INT)));
            }
        });
        ((AbstractProtocol<ClientboundPackets1_16_2, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_16_2.PLAYER_POSITION, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.BYTE);
                this.map(Type.VAR_INT);
                this.handler(wrapper -> wrapper.write(Type.BOOLEAN, false));
            }
        });
        ((Protocol<ClientboundPackets1_16_2, ClientboundPackets1_17, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_16_2.COMBAT_EVENT, null, new PacketRemapper() {
            @Override
            public void registerMap() {
                final int type;
                ClientboundPacketType packetType = null;
                final IllegalArgumentException ex;
                this.handler(wrapper -> {
                    type = wrapper.read((Type<Integer>)Type.VAR_INT);
                    switch (type) {
                        case 0: {
                            packetType = ClientboundPackets1_17.COMBAT_ENTER;
                            break;
                        }
                        case 1: {
                            packetType = ClientboundPackets1_17.COMBAT_END;
                            break;
                        }
                        case 2: {
                            packetType = ClientboundPackets1_17.COMBAT_KILL;
                            break;
                        }
                        default: {
                            new IllegalArgumentException("Invalid combat type received: " + type);
                            throw ex;
                        }
                    }
                    wrapper.setId(packetType.getId());
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_16_2, C2, S1, S2>)this.protocol).cancelClientbound(ClientboundPackets1_16_2.ENTITY_MOVEMENT);
    }
    
    @Override
    protected void registerRewrites() {
        int pose;
        this.filter().handler((event, meta) -> {
            meta.setMetaType(Types1_17.META_TYPES.byId(meta.metaType().typeId()));
            if (meta.metaType() == Types1_17.META_TYPES.poseType) {
                pose = (int)meta.value();
                if (pose > 5) {
                    meta.setValue(pose + 1);
                }
            }
            return;
        });
        this.registerMetaTypeHandler(Types1_17.META_TYPES.itemType, Types1_17.META_TYPES.blockStateType, Types1_17.META_TYPES.particleType);
        this.filter().filterFamily(Entity1_17Types.ENTITY).addIndex(7);
        final int data;
        this.filter().filterFamily(Entity1_17Types.MINECART_ABSTRACT).index(11).handler((event, meta) -> {
            data = (int)meta.getValue();
            meta.setValue(((Protocol1_17To1_16_4)this.protocol).getMappingData().getNewBlockStateId(data));
            return;
        });
        this.filter().type(Entity1_17Types.SHULKER).removeIndex(17);
    }
    
    @Override
    public EntityType typeFromId(final int type) {
        return Entity1_17Types.getTypeFromId(type);
    }
}
