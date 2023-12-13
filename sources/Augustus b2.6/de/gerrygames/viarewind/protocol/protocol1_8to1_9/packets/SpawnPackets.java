// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_8to1_9.packets;

import de.gerrygames.viarewind.protocol.protocol1_8to1_9.metadata.MetadataRewriter;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import java.util.List;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.entityreplacement.ShulkerReplacement;
import com.viaversion.viaversion.api.type.types.version.Types1_8;
import com.viaversion.viaversion.api.type.types.version.Types1_9;
import de.gerrygames.viarewind.replacement.Replacement;
import de.gerrygames.viarewind.utils.PacketUtil;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.items.ReplacementRegistry1_8to1_9;
import de.gerrygames.viarewind.replacement.EntityReplacement;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.entityreplacement.ShulkerBulletReplacement;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import de.gerrygames.viarewind.ViaRewind;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_10Types;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage.EntityTracker;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.Protocol1_8TO1_9;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.protocols.protocol1_8.ServerboundPackets1_8;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.ServerboundPackets1_9;
import com.viaversion.viaversion.protocols.protocol1_8.ClientboundPackets1_8;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.ClientboundPackets1_9;
import com.viaversion.viaversion.api.protocol.Protocol;

public class SpawnPackets
{
    public static void register(final Protocol<ClientboundPackets1_9, ClientboundPackets1_8, ServerboundPackets1_9, ServerboundPackets1_8> protocol) {
        protocol.registerClientbound(ClientboundPackets1_9.SPAWN_ENTITY, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.UUID, Type.NOTHING);
                this.map(Type.BYTE);
                this.map(Type.DOUBLE, Protocol1_8TO1_9.TO_OLD_INT);
                this.map(Type.DOUBLE, Protocol1_8TO1_9.TO_OLD_INT);
                this.map(Type.DOUBLE, Protocol1_8TO1_9.TO_OLD_INT);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.INT);
                final int entityId;
                final int typeId;
                final EntityTracker tracker;
                final Entity1_10Types.EntityType type;
                int x;
                int y;
                int z;
                byte yaw;
                byte yaw2;
                ShulkerBulletReplacement shulkerBulletReplacement;
                int data;
                int blockId;
                int blockData;
                Replacement replace;
                short vX;
                short vY;
                short vZ;
                PacketWrapper velocityPacket;
                this.handler(packetWrapper -> {
                    entityId = packetWrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    typeId = packetWrapper.get((Type<Byte>)Type.BYTE, 0);
                    tracker = packetWrapper.user().get(EntityTracker.class);
                    type = Entity1_10Types.getTypeFromId(typeId, true);
                    if (typeId == 3 || typeId == 91 || typeId == 92 || typeId == 93) {
                        packetWrapper.cancel();
                    }
                    else if (type == null) {
                        ViaRewind.getPlatform().getLogger().warning("[ViaRewind] Unhandled Spawn Object Type: " + typeId);
                        packetWrapper.cancel();
                    }
                    else {
                        x = packetWrapper.get((Type<Integer>)Type.INT, 0);
                        y = packetWrapper.get((Type<Integer>)Type.INT, 1);
                        z = packetWrapper.get((Type<Integer>)Type.INT, 2);
                        if (type.is(Entity1_10Types.EntityType.BOAT)) {
                            yaw = packetWrapper.get((Type<Byte>)Type.BYTE, 1);
                            yaw2 = (byte)(yaw - 64);
                            packetWrapper.set(Type.BYTE, 1, yaw2);
                            y += 10;
                            packetWrapper.set(Type.INT, 1, y);
                        }
                        else if (type.is(Entity1_10Types.EntityType.SHULKER_BULLET)) {
                            packetWrapper.cancel();
                            shulkerBulletReplacement = new ShulkerBulletReplacement(entityId, packetWrapper.user());
                            shulkerBulletReplacement.setLocation(x / 32.0, y / 32.0, z / 32.0);
                            tracker.addEntityReplacement(shulkerBulletReplacement);
                            return;
                        }
                        data = packetWrapper.get((Type<Integer>)Type.INT, 3);
                        if (type.isOrHasParent(Entity1_10Types.EntityType.ARROW) && data != 0) {
                            packetWrapper.set(Type.INT, 3, --data);
                        }
                        if (type.is(Entity1_10Types.EntityType.FALLING_BLOCK)) {
                            blockId = (data & 0xFFF);
                            blockData = (data >> 12 & 0xF);
                            replace = ReplacementRegistry1_8to1_9.getReplacement(blockId, blockData);
                            if (replace != null) {
                                packetWrapper.set(Type.INT, 3, replace.getId() | replace.replaceData(data) << 12);
                            }
                        }
                        if (data > 0) {
                            packetWrapper.passthrough((Type<Object>)Type.SHORT);
                            packetWrapper.passthrough((Type<Object>)Type.SHORT);
                            packetWrapper.passthrough((Type<Object>)Type.SHORT);
                        }
                        else {
                            vX = packetWrapper.read((Type<Short>)Type.SHORT);
                            vY = packetWrapper.read((Type<Short>)Type.SHORT);
                            vZ = packetWrapper.read((Type<Short>)Type.SHORT);
                            velocityPacket = PacketWrapper.create(18, null, packetWrapper.user());
                            velocityPacket.write(Type.VAR_INT, entityId);
                            velocityPacket.write(Type.SHORT, vX);
                            velocityPacket.write(Type.SHORT, vY);
                            velocityPacket.write(Type.SHORT, vZ);
                            PacketUtil.sendPacket(velocityPacket, Protocol1_8TO1_9.class);
                        }
                        tracker.getClientEntityTypes().put(entityId, type);
                        tracker.sendMetadataBuffer(entityId);
                    }
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_9.SPAWN_EXPERIENCE_ORB, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.DOUBLE, Protocol1_8TO1_9.TO_OLD_INT);
                this.map(Type.DOUBLE, Protocol1_8TO1_9.TO_OLD_INT);
                this.map(Type.DOUBLE, Protocol1_8TO1_9.TO_OLD_INT);
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
        protocol.registerClientbound(ClientboundPackets1_9.SPAWN_GLOBAL_ENTITY, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.BYTE);
                this.map(Type.DOUBLE, Protocol1_8TO1_9.TO_OLD_INT);
                this.map(Type.DOUBLE, Protocol1_8TO1_9.TO_OLD_INT);
                this.map(Type.DOUBLE, Protocol1_8TO1_9.TO_OLD_INT);
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
        protocol.registerClientbound(ClientboundPackets1_9.SPAWN_MOB, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.UUID, Type.NOTHING);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.DOUBLE, Protocol1_8TO1_9.TO_OLD_INT);
                this.map(Type.DOUBLE, Protocol1_8TO1_9.TO_OLD_INT);
                this.map(Type.DOUBLE, Protocol1_8TO1_9.TO_OLD_INT);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.SHORT);
                this.map(Type.SHORT);
                this.map(Type.SHORT);
                this.map(Types1_9.METADATA_LIST, Types1_8.METADATA_LIST);
                final int entityId;
                final int typeId;
                final int x;
                final int y;
                final int z;
                final byte pitch;
                final byte yaw;
                final byte headYaw;
                EntityTracker tracker;
                ShulkerReplacement shulkerReplacement;
                this.handler(packetWrapper -> {
                    entityId = packetWrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    typeId = packetWrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 0);
                    x = packetWrapper.get((Type<Integer>)Type.INT, 0);
                    y = packetWrapper.get((Type<Integer>)Type.INT, 1);
                    z = packetWrapper.get((Type<Integer>)Type.INT, 2);
                    pitch = packetWrapper.get((Type<Byte>)Type.BYTE, 1);
                    yaw = packetWrapper.get((Type<Byte>)Type.BYTE, 0);
                    headYaw = packetWrapper.get((Type<Byte>)Type.BYTE, 2);
                    if (typeId == 69) {
                        packetWrapper.cancel();
                        tracker = packetWrapper.user().get(EntityTracker.class);
                        shulkerReplacement = new ShulkerReplacement(entityId, packetWrapper.user());
                        shulkerReplacement.setLocation(x / 32.0, y / 32.0, z / 32.0);
                        shulkerReplacement.setYawPitch(yaw * 360.0f / 256.0f, pitch * 360.0f / 256.0f);
                        shulkerReplacement.setHeadYaw(headYaw * 360.0f / 256.0f);
                        tracker.addEntityReplacement(shulkerReplacement);
                    }
                    else if (typeId == -1 || typeId == 255) {
                        packetWrapper.cancel();
                    }
                    return;
                });
                final int entityId2;
                final int typeId2;
                final EntityTracker tracker2;
                this.handler(packetWrapper -> {
                    entityId2 = packetWrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    typeId2 = packetWrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 0);
                    tracker2 = packetWrapper.user().get(EntityTracker.class);
                    tracker2.getClientEntityTypes().put(entityId2, Entity1_10Types.getTypeFromId(typeId2, false));
                    tracker2.sendMetadataBuffer(entityId2);
                    return;
                });
                final List<Metadata> metadataList;
                final int entityId3;
                final EntityTracker tracker3;
                final EntityReplacement replacement;
                final Object o;
                this.handler(wrapper -> {
                    metadataList = wrapper.get(Types1_8.METADATA_LIST, 0);
                    entityId3 = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    tracker3 = wrapper.user().get(EntityTracker.class);
                    replacement = tracker3.getEntityReplacement(entityId3);
                    if (o != null) {
                        replacement.updateMetadata(metadataList);
                    }
                    else if (tracker3.getClientEntityTypes().containsKey(entityId3)) {
                        MetadataRewriter.transform(tracker3.getClientEntityTypes().get(entityId3), metadataList);
                    }
                    else {
                        wrapper.cancel();
                    }
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_9.SPAWN_PAINTING, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.UUID, Type.NOTHING);
                this.map(Type.STRING);
                this.map(Type.POSITION);
                this.map(Type.BYTE, Type.UNSIGNED_BYTE);
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
        protocol.registerClientbound(ClientboundPackets1_9.SPAWN_PLAYER, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.UUID);
                this.map(Type.DOUBLE, Protocol1_8TO1_9.TO_OLD_INT);
                this.map(Type.DOUBLE, Protocol1_8TO1_9.TO_OLD_INT);
                this.map(Type.DOUBLE, Protocol1_8TO1_9.TO_OLD_INT);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.handler(packetWrapper -> packetWrapper.write(Type.SHORT, (Short)0));
                this.map(Types1_9.METADATA_LIST, Types1_8.METADATA_LIST);
                final List<Metadata> metadataList;
                this.handler(wrapper -> {
                    metadataList = wrapper.get(Types1_8.METADATA_LIST, 0);
                    MetadataRewriter.transform(Entity1_10Types.EntityType.PLAYER, metadataList);
                    return;
                });
                final int entityId;
                final EntityTracker tracker;
                this.handler(packetWrapper -> {
                    entityId = packetWrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    tracker = packetWrapper.user().get(EntityTracker.class);
                    tracker.getClientEntityTypes().put(entityId, Entity1_10Types.EntityType.PLAYER);
                    tracker.sendMetadataBuffer(entityId);
                });
            }
        });
    }
}
