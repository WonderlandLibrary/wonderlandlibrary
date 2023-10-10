// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.Windows;
import java.util.Iterator;
import com.viaversion.viaversion.libs.gson.JsonParser;
import de.gerrygames.viarewind.utils.math.AABB;
import de.gerrygames.viarewind.replacement.EntityReplacement;
import de.gerrygames.viarewind.utils.math.RayTracing;
import de.gerrygames.viarewind.utils.math.Ray3d;
import de.gerrygames.viarewind.utils.math.Vector3d;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.entityreplacements.ArmorStandReplacement;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.ServerboundPackets1_7;
import io.netty.buffer.ByteBufAllocator;
import de.gerrygames.viarewind.utils.Utils;
import com.viaversion.viaversion.api.Via;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.provider.TitleRenderProvider;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.ClientboundPackets1_7;
import io.netty.buffer.Unpooled;
import java.nio.charset.StandardCharsets;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.items.ItemRewriter;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.PlayerAbilities;
import java.util.List;
import de.gerrygames.viarewind.utils.ChatUtil;
import com.viaversion.viaversion.libs.gson.JsonElement;
import java.util.UUID;
import com.viaversion.viaversion.api.protocol.Protocol;
import de.gerrygames.viarewind.utils.PacketUtil;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types.Types1_7_6_10;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.minecraft.item.Item;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.GameProfileStorage;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.PlayerPosition;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.connection.StorableObject;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.Scoreboard;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_10Types;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.EntityTracker;
import de.gerrygames.viarewind.ViaRewind;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.protocols.protocol1_8.ClientboundPackets1_8;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.Protocol1_7_6_10TO1_8;

public class PlayerPackets
{
    public static void register(final Protocol1_7_6_10TO1_8 protocol) {
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.JOIN_GAME, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.BYTE);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.STRING);
                this.map(Type.BOOLEAN, Type.NOTHING);
                this.handler(packetWrapper -> {
                    if (!ViaRewind.getConfig().isReplaceAdventureMode()) {
                        return;
                    }
                    else {
                        if (packetWrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 0) == 2) {
                            packetWrapper.set(Type.UNSIGNED_BYTE, 0, (Short)0);
                        }
                        return;
                    }
                });
                final EntityTracker tracker;
                this.handler(packetWrapper -> {
                    tracker = packetWrapper.user().get(EntityTracker.class);
                    tracker.setGamemode(packetWrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 0));
                    tracker.setPlayerId(packetWrapper.get((Type<Integer>)Type.INT, 0));
                    tracker.getClientEntityTypes().put(tracker.getPlayerId(), Entity1_10Types.EntityType.ENTITY_HUMAN);
                    tracker.setDimension(packetWrapper.get((Type<Byte>)Type.BYTE, 0));
                    return;
                });
                final ClientWorld world;
                this.handler(packetWrapper -> {
                    world = packetWrapper.user().get(ClientWorld.class);
                    world.setEnvironment(packetWrapper.get((Type<Byte>)Type.BYTE, 0));
                    return;
                });
                this.handler(wrapper -> wrapper.user().put(new Scoreboard(wrapper.user())));
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.CHAT_MESSAGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.COMPONENT);
                final int position;
                this.handler(packetWrapper -> {
                    position = packetWrapper.read((Type<Byte>)Type.BYTE);
                    if (position == 2) {
                        packetWrapper.cancel();
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.SPAWN_POSITION, new PacketRemapper() {
            @Override
            public void registerMap() {
                final Position position;
                this.handler(packetWrapper -> {
                    position = packetWrapper.read(Type.POSITION);
                    packetWrapper.write(Type.INT, position.getX());
                    packetWrapper.write(Type.INT, position.getY());
                    packetWrapper.write(Type.INT, position.getZ());
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.UPDATE_HEALTH, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.FLOAT);
                this.map(Type.VAR_INT, Type.SHORT);
                this.map(Type.FLOAT);
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.RESPAWN, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.STRING);
                this.handler(packetWrapper -> {
                    if (!ViaRewind.getConfig().isReplaceAdventureMode()) {
                        return;
                    }
                    else {
                        if (packetWrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 1) == 2) {
                            packetWrapper.set(Type.UNSIGNED_BYTE, 1, (Short)0);
                        }
                        return;
                    }
                });
                final EntityTracker tracker;
                this.handler(packetWrapper -> {
                    tracker = packetWrapper.user().get(EntityTracker.class);
                    tracker.setGamemode(packetWrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 1));
                    if (tracker.getDimension() != packetWrapper.get((Type<Integer>)Type.INT, 0)) {
                        tracker.setDimension(packetWrapper.get((Type<Integer>)Type.INT, 0));
                        tracker.clearEntities();
                        tracker.getClientEntityTypes().put(tracker.getPlayerId(), Entity1_10Types.EntityType.ENTITY_HUMAN);
                    }
                    return;
                });
                final ClientWorld world;
                this.handler(packetWrapper -> {
                    world = packetWrapper.user().get(ClientWorld.class);
                    world.setEnvironment(packetWrapper.get((Type<Integer>)Type.INT, 0));
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.PLAYER_POSITION, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                final PlayerPosition playerPosition;
                final int flags;
                double x;
                double x2;
                double y;
                final double y2;
                double z;
                double z2;
                float yaw;
                float yaw2;
                float pitch;
                float pitch2;
                this.handler(packetWrapper -> {
                    playerPosition = packetWrapper.user().get(PlayerPosition.class);
                    playerPosition.setPositionPacketReceived(true);
                    flags = packetWrapper.read((Type<Byte>)Type.BYTE);
                    if ((flags & 0x1) == 0x1) {
                        x = packetWrapper.get((Type<Double>)Type.DOUBLE, 0);
                        x2 = x + playerPosition.getPosX();
                        packetWrapper.set(Type.DOUBLE, 0, x2);
                    }
                    y = packetWrapper.get((Type<Double>)Type.DOUBLE, 1);
                    if ((flags & 0x2) == 0x2) {
                        y += playerPosition.getPosY();
                    }
                    playerPosition.setReceivedPosY(y);
                    y2 = y + 1.6200000047683716;
                    packetWrapper.set(Type.DOUBLE, 1, y2);
                    if ((flags & 0x4) == 0x4) {
                        z = packetWrapper.get((Type<Double>)Type.DOUBLE, 2);
                        z2 = z + playerPosition.getPosZ();
                        packetWrapper.set(Type.DOUBLE, 2, z2);
                    }
                    if ((flags & 0x8) == 0x8) {
                        yaw = packetWrapper.get((Type<Float>)Type.FLOAT, 0);
                        yaw2 = yaw + playerPosition.getYaw();
                        packetWrapper.set(Type.FLOAT, 0, yaw2);
                    }
                    if ((flags & 0x10) == 0x10) {
                        pitch = packetWrapper.get((Type<Float>)Type.FLOAT, 1);
                        pitch2 = pitch + playerPosition.getPitch();
                        packetWrapper.set(Type.FLOAT, 1, pitch2);
                    }
                    return;
                });
                final PlayerPosition playerPosition2;
                this.handler(packetWrapper -> {
                    playerPosition2 = packetWrapper.user().get(PlayerPosition.class);
                    packetWrapper.write(Type.BOOLEAN, playerPosition2.isOnGround());
                    return;
                });
                final EntityTracker tracker;
                this.handler(packetWrapper -> {
                    tracker = packetWrapper.user().get(EntityTracker.class);
                    if (tracker.getSpectating() != tracker.getPlayerId()) {
                        packetWrapper.cancel();
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.SET_EXPERIENCE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.FLOAT);
                this.map(Type.VAR_INT, Type.SHORT);
                this.map(Type.VAR_INT, Type.SHORT);
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.GAME_EVENT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.FLOAT);
                final int mode;
                int gamemode;
                EntityTracker tracker;
                UUID uuid;
                GameProfileStorage.GameProfile profile;
                Item[] equipment;
                int i;
                PacketWrapper setSlot;
                this.handler(packetWrapper -> {
                    mode = packetWrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 0);
                    if (mode != 3) {
                        return;
                    }
                    else {
                        gamemode = packetWrapper.get((Type<Float>)Type.FLOAT, 0).intValue();
                        tracker = packetWrapper.user().get(EntityTracker.class);
                        if (gamemode == 3 || tracker.getGamemode() == 3) {
                            uuid = packetWrapper.user().getProtocolInfo().getUuid();
                            if (gamemode == 3) {
                                profile = packetWrapper.user().get(GameProfileStorage.class).get(uuid);
                                equipment = new Item[] { null, null, null, null, profile.getSkull() };
                            }
                            else {
                                equipment = tracker.getPlayerEquipment(uuid);
                                if (equipment == null) {
                                    equipment = new Item[5];
                                }
                            }
                            for (i = 1; i < 5; ++i) {
                                setSlot = PacketWrapper.create(47, null, packetWrapper.user());
                                setSlot.write(Type.BYTE, (Byte)0);
                                setSlot.write(Type.SHORT, (short)(9 - i));
                                setSlot.write(Types1_7_6_10.COMPRESSED_NBT_ITEM, equipment[i]);
                                PacketUtil.sendPacket(setSlot, Protocol1_7_6_10TO1_8.class);
                            }
                        }
                        return;
                    }
                });
                final int mode2;
                int gamemode2;
                this.handler(packetWrapper -> {
                    mode2 = packetWrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 0);
                    if (mode2 == 3) {
                        gamemode2 = packetWrapper.get((Type<Float>)Type.FLOAT, 0).intValue();
                        if (gamemode2 == 2 && ViaRewind.getConfig().isReplaceAdventureMode()) {
                            gamemode2 = 0;
                            packetWrapper.set(Type.FLOAT, 0, 0.0f);
                        }
                        packetWrapper.user().get(EntityTracker.class).setGamemode(gamemode2);
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.OPEN_SIGN_EDITOR, new PacketRemapper() {
            @Override
            public void registerMap() {
                final Position position;
                this.handler(packetWrapper -> {
                    position = packetWrapper.read(Type.POSITION);
                    packetWrapper.write(Type.INT, position.getX());
                    packetWrapper.write(Type.INT, position.getY());
                    packetWrapper.write(Type.INT, position.getZ());
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.PLAYER_INFO, new PacketRemapper() {
            @Override
            public void registerMap() {
                final int action;
                final int count;
                final GameProfileStorage gameProfileStorage;
                int i;
                UUID uuid;
                String name;
                GameProfileStorage.GameProfile gameProfile;
                int propertyCount;
                final Object o;
                List<GameProfileStorage.Property> properties;
                final GameProfileStorage.Property property;
                int gamemode;
                int ping;
                PacketWrapper packet;
                int gamemode2;
                GameProfileStorage.GameProfile gameProfile2;
                EntityTracker tracker;
                int entityId;
                Item[] equipment;
                short slot;
                PacketWrapper equipmentPacket;
                int ping2;
                GameProfileStorage.GameProfile gameProfile3;
                PacketWrapper packet2;
                String displayName;
                GameProfileStorage.GameProfile gameProfile4;
                GameProfileStorage.GameProfile gameProfile5;
                PacketWrapper packet3;
                this.handler(packetWrapper -> {
                    packetWrapper.cancel();
                    action = packetWrapper.read((Type<Integer>)Type.VAR_INT);
                    count = packetWrapper.read((Type<Integer>)Type.VAR_INT);
                    gameProfileStorage = packetWrapper.user().get(GameProfileStorage.class);
                    for (i = 0; i < count; ++i) {
                        uuid = packetWrapper.read(Type.UUID);
                        if (action == 0) {
                            name = packetWrapper.read(Type.STRING);
                            gameProfile = gameProfileStorage.get(uuid);
                            if (gameProfile == null) {
                                gameProfile = gameProfileStorage.put(uuid, name);
                            }
                            propertyCount = packetWrapper.read((Type<Integer>)Type.VAR_INT);
                            while (true) {
                                propertyCount--;
                                if (o > 0) {
                                    properties = gameProfile.properties;
                                    new GameProfileStorage.Property(packetWrapper.read(Type.STRING), packetWrapper.read(Type.STRING), packetWrapper.read((Type<Boolean>)Type.BOOLEAN) ? packetWrapper.read(Type.STRING) : null);
                                    properties.add(property);
                                }
                                else {
                                    break;
                                }
                            }
                            gamemode = packetWrapper.read((Type<Integer>)Type.VAR_INT);
                            ping = packetWrapper.read((Type<Integer>)Type.VAR_INT);
                            gameProfile.ping = ping;
                            gameProfile.gamemode = gamemode;
                            if (packetWrapper.read((Type<Boolean>)Type.BOOLEAN)) {
                                gameProfile.setDisplayName(ChatUtil.jsonToLegacy(packetWrapper.read(Type.COMPONENT)));
                            }
                            packet = PacketWrapper.create(56, null, packetWrapper.user());
                            packet.write(Type.STRING, gameProfile.name);
                            packet.write(Type.BOOLEAN, true);
                            packet.write(Type.SHORT, (short)ping);
                            PacketUtil.sendPacket(packet, Protocol1_7_6_10TO1_8.class);
                        }
                        else if (action == 1) {
                            gamemode2 = packetWrapper.read((Type<Integer>)Type.VAR_INT);
                            gameProfile2 = gameProfileStorage.get(uuid);
                            if (gameProfile2 != null) {
                                if (gameProfile2.gamemode != gamemode2) {
                                    if (gamemode2 == 3 || gameProfile2.gamemode == 3) {
                                        tracker = packetWrapper.user().get(EntityTracker.class);
                                        entityId = tracker.getPlayerEntityId(uuid);
                                        if (entityId != -1) {
                                            if (gamemode2 == 3) {
                                                equipment = new Item[] { null, null, null, null, gameProfile2.getSkull() };
                                            }
                                            else {
                                                equipment = tracker.getPlayerEquipment(uuid);
                                                if (equipment == null) {
                                                    equipment = new Item[5];
                                                }
                                            }
                                            for (slot = 0; slot < 5; ++slot) {
                                                equipmentPacket = PacketWrapper.create(4, null, packetWrapper.user());
                                                equipmentPacket.write(Type.INT, entityId);
                                                equipmentPacket.write(Type.SHORT, slot);
                                                equipmentPacket.write(Types1_7_6_10.COMPRESSED_NBT_ITEM, equipment[slot]);
                                                PacketUtil.sendPacket(equipmentPacket, Protocol1_7_6_10TO1_8.class);
                                            }
                                        }
                                    }
                                    gameProfile2.gamemode = gamemode2;
                                }
                            }
                        }
                        else if (action == 2) {
                            ping2 = packetWrapper.read((Type<Integer>)Type.VAR_INT);
                            gameProfile3 = gameProfileStorage.get(uuid);
                            if (gameProfile3 != null) {
                                gameProfile3.ping = ping2;
                                packet2 = PacketWrapper.create(56, null, packetWrapper.user());
                                packet2.write(Type.STRING, gameProfile3.name);
                                packet2.write(Type.BOOLEAN, true);
                                packet2.write(Type.SHORT, (short)ping2);
                                PacketUtil.sendPacket(packet2, Protocol1_7_6_10TO1_8.class);
                            }
                        }
                        else if (action == 3) {
                            displayName = (packetWrapper.read((Type<Boolean>)Type.BOOLEAN) ? ChatUtil.jsonToLegacy(packetWrapper.read(Type.COMPONENT)) : null);
                            gameProfile4 = gameProfileStorage.get(uuid);
                            if (gameProfile4 != null) {
                                if (gameProfile4.displayName != null || displayName != null) {
                                    if ((gameProfile4.displayName == null && displayName != null) || (gameProfile4.displayName != null && displayName == null) || !gameProfile4.displayName.equals(displayName)) {
                                        gameProfile4.setDisplayName(displayName);
                                    }
                                }
                            }
                        }
                        else if (action == 4) {
                            gameProfile5 = gameProfileStorage.remove(uuid);
                            if (gameProfile5 != null) {
                                packet3 = PacketWrapper.create(56, null, packetWrapper.user());
                                packet3.write(Type.STRING, gameProfile5.name);
                                packet3.write(Type.BOOLEAN, false);
                                packet3.write(Type.SHORT, (short)gameProfile5.ping);
                                PacketUtil.sendPacket(packet3, Protocol1_7_6_10TO1_8.class);
                            }
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.PLAYER_ABILITIES, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.BYTE);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                final byte flags;
                final float flySpeed;
                final float walkSpeed;
                final PlayerAbilities abilities;
                this.handler(packetWrapper -> {
                    flags = packetWrapper.get((Type<Byte>)Type.BYTE, 0);
                    flySpeed = packetWrapper.get((Type<Float>)Type.FLOAT, 0);
                    walkSpeed = packetWrapper.get((Type<Float>)Type.FLOAT, 1);
                    abilities = packetWrapper.user().get(PlayerAbilities.class);
                    abilities.setInvincible((flags & 0x8) == 0x8);
                    abilities.setAllowFly((flags & 0x4) == 0x4);
                    abilities.setFlying((flags & 0x2) == 0x2);
                    abilities.setCreative((flags & 0x1) == 0x1);
                    abilities.setFlySpeed(flySpeed);
                    abilities.setWalkSpeed(walkSpeed);
                    if (abilities.isSprinting() && abilities.isFlying()) {
                        packetWrapper.set(Type.FLOAT, 0, abilities.getFlySpeed() * 2.0f);
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.PLUGIN_MESSAGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                final String channel;
                int size;
                int i;
                Item item;
                Item item2;
                boolean has3Items;
                Item item3;
                final ByteBuf newPacketBuf;
                final PacketWrapper newWrapper;
                this.handler(packetWrapper -> {
                    channel = packetWrapper.get(Type.STRING, 0);
                    if (channel.equalsIgnoreCase("MC|TrList")) {
                        packetWrapper.passthrough((Type<Object>)Type.INT);
                        if (packetWrapper.isReadable(Type.BYTE, 0)) {
                            size = packetWrapper.passthrough((Type<Byte>)Type.BYTE);
                        }
                        else {
                            size = packetWrapper.passthrough((Type<Short>)Type.UNSIGNED_BYTE);
                        }
                        for (i = 0; i < size; ++i) {
                            item = ItemRewriter.toClient(packetWrapper.read(Type.ITEM));
                            packetWrapper.write(Types1_7_6_10.COMPRESSED_NBT_ITEM, item);
                            item2 = ItemRewriter.toClient(packetWrapper.read(Type.ITEM));
                            packetWrapper.write(Types1_7_6_10.COMPRESSED_NBT_ITEM, item2);
                            has3Items = packetWrapper.passthrough((Type<Boolean>)Type.BOOLEAN);
                            if (has3Items) {
                                item3 = ItemRewriter.toClient(packetWrapper.read(Type.ITEM));
                                packetWrapper.write(Types1_7_6_10.COMPRESSED_NBT_ITEM, item3);
                            }
                            packetWrapper.passthrough((Type<Object>)Type.BOOLEAN);
                            packetWrapper.read((Type<Object>)Type.INT);
                            packetWrapper.read((Type<Object>)Type.INT);
                        }
                    }
                    else if (channel.equalsIgnoreCase("MC|Brand")) {
                        packetWrapper.write(Type.REMAINING_BYTES, packetWrapper.read(Type.STRING).getBytes(StandardCharsets.UTF_8));
                    }
                    packetWrapper.cancel();
                    packetWrapper.setId(-1);
                    newPacketBuf = Unpooled.buffer();
                    packetWrapper.writeToBuffer(newPacketBuf);
                    newWrapper = PacketWrapper.create(63, newPacketBuf, packetWrapper.user());
                    newWrapper.passthrough(Type.STRING);
                    if (newPacketBuf.readableBytes() <= 32767) {
                        newWrapper.write(Type.SHORT, (short)newPacketBuf.readableBytes());
                        newWrapper.send(Protocol1_7_6_10TO1_8.class);
                    }
                });
            }
        });
        ((Protocol<ClientboundPackets1_8, ClientboundPackets1_7, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.CAMERA, null, new PacketRemapper() {
            @Override
            public void registerMap() {
                final EntityTracker tracker;
                final int entityId;
                final int spectating;
                this.handler(packetWrapper -> {
                    packetWrapper.cancel();
                    tracker = packetWrapper.user().get(EntityTracker.class);
                    entityId = packetWrapper.read((Type<Integer>)Type.VAR_INT);
                    spectating = tracker.getSpectating();
                    if (spectating != entityId) {
                        tracker.setSpectating(entityId);
                    }
                });
            }
        });
        ((Protocol<ClientboundPackets1_8, ClientboundPackets1_7, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.TITLE, null, new PacketRemapper() {
            @Override
            public void registerMap() {
                final TitleRenderProvider titleRenderProvider;
                int action;
                UUID uuid;
                this.handler(packetWrapper -> {
                    packetWrapper.cancel();
                    titleRenderProvider = Via.getManager().getProviders().get(TitleRenderProvider.class);
                    if (titleRenderProvider != null) {
                        action = packetWrapper.read((Type<Integer>)Type.VAR_INT);
                        uuid = Utils.getUUID(packetWrapper.user());
                        switch (action) {
                            case 0: {
                                titleRenderProvider.setTitle(uuid, packetWrapper.read(Type.STRING));
                                break;
                            }
                            case 1: {
                                titleRenderProvider.setSubTitle(uuid, packetWrapper.read(Type.STRING));
                                break;
                            }
                            case 2: {
                                titleRenderProvider.setTimings(uuid, packetWrapper.read((Type<Integer>)Type.INT), packetWrapper.read((Type<Integer>)Type.INT), packetWrapper.read((Type<Integer>)Type.INT));
                                break;
                            }
                            case 3: {
                                titleRenderProvider.clear(uuid);
                                break;
                            }
                            case 4: {
                                titleRenderProvider.reset(uuid);
                                break;
                            }
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).cancelClientbound(ClientboundPackets1_8.TAB_LIST);
        ((Protocol<ClientboundPackets1_8, ClientboundPackets1_7, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.RESOURCE_PACK, ClientboundPackets1_7.PLUGIN_MESSAGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.create(Type.STRING, "MC|RPack");
                final ByteBuf buf;
                this.handler(packetWrapper -> {
                    buf = ByteBufAllocator.DEFAULT.buffer();
                    try {
                        Type.STRING.write(buf, packetWrapper.read(Type.STRING));
                        packetWrapper.write(Type.SHORT_BYTE_ARRAY, Type.REMAINING_BYTES.read(buf));
                    }
                    finally {
                        buf.release();
                    }
                    return;
                });
                this.map(Type.STRING, Type.NOTHING);
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_7>)protocol).registerServerbound(ServerboundPackets1_7.CHAT_MESSAGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                final String msg;
                final int gamemode;
                String username;
                GameProfileStorage storage;
                GameProfileStorage.GameProfile profile;
                PacketWrapper teleportPacket;
                this.handler(packetWrapper -> {
                    msg = packetWrapper.get(Type.STRING, 0);
                    gamemode = packetWrapper.user().get(EntityTracker.class).getGamemode();
                    if (gamemode == 3 && msg.toLowerCase().startsWith("/stp ")) {
                        username = msg.split(" ")[1];
                        storage = packetWrapper.user().get(GameProfileStorage.class);
                        profile = storage.get(username, true);
                        if (profile != null && profile.uuid != null) {
                            packetWrapper.cancel();
                            teleportPacket = PacketWrapper.create(24, null, packetWrapper.user());
                            teleportPacket.write(Type.UUID, profile.uuid);
                            PacketUtil.sendToServer(teleportPacket, Protocol1_7_6_10TO1_8.class, true, true);
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_7>)protocol).registerServerbound(ServerboundPackets1_7.INTERACT_ENTITY, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT, Type.VAR_INT);
                this.map(Type.BYTE, Type.VAR_INT);
                final int mode;
                int entityId;
                EntityTracker tracker;
                EntityReplacement replacement;
                ArmorStandReplacement armorStand;
                AABB boundingBox;
                PlayerPosition playerPosition;
                Vector3d pos;
                double yaw;
                double pitch;
                Vector3d dir;
                Ray3d ray;
                Vector3d intersection;
                int mode2;
                this.handler(packetWrapper -> {
                    mode = packetWrapper.get((Type<Integer>)Type.VAR_INT, 1);
                    if (mode == 0) {
                        entityId = packetWrapper.get((Type<Integer>)Type.VAR_INT, 0);
                        tracker = packetWrapper.user().get(EntityTracker.class);
                        replacement = tracker.getEntityReplacement(entityId);
                        if (!(!(replacement instanceof ArmorStandReplacement))) {
                            armorStand = (ArmorStandReplacement)replacement;
                            boundingBox = armorStand.getBoundingBox();
                            playerPosition = packetWrapper.user().get(PlayerPosition.class);
                            pos = new Vector3d(playerPosition.getPosX(), playerPosition.getPosY() + 1.8, playerPosition.getPosZ());
                            yaw = Math.toRadians(playerPosition.getYaw());
                            pitch = Math.toRadians(playerPosition.getPitch());
                            dir = new Vector3d(-Math.cos(pitch) * Math.sin(yaw), -Math.sin(pitch), Math.cos(pitch) * Math.cos(yaw));
                            ray = new Ray3d(pos, dir);
                            intersection = RayTracing.trace(ray, boundingBox, 5.0);
                            if (intersection != null) {
                                intersection.substract(boundingBox.getMin());
                                mode2 = 2;
                                packetWrapper.set(Type.VAR_INT, 1, mode2);
                                packetWrapper.write(Type.FLOAT, (float)intersection.getX());
                                packetWrapper.write(Type.FLOAT, (float)intersection.getY());
                                packetWrapper.write(Type.FLOAT, (float)intersection.getZ());
                            }
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_7>)protocol).registerServerbound(ServerboundPackets1_7.PLAYER_MOVEMENT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.BOOLEAN);
                final PlayerPosition playerPosition;
                this.handler(packetWrapper -> {
                    playerPosition = packetWrapper.user().get(PlayerPosition.class);
                    playerPosition.setOnGround(packetWrapper.get((Type<Boolean>)Type.BOOLEAN, 0));
                });
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_7>)protocol).registerServerbound(ServerboundPackets1_7.PLAYER_POSITION, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE, Type.NOTHING);
                this.map(Type.DOUBLE);
                this.map(Type.BOOLEAN);
                final double x;
                double feetY;
                final double z;
                final PlayerPosition playerPosition;
                this.handler(packetWrapper -> {
                    x = packetWrapper.get((Type<Double>)Type.DOUBLE, 0);
                    feetY = packetWrapper.get((Type<Double>)Type.DOUBLE, 1);
                    z = packetWrapper.get((Type<Double>)Type.DOUBLE, 2);
                    playerPosition = packetWrapper.user().get(PlayerPosition.class);
                    if (playerPosition.isPositionPacketReceived()) {
                        playerPosition.setPositionPacketReceived(false);
                        feetY -= 0.01;
                        packetWrapper.set(Type.DOUBLE, 1, feetY);
                    }
                    playerPosition.setOnGround(packetWrapper.get((Type<Boolean>)Type.BOOLEAN, 0));
                    playerPosition.setPos(x, feetY, z);
                });
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_7>)protocol).registerServerbound(ServerboundPackets1_7.PLAYER_ROTATION, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.BOOLEAN);
                final PlayerPosition playerPosition;
                this.handler(packetWrapper -> {
                    playerPosition = packetWrapper.user().get(PlayerPosition.class);
                    playerPosition.setYaw(packetWrapper.get((Type<Float>)Type.FLOAT, 0));
                    playerPosition.setPitch(packetWrapper.get((Type<Float>)Type.FLOAT, 1));
                    playerPosition.setOnGround(packetWrapper.get((Type<Boolean>)Type.BOOLEAN, 0));
                });
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_7>)protocol).registerServerbound(ServerboundPackets1_7.PLAYER_POSITION_AND_ROTATION, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE, Type.NOTHING);
                this.map(Type.DOUBLE);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.BOOLEAN);
                final double x;
                double feetY;
                final double z;
                final float yaw;
                final float pitch;
                final PlayerPosition playerPosition;
                this.handler(packetWrapper -> {
                    x = packetWrapper.get((Type<Double>)Type.DOUBLE, 0);
                    feetY = packetWrapper.get((Type<Double>)Type.DOUBLE, 1);
                    z = packetWrapper.get((Type<Double>)Type.DOUBLE, 2);
                    yaw = packetWrapper.get((Type<Float>)Type.FLOAT, 0);
                    pitch = packetWrapper.get((Type<Float>)Type.FLOAT, 1);
                    playerPosition = packetWrapper.user().get(PlayerPosition.class);
                    if (playerPosition.isPositionPacketReceived()) {
                        playerPosition.setPositionPacketReceived(false);
                        feetY = playerPosition.getReceivedPosY();
                        packetWrapper.set(Type.DOUBLE, 1, feetY);
                    }
                    playerPosition.setOnGround(packetWrapper.get((Type<Boolean>)Type.BOOLEAN, 0));
                    playerPosition.setPos(x, feetY, z);
                    playerPosition.setYaw(yaw);
                    playerPosition.setPitch(pitch);
                });
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_7>)protocol).registerServerbound(ServerboundPackets1_7.PLAYER_DIGGING, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                final int x;
                final short y;
                final int z;
                this.handler(packetWrapper -> {
                    x = packetWrapper.read((Type<Integer>)Type.INT);
                    y = packetWrapper.read((Type<Short>)Type.UNSIGNED_BYTE);
                    z = packetWrapper.read((Type<Integer>)Type.INT);
                    packetWrapper.write(Type.POSITION, new Position(x, y, z));
                });
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_7>)protocol).registerServerbound(ServerboundPackets1_7.PLAYER_BLOCK_PLACEMENT, new PacketRemapper() {
            @Override
            public void registerMap() {
                final int x;
                final short y;
                final int z;
                final Item item;
                final Item item2;
                int i;
                this.handler(packetWrapper -> {
                    x = packetWrapper.read((Type<Integer>)Type.INT);
                    y = packetWrapper.read((Type<Short>)Type.UNSIGNED_BYTE);
                    z = packetWrapper.read((Type<Integer>)Type.INT);
                    packetWrapper.write(Type.POSITION, new Position(x, y, z));
                    packetWrapper.passthrough((Type<Object>)Type.BYTE);
                    item = packetWrapper.read(Types1_7_6_10.COMPRESSED_NBT_ITEM);
                    item2 = ItemRewriter.toServer(item);
                    packetWrapper.write(Type.ITEM, item2);
                    for (i = 0; i < 3; ++i) {
                        packetWrapper.passthrough((Type<Object>)Type.BYTE);
                    }
                });
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_7>)protocol).registerServerbound(ServerboundPackets1_7.ANIMATION, new PacketRemapper() {
            @Override
            public void registerMap() {
                final int entityId;
                final int animation;
                int animation2 = 0;
                PacketWrapper entityAction;
                this.handler(packetWrapper -> {
                    entityId = packetWrapper.read((Type<Integer>)Type.INT);
                    animation = packetWrapper.read((Type<Byte>)Type.BYTE);
                    if (animation != 1) {
                        packetWrapper.cancel();
                        switch (animation) {
                            case 104: {
                                animation2 = 0;
                                break;
                            }
                            case 105: {
                                animation2 = 1;
                                break;
                            }
                            case 3: {
                                animation2 = 2;
                                break;
                            }
                            default: {
                                return;
                            }
                        }
                        entityAction = PacketWrapper.create(11, null, packetWrapper.user());
                        entityAction.write(Type.VAR_INT, entityId);
                        entityAction.write(Type.VAR_INT, animation2);
                        entityAction.write(Type.VAR_INT, 0);
                        PacketUtil.sendPacket(entityAction, Protocol1_7_6_10TO1_8.class, true, true);
                    }
                });
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_7>)protocol).registerServerbound(ServerboundPackets1_7.ENTITY_ACTION, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT, Type.VAR_INT);
                this.handler(packetWrapper -> packetWrapper.write(Type.VAR_INT, packetWrapper.read((Type<Byte>)Type.BYTE) - 1));
                this.map(Type.INT, Type.VAR_INT);
                final int action;
                PlayerAbilities abilities;
                PacketWrapper abilitiesPacket;
                this.handler(packetWrapper -> {
                    action = packetWrapper.get((Type<Integer>)Type.VAR_INT, 1);
                    if (action == 3 || action == 4) {
                        abilities = packetWrapper.user().get(PlayerAbilities.class);
                        abilities.setSprinting(action == 3);
                        abilitiesPacket = PacketWrapper.create(57, null, packetWrapper.user());
                        abilitiesPacket.write(Type.BYTE, abilities.getFlags());
                        abilitiesPacket.write(Type.FLOAT, abilities.isSprinting() ? (abilities.getFlySpeed() * 2.0f) : abilities.getFlySpeed());
                        abilitiesPacket.write(Type.FLOAT, abilities.getWalkSpeed());
                        PacketUtil.sendPacket(abilitiesPacket, Protocol1_7_6_10TO1_8.class);
                    }
                });
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_7>)protocol).registerServerbound(ServerboundPackets1_7.STEER_VEHICLE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                final boolean jump;
                final boolean unmount;
                short flags;
                EntityTracker tracker;
                PacketWrapper sneakPacket;
                PacketWrapper unsneakPacket;
                this.handler(packetWrapper -> {
                    jump = packetWrapper.read((Type<Boolean>)Type.BOOLEAN);
                    unmount = packetWrapper.read((Type<Boolean>)Type.BOOLEAN);
                    flags = 0;
                    if (jump) {
                        ++flags;
                    }
                    if (unmount) {
                        flags += 2;
                    }
                    packetWrapper.write(Type.UNSIGNED_BYTE, flags);
                    if (unmount) {
                        tracker = packetWrapper.user().get(EntityTracker.class);
                        if (tracker.getSpectating() != tracker.getPlayerId()) {
                            sneakPacket = PacketWrapper.create(11, null, packetWrapper.user());
                            sneakPacket.write(Type.VAR_INT, tracker.getPlayerId());
                            sneakPacket.write(Type.VAR_INT, 0);
                            sneakPacket.write(Type.VAR_INT, 0);
                            unsneakPacket = PacketWrapper.create(11, null, packetWrapper.user());
                            unsneakPacket.write(Type.VAR_INT, tracker.getPlayerId());
                            unsneakPacket.write(Type.VAR_INT, 1);
                            unsneakPacket.write(Type.VAR_INT, 0);
                            PacketUtil.sendToServer(sneakPacket, Protocol1_7_6_10TO1_8.class);
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_7>)protocol).registerServerbound(ServerboundPackets1_7.UPDATE_SIGN, new PacketRemapper() {
            @Override
            public void registerMap() {
                final int x;
                final short y;
                final int z;
                int i;
                String line;
                String line2;
                this.handler(packetWrapper -> {
                    x = packetWrapper.read((Type<Integer>)Type.INT);
                    y = packetWrapper.read((Type<Short>)Type.SHORT);
                    z = packetWrapper.read((Type<Integer>)Type.INT);
                    packetWrapper.write(Type.POSITION, new Position(x, y, z));
                    for (i = 0; i < 4; ++i) {
                        line = packetWrapper.read(Type.STRING);
                        line2 = ChatUtil.legacyToJson(line);
                        packetWrapper.write(Type.COMPONENT, JsonParser.parseString(line2));
                    }
                });
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_7>)protocol).registerServerbound(ServerboundPackets1_7.PLAYER_ABILITIES, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.BYTE);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                final PlayerAbilities abilities;
                byte flags;
                this.handler(packetWrapper -> {
                    abilities = packetWrapper.user().get(PlayerAbilities.class);
                    if (abilities.isAllowFly()) {
                        flags = packetWrapper.get((Type<Byte>)Type.BYTE, 0);
                        abilities.setFlying((flags & 0x2) == 0x2);
                    }
                    packetWrapper.set(Type.FLOAT, 0, abilities.getFlySpeed());
                });
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_7>)protocol).registerServerbound(ServerboundPackets1_7.TAB_COMPLETE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                this.create(Type.OPTIONAL_POSITION, null);
                final String msg;
                String[] args;
                String prefix;
                GameProfileStorage storage;
                List<GameProfileStorage.GameProfile> profiles;
                PacketWrapper tabComplete;
                final Iterator<GameProfileStorage.GameProfile> iterator;
                GameProfileStorage.GameProfile profile;
                this.handler(packetWrapper -> {
                    msg = packetWrapper.get(Type.STRING, 0);
                    if (msg.toLowerCase().startsWith("/stp ")) {
                        packetWrapper.cancel();
                        args = msg.split(" ");
                        if (args.length <= 2) {
                            prefix = ((args.length == 1) ? "" : args[1]);
                            storage = packetWrapper.user().get(GameProfileStorage.class);
                            profiles = storage.getAllWithPrefix(prefix, true);
                            tabComplete = PacketWrapper.create(58, null, packetWrapper.user());
                            tabComplete.write(Type.VAR_INT, profiles.size());
                            profiles.iterator();
                            while (iterator.hasNext()) {
                                profile = iterator.next();
                                tabComplete.write(Type.STRING, profile.name);
                            }
                            PacketUtil.sendPacket(tabComplete, Protocol1_7_6_10TO1_8.class);
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_7>)protocol).registerServerbound(ServerboundPackets1_7.CLIENT_SETTINGS, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.BOOLEAN);
                this.map(Type.BYTE, Type.NOTHING);
                final boolean cape;
                this.handler(packetWrapper -> {
                    cape = packetWrapper.read((Type<Boolean>)Type.BOOLEAN);
                    packetWrapper.write(Type.UNSIGNED_BYTE, (short)(cape ? 127 : 126));
                });
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_7>)protocol).registerServerbound(ServerboundPackets1_7.PLUGIN_MESSAGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     1: getstatic       com/viaversion/viaversion/api/type/Type.STRING:Lcom/viaversion/viaversion/api/type/Type;
                //     4: invokevirtual   de/gerrygames/viarewind/protocol/protocol1_7_6_10to1_8/packets/PlayerPackets$31.map:(Lcom/viaversion/viaversion/api/type/Type;)V
                //     7: aload_0         /* this */
                //     8: getstatic       com/viaversion/viaversion/api/type/Type.SHORT:Lcom/viaversion/viaversion/api/type/types/ShortType;
                //    11: getstatic       com/viaversion/viaversion/api/type/Type.NOTHING:Lcom/viaversion/viaversion/api/type/types/VoidType;
                //    14: invokevirtual   de/gerrygames/viarewind/protocol/protocol1_7_6_10to1_8/packets/PlayerPackets$31.map:(Lcom/viaversion/viaversion/api/type/Type;Lcom/viaversion/viaversion/api/type/Type;)V
                //    17: aload_0         /* this */
                //    18: invokedynamic   BootstrapMethod #0, handle:()Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;
                //    23: invokevirtual   de/gerrygames/viarewind/protocol/protocol1_7_6_10to1_8/packets/PlayerPackets$31.handler:(Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
                //    26: return         
                // 
                // The error that occurred was:
                // 
                // java.lang.NullPointerException
                //     at com.strobel.decompiler.languages.java.ast.NameVariables.generateNameForVariable(NameVariables.java:264)
                //     at com.strobel.decompiler.languages.java.ast.NameVariables.assignNamesToVariables(NameVariables.java:198)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:276)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1164)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:1009)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
                //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
                //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
                //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
                //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
                //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
                // 
                throw new IllegalStateException("An error occurred while decompiling this method.");
            }
        });
    }
}
