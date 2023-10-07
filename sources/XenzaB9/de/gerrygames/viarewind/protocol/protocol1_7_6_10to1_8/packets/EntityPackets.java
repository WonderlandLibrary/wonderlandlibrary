// 
// Decompiled by Procyon v0.6.0
// 

package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.metadata.MetadataRewriter;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.viaversion.viaversion.api.type.types.version.Types1_8;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_10Types;
import de.gerrygames.viarewind.replacement.EntityReplacement;
import com.viaversion.viaversion.api.protocol.Protocol;
import de.gerrygames.viarewind.utils.PacketUtil;
import java.util.function.ToIntFunction;
import java.util.List;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.ClientboundPackets1_7;
import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import java.util.UUID;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.GameProfileStorage;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.EntityTracker;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.items.ItemRewriter;
import com.viaversion.viaversion.api.minecraft.item.Item;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types.Types1_7_6_10;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.protocols.protocol1_8.ClientboundPackets1_8;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.Protocol1_7_6_10TO1_8;

public class EntityPackets
{
    public static void register(final Protocol1_7_6_10TO1_8 protocol) {
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.ENTITY_EQUIPMENT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT, Type.INT);
                this.map(Type.SHORT);
                this.map(Type.ITEM, Types1_7_6_10.COMPRESSED_NBT_ITEM);
                this.handler(packetWrapper -> {
                    final Item item = packetWrapper.get(Types1_7_6_10.COMPRESSED_NBT_ITEM, 0);
                    ItemRewriter.toClient(item);
                    packetWrapper.set(Types1_7_6_10.COMPRESSED_NBT_ITEM, 0, item);
                    return;
                });
                this.handler(packetWrapper -> {
                    final EntityTracker tracker = packetWrapper.user().get(EntityTracker.class);
                    final int id = packetWrapper.get((Type<Integer>)Type.INT, 0);
                    final int limit = (tracker.getPlayerId() == id) ? 3 : 4;
                    if (packetWrapper.get((Type<Short>)Type.SHORT, 0) > limit) {
                        packetWrapper.cancel();
                    }
                    return;
                });
                this.handler(packetWrapper -> {
                    final short slot = packetWrapper.get((Type<Short>)Type.SHORT, 0);
                    if (!packetWrapper.isCancelled()) {
                        final EntityTracker tracker2 = packetWrapper.user().get(EntityTracker.class);
                        final UUID uuid = tracker2.getPlayerUUID(packetWrapper.get((Type<Integer>)Type.INT, 0));
                        if (uuid != null) {
                            final Item item2 = packetWrapper.get(Types1_7_6_10.COMPRESSED_NBT_ITEM, 0);
                            tracker2.setPlayerEquipment(uuid, item2, slot);
                            final GameProfileStorage storage = packetWrapper.user().get(GameProfileStorage.class);
                            final GameProfileStorage.GameProfile profile = storage.get(uuid);
                            if (profile != null && profile.gamemode == 3) {
                                packetWrapper.cancel();
                            }
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.USE_BED, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT, Type.INT);
                this.handler(packetWrapper -> {
                    final Position position = packetWrapper.read(Type.POSITION);
                    packetWrapper.write(Type.INT, position.getX());
                    packetWrapper.write(Type.UNSIGNED_BYTE, (short)position.getY());
                    packetWrapper.write(Type.INT, position.getZ());
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.COLLECT_ITEM, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT, Type.INT);
                this.map(Type.VAR_INT, Type.INT);
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.ENTITY_VELOCITY, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT, Type.INT);
                this.map(Type.SHORT);
                this.map(Type.SHORT);
                this.map(Type.SHORT);
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.DESTROY_ENTITIES, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(packetWrapper -> {
                    final int[] entityIds = packetWrapper.read(Type.VAR_INT_ARRAY_PRIMITIVE);
                    final EntityTracker tracker = packetWrapper.user().get(EntityTracker.class);
                    final int[] array;
                    int j = 0;
                    for (int length = array.length; j < length; ++j) {
                        final int entityId = array[j];
                        tracker.removeEntity(entityId);
                    }
                    final List<List<Integer>> parts = Lists.partition(Ints.asList(entityIds), 127);
                    for (int i = 0; i < parts.size() - 1; ++i) {
                        final PacketWrapper destroy = PacketWrapper.create(ClientboundPackets1_7.DESTROY_ENTITIES, packetWrapper.user());
                        destroy.write(Types1_7_6_10.INT_ARRAY, ((List)parts.get(i)).stream().mapToInt(Integer::intValue).toArray());
                        PacketUtil.sendPacket(destroy, Protocol1_7_6_10TO1_8.class);
                    }
                    packetWrapper.write(Types1_7_6_10.INT_ARRAY, ((List)parts.get(parts.size() - 1)).stream().mapToInt(Integer::intValue).toArray());
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.ENTITY_MOVEMENT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT, Type.INT);
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.ENTITY_POSITION, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT, Type.INT);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.BOOLEAN, Type.NOTHING);
                this.handler(packetWrapper -> {
                    final int entityId = packetWrapper.get((Type<Integer>)Type.INT, 0);
                    final EntityTracker tracker = packetWrapper.user().get(EntityTracker.class);
                    final EntityReplacement replacement = tracker.getEntityReplacement(entityId);
                    if (replacement != null) {
                        packetWrapper.cancel();
                        final int x = packetWrapper.get((Type<Byte>)Type.BYTE, 0);
                        final int y = packetWrapper.get((Type<Byte>)Type.BYTE, 1);
                        final int z = packetWrapper.get((Type<Byte>)Type.BYTE, 2);
                        replacement.relMove(x / 32.0, y / 32.0, z / 32.0);
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.ENTITY_ROTATION, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT, Type.INT);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.BOOLEAN, Type.NOTHING);
                this.handler(packetWrapper -> {
                    final int entityId = packetWrapper.get((Type<Integer>)Type.INT, 0);
                    final EntityTracker tracker = packetWrapper.user().get(EntityTracker.class);
                    final EntityReplacement replacement = tracker.getEntityReplacement(entityId);
                    if (replacement != null) {
                        packetWrapper.cancel();
                        final int yaw = packetWrapper.get((Type<Byte>)Type.BYTE, 0);
                        final int pitch = packetWrapper.get((Type<Byte>)Type.BYTE, 1);
                        replacement.setYawPitch(yaw * 360.0f / 256.0f, pitch * 360.0f / 256.0f);
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.ENTITY_POSITION_AND_ROTATION, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT, Type.INT);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.BOOLEAN, Type.NOTHING);
                this.handler(packetWrapper -> {
                    final int entityId = packetWrapper.get((Type<Integer>)Type.INT, 0);
                    final EntityTracker tracker = packetWrapper.user().get(EntityTracker.class);
                    final EntityReplacement replacement = tracker.getEntityReplacement(entityId);
                    if (replacement != null) {
                        packetWrapper.cancel();
                        final int x = packetWrapper.get((Type<Byte>)Type.BYTE, 0);
                        final int y = packetWrapper.get((Type<Byte>)Type.BYTE, 1);
                        final int z = packetWrapper.get((Type<Byte>)Type.BYTE, 2);
                        final int yaw = packetWrapper.get((Type<Byte>)Type.BYTE, 3);
                        final int pitch = packetWrapper.get((Type<Byte>)Type.BYTE, 4);
                        replacement.relMove(x / 32.0, y / 32.0, z / 32.0);
                        replacement.setYawPitch(yaw * 360.0f / 256.0f, pitch * 360.0f / 256.0f);
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.ENTITY_TELEPORT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT, Type.INT);
                this.map(Type.INT);
                this.map(Type.INT);
                this.map(Type.INT);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.BOOLEAN, Type.NOTHING);
                this.handler(packetWrapper -> {
                    final int entityId = packetWrapper.get((Type<Integer>)Type.INT, 0);
                    final EntityTracker tracker = packetWrapper.user().get(EntityTracker.class);
                    final Entity1_10Types.EntityType type = tracker.getClientEntityTypes().get(entityId);
                    if (type == Entity1_10Types.EntityType.MINECART_ABSTRACT) {
                        int y = packetWrapper.get((Type<Integer>)Type.INT, 2);
                        y += 12;
                        packetWrapper.set(Type.INT, 2, y);
                    }
                    return;
                });
                this.handler(packetWrapper -> {
                    final int entityId2 = packetWrapper.get((Type<Integer>)Type.INT, 0);
                    final EntityTracker tracker2 = packetWrapper.user().get(EntityTracker.class);
                    final EntityReplacement replacement = tracker2.getEntityReplacement(entityId2);
                    if (replacement != null) {
                        packetWrapper.cancel();
                        final int x = packetWrapper.get((Type<Integer>)Type.INT, 1);
                        final int y2 = packetWrapper.get((Type<Integer>)Type.INT, 2);
                        final int z = packetWrapper.get((Type<Integer>)Type.INT, 3);
                        final int yaw = packetWrapper.get((Type<Byte>)Type.BYTE, 0);
                        final int pitch = packetWrapper.get((Type<Byte>)Type.BYTE, 1);
                        replacement.setLocation(x / 32.0, y2 / 32.0, z / 32.0);
                        replacement.setYawPitch(yaw * 360.0f / 256.0f, pitch * 360.0f / 256.0f);
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.ENTITY_HEAD_LOOK, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT, Type.INT);
                this.map(Type.BYTE);
                this.handler(packetWrapper -> {
                    final int entityId = packetWrapper.get((Type<Integer>)Type.INT, 0);
                    final EntityTracker tracker = packetWrapper.user().get(EntityTracker.class);
                    final EntityReplacement replacement = tracker.getEntityReplacement(entityId);
                    if (replacement != null) {
                        packetWrapper.cancel();
                        final int yaw = packetWrapper.get((Type<Byte>)Type.BYTE, 0);
                        replacement.setHeadYaw(yaw * 360.0f / 256.0f);
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.ATTACH_ENTITY, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.INT);
                this.map(Type.BOOLEAN);
                this.handler(packetWrapper -> {
                    final boolean leash = packetWrapper.get((Type<Boolean>)Type.BOOLEAN, 0);
                    if (!leash) {
                        final int passenger = packetWrapper.get((Type<Integer>)Type.INT, 0);
                        final int vehicle = packetWrapper.get((Type<Integer>)Type.INT, 1);
                        final EntityTracker tracker = packetWrapper.user().get(EntityTracker.class);
                        tracker.setPassenger(vehicle, passenger);
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.ENTITY_METADATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT, Type.INT);
                this.map(Types1_8.METADATA_LIST, Types1_7_6_10.METADATA_LIST);
                this.handler(wrapper -> {
                    final List<Metadata> metadataList = wrapper.get(Types1_7_6_10.METADATA_LIST, 0);
                    final int entityId = wrapper.get((Type<Integer>)Type.INT, 0);
                    final EntityTracker tracker = wrapper.user().get(EntityTracker.class);
                    if (tracker.getClientEntityTypes().containsKey(entityId)) {
                        final EntityReplacement replacement = tracker.getEntityReplacement(entityId);
                        if (replacement != null) {
                            wrapper.cancel();
                            replacement.updateMetadata(metadataList);
                        }
                        else {
                            MetadataRewriter.transform(tracker.getClientEntityTypes().get(entityId), metadataList);
                            if (metadataList.isEmpty()) {
                                wrapper.cancel();
                            }
                        }
                    }
                    else {
                        tracker.addMetadataToBuffer(entityId, metadataList);
                        wrapper.cancel();
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.ENTITY_EFFECT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT, Type.INT);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.VAR_INT, Type.SHORT);
                this.map(Type.BYTE, Type.NOTHING);
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.REMOVE_ENTITY_EFFECT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT, Type.INT);
                this.map(Type.BYTE);
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.ENTITY_PROPERTIES, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT, Type.INT);
                this.handler(packetWrapper -> {
                    final int entityId = packetWrapper.get((Type<Integer>)Type.INT, 0);
                    final EntityTracker tracker = packetWrapper.user().get(EntityTracker.class);
                    if (tracker.getEntityReplacement(entityId) != null) {
                        packetWrapper.cancel();
                    }
                    else {
                        for (int amount = packetWrapper.passthrough((Type<Integer>)Type.INT), i = 0; i < amount; ++i) {
                            packetWrapper.passthrough(Type.STRING);
                            packetWrapper.passthrough((Type<Object>)Type.DOUBLE);
                            final int modifierlength = packetWrapper.read((Type<Integer>)Type.VAR_INT);
                            packetWrapper.write(Type.SHORT, (short)modifierlength);
                            for (int j = 0; j < modifierlength; ++j) {
                                packetWrapper.passthrough(Type.UUID);
                                packetWrapper.passthrough((Type<Object>)Type.DOUBLE);
                                packetWrapper.passthrough((Type<Object>)Type.BYTE);
                            }
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).cancelClientbound(ClientboundPackets1_8.UPDATE_ENTITY_NBT);
    }
}
