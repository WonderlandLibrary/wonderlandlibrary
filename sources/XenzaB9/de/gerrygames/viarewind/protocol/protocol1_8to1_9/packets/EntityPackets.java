// 
// Decompiled by Procyon v0.6.0
// 

package de.gerrygames.viarewind.protocol.protocol1_8to1_9.packets;

import de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage.Cooldown;
import java.util.UUID;
import com.viaversion.viaversion.util.Pair;
import java.util.Iterator;
import java.util.ArrayList;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.items.ItemRewriter;
import com.viaversion.viaversion.api.minecraft.item.Item;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.metadata.MetadataRewriter;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import java.util.List;
import com.viaversion.viaversion.api.type.types.version.Types1_8;
import com.viaversion.viaversion.api.type.types.version.Types1_9;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage.Levitation;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage.PlayerPosition;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_10Types;
import com.viaversion.viaversion.api.minecraft.Vector;
import de.gerrygames.viarewind.replacement.EntityReplacement;
import de.gerrygames.viarewind.utils.PacketUtil;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.Protocol1_8TO1_9;
import io.netty.buffer.ByteBuf;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.util.RelativeMoveUtil;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage.EntityTracker;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.protocols.protocol1_8.ServerboundPackets1_8;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.ServerboundPackets1_9;
import com.viaversion.viaversion.protocols.protocol1_8.ClientboundPackets1_8;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.ClientboundPackets1_9;
import com.viaversion.viaversion.api.protocol.Protocol;

public class EntityPackets
{
    public static void register(final Protocol<ClientboundPackets1_9, ClientboundPackets1_8, ServerboundPackets1_9, ServerboundPackets1_8> protocol) {
        protocol.registerClientbound(ClientboundPackets1_9.ENTITY_STATUS, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.handler(packetWrapper -> {
                    final byte status = packetWrapper.read((Type<Byte>)Type.BYTE);
                    if (status > 23) {
                        packetWrapper.cancel();
                    }
                    else {
                        packetWrapper.write(Type.BYTE, status);
                    }
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_9.ENTITY_POSITION, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.handler(packetWrapper -> {
                    final int entityId = packetWrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    final int relX = packetWrapper.read((Type<Short>)Type.SHORT);
                    final int relY = packetWrapper.read((Type<Short>)Type.SHORT);
                    final int relZ = packetWrapper.read((Type<Short>)Type.SHORT);
                    final EntityTracker tracker = packetWrapper.user().get(EntityTracker.class);
                    final EntityReplacement replacement = tracker.getEntityReplacement(entityId);
                    if (replacement != null) {
                        packetWrapper.cancel();
                        replacement.relMove(relX / 4096.0, relY / 4096.0, relZ / 4096.0);
                    }
                    else {
                        final Vector[] moves = RelativeMoveUtil.calculateRelativeMoves(packetWrapper.user(), entityId, relX, relY, relZ);
                        packetWrapper.write(Type.BYTE, (byte)moves[0].getBlockX());
                        packetWrapper.write(Type.BYTE, (byte)moves[0].getBlockY());
                        packetWrapper.write(Type.BYTE, (byte)moves[0].getBlockZ());
                        final boolean onGround = packetWrapper.passthrough((Type<Boolean>)Type.BOOLEAN);
                        if (moves.length > 1) {
                            final PacketWrapper secondPacket = PacketWrapper.create(21, null, packetWrapper.user());
                            secondPacket.write((Type<Object>)Type.VAR_INT, packetWrapper.get((Type<T>)Type.VAR_INT, 0));
                            secondPacket.write(Type.BYTE, (byte)moves[1].getBlockX());
                            secondPacket.write(Type.BYTE, (byte)moves[1].getBlockY());
                            secondPacket.write(Type.BYTE, (byte)moves[1].getBlockZ());
                            secondPacket.write(Type.BOOLEAN, onGround);
                            PacketUtil.sendPacket(secondPacket, Protocol1_8TO1_9.class);
                        }
                    }
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_9.ENTITY_POSITION_AND_ROTATION, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.handler(packetWrapper -> {
                    final int entityId = packetWrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    final int relX = packetWrapper.read((Type<Short>)Type.SHORT);
                    final int relY = packetWrapper.read((Type<Short>)Type.SHORT);
                    final int relZ = packetWrapper.read((Type<Short>)Type.SHORT);
                    final EntityTracker tracker = packetWrapper.user().get(EntityTracker.class);
                    final EntityReplacement replacement = tracker.getEntityReplacement(entityId);
                    if (replacement != null) {
                        packetWrapper.cancel();
                        replacement.relMove(relX / 4096.0, relY / 4096.0, relZ / 4096.0);
                        replacement.setYawPitch(packetWrapper.read((Type<Byte>)Type.BYTE) * 360.0f / 256.0f, packetWrapper.read((Type<Byte>)Type.BYTE) * 360.0f / 256.0f);
                    }
                    else {
                        final Vector[] moves = RelativeMoveUtil.calculateRelativeMoves(packetWrapper.user(), entityId, relX, relY, relZ);
                        packetWrapper.write(Type.BYTE, (byte)moves[0].getBlockX());
                        packetWrapper.write(Type.BYTE, (byte)moves[0].getBlockY());
                        packetWrapper.write(Type.BYTE, (byte)moves[0].getBlockZ());
                        byte yaw = packetWrapper.passthrough((Type<Byte>)Type.BYTE);
                        final byte pitch = packetWrapper.passthrough((Type<Byte>)Type.BYTE);
                        final boolean onGround = packetWrapper.passthrough((Type<Boolean>)Type.BOOLEAN);
                        final Entity1_10Types.EntityType type = packetWrapper.user().get(EntityTracker.class).getClientEntityTypes().get(entityId);
                        if (type == Entity1_10Types.EntityType.BOAT) {
                            yaw -= 64;
                            packetWrapper.set(Type.BYTE, 3, yaw);
                        }
                        if (moves.length > 1) {
                            final PacketWrapper secondPacket = PacketWrapper.create(23, null, packetWrapper.user());
                            secondPacket.write((Type<Object>)Type.VAR_INT, packetWrapper.get((Type<T>)Type.VAR_INT, 0));
                            secondPacket.write(Type.BYTE, (byte)moves[1].getBlockX());
                            secondPacket.write(Type.BYTE, (byte)moves[1].getBlockY());
                            secondPacket.write(Type.BYTE, (byte)moves[1].getBlockZ());
                            secondPacket.write(Type.BYTE, yaw);
                            secondPacket.write(Type.BYTE, pitch);
                            secondPacket.write(Type.BOOLEAN, onGround);
                            PacketUtil.sendPacket(secondPacket, Protocol1_8TO1_9.class);
                        }
                    }
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_9.ENTITY_ROTATION, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.BOOLEAN);
                this.handler(packetWrapper -> {
                    final int entityId = packetWrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    final EntityTracker tracker = packetWrapper.user().get(EntityTracker.class);
                    final EntityReplacement replacement = tracker.getEntityReplacement(entityId);
                    if (replacement != null) {
                        packetWrapper.cancel();
                        final int yaw = packetWrapper.get((Type<Byte>)Type.BYTE, 0);
                        final int pitch = packetWrapper.get((Type<Byte>)Type.BYTE, 1);
                        replacement.setYawPitch(yaw * 360.0f / 256.0f, pitch * 360.0f / 256.0f);
                    }
                    return;
                });
                this.handler(packetWrapper -> {
                    final int entityId2 = packetWrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    final Entity1_10Types.EntityType type = packetWrapper.user().get(EntityTracker.class).getClientEntityTypes().get(entityId2);
                    if (type == Entity1_10Types.EntityType.BOAT) {
                        final byte yaw2 = packetWrapper.get((Type<Byte>)Type.BYTE, 0);
                        final byte yaw3 = (byte)(yaw2 - 64);
                        packetWrapper.set(Type.BYTE, 0, yaw3);
                    }
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_9.VEHICLE_MOVE, ClientboundPackets1_8.ENTITY_TELEPORT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(packetWrapper -> {
                    final EntityTracker tracker = packetWrapper.user().get(EntityTracker.class);
                    final int vehicle = tracker.getVehicle(tracker.getPlayerId());
                    if (vehicle == -1) {
                        packetWrapper.cancel();
                    }
                    packetWrapper.write(Type.VAR_INT, vehicle);
                    return;
                });
                this.map(Type.DOUBLE, Protocol1_8TO1_9.TO_OLD_INT);
                this.map(Type.DOUBLE, Protocol1_8TO1_9.TO_OLD_INT);
                this.map(Type.DOUBLE, Protocol1_8TO1_9.TO_OLD_INT);
                this.map(Type.FLOAT, Protocol1_8TO1_9.DEGREES_TO_ANGLE);
                this.map(Type.FLOAT, Protocol1_8TO1_9.DEGREES_TO_ANGLE);
                this.handler(packetWrapper -> {
                    if (packetWrapper.isCancelled()) {
                        return;
                    }
                    else {
                        final PlayerPosition position = packetWrapper.user().get(PlayerPosition.class);
                        final double x = packetWrapper.get((Type<Integer>)Type.INT, 0) / 32.0;
                        final double y = packetWrapper.get((Type<Integer>)Type.INT, 1) / 32.0;
                        final double z = packetWrapper.get((Type<Integer>)Type.INT, 2) / 32.0;
                        position.setPos(x, y, z);
                        return;
                    }
                });
                this.create(Type.BOOLEAN, true);
                this.handler(packetWrapper -> {
                    final int entityId = packetWrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    final Entity1_10Types.EntityType type = packetWrapper.user().get(EntityTracker.class).getClientEntityTypes().get(entityId);
                    if (type == Entity1_10Types.EntityType.BOAT) {
                        final byte yaw = packetWrapper.get((Type<Byte>)Type.BYTE, 1);
                        final byte yaw2 = (byte)(yaw - 64);
                        packetWrapper.set(Type.BYTE, 0, yaw2);
                        int y2 = packetWrapper.get((Type<Integer>)Type.INT, 1);
                        y2 += 10;
                        packetWrapper.set(Type.INT, 1, y2);
                    }
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_9.DESTROY_ENTITIES, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT_ARRAY_PRIMITIVE);
                this.handler(packetWrapper -> {
                    final EntityTracker tracker = packetWrapper.user().get(EntityTracker.class);
                    final int[] array = packetWrapper.get(Type.VAR_INT_ARRAY_PRIMITIVE, 0);
                    int i = 0;
                    for (int length = array.length; i < length; ++i) {
                        final int entityId = array[i];
                        tracker.removeEntity(entityId);
                    }
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_9.REMOVE_ENTITY_EFFECT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.BYTE);
                this.handler(packetWrapper -> {
                    final int id = packetWrapper.get((Type<Byte>)Type.BYTE, 0);
                    if (id > 23) {
                        packetWrapper.cancel();
                    }
                    if (id == 25) {
                        if (packetWrapper.get((Type<Integer>)Type.VAR_INT, 0) == packetWrapper.user().get(EntityTracker.class).getPlayerId()) {
                            final Levitation levitation = packetWrapper.user().get(Levitation.class);
                            levitation.setActive(false);
                        }
                    }
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_9.ENTITY_HEAD_LOOK, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.BYTE);
                this.handler(packetWrapper -> {
                    final int entityId = packetWrapper.get((Type<Integer>)Type.VAR_INT, 0);
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
        protocol.registerClientbound(ClientboundPackets1_9.ENTITY_METADATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Types1_9.METADATA_LIST, Types1_8.METADATA_LIST);
                this.handler(wrapper -> {
                    final List<Metadata> metadataList = wrapper.get(Types1_8.METADATA_LIST, 0);
                    final int entityId = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    final EntityTracker tracker = wrapper.user().get(EntityTracker.class);
                    if (tracker.getClientEntityTypes().containsKey(entityId)) {
                        MetadataRewriter.transform(tracker.getClientEntityTypes().get(entityId), metadataList);
                        if (metadataList.isEmpty()) {
                            wrapper.cancel();
                        }
                    }
                    else {
                        tracker.addMetadataToBuffer(entityId, metadataList);
                        wrapper.cancel();
                    }
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_9.ATTACH_ENTITY, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.INT);
                this.create(Type.BOOLEAN, true);
            }
        });
        protocol.registerClientbound(ClientboundPackets1_9.ENTITY_EQUIPMENT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.handler(packetWrapper -> {
                    int slot = packetWrapper.read((Type<Integer>)Type.VAR_INT);
                    if (slot == 1) {
                        packetWrapper.cancel();
                    }
                    else if (slot > 1) {
                        --slot;
                    }
                    packetWrapper.write(Type.SHORT, (short)slot);
                    return;
                });
                this.map(Type.ITEM);
                this.handler(packetWrapper -> packetWrapper.set(Type.ITEM, 0, ItemRewriter.toClient(packetWrapper.get(Type.ITEM, 0))));
            }
        });
        protocol.registerClientbound(ClientboundPackets1_9.SET_PASSENGERS, null, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(packetWrapper -> {
                    packetWrapper.cancel();
                    final EntityTracker entityTracker = packetWrapper.user().get(EntityTracker.class);
                    final int vehicle = packetWrapper.read((Type<Integer>)Type.VAR_INT);
                    final int count = packetWrapper.read((Type<Integer>)Type.VAR_INT);
                    final ArrayList<Integer> passengers = new ArrayList<Integer>();
                    for (int i = 0; i < count; ++i) {
                        passengers.add(packetWrapper.read((Type<Integer>)Type.VAR_INT));
                    }
                    final List<Integer> oldPassengers = entityTracker.getPassengers(vehicle);
                    entityTracker.setPassengers(vehicle, passengers);
                    if (!oldPassengers.isEmpty()) {
                        oldPassengers.iterator();
                        final Iterator iterator;
                        while (iterator.hasNext()) {
                            final Integer passenger = iterator.next();
                            final PacketWrapper detach = PacketWrapper.create(27, null, packetWrapper.user());
                            detach.write(Type.INT, passenger);
                            detach.write(Type.INT, -1);
                            detach.write(Type.BOOLEAN, false);
                            PacketUtil.sendPacket(detach, Protocol1_8TO1_9.class);
                        }
                    }
                    for (int j = 0; j < count; ++j) {
                        final int v = (j == 0) ? vehicle : passengers.get(j - 1);
                        final int p = passengers.get(j);
                        final PacketWrapper attach = PacketWrapper.create(27, null, packetWrapper.user());
                        attach.write(Type.INT, p);
                        attach.write(Type.INT, v);
                        attach.write(Type.BOOLEAN, false);
                        PacketUtil.sendPacket(attach, Protocol1_8TO1_9.class);
                    }
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_9.ENTITY_TELEPORT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.DOUBLE, Protocol1_8TO1_9.TO_OLD_INT);
                this.map(Type.DOUBLE, Protocol1_8TO1_9.TO_OLD_INT);
                this.map(Type.DOUBLE, Protocol1_8TO1_9.TO_OLD_INT);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.BOOLEAN);
                this.handler(packetWrapper -> {
                    final int entityId = packetWrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    final Entity1_10Types.EntityType type = packetWrapper.user().get(EntityTracker.class).getClientEntityTypes().get(entityId);
                    if (type == Entity1_10Types.EntityType.BOAT) {
                        final byte yaw = packetWrapper.get((Type<Byte>)Type.BYTE, 1);
                        final byte yaw2 = (byte)(yaw - 64);
                        packetWrapper.set(Type.BYTE, 0, yaw2);
                        int y = packetWrapper.get((Type<Integer>)Type.INT, 1);
                        y += 10;
                        packetWrapper.set(Type.INT, 1, y);
                    }
                    return;
                });
                this.handler(packetWrapper -> {
                    final int entityId2 = packetWrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    packetWrapper.user().get(EntityTracker.class).resetEntityOffset(entityId2);
                    return;
                });
                this.handler(packetWrapper -> {
                    final int entityId3 = packetWrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    final EntityTracker tracker = packetWrapper.user().get(EntityTracker.class);
                    final EntityReplacement replacement = tracker.getEntityReplacement(entityId3);
                    if (replacement != null) {
                        packetWrapper.cancel();
                        final int x = packetWrapper.get((Type<Integer>)Type.INT, 0);
                        final int y2 = packetWrapper.get((Type<Integer>)Type.INT, 1);
                        final int z = packetWrapper.get((Type<Integer>)Type.INT, 2);
                        final int yaw3 = packetWrapper.get((Type<Byte>)Type.BYTE, 0);
                        final int pitch = packetWrapper.get((Type<Byte>)Type.BYTE, 1);
                        replacement.setLocation(x / 32.0, y2 / 32.0, z / 32.0);
                        replacement.setYawPitch(yaw3 * 360.0f / 256.0f, pitch * 360.0f / 256.0f);
                    }
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_9.ENTITY_PROPERTIES, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.INT);
                this.handler(packetWrapper -> {
                    final boolean player = packetWrapper.get((Type<Integer>)Type.VAR_INT, 0) == packetWrapper.user().get(EntityTracker.class).getPlayerId();
                    final int size = packetWrapper.get((Type<Integer>)Type.INT, 0);
                    int removed = 0;
                    for (int i = 0; i < size; ++i) {
                        final String key = packetWrapper.read(Type.STRING);
                        final boolean skip = !Protocol1_8TO1_9.VALID_ATTRIBUTES.contains(key);
                        final double value = packetWrapper.read((Type<Double>)Type.DOUBLE);
                        final int modifiersize = packetWrapper.read((Type<Integer>)Type.VAR_INT);
                        if (!skip) {
                            packetWrapper.write(Type.STRING, key);
                            packetWrapper.write(Type.DOUBLE, value);
                            packetWrapper.write(Type.VAR_INT, modifiersize);
                        }
                        else {
                            ++removed;
                        }
                        final ArrayList<Pair<Byte, Double>> modifiers = new ArrayList<Pair<Byte, Double>>();
                        for (int j = 0; j < modifiersize; ++j) {
                            final UUID uuid = packetWrapper.read(Type.UUID);
                            final double amount = packetWrapper.read((Type<Double>)Type.DOUBLE);
                            final byte operation = packetWrapper.read((Type<Byte>)Type.BYTE);
                            modifiers.add(new Pair<Byte, Double>(operation, amount));
                            if (!skip) {
                                packetWrapper.write(Type.UUID, uuid);
                                packetWrapper.write(Type.DOUBLE, amount);
                                packetWrapper.write(Type.BYTE, operation);
                            }
                        }
                        if (player && key.equals("generic.attackSpeed")) {
                            packetWrapper.user().get(Cooldown.class).setAttackSpeed(value, modifiers);
                        }
                    }
                    packetWrapper.set(Type.INT, 0, size - removed);
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_9.ENTITY_EFFECT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.VAR_INT);
                this.map(Type.BYTE);
                this.handler(packetWrapper -> {
                    final int id = packetWrapper.get((Type<Byte>)Type.BYTE, 0);
                    if (id > 23) {
                        packetWrapper.cancel();
                    }
                    if (id == 25) {
                        if (packetWrapper.get((Type<Integer>)Type.VAR_INT, 0) == packetWrapper.user().get(EntityTracker.class).getPlayerId()) {
                            final Levitation levitation = packetWrapper.user().get(Levitation.class);
                            levitation.setActive(true);
                            levitation.setAmplifier(packetWrapper.get((Type<Byte>)Type.BYTE, 1));
                        }
                    }
                });
            }
        });
    }
}
