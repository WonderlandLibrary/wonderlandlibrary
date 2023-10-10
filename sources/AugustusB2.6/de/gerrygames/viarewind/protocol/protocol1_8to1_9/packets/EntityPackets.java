// 
// Decompiled by Procyon v0.5.36
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
                final byte status;
                this.handler(packetWrapper -> {
                    status = packetWrapper.read((Type<Byte>)Type.BYTE);
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
                final int entityId;
                final int relX;
                final int relY;
                final int relZ;
                final EntityTracker tracker;
                final EntityReplacement replacement;
                Vector[] moves;
                boolean onGround;
                PacketWrapper secondPacket;
                this.handler(packetWrapper -> {
                    entityId = packetWrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    relX = packetWrapper.read((Type<Short>)Type.SHORT);
                    relY = packetWrapper.read((Type<Short>)Type.SHORT);
                    relZ = packetWrapper.read((Type<Short>)Type.SHORT);
                    tracker = packetWrapper.user().get(EntityTracker.class);
                    replacement = tracker.getEntityReplacement(entityId);
                    if (replacement != null) {
                        packetWrapper.cancel();
                        replacement.relMove(relX / 4096.0, relY / 4096.0, relZ / 4096.0);
                    }
                    else {
                        moves = RelativeMoveUtil.calculateRelativeMoves(packetWrapper.user(), entityId, relX, relY, relZ);
                        packetWrapper.write(Type.BYTE, (byte)moves[0].getBlockX());
                        packetWrapper.write(Type.BYTE, (byte)moves[0].getBlockY());
                        packetWrapper.write(Type.BYTE, (byte)moves[0].getBlockZ());
                        onGround = packetWrapper.passthrough((Type<Boolean>)Type.BOOLEAN);
                        if (moves.length > 1) {
                            secondPacket = PacketWrapper.create(21, null, packetWrapper.user());
                            secondPacket.write(Type.VAR_INT, (Integer)packetWrapper.get((Type<T>)Type.VAR_INT, 0));
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
                final int entityId;
                final int relX;
                final int relY;
                final int relZ;
                final EntityTracker tracker;
                final EntityReplacement replacement;
                Vector[] moves;
                byte yaw;
                byte pitch;
                boolean onGround;
                Entity1_10Types.EntityType type;
                PacketWrapper secondPacket;
                this.handler(packetWrapper -> {
                    entityId = packetWrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    relX = packetWrapper.read((Type<Short>)Type.SHORT);
                    relY = packetWrapper.read((Type<Short>)Type.SHORT);
                    relZ = packetWrapper.read((Type<Short>)Type.SHORT);
                    tracker = packetWrapper.user().get(EntityTracker.class);
                    replacement = tracker.getEntityReplacement(entityId);
                    if (replacement != null) {
                        packetWrapper.cancel();
                        replacement.relMove(relX / 4096.0, relY / 4096.0, relZ / 4096.0);
                        replacement.setYawPitch(packetWrapper.read((Type<Byte>)Type.BYTE) * 360.0f / 256.0f, packetWrapper.read((Type<Byte>)Type.BYTE) * 360.0f / 256.0f);
                    }
                    else {
                        moves = RelativeMoveUtil.calculateRelativeMoves(packetWrapper.user(), entityId, relX, relY, relZ);
                        packetWrapper.write(Type.BYTE, (byte)moves[0].getBlockX());
                        packetWrapper.write(Type.BYTE, (byte)moves[0].getBlockY());
                        packetWrapper.write(Type.BYTE, (byte)moves[0].getBlockZ());
                        yaw = packetWrapper.passthrough((Type<Byte>)Type.BYTE);
                        pitch = packetWrapper.passthrough((Type<Byte>)Type.BYTE);
                        onGround = packetWrapper.passthrough((Type<Boolean>)Type.BOOLEAN);
                        type = packetWrapper.user().get(EntityTracker.class).getClientEntityTypes().get(entityId);
                        if (type == Entity1_10Types.EntityType.BOAT) {
                            yaw -= 64;
                            packetWrapper.set(Type.BYTE, 3, yaw);
                        }
                        if (moves.length > 1) {
                            secondPacket = PacketWrapper.create(23, null, packetWrapper.user());
                            secondPacket.write(Type.VAR_INT, (Integer)packetWrapper.get((Type<T>)Type.VAR_INT, 0));
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
                final int entityId;
                final EntityTracker tracker;
                final EntityReplacement replacement;
                int yaw;
                int pitch;
                this.handler(packetWrapper -> {
                    entityId = packetWrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    tracker = packetWrapper.user().get(EntityTracker.class);
                    replacement = tracker.getEntityReplacement(entityId);
                    if (replacement != null) {
                        packetWrapper.cancel();
                        yaw = packetWrapper.get((Type<Byte>)Type.BYTE, 0);
                        pitch = packetWrapper.get((Type<Byte>)Type.BYTE, 1);
                        replacement.setYawPitch(yaw * 360.0f / 256.0f, pitch * 360.0f / 256.0f);
                    }
                    return;
                });
                final int entityId2;
                final Entity1_10Types.EntityType type;
                byte yaw2;
                byte yaw3;
                this.handler(packetWrapper -> {
                    entityId2 = packetWrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    type = packetWrapper.user().get(EntityTracker.class).getClientEntityTypes().get(entityId2);
                    if (type == Entity1_10Types.EntityType.BOAT) {
                        yaw2 = packetWrapper.get((Type<Byte>)Type.BYTE, 0);
                        yaw3 = (byte)(yaw2 - 64);
                        packetWrapper.set(Type.BYTE, 0, yaw3);
                    }
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_9.VEHICLE_MOVE, ClientboundPackets1_8.ENTITY_TELEPORT, new PacketRemapper() {
            @Override
            public void registerMap() {
                final EntityTracker tracker;
                final int vehicle;
                this.handler(packetWrapper -> {
                    tracker = packetWrapper.user().get(EntityTracker.class);
                    vehicle = tracker.getVehicle(tracker.getPlayerId());
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
                PlayerPosition position;
                double x;
                double y;
                double z;
                this.handler(packetWrapper -> {
                    if (packetWrapper.isCancelled()) {
                        return;
                    }
                    else {
                        position = packetWrapper.user().get(PlayerPosition.class);
                        x = packetWrapper.get((Type<Integer>)Type.INT, 0) / 32.0;
                        y = packetWrapper.get((Type<Integer>)Type.INT, 1) / 32.0;
                        z = packetWrapper.get((Type<Integer>)Type.INT, 2) / 32.0;
                        position.setPos(x, y, z);
                        return;
                    }
                });
                this.create(Type.BOOLEAN, true);
                final int entityId;
                final Entity1_10Types.EntityType type;
                byte yaw;
                byte yaw2;
                int y2;
                this.handler(packetWrapper -> {
                    entityId = packetWrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    type = packetWrapper.user().get(EntityTracker.class).getClientEntityTypes().get(entityId);
                    if (type == Entity1_10Types.EntityType.BOAT) {
                        yaw = packetWrapper.get((Type<Byte>)Type.BYTE, 1);
                        yaw2 = (byte)(yaw - 64);
                        packetWrapper.set(Type.BYTE, 0, yaw2);
                        y2 = packetWrapper.get((Type<Integer>)Type.INT, 1);
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
                final EntityTracker tracker;
                final int[] array;
                int length;
                int i = 0;
                int entityId;
                this.handler(packetWrapper -> {
                    tracker = packetWrapper.user().get(EntityTracker.class);
                    array = packetWrapper.get(Type.VAR_INT_ARRAY_PRIMITIVE, 0);
                    for (length = array.length; i < length; ++i) {
                        entityId = array[i];
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
                final int id;
                Levitation levitation;
                this.handler(packetWrapper -> {
                    id = packetWrapper.get((Type<Byte>)Type.BYTE, 0);
                    if (id > 23) {
                        packetWrapper.cancel();
                    }
                    if (id == 25) {
                        if (packetWrapper.get((Type<Integer>)Type.VAR_INT, 0) == packetWrapper.user().get(EntityTracker.class).getPlayerId()) {
                            levitation = packetWrapper.user().get(Levitation.class);
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
                final int entityId;
                final EntityTracker tracker;
                final EntityReplacement replacement;
                int yaw;
                this.handler(packetWrapper -> {
                    entityId = packetWrapper.get((Type<Integer>)Type.VAR_INT, 0);
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
        protocol.registerClientbound(ClientboundPackets1_9.ENTITY_METADATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Types1_9.METADATA_LIST, Types1_8.METADATA_LIST);
                final List<Metadata> metadataList;
                final int entityId;
                final EntityTracker tracker;
                this.handler(wrapper -> {
                    metadataList = wrapper.get(Types1_8.METADATA_LIST, 0);
                    entityId = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    tracker = wrapper.user().get(EntityTracker.class);
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
                int slot;
                this.handler(packetWrapper -> {
                    slot = packetWrapper.read((Type<Integer>)Type.VAR_INT);
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
                final EntityTracker entityTracker;
                final int vehicle;
                final int count;
                final ArrayList<Integer> passengers;
                int i;
                final List<Integer> oldPassengers;
                final Iterator<Integer> iterator;
                Integer passenger;
                PacketWrapper detach;
                int j;
                int v;
                int p;
                PacketWrapper attach;
                this.handler(packetWrapper -> {
                    packetWrapper.cancel();
                    entityTracker = packetWrapper.user().get(EntityTracker.class);
                    vehicle = packetWrapper.read((Type<Integer>)Type.VAR_INT);
                    count = packetWrapper.read((Type<Integer>)Type.VAR_INT);
                    passengers = new ArrayList<Integer>();
                    for (i = 0; i < count; ++i) {
                        passengers.add(packetWrapper.read((Type<Integer>)Type.VAR_INT));
                    }
                    oldPassengers = entityTracker.getPassengers(vehicle);
                    entityTracker.setPassengers(vehicle, passengers);
                    if (!oldPassengers.isEmpty()) {
                        oldPassengers.iterator();
                        while (iterator.hasNext()) {
                            passenger = iterator.next();
                            detach = PacketWrapper.create(27, null, packetWrapper.user());
                            detach.write(Type.INT, passenger);
                            detach.write(Type.INT, -1);
                            detach.write(Type.BOOLEAN, false);
                            PacketUtil.sendPacket(detach, Protocol1_8TO1_9.class);
                        }
                    }
                    for (j = 0; j < count; ++j) {
                        v = ((j == 0) ? vehicle : passengers.get(j - 1));
                        p = passengers.get(j);
                        attach = PacketWrapper.create(27, null, packetWrapper.user());
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
                final int entityId;
                final Entity1_10Types.EntityType type;
                byte yaw;
                byte yaw2;
                int y;
                this.handler(packetWrapper -> {
                    entityId = packetWrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    type = packetWrapper.user().get(EntityTracker.class).getClientEntityTypes().get(entityId);
                    if (type == Entity1_10Types.EntityType.BOAT) {
                        yaw = packetWrapper.get((Type<Byte>)Type.BYTE, 1);
                        yaw2 = (byte)(yaw - 64);
                        packetWrapper.set(Type.BYTE, 0, yaw2);
                        y = packetWrapper.get((Type<Integer>)Type.INT, 1);
                        y += 10;
                        packetWrapper.set(Type.INT, 1, y);
                    }
                    return;
                });
                final int entityId2;
                this.handler(packetWrapper -> {
                    entityId2 = packetWrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    packetWrapper.user().get(EntityTracker.class).resetEntityOffset(entityId2);
                    return;
                });
                final int entityId3;
                final EntityTracker tracker;
                final EntityReplacement replacement;
                int x;
                int y2;
                int z;
                int yaw3;
                int pitch;
                this.handler(packetWrapper -> {
                    entityId3 = packetWrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    tracker = packetWrapper.user().get(EntityTracker.class);
                    replacement = tracker.getEntityReplacement(entityId3);
                    if (replacement != null) {
                        packetWrapper.cancel();
                        x = packetWrapper.get((Type<Integer>)Type.INT, 0);
                        y2 = packetWrapper.get((Type<Integer>)Type.INT, 1);
                        z = packetWrapper.get((Type<Integer>)Type.INT, 2);
                        yaw3 = packetWrapper.get((Type<Byte>)Type.BYTE, 0);
                        pitch = packetWrapper.get((Type<Byte>)Type.BYTE, 1);
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
                final boolean player;
                final int size;
                int removed;
                int i;
                String key;
                boolean skip;
                double value;
                int modifiersize;
                ArrayList<Pair<Byte, Double>> modifiers;
                int j;
                UUID uuid;
                double amount;
                byte operation;
                this.handler(packetWrapper -> {
                    player = (packetWrapper.get((Type<Integer>)Type.VAR_INT, 0) == packetWrapper.user().get(EntityTracker.class).getPlayerId());
                    size = packetWrapper.get((Type<Integer>)Type.INT, 0);
                    removed = 0;
                    for (i = 0; i < size; ++i) {
                        key = packetWrapper.read(Type.STRING);
                        skip = !Protocol1_8TO1_9.VALID_ATTRIBUTES.contains(key);
                        value = packetWrapper.read((Type<Double>)Type.DOUBLE);
                        modifiersize = packetWrapper.read((Type<Integer>)Type.VAR_INT);
                        if (!skip) {
                            packetWrapper.write(Type.STRING, key);
                            packetWrapper.write(Type.DOUBLE, value);
                            packetWrapper.write(Type.VAR_INT, modifiersize);
                        }
                        else {
                            ++removed;
                        }
                        modifiers = new ArrayList<Pair<Byte, Double>>();
                        for (j = 0; j < modifiersize; ++j) {
                            uuid = packetWrapper.read(Type.UUID);
                            amount = packetWrapper.read((Type<Double>)Type.DOUBLE);
                            operation = packetWrapper.read((Type<Byte>)Type.BYTE);
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
                final int id;
                Levitation levitation;
                this.handler(packetWrapper -> {
                    id = packetWrapper.get((Type<Byte>)Type.BYTE, 0);
                    if (id > 23) {
                        packetWrapper.cancel();
                    }
                    if (id == 25) {
                        if (packetWrapper.get((Type<Integer>)Type.VAR_INT, 0) == packetWrapper.user().get(EntityTracker.class).getPlayerId()) {
                            levitation = packetWrapper.user().get(Levitation.class);
                            levitation.setActive(true);
                            levitation.setAmplifier(packetWrapper.get((Type<Byte>)Type.BYTE, 1));
                        }
                    }
                });
            }
        });
    }
}
