// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.metadata.MetadataRewriter;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import java.util.List;
import com.viaversion.viaversion.api.type.types.version.Types1_8;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_10Types;
import de.gerrygames.viarewind.replacement.EntityReplacement;
import com.viaversion.viaversion.api.protocol.Protocol;
import de.gerrygames.viarewind.utils.PacketUtil;
import io.netty.buffer.ByteBuf;
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
                final Item item;
                this.handler(packetWrapper -> {
                    item = packetWrapper.get(Types1_7_6_10.COMPRESSED_NBT_ITEM, 0);
                    ItemRewriter.toClient(item);
                    packetWrapper.set(Types1_7_6_10.COMPRESSED_NBT_ITEM, 0, item);
                    return;
                });
                this.handler(packetWrapper -> {
                    if (packetWrapper.get((Type<Short>)Type.SHORT, 0) > 4) {
                        packetWrapper.cancel();
                    }
                    return;
                });
                EntityTracker tracker;
                UUID uuid;
                Item[] equipment;
                final EntityTracker entityTracker;
                final UUID uuid2;
                final Item[] equipment2;
                GameProfileStorage storage;
                GameProfileStorage.GameProfile profile;
                this.handler(packetWrapper -> {
                    if (!packetWrapper.isCancelled()) {
                        tracker = packetWrapper.user().get(EntityTracker.class);
                        uuid = tracker.getPlayerUUID(packetWrapper.get((Type<Integer>)Type.INT, 0));
                        if (uuid != null) {
                            equipment = tracker.getPlayerEquipment(uuid);
                            if (equipment == null) {
                                equipment = new Item[5];
                                entityTracker.setPlayerEquipment(uuid2, equipment2);
                            }
                            equipment[packetWrapper.get((Type<Short>)Type.SHORT, 0)] = packetWrapper.get(Types1_7_6_10.COMPRESSED_NBT_ITEM, 0);
                            storage = packetWrapper.user().get(GameProfileStorage.class);
                            profile = storage.get(uuid);
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
                final Position position;
                this.handler(packetWrapper -> {
                    position = packetWrapper.read(Type.POSITION);
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
                int[] entityIds;
                final EntityTracker tracker;
                final int[] array;
                int length;
                int i = 0;
                int entityId;
                int[] entityIds2;
                int[] temp;
                PacketWrapper destroy;
                this.handler(packetWrapper -> {
                    entityIds = packetWrapper.read(Type.VAR_INT_ARRAY_PRIMITIVE);
                    tracker = packetWrapper.user().get(EntityTracker.class);
                    for (length = array.length; i < length; ++i) {
                        entityId = array[i];
                        tracker.removeEntity(entityId);
                    }
                    while (entityIds.length > 127) {
                        entityIds2 = new int[127];
                        System.arraycopy(entityIds, 0, entityIds2, 0, 127);
                        temp = new int[entityIds.length - 127];
                        System.arraycopy(entityIds, 127, temp, 0, temp.length);
                        entityIds = temp;
                        destroy = PacketWrapper.create(19, null, packetWrapper.user());
                        destroy.write(Types1_7_6_10.INT_ARRAY, entityIds2);
                        PacketUtil.sendPacket(destroy, Protocol1_7_6_10TO1_8.class);
                    }
                    packetWrapper.write(Types1_7_6_10.INT_ARRAY, entityIds);
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
                final int entityId;
                final EntityTracker tracker;
                final EntityReplacement replacement;
                int x;
                int y;
                int z;
                this.handler(packetWrapper -> {
                    entityId = packetWrapper.get((Type<Integer>)Type.INT, 0);
                    tracker = packetWrapper.user().get(EntityTracker.class);
                    replacement = tracker.getEntityReplacement(entityId);
                    if (replacement != null) {
                        packetWrapper.cancel();
                        x = packetWrapper.get((Type<Byte>)Type.BYTE, 0);
                        y = packetWrapper.get((Type<Byte>)Type.BYTE, 1);
                        z = packetWrapper.get((Type<Byte>)Type.BYTE, 2);
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
                final int entityId;
                final EntityTracker tracker;
                final EntityReplacement replacement;
                int yaw;
                int pitch;
                this.handler(packetWrapper -> {
                    entityId = packetWrapper.get((Type<Integer>)Type.INT, 0);
                    tracker = packetWrapper.user().get(EntityTracker.class);
                    replacement = tracker.getEntityReplacement(entityId);
                    if (replacement != null) {
                        packetWrapper.cancel();
                        yaw = packetWrapper.get((Type<Byte>)Type.BYTE, 0);
                        pitch = packetWrapper.get((Type<Byte>)Type.BYTE, 1);
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
                final int entityId;
                final EntityTracker tracker;
                final EntityReplacement replacement;
                int x;
                int y;
                int z;
                int yaw;
                int pitch;
                this.handler(packetWrapper -> {
                    entityId = packetWrapper.get((Type<Integer>)Type.INT, 0);
                    tracker = packetWrapper.user().get(EntityTracker.class);
                    replacement = tracker.getEntityReplacement(entityId);
                    if (replacement != null) {
                        packetWrapper.cancel();
                        x = packetWrapper.get((Type<Byte>)Type.BYTE, 0);
                        y = packetWrapper.get((Type<Byte>)Type.BYTE, 1);
                        z = packetWrapper.get((Type<Byte>)Type.BYTE, 2);
                        yaw = packetWrapper.get((Type<Byte>)Type.BYTE, 3);
                        pitch = packetWrapper.get((Type<Byte>)Type.BYTE, 4);
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
                final int entityId;
                final EntityTracker tracker;
                final Entity1_10Types.EntityType type;
                int y;
                this.handler(packetWrapper -> {
                    entityId = packetWrapper.get((Type<Integer>)Type.INT, 0);
                    tracker = packetWrapper.user().get(EntityTracker.class);
                    type = tracker.getClientEntityTypes().get(entityId);
                    if (type == Entity1_10Types.EntityType.MINECART_ABSTRACT) {
                        y = packetWrapper.get((Type<Integer>)Type.INT, 2);
                        y += 12;
                        packetWrapper.set(Type.INT, 2, y);
                    }
                    return;
                });
                final int entityId2;
                final EntityTracker tracker2;
                final EntityReplacement replacement;
                int x;
                int y2;
                int z;
                int yaw;
                int pitch;
                this.handler(packetWrapper -> {
                    entityId2 = packetWrapper.get((Type<Integer>)Type.INT, 0);
                    tracker2 = packetWrapper.user().get(EntityTracker.class);
                    replacement = tracker2.getEntityReplacement(entityId2);
                    if (replacement != null) {
                        packetWrapper.cancel();
                        x = packetWrapper.get((Type<Integer>)Type.INT, 1);
                        y2 = packetWrapper.get((Type<Integer>)Type.INT, 2);
                        z = packetWrapper.get((Type<Integer>)Type.INT, 3);
                        yaw = packetWrapper.get((Type<Byte>)Type.BYTE, 0);
                        pitch = packetWrapper.get((Type<Byte>)Type.BYTE, 1);
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
                final int entityId;
                final EntityTracker tracker;
                final EntityReplacement replacement;
                int yaw;
                this.handler(packetWrapper -> {
                    entityId = packetWrapper.get((Type<Integer>)Type.INT, 0);
                    tracker = packetWrapper.user().get(EntityTracker.class);
                    replacement = tracker.getEntityReplacement(entityId);
                    if (replacement != null) {
                        packetWrapper.cancel();
                        yaw = packetWrapper.get((Type<Byte>)Type.BYTE, 0);
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
                final boolean leash;
                int passenger;
                int vehicle;
                EntityTracker tracker;
                this.handler(packetWrapper -> {
                    leash = packetWrapper.get((Type<Boolean>)Type.BOOLEAN, 0);
                    if (!leash) {
                        passenger = packetWrapper.get((Type<Integer>)Type.INT, 0);
                        vehicle = packetWrapper.get((Type<Integer>)Type.INT, 1);
                        tracker = packetWrapper.user().get(EntityTracker.class);
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
                final List<Metadata> metadataList;
                final int entityId;
                final EntityTracker tracker;
                EntityReplacement replacement;
                this.handler(wrapper -> {
                    metadataList = wrapper.get(Types1_7_6_10.METADATA_LIST, 0);
                    entityId = wrapper.get((Type<Integer>)Type.INT, 0);
                    tracker = wrapper.user().get(EntityTracker.class);
                    if (tracker.getClientEntityTypes().containsKey(entityId)) {
                        replacement = tracker.getEntityReplacement(entityId);
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
                final int entityId;
                final EntityTracker tracker;
                int amount;
                int i;
                int modifierlength;
                int j;
                this.handler(packetWrapper -> {
                    entityId = packetWrapper.get((Type<Integer>)Type.INT, 0);
                    tracker = packetWrapper.user().get(EntityTracker.class);
                    if (tracker.getEntityReplacement(entityId) != null) {
                        packetWrapper.cancel();
                    }
                    else {
                        for (amount = packetWrapper.passthrough((Type<Integer>)Type.INT), i = 0; i < amount; ++i) {
                            packetWrapper.passthrough(Type.STRING);
                            packetWrapper.passthrough((Type<Object>)Type.DOUBLE);
                            modifierlength = packetWrapper.read((Type<Integer>)Type.VAR_INT);
                            packetWrapper.write(Type.SHORT, (short)modifierlength);
                            for (j = 0; j < modifierlength; ++j) {
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
