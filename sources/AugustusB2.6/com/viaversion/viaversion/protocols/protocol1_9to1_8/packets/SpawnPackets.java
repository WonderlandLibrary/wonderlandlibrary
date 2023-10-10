// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_9to1_8.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.ClientboundPackets1_9;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.metadata.MetadataRewriter1_9To1_8;
import com.viaversion.viaversion.api.type.types.version.Types1_8;
import com.viaversion.viaversion.api.minecraft.item.Item;
import java.util.List;
import com.viaversion.viaversion.api.type.types.version.Types1_9;
import com.viaversion.viaversion.api.minecraft.metadata.MetaType;
import com.viaversion.viaversion.api.minecraft.metadata.types.MetaType1_9;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.ItemRewriter;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.minecraft.item.DataItem;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import java.util.ArrayList;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_10Types;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.storage.EntityTracker1_9;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.protocols.protocol1_8.ClientboundPackets1_8;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.Protocol1_9To1_8;
import com.viaversion.viaversion.api.protocol.remapper.ValueTransformer;

public class SpawnPackets
{
    public static final ValueTransformer<Integer, Double> toNewDouble;
    
    public static void register(final Protocol1_9To1_8 protocol) {
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.SPAWN_ENTITY, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int entityID = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                        final EntityTracker1_9 tracker = wrapper.user().getEntityTracker(Protocol1_9To1_8.class);
                        wrapper.write(Type.UUID, tracker.getEntityUUID(entityID));
                    }
                });
                this.map(Type.BYTE);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int entityID = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                        final int typeID = wrapper.get((Type<Byte>)Type.BYTE, 0);
                        final EntityTracker1_9 tracker = wrapper.user().getEntityTracker(Protocol1_9To1_8.class);
                        tracker.addEntity(entityID, Entity1_10Types.getTypeFromId(typeID, true));
                        tracker.sendMetadataBuffer(entityID);
                    }
                });
                this.map(Type.INT, SpawnPackets.toNewDouble);
                this.map(Type.INT, SpawnPackets.toNewDouble);
                this.map(Type.INT, SpawnPackets.toNewDouble);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.INT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int data = wrapper.get((Type<Integer>)Type.INT, 0);
                        short vX = 0;
                        short vY = 0;
                        short vZ = 0;
                        if (data > 0) {
                            vX = wrapper.read((Type<Short>)Type.SHORT);
                            vY = wrapper.read((Type<Short>)Type.SHORT);
                            vZ = wrapper.read((Type<Short>)Type.SHORT);
                        }
                        wrapper.write(Type.SHORT, vX);
                        wrapper.write(Type.SHORT, vY);
                        wrapper.write(Type.SHORT, vZ);
                    }
                });
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int entityID = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                        final int data = wrapper.get((Type<Integer>)Type.INT, 0);
                        final int typeID = wrapper.get((Type<Byte>)Type.BYTE, 0);
                        if (Entity1_10Types.getTypeFromId(typeID, true) == Entity1_10Types.EntityType.SPLASH_POTION) {
                            final PacketWrapper metaPacket = wrapper.create(57, new PacketHandler() {
                                @Override
                                public void handle(final PacketWrapper wrapper) throws Exception {
                                    wrapper.write(Type.VAR_INT, entityID);
                                    final List<Metadata> meta = new ArrayList<Metadata>();
                                    final Item item = new DataItem(373, (byte)1, (short)data, null);
                                    ItemRewriter.toClient(item);
                                    final Metadata potion = new Metadata(5, MetaType1_9.Slot, item);
                                    meta.add(potion);
                                    wrapper.write(Types1_9.METADATA_LIST, meta);
                                }
                            });
                            metaPacket.scheduleSend(Protocol1_9To1_8.class);
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.SPAWN_EXPERIENCE_ORB, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int entityID = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                        final EntityTracker1_9 tracker = wrapper.user().getEntityTracker(Protocol1_9To1_8.class);
                        tracker.addEntity(entityID, Entity1_10Types.EntityType.EXPERIENCE_ORB);
                        tracker.sendMetadataBuffer(entityID);
                    }
                });
                this.map(Type.INT, SpawnPackets.toNewDouble);
                this.map(Type.INT, SpawnPackets.toNewDouble);
                this.map(Type.INT, SpawnPackets.toNewDouble);
                this.map(Type.SHORT);
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.SPAWN_GLOBAL_ENTITY, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.BYTE);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int entityID = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                        final EntityTracker1_9 tracker = wrapper.user().getEntityTracker(Protocol1_9To1_8.class);
                        tracker.addEntity(entityID, Entity1_10Types.EntityType.LIGHTNING);
                        tracker.sendMetadataBuffer(entityID);
                    }
                });
                this.map(Type.INT, SpawnPackets.toNewDouble);
                this.map(Type.INT, SpawnPackets.toNewDouble);
                this.map(Type.INT, SpawnPackets.toNewDouble);
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.SPAWN_MOB, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int entityID = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                        final EntityTracker1_9 tracker = wrapper.user().getEntityTracker(Protocol1_9To1_8.class);
                        wrapper.write(Type.UUID, tracker.getEntityUUID(entityID));
                    }
                });
                this.map(Type.UNSIGNED_BYTE);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int entityID = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                        final int typeID = wrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 0);
                        final EntityTracker1_9 tracker = wrapper.user().getEntityTracker(Protocol1_9To1_8.class);
                        tracker.addEntity(entityID, Entity1_10Types.getTypeFromId(typeID, false));
                        tracker.sendMetadataBuffer(entityID);
                    }
                });
                this.map(Type.INT, SpawnPackets.toNewDouble);
                this.map(Type.INT, SpawnPackets.toNewDouble);
                this.map(Type.INT, SpawnPackets.toNewDouble);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.SHORT);
                this.map(Type.SHORT);
                this.map(Type.SHORT);
                this.map(Types1_8.METADATA_LIST, Types1_9.METADATA_LIST);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final List<Metadata> metadataList = wrapper.get(Types1_9.METADATA_LIST, 0);
                        final int entityId = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                        final EntityTracker1_9 tracker = wrapper.user().getEntityTracker(Protocol1_9To1_8.class);
                        if (tracker.hasEntity(entityId)) {
                            protocol.get(MetadataRewriter1_9To1_8.class).handleMetadata(entityId, metadataList, wrapper.user());
                        }
                        else {
                            Via.getPlatform().getLogger().warning("Unable to find entity for metadata, entity ID: " + entityId);
                            metadataList.clear();
                        }
                    }
                });
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final List<Metadata> metadataList = wrapper.get(Types1_9.METADATA_LIST, 0);
                        final int entityID = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                        final EntityTracker1_9 tracker = wrapper.user().getEntityTracker(Protocol1_9To1_8.class);
                        tracker.handleMetadata(entityID, metadataList);
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.SPAWN_PAINTING, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int entityID = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                        final EntityTracker1_9 tracker = wrapper.user().getEntityTracker(Protocol1_9To1_8.class);
                        tracker.addEntity(entityID, Entity1_10Types.EntityType.PAINTING);
                        tracker.sendMetadataBuffer(entityID);
                    }
                });
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int entityID = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                        final EntityTracker1_9 tracker = wrapper.user().getEntityTracker(Protocol1_9To1_8.class);
                        wrapper.write(Type.UUID, tracker.getEntityUUID(entityID));
                    }
                });
                this.map(Type.STRING);
                this.map(Type.POSITION);
                this.map(Type.BYTE);
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.SPAWN_PLAYER, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.UUID);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int entityID = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                        final EntityTracker1_9 tracker = wrapper.user().getEntityTracker(Protocol1_9To1_8.class);
                        tracker.addEntity(entityID, Entity1_10Types.EntityType.PLAYER);
                        tracker.sendMetadataBuffer(entityID);
                    }
                });
                this.map(Type.INT, SpawnPackets.toNewDouble);
                this.map(Type.INT, SpawnPackets.toNewDouble);
                this.map(Type.INT, SpawnPackets.toNewDouble);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final short item = wrapper.read((Type<Short>)Type.SHORT);
                        if (item != 0) {
                            final PacketWrapper packet = PacketWrapper.create(ClientboundPackets1_9.ENTITY_EQUIPMENT, null, wrapper.user());
                            packet.write(Type.VAR_INT, (Integer)wrapper.get((Type<T>)Type.VAR_INT, 0));
                            packet.write(Type.VAR_INT, 0);
                            packet.write((Type<DataItem>)Type.ITEM, new DataItem(item, (byte)1, (short)0, null));
                            try {
                                packet.send(Protocol1_9To1_8.class);
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                this.map(Types1_8.METADATA_LIST, Types1_9.METADATA_LIST);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final List<Metadata> metadataList = wrapper.get(Types1_9.METADATA_LIST, 0);
                        final int entityId = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                        final EntityTracker1_9 tracker = wrapper.user().getEntityTracker(Protocol1_9To1_8.class);
                        if (tracker.hasEntity(entityId)) {
                            protocol.get(MetadataRewriter1_9To1_8.class).handleMetadata(entityId, metadataList, wrapper.user());
                        }
                        else {
                            Via.getPlatform().getLogger().warning("Unable to find entity for metadata, entity ID: " + entityId);
                            metadataList.clear();
                        }
                    }
                });
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final List<Metadata> metadataList = wrapper.get(Types1_9.METADATA_LIST, 0);
                        final int entityID = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                        final EntityTracker1_9 tracker = wrapper.user().getEntityTracker(Protocol1_9To1_8.class);
                        tracker.handleMetadata(entityID, metadataList);
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.DESTROY_ENTITIES, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT_ARRAY_PRIMITIVE);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int[] array;
                        final int[] entities = array = wrapper.get(Type.VAR_INT_ARRAY_PRIMITIVE, 0);
                        for (final int entity : array) {
                            wrapper.user().getEntityTracker(Protocol1_9To1_8.class).removeEntity(entity);
                        }
                    }
                });
            }
        });
    }
    
    static {
        toNewDouble = new ValueTransformer<Integer, Double>() {
            @Override
            public Double transform(final PacketWrapper wrapper, final Integer inputValue) {
                return inputValue / 32.0;
            }
        };
    }
}
