// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.minecraft.Position;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.entityreplacements.EndermiteReplacement;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.entityreplacements.GuardianReplacement;
import com.viaversion.viaversion.api.type.types.IntType;
import de.gerrygames.viarewind.replacement.Replacement;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.items.ReplacementRegistry1_7_6_10to1_8;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import de.gerrygames.viarewind.replacement.EntityReplacement;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.entityreplacements.ArmorStandReplacement;
import java.util.Iterator;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.metadata.MetadataRewriter;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_10Types;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import java.util.List;
import com.viaversion.viaversion.api.type.types.version.Types1_8;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.EntityTracker;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.protocol.Protocol;
import de.gerrygames.viarewind.utils.PacketUtil;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types.Types1_7_6_10;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.GameProfileStorage;
import java.util.UUID;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.protocols.protocol1_8.ClientboundPackets1_8;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.Protocol1_7_6_10TO1_8;

public class SpawnPackets
{
    public static void register(final Protocol1_7_6_10TO1_8 protocol) {
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.SPAWN_PLAYER, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                final UUID uuid;
                final GameProfileStorage gameProfileStorage;
                final GameProfileStorage.GameProfile gameProfile;
                final Iterator<GameProfileStorage.Property> iterator;
                GameProfileStorage.Property property;
                int entityId;
                PacketWrapper equipmentPacket;
                short i;
                PacketWrapper equipmentPacket2;
                final EntityTracker tracker;
                this.handler(packetWrapper -> {
                    uuid = packetWrapper.read(Type.UUID);
                    packetWrapper.write(Type.STRING, uuid.toString());
                    gameProfileStorage = packetWrapper.user().get(GameProfileStorage.class);
                    gameProfile = gameProfileStorage.get(uuid);
                    if (gameProfile == null) {
                        packetWrapper.write(Type.STRING, "");
                        packetWrapper.write(Type.VAR_INT, 0);
                    }
                    else {
                        packetWrapper.write(Type.STRING, (gameProfile.name.length() > 16) ? gameProfile.name.substring(0, 16) : gameProfile.name);
                        packetWrapper.write(Type.VAR_INT, gameProfile.properties.size());
                        gameProfile.properties.iterator();
                        while (iterator.hasNext()) {
                            property = iterator.next();
                            packetWrapper.write(Type.STRING, property.name);
                            packetWrapper.write(Type.STRING, property.value);
                            packetWrapper.write(Type.STRING, (property.signature == null) ? "" : property.signature);
                        }
                    }
                    if (gameProfile != null && gameProfile.gamemode == 3) {
                        entityId = packetWrapper.get((Type<Integer>)Type.VAR_INT, 0);
                        equipmentPacket = PacketWrapper.create(4, null, packetWrapper.user());
                        equipmentPacket.write(Type.INT, entityId);
                        equipmentPacket.write(Type.SHORT, (Short)4);
                        equipmentPacket.write(Types1_7_6_10.COMPRESSED_NBT_ITEM, gameProfile.getSkull());
                        PacketUtil.sendPacket(equipmentPacket, Protocol1_7_6_10TO1_8.class);
                        for (i = 0; i < 4; ++i) {
                            equipmentPacket2 = PacketWrapper.create(4, null, packetWrapper.user());
                            equipmentPacket2.write(Type.INT, entityId);
                            equipmentPacket2.write(Type.SHORT, i);
                            equipmentPacket2.write(Types1_7_6_10.COMPRESSED_NBT_ITEM, null);
                            PacketUtil.sendPacket(equipmentPacket2, Protocol1_7_6_10TO1_8.class);
                        }
                    }
                    tracker = packetWrapper.user().get(EntityTracker.class);
                    tracker.addPlayer(packetWrapper.get((Type<Integer>)Type.VAR_INT, 0), uuid);
                    return;
                });
                this.map(Type.INT);
                this.map(Type.INT);
                this.map(Type.INT);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.SHORT);
                this.map(Types1_8.METADATA_LIST, Types1_7_6_10.METADATA_LIST);
                final List<Metadata> metadata;
                this.handler(packetWrapper -> {
                    metadata = packetWrapper.get(Types1_7_6_10.METADATA_LIST, 0);
                    MetadataRewriter.transform(Entity1_10Types.EntityType.PLAYER, metadata);
                    return;
                });
                final int entityId2;
                final EntityTracker tracker2;
                this.handler(packetWrapper -> {
                    entityId2 = packetWrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    tracker2 = packetWrapper.user().get(EntityTracker.class);
                    tracker2.getClientEntityTypes().put(entityId2, Entity1_10Types.EntityType.PLAYER);
                    tracker2.sendMetadataBuffer(entityId2);
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.SPAWN_ENTITY, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.BYTE);
                this.map(Type.INT);
                this.map(Type.INT);
                this.map(Type.INT);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.INT);
                final int entityId;
                final byte typeId;
                int x;
                int y;
                int z;
                final byte pitch;
                byte yaw;
                EntityTracker tracker;
                ArmorStandReplacement armorStand;
                final EntityTracker tracker2;
                final Entity1_10Types.EntityType type;
                int data;
                int blockId;
                int blockData;
                Replacement replace;
                IntType int1;
                final int n;
                final int i;
                this.handler(packetWrapper -> {
                    entityId = packetWrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    typeId = packetWrapper.get((Type<Byte>)Type.BYTE, 0);
                    x = packetWrapper.get((Type<Integer>)Type.INT, 0);
                    y = packetWrapper.get((Type<Integer>)Type.INT, 1);
                    z = packetWrapper.get((Type<Integer>)Type.INT, 2);
                    pitch = packetWrapper.get((Type<Byte>)Type.BYTE, 1);
                    yaw = packetWrapper.get((Type<Byte>)Type.BYTE, 2);
                    if (typeId == 71) {
                        switch (yaw) {
                            case Byte.MIN_VALUE: {
                                z += 32;
                                yaw = 0;
                                break;
                            }
                            case -64: {
                                x -= 32;
                                yaw = -64;
                                break;
                            }
                            case 0: {
                                z -= 32;
                                yaw = -128;
                                break;
                            }
                            case 64: {
                                x += 32;
                                yaw = 64;
                                break;
                            }
                        }
                    }
                    else if (typeId == 78) {
                        packetWrapper.cancel();
                        tracker = packetWrapper.user().get(EntityTracker.class);
                        armorStand = new ArmorStandReplacement(entityId, packetWrapper.user());
                        armorStand.setLocation(x / 32.0, y / 32.0, z / 32.0);
                        armorStand.setYawPitch(yaw * 360.0f / 256.0f, pitch * 360.0f / 256.0f);
                        armorStand.setHeadYaw(yaw * 360.0f / 256.0f);
                        tracker.addEntityReplacement(armorStand);
                    }
                    else if (typeId == 10) {
                        y += 12;
                    }
                    packetWrapper.set(Type.BYTE, 0, typeId);
                    packetWrapper.set(Type.INT, 0, x);
                    packetWrapper.set(Type.INT, 1, y);
                    packetWrapper.set(Type.INT, 2, z);
                    packetWrapper.set(Type.BYTE, 1, pitch);
                    packetWrapper.set(Type.BYTE, 2, yaw);
                    tracker2 = packetWrapper.user().get(EntityTracker.class);
                    type = Entity1_10Types.getTypeFromId(typeId, true);
                    tracker2.getClientEntityTypes().put(entityId, type);
                    tracker2.sendMetadataBuffer(entityId);
                    data = packetWrapper.get((Type<Integer>)Type.INT, 3);
                    if (type != null && type.isOrHasParent(Entity1_10Types.EntityType.FALLING_BLOCK)) {
                        blockId = (data & 0xFFF);
                        blockData = (data >> 12 & 0xF);
                        replace = ReplacementRegistry1_7_6_10to1_8.getReplacement(blockId, blockData);
                        if (replace != null) {
                            blockId = replace.getId();
                            blockData = replace.replaceData(blockData);
                        }
                        int1 = Type.INT;
                        data = (blockId | blockData << 16);
                        packetWrapper.set(int1, n, i);
                    }
                    if (data > 0) {
                        packetWrapper.passthrough((Type<Object>)Type.SHORT);
                        packetWrapper.passthrough((Type<Object>)Type.SHORT);
                        packetWrapper.passthrough((Type<Object>)Type.SHORT);
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.SPAWN_MOB, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.INT);
                this.map(Type.INT);
                this.map(Type.INT);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.SHORT);
                this.map(Type.SHORT);
                this.map(Type.SHORT);
                this.map(Types1_8.METADATA_LIST, Types1_7_6_10.METADATA_LIST);
                final int entityId;
                final int typeId;
                final int x;
                final int y;
                final int z;
                final byte pitch;
                final byte yaw;
                final byte headYaw;
                EntityTracker tracker;
                ArmorStandReplacement armorStand;
                EntityTracker tracker2;
                GuardianReplacement guardian;
                EntityTracker tracker3;
                EndermiteReplacement endermite;
                this.handler(packetWrapper -> {
                    entityId = packetWrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    typeId = packetWrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 0);
                    x = packetWrapper.get((Type<Integer>)Type.INT, 0);
                    y = packetWrapper.get((Type<Integer>)Type.INT, 1);
                    z = packetWrapper.get((Type<Integer>)Type.INT, 2);
                    pitch = packetWrapper.get((Type<Byte>)Type.BYTE, 1);
                    yaw = packetWrapper.get((Type<Byte>)Type.BYTE, 0);
                    headYaw = packetWrapper.get((Type<Byte>)Type.BYTE, 2);
                    if (typeId == 30) {
                        packetWrapper.cancel();
                        tracker = packetWrapper.user().get(EntityTracker.class);
                        armorStand = new ArmorStandReplacement(entityId, packetWrapper.user());
                        armorStand.setLocation(x / 32.0, y / 32.0, z / 32.0);
                        armorStand.setYawPitch(yaw * 360.0f / 256.0f, pitch * 360.0f / 256.0f);
                        armorStand.setHeadYaw(headYaw * 360.0f / 256.0f);
                        tracker.addEntityReplacement(armorStand);
                    }
                    else if (typeId == 68) {
                        packetWrapper.cancel();
                        tracker2 = packetWrapper.user().get(EntityTracker.class);
                        guardian = new GuardianReplacement(entityId, packetWrapper.user());
                        guardian.setLocation(x / 32.0, y / 32.0, z / 32.0);
                        guardian.setYawPitch(yaw * 360.0f / 256.0f, pitch * 360.0f / 256.0f);
                        guardian.setHeadYaw(headYaw * 360.0f / 256.0f);
                        tracker2.addEntityReplacement(guardian);
                    }
                    else if (typeId == 67) {
                        packetWrapper.cancel();
                        tracker3 = packetWrapper.user().get(EntityTracker.class);
                        endermite = new EndermiteReplacement(entityId, packetWrapper.user());
                        endermite.setLocation(x / 32.0, y / 32.0, z / 32.0);
                        endermite.setYawPitch(yaw * 360.0f / 256.0f, pitch * 360.0f / 256.0f);
                        endermite.setHeadYaw(headYaw * 360.0f / 256.0f);
                        tracker3.addEntityReplacement(endermite);
                    }
                    else if (typeId == 101 || typeId == 255 || typeId == -1) {
                        packetWrapper.cancel();
                    }
                    return;
                });
                final int entityId2;
                final int typeId2;
                final EntityTracker tracker4;
                this.handler(packetWrapper -> {
                    entityId2 = packetWrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    typeId2 = packetWrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 0);
                    tracker4 = packetWrapper.user().get(EntityTracker.class);
                    tracker4.getClientEntityTypes().put(entityId2, Entity1_10Types.getTypeFromId(typeId2, false));
                    tracker4.sendMetadataBuffer(entityId2);
                    return;
                });
                final List<Metadata> metadataList;
                final int entityId3;
                final EntityTracker tracker5;
                this.handler(wrapper -> {
                    metadataList = wrapper.get(Types1_7_6_10.METADATA_LIST, 0);
                    entityId3 = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    tracker5 = wrapper.user().get(EntityTracker.class);
                    if (tracker5.getEntityReplacement(entityId3) != null) {
                        tracker5.getEntityReplacement(entityId3).updateMetadata(metadataList);
                    }
                    else if (tracker5.getClientEntityTypes().containsKey(entityId3)) {
                        MetadataRewriter.transform(tracker5.getClientEntityTypes().get(entityId3), metadataList);
                    }
                    else {
                        wrapper.cancel();
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.SPAWN_PAINTING, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.STRING);
                final Position position;
                this.handler(packetWrapper -> {
                    position = packetWrapper.read(Type.POSITION);
                    packetWrapper.write(Type.INT, position.getX());
                    packetWrapper.write(Type.INT, position.getY());
                    packetWrapper.write(Type.INT, position.getZ());
                    return;
                });
                this.map(Type.UNSIGNED_BYTE, Type.INT);
                final int entityId;
                final EntityTracker tracker;
                this.handler(packetWrapper -> {
                    entityId = packetWrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    tracker = packetWrapper.user().get(EntityTracker.class);
                    tracker.getClientEntityTypes().put(entityId, Entity1_10Types.EntityType.PAINTING);
                    tracker.sendMetadataBuffer(entityId);
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.SPAWN_EXPERIENCE_ORB, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.INT);
                this.map(Type.INT);
                this.map(Type.INT);
                this.map(Type.SHORT);
                final int entityId;
                final EntityTracker tracker;
                this.handler(packetWrapper -> {
                    entityId = packetWrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    tracker = packetWrapper.user().get(EntityTracker.class);
                    tracker.getClientEntityTypes().put(entityId, Entity1_10Types.EntityType.EXPERIENCE_ORB);
                    tracker.sendMetadataBuffer(entityId);
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.SPAWN_GLOBAL_ENTITY, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.BYTE);
                this.map(Type.INT);
                this.map(Type.INT);
                this.map(Type.INT);
                final int entityId;
                final EntityTracker tracker;
                this.handler(packetWrapper -> {
                    entityId = packetWrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    tracker = packetWrapper.user().get(EntityTracker.class);
                    tracker.getClientEntityTypes().put(entityId, Entity1_10Types.EntityType.LIGHTNING);
                    tracker.sendMetadataBuffer(entityId);
                });
            }
        });
    }
}
