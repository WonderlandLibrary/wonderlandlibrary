// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_14to1_13_2.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viaversion.api.minecraft.Position;
import java.util.List;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import java.util.LinkedList;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.ClientboundPackets1_14;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.storage.EntityTracker1_14;
import com.viaversion.viaversion.api.type.types.version.Types1_14;
import com.viaversion.viaversion.api.type.types.version.Types1_13_2;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_14Types;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_13Types;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.metadata.MetadataRewriter1_14To1_13_2;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.Protocol1_14To1_13_2;

public class EntityPackets
{
    public static void register(final Protocol1_14To1_13_2 protocol) {
        final MetadataRewriter1_14To1_13_2 metadataRewriter = protocol.get(MetadataRewriter1_14To1_13_2.class);
        ((AbstractProtocol<ClientboundPackets1_13, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_13.SPAWN_ENTITY, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.UUID);
                this.map(Type.BYTE, Type.VAR_INT);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.INT);
                this.map(Type.SHORT);
                this.map(Type.SHORT);
                this.map(Type.SHORT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int entityId = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                        int typeId = wrapper.get((Type<Integer>)Type.VAR_INT, 1);
                        final Entity1_13Types.EntityType type1_13 = Entity1_13Types.getTypeFromId(typeId, true);
                        typeId = metadataRewriter.newEntityId(type1_13.getId());
                        final EntityType type1_14 = Entity1_14Types.getTypeFromId(typeId);
                        if (type1_14 != null) {
                            final int data = wrapper.get((Type<Integer>)Type.INT, 0);
                            if (type1_14.is(Entity1_14Types.FALLING_BLOCK)) {
                                wrapper.set(Type.INT, 0, protocol.getMappingData().getNewBlockStateId(data));
                            }
                            else if (type1_14.is(Entity1_14Types.MINECART)) {
                                switch (data) {
                                    case 1: {
                                        typeId = Entity1_14Types.CHEST_MINECART.getId();
                                        break;
                                    }
                                    case 2: {
                                        typeId = Entity1_14Types.FURNACE_MINECART.getId();
                                        break;
                                    }
                                    case 3: {
                                        typeId = Entity1_14Types.TNT_MINECART.getId();
                                        break;
                                    }
                                    case 4: {
                                        typeId = Entity1_14Types.SPAWNER_MINECART.getId();
                                        break;
                                    }
                                    case 5: {
                                        typeId = Entity1_14Types.HOPPER_MINECART.getId();
                                        break;
                                    }
                                    case 6: {
                                        typeId = Entity1_14Types.COMMAND_BLOCK_MINECART.getId();
                                        break;
                                    }
                                }
                            }
                            else if ((type1_14.is(Entity1_14Types.ITEM) && data > 0) || type1_14.isOrHasParent(Entity1_14Types.ABSTRACT_ARROW)) {
                                if (type1_14.isOrHasParent(Entity1_14Types.ABSTRACT_ARROW)) {
                                    wrapper.set(Type.INT, 0, data - 1);
                                }
                                final PacketWrapper velocity = wrapper.create(69);
                                velocity.write(Type.VAR_INT, entityId);
                                velocity.write(Type.SHORT, (Short)wrapper.get((Type<T>)Type.SHORT, 0));
                                velocity.write(Type.SHORT, (Short)wrapper.get((Type<T>)Type.SHORT, 1));
                                velocity.write(Type.SHORT, (Short)wrapper.get((Type<T>)Type.SHORT, 2));
                                velocity.scheduleSend(Protocol1_14To1_13_2.class);
                            }
                            wrapper.user().getEntityTracker(Protocol1_14To1_13_2.class).addEntity(entityId, type1_14);
                        }
                        wrapper.set(Type.VAR_INT, 1, typeId);
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_13, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_13.SPAWN_MOB, new PacketRemapper() {
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
                this.map(Types1_13_2.METADATA_LIST, Types1_14.METADATA_LIST);
                this.handler(metadataRewriter.trackerAndRewriterHandler(Types1_14.METADATA_LIST));
            }
        });
        ((AbstractProtocol<ClientboundPackets1_13, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_13.SPAWN_PAINTING, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.UUID);
                this.map(Type.VAR_INT);
                this.map(Type.POSITION, Type.POSITION1_14);
                this.map(Type.BYTE);
            }
        });
        ((AbstractProtocol<ClientboundPackets1_13, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_13.SPAWN_PLAYER, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.UUID);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Types1_13_2.METADATA_LIST, Types1_14.METADATA_LIST);
                this.handler(metadataRewriter.trackerAndRewriterHandler(Types1_14.METADATA_LIST, Entity1_14Types.PLAYER));
            }
        });
        ((AbstractProtocol<ClientboundPackets1_13, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_13.ENTITY_ANIMATION, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final short animation = wrapper.passthrough((Type<Short>)Type.UNSIGNED_BYTE);
                        if (animation == 2) {
                            final EntityTracker1_14 tracker = wrapper.user().getEntityTracker(Protocol1_14To1_13_2.class);
                            final int entityId = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                            tracker.setSleeping(entityId, false);
                            final PacketWrapper metadataPacket = wrapper.create(ClientboundPackets1_14.ENTITY_METADATA);
                            metadataPacket.write(Type.VAR_INT, entityId);
                            final List<Metadata> metadataList = new LinkedList<Metadata>();
                            if (tracker.clientEntityId() != entityId) {
                                metadataList.add(new Metadata(6, Types1_14.META_TYPES.poseType, MetadataRewriter1_14To1_13_2.recalculatePlayerPose(entityId, tracker)));
                            }
                            metadataList.add(new Metadata(12, Types1_14.META_TYPES.optionalPositionType, null));
                            metadataPacket.write(Types1_14.METADATA_LIST, metadataList);
                            metadataPacket.scheduleSend(Protocol1_14To1_13_2.class);
                        }
                    }
                });
            }
        });
        ((Protocol<ClientboundPackets1_13, ClientboundPackets1_14, S1, S2>)protocol).registerClientbound(ClientboundPackets1_13.USE_BED, ClientboundPackets1_14.ENTITY_METADATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final EntityTracker1_14 tracker = wrapper.user().getEntityTracker(Protocol1_14To1_13_2.class);
                        final int entityId = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                        tracker.setSleeping(entityId, true);
                        final Position position = wrapper.read(Type.POSITION);
                        final List<Metadata> metadataList = new LinkedList<Metadata>();
                        metadataList.add(new Metadata(12, Types1_14.META_TYPES.optionalPositionType, position));
                        if (tracker.clientEntityId() != entityId) {
                            metadataList.add(new Metadata(6, Types1_14.META_TYPES.poseType, MetadataRewriter1_14To1_13_2.recalculatePlayerPose(entityId, tracker)));
                        }
                        wrapper.write(Types1_14.METADATA_LIST, metadataList);
                    }
                });
            }
        });
        metadataRewriter.registerRemoveEntities(ClientboundPackets1_13.DESTROY_ENTITIES);
        metadataRewriter.registerMetadataRewriter(ClientboundPackets1_13.ENTITY_METADATA, Types1_13_2.METADATA_LIST, Types1_14.METADATA_LIST);
    }
}
