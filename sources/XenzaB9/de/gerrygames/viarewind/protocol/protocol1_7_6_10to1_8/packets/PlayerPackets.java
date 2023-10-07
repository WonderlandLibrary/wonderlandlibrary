// 
// Decompiled by Procyon v0.6.0
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
import io.netty.buffer.Unpooled;
import java.nio.charset.StandardCharsets;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.items.ItemRewriter;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.PlayerAbilities;
import java.util.List;
import io.netty.buffer.ByteBuf;
import de.gerrygames.viarewind.utils.ChatUtil;
import com.viaversion.viaversion.libs.gson.JsonElement;
import java.util.UUID;
import com.viaversion.viaversion.api.protocol.Protocol;
import de.gerrygames.viarewind.utils.PacketUtil;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types.Types1_7_6_10;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.ClientboundPackets1_7;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.GameProfileStorage;
import com.viaversion.viaversion.api.minecraft.item.Item;
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
                this.handler(packetWrapper -> {
                    final EntityTracker tracker = packetWrapper.user().get(EntityTracker.class);
                    tracker.setGamemode(packetWrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 0));
                    tracker.setPlayerId(packetWrapper.get((Type<Integer>)Type.INT, 0));
                    tracker.getClientEntityTypes().put(tracker.getPlayerId(), Entity1_10Types.EntityType.ENTITY_HUMAN);
                    tracker.setDimension(packetWrapper.get((Type<Byte>)Type.BYTE, 0));
                    tracker.addPlayer(tracker.getPlayerId(), packetWrapper.user().getProtocolInfo().getUuid());
                    return;
                });
                this.handler(packetWrapper -> {
                    final ClientWorld world = packetWrapper.user().get(ClientWorld.class);
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
                this.handler(packetWrapper -> {
                    final int position = packetWrapper.read((Type<Byte>)Type.BYTE);
                    if (position == 2) {
                        packetWrapper.cancel();
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.SPAWN_POSITION, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(packetWrapper -> {
                    final Position position = packetWrapper.read(Type.POSITION);
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
                this.handler(packetWrapper -> {
                    final EntityTracker tracker = packetWrapper.user().get(EntityTracker.class);
                    tracker.setGamemode(packetWrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 1));
                    if (tracker.getDimension() != packetWrapper.get((Type<Integer>)Type.INT, 0)) {
                        tracker.setDimension(packetWrapper.get((Type<Integer>)Type.INT, 0));
                        tracker.clearEntities();
                        tracker.getClientEntityTypes().put(tracker.getPlayerId(), Entity1_10Types.EntityType.ENTITY_HUMAN);
                    }
                    return;
                });
                this.handler(packetWrapper -> {
                    final ClientWorld world = packetWrapper.user().get(ClientWorld.class);
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
                this.handler(packetWrapper -> {
                    final PlayerPosition playerPosition = packetWrapper.user().get(PlayerPosition.class);
                    playerPosition.setPositionPacketReceived(true);
                    final int flags = packetWrapper.read((Type<Byte>)Type.BYTE);
                    if ((flags & 0x1) == 0x1) {
                        final double x = packetWrapper.get((Type<Double>)Type.DOUBLE, 0);
                        final double x2 = x + playerPosition.getPosX();
                        packetWrapper.set(Type.DOUBLE, 0, x2);
                    }
                    double y = packetWrapper.get((Type<Double>)Type.DOUBLE, 1);
                    if ((flags & 0x2) == 0x2) {
                        y += playerPosition.getPosY();
                    }
                    playerPosition.setReceivedPosY(y);
                    final double y2 = y + 1.6200000047683716;
                    packetWrapper.set(Type.DOUBLE, 1, y2);
                    if ((flags & 0x4) == 0x4) {
                        final double z = packetWrapper.get((Type<Double>)Type.DOUBLE, 2);
                        final double z2 = z + playerPosition.getPosZ();
                        packetWrapper.set(Type.DOUBLE, 2, z2);
                    }
                    if ((flags & 0x8) == 0x8) {
                        final float yaw = packetWrapper.get((Type<Float>)Type.FLOAT, 0);
                        final float yaw2 = yaw + playerPosition.getYaw();
                        packetWrapper.set(Type.FLOAT, 0, yaw2);
                    }
                    if ((flags & 0x10) == 0x10) {
                        final float pitch = packetWrapper.get((Type<Float>)Type.FLOAT, 1);
                        final float pitch2 = pitch + playerPosition.getPitch();
                        packetWrapper.set(Type.FLOAT, 1, pitch2);
                    }
                    return;
                });
                this.handler(packetWrapper -> {
                    final PlayerPosition playerPosition2 = packetWrapper.user().get(PlayerPosition.class);
                    packetWrapper.write(Type.BOOLEAN, playerPosition2.isOnGround());
                    return;
                });
                this.handler(packetWrapper -> {
                    final EntityTracker tracker = packetWrapper.user().get(EntityTracker.class);
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
                this.handler(packetWrapper -> {
                    final int mode = packetWrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 0);
                    if (mode != 3) {
                        return;
                    }
                    else {
                        final int gamemode = packetWrapper.get((Type<Float>)Type.FLOAT, 0).intValue();
                        final EntityTracker tracker = packetWrapper.user().get(EntityTracker.class);
                        if (gamemode == 3 || tracker.getGamemode() == 3) {
                            final UUID myId = packetWrapper.user().getProtocolInfo().getUuid();
                            final Item[] equipment = new Item[4];
                            if (gamemode == 3) {
                                final GameProfileStorage.GameProfile profile = packetWrapper.user().get(GameProfileStorage.class).get(myId);
                                equipment[3] = profile.getSkull();
                            }
                            else {
                                for (int i = 0; i < equipment.length; ++i) {
                                    equipment[i] = tracker.getPlayerEquipment(myId, i);
                                }
                            }
                            for (int j = 0; j < equipment.length; ++j) {
                                final PacketWrapper setSlot = PacketWrapper.create(ClientboundPackets1_7.SET_SLOT, packetWrapper.user());
                                setSlot.write(Type.BYTE, (Byte)0);
                                setSlot.write(Type.SHORT, (short)(8 - j));
                                setSlot.write(Types1_7_6_10.COMPRESSED_NBT_ITEM, equipment[j]);
                                PacketUtil.sendPacket(setSlot, Protocol1_7_6_10TO1_8.class);
                            }
                        }
                        return;
                    }
                });
                this.handler(packetWrapper -> {
                    final int mode2 = packetWrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 0);
                    if (mode2 == 3) {
                        int gamemode2 = packetWrapper.get((Type<Float>)Type.FLOAT, 0).intValue();
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
                this.handler(packetWrapper -> {
                    final Position position = packetWrapper.read(Type.POSITION);
                    packetWrapper.write(Type.INT, position.getX());
                    packetWrapper.write(Type.INT, position.getY());
                    packetWrapper.write(Type.INT, position.getZ());
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.PLAYER_INFO, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(packetWrapper -> {
                    packetWrapper.cancel();
                    final int action = packetWrapper.read((Type<Integer>)Type.VAR_INT);
                    final int count = packetWrapper.read((Type<Integer>)Type.VAR_INT);
                    final GameProfileStorage gameProfileStorage = packetWrapper.user().get(GameProfileStorage.class);
                    for (int i = 0; i < count; ++i) {
                        final UUID uuid = packetWrapper.read(Type.UUID);
                        if (action == 0) {
                            final String name = packetWrapper.read(Type.STRING);
                            GameProfileStorage.GameProfile gameProfile = gameProfileStorage.get(uuid);
                            if (gameProfile == null) {
                                gameProfile = gameProfileStorage.put(uuid, name);
                            }
                            int propertyCount = packetWrapper.read((Type<Integer>)Type.VAR_INT);
                            while (true) {
                                propertyCount--;
                                final Object o;
                                if (o > 0) {
                                    final List<GameProfileStorage.Property> properties = gameProfile.properties;
                                    new GameProfileStorage.Property(packetWrapper.read(Type.STRING), packetWrapper.read(Type.STRING), packetWrapper.read((Type<Boolean>)Type.BOOLEAN) ? packetWrapper.read(Type.STRING) : null);
                                    final GameProfileStorage.Property property;
                                    properties.add(property);
                                }
                                else {
                                    break;
                                }
                            }
                            final int gamemode = packetWrapper.read((Type<Integer>)Type.VAR_INT);
                            final int ping = packetWrapper.read((Type<Integer>)Type.VAR_INT);
                            gameProfile.ping = ping;
                            gameProfile.gamemode = gamemode;
                            if (packetWrapper.read((Type<Boolean>)Type.BOOLEAN)) {
                                gameProfile.setDisplayName(ChatUtil.jsonToLegacy(packetWrapper.read(Type.COMPONENT)));
                            }
                            final PacketWrapper packet = PacketWrapper.create(56, null, packetWrapper.user());
                            packet.write(Type.STRING, gameProfile.name);
                            packet.write(Type.BOOLEAN, true);
                            packet.write(Type.SHORT, (short)ping);
                            PacketUtil.sendPacket(packet, Protocol1_7_6_10TO1_8.class);
                        }
                        else if (action == 1) {
                            final int gamemode2 = packetWrapper.read((Type<Integer>)Type.VAR_INT);
                            final GameProfileStorage.GameProfile gameProfile2 = gameProfileStorage.get(uuid);
                            if (gameProfile2 != null) {
                                if (gameProfile2.gamemode != gamemode2) {
                                    if (gamemode2 == 3 || gameProfile2.gamemode == 3) {
                                        final EntityTracker tracker = packetWrapper.user().get(EntityTracker.class);
                                        final int entityId = tracker.getPlayerEntityId(uuid);
                                        final boolean isOwnPlayer = entityId == tracker.getPlayerId();
                                        if (entityId != -1) {
                                            final Item[] equipment = new Item[isOwnPlayer ? 4 : 5];
                                            if (gamemode2 == 3) {
                                                equipment[isOwnPlayer ? 3 : 4] = gameProfile2.getSkull();
                                            }
                                            else {
                                                for (int j = 0; j < equipment.length; ++j) {
                                                    equipment[j] = tracker.getPlayerEquipment(uuid, j);
                                                }
                                            }
                                            for (short slot = 0; slot < equipment.length; ++slot) {
                                                final PacketWrapper equipmentPacket = PacketWrapper.create(ClientboundPackets1_7.ENTITY_EQUIPMENT, packetWrapper.user());
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
                            final int ping2 = packetWrapper.read((Type<Integer>)Type.VAR_INT);
                            final GameProfileStorage.GameProfile gameProfile3 = gameProfileStorage.get(uuid);
                            if (gameProfile3 != null) {
                                gameProfile3.ping = ping2;
                                final PacketWrapper packet2 = PacketWrapper.create(56, null, packetWrapper.user());
                                packet2.write(Type.STRING, gameProfile3.name);
                                packet2.write(Type.BOOLEAN, true);
                                packet2.write(Type.SHORT, (short)ping2);
                                PacketUtil.sendPacket(packet2, Protocol1_7_6_10TO1_8.class);
                            }
                        }
                        else if (action == 3) {
                            final String displayName = packetWrapper.read((Type<Boolean>)Type.BOOLEAN) ? ChatUtil.jsonToLegacy(packetWrapper.read(Type.COMPONENT)) : null;
                            final GameProfileStorage.GameProfile gameProfile4 = gameProfileStorage.get(uuid);
                            if (gameProfile4 != null) {
                                if (gameProfile4.displayName != null || displayName != null) {
                                    if ((gameProfile4.displayName == null && displayName != null) || (gameProfile4.displayName != null && displayName == null) || !gameProfile4.displayName.equals(displayName)) {
                                        gameProfile4.setDisplayName(displayName);
                                    }
                                }
                            }
                        }
                        else if (action == 4) {
                            final GameProfileStorage.GameProfile gameProfile5 = gameProfileStorage.remove(uuid);
                            if (gameProfile5 != null) {
                                final PacketWrapper packet3 = PacketWrapper.create(56, null, packetWrapper.user());
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
                this.handler(packetWrapper -> {
                    final byte flags = packetWrapper.get((Type<Byte>)Type.BYTE, 0);
                    final float flySpeed = packetWrapper.get((Type<Float>)Type.FLOAT, 0);
                    final float walkSpeed = packetWrapper.get((Type<Float>)Type.FLOAT, 1);
                    final PlayerAbilities abilities = packetWrapper.user().get(PlayerAbilities.class);
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
                this.handler(packetWrapper -> {
                    final String channel = packetWrapper.get(Type.STRING, 0);
                    if (channel.equalsIgnoreCase("MC|TrList")) {
                        packetWrapper.passthrough((Type<Object>)Type.INT);
                        int size;
                        if (packetWrapper.isReadable(Type.BYTE, 0)) {
                            size = packetWrapper.passthrough((Type<Byte>)Type.BYTE);
                        }
                        else {
                            size = packetWrapper.passthrough((Type<Short>)Type.UNSIGNED_BYTE);
                        }
                        for (int i = 0; i < size; ++i) {
                            final Item item = ItemRewriter.toClient(packetWrapper.read(Type.ITEM));
                            packetWrapper.write(Types1_7_6_10.COMPRESSED_NBT_ITEM, item);
                            final Item item2 = ItemRewriter.toClient(packetWrapper.read(Type.ITEM));
                            packetWrapper.write(Types1_7_6_10.COMPRESSED_NBT_ITEM, item2);
                            final boolean has3Items = packetWrapper.passthrough((Type<Boolean>)Type.BOOLEAN);
                            if (has3Items) {
                                final Item item3 = ItemRewriter.toClient(packetWrapper.read(Type.ITEM));
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
                    final ByteBuf newPacketBuf = Unpooled.buffer();
                    packetWrapper.writeToBuffer(newPacketBuf);
                    final PacketWrapper newWrapper = PacketWrapper.create(63, newPacketBuf, packetWrapper.user());
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
                this.handler(packetWrapper -> {
                    packetWrapper.cancel();
                    final EntityTracker tracker = packetWrapper.user().get(EntityTracker.class);
                    final int entityId = packetWrapper.read((Type<Integer>)Type.VAR_INT);
                    final int spectating = tracker.getSpectating();
                    if (spectating != entityId) {
                        tracker.setSpectating(entityId);
                    }
                });
            }
        });
        ((Protocol<ClientboundPackets1_8, ClientboundPackets1_7, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.TITLE, null, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(packetWrapper -> {
                    packetWrapper.cancel();
                    final TitleRenderProvider titleRenderProvider = Via.getManager().getProviders().get(TitleRenderProvider.class);
                    if (titleRenderProvider != null) {
                        final int action = packetWrapper.read((Type<Integer>)Type.VAR_INT);
                        final UUID uuid = Utils.getUUID(packetWrapper.user());
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
                this.handler(packetWrapper -> {
                    final ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
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
                this.handler(packetWrapper -> {
                    final String msg = packetWrapper.get(Type.STRING, 0);
                    final int gamemode = packetWrapper.user().get(EntityTracker.class).getGamemode();
                    if (gamemode == 3 && msg.toLowerCase().startsWith("/stp ")) {
                        final String username = msg.split(" ")[1];
                        final GameProfileStorage storage = packetWrapper.user().get(GameProfileStorage.class);
                        final GameProfileStorage.GameProfile profile = storage.get(username, true);
                        if (profile != null && profile.uuid != null) {
                            packetWrapper.cancel();
                            final PacketWrapper teleportPacket = PacketWrapper.create(24, null, packetWrapper.user());
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
                this.handler(packetWrapper -> {
                    final int mode = packetWrapper.get((Type<Integer>)Type.VAR_INT, 1);
                    if (mode == 0) {
                        final int entityId = packetWrapper.get((Type<Integer>)Type.VAR_INT, 0);
                        final EntityTracker tracker = packetWrapper.user().get(EntityTracker.class);
                        final EntityReplacement replacement = tracker.getEntityReplacement(entityId);
                        if (!(!(replacement instanceof ArmorStandReplacement))) {
                            final ArmorStandReplacement armorStand = (ArmorStandReplacement)replacement;
                            final AABB boundingBox = armorStand.getBoundingBox();
                            final PlayerPosition playerPosition = packetWrapper.user().get(PlayerPosition.class);
                            final Vector3d pos = new Vector3d(playerPosition.getPosX(), playerPosition.getPosY() + 1.8, playerPosition.getPosZ());
                            final double yaw = Math.toRadians(playerPosition.getYaw());
                            final double pitch = Math.toRadians(playerPosition.getPitch());
                            final Vector3d dir = new Vector3d(-Math.cos(pitch) * Math.sin(yaw), -Math.sin(pitch), Math.cos(pitch) * Math.cos(yaw));
                            final Ray3d ray = new Ray3d(pos, dir);
                            final Vector3d intersection = RayTracing.trace(ray, boundingBox, 5.0);
                            if (intersection != null) {
                                intersection.substract(boundingBox.getMin());
                                final int mode2 = 2;
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
                this.handler(packetWrapper -> {
                    final PlayerPosition playerPosition = packetWrapper.user().get(PlayerPosition.class);
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
                this.handler(packetWrapper -> {
                    final double x = packetWrapper.get((Type<Double>)Type.DOUBLE, 0);
                    double feetY = packetWrapper.get((Type<Double>)Type.DOUBLE, 1);
                    final double z = packetWrapper.get((Type<Double>)Type.DOUBLE, 2);
                    final PlayerPosition playerPosition = packetWrapper.user().get(PlayerPosition.class);
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
                this.handler(packetWrapper -> {
                    final PlayerPosition playerPosition = packetWrapper.user().get(PlayerPosition.class);
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
                this.handler(packetWrapper -> {
                    final double x = packetWrapper.get((Type<Double>)Type.DOUBLE, 0);
                    double feetY = packetWrapper.get((Type<Double>)Type.DOUBLE, 1);
                    final double z = packetWrapper.get((Type<Double>)Type.DOUBLE, 2);
                    final float yaw = packetWrapper.get((Type<Float>)Type.FLOAT, 0);
                    final float pitch = packetWrapper.get((Type<Float>)Type.FLOAT, 1);
                    final PlayerPosition playerPosition = packetWrapper.user().get(PlayerPosition.class);
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
                this.handler(packetWrapper -> {
                    final int x = packetWrapper.read((Type<Integer>)Type.INT);
                    final short y = packetWrapper.read((Type<Short>)Type.UNSIGNED_BYTE);
                    final int z = packetWrapper.read((Type<Integer>)Type.INT);
                    packetWrapper.write(Type.POSITION, new Position(x, y, z));
                });
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_7>)protocol).registerServerbound(ServerboundPackets1_7.PLAYER_BLOCK_PLACEMENT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(packetWrapper -> {
                    final int x = packetWrapper.read((Type<Integer>)Type.INT);
                    final short y = packetWrapper.read((Type<Short>)Type.UNSIGNED_BYTE);
                    final int z = packetWrapper.read((Type<Integer>)Type.INT);
                    packetWrapper.write(Type.POSITION, new Position(x, y, z));
                    packetWrapper.passthrough((Type<Object>)Type.BYTE);
                    final Item item = packetWrapper.read(Types1_7_6_10.COMPRESSED_NBT_ITEM);
                    final Item item2 = ItemRewriter.toServer(item);
                    packetWrapper.write(Type.ITEM, item2);
                    for (int i = 0; i < 3; ++i) {
                        packetWrapper.passthrough((Type<Object>)Type.BYTE);
                    }
                });
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_7>)protocol).registerServerbound(ServerboundPackets1_7.ANIMATION, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(packetWrapper -> {
                    final int entityId = packetWrapper.read((Type<Integer>)Type.INT);
                    final int animation = packetWrapper.read((Type<Byte>)Type.BYTE);
                    if (animation != 1) {
                        packetWrapper.cancel();
                        int animation2 = 0;
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
                        final PacketWrapper entityAction = PacketWrapper.create(11, null, packetWrapper.user());
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
                this.handler(packetWrapper -> {
                    final int action = packetWrapper.get((Type<Integer>)Type.VAR_INT, 1);
                    if (action == 3 || action == 4) {
                        final PlayerAbilities abilities = packetWrapper.user().get(PlayerAbilities.class);
                        abilities.setSprinting(action == 3);
                        final PacketWrapper abilitiesPacket = PacketWrapper.create(57, null, packetWrapper.user());
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
                this.handler(packetWrapper -> {
                    final boolean jump = packetWrapper.read((Type<Boolean>)Type.BOOLEAN);
                    final boolean unmount = packetWrapper.read((Type<Boolean>)Type.BOOLEAN);
                    short flags = 0;
                    if (jump) {
                        ++flags;
                    }
                    if (unmount) {
                        flags += 2;
                    }
                    packetWrapper.write(Type.UNSIGNED_BYTE, flags);
                    if (unmount) {
                        final EntityTracker tracker = packetWrapper.user().get(EntityTracker.class);
                        if (tracker.getSpectating() != tracker.getPlayerId()) {
                            final PacketWrapper sneakPacket = PacketWrapper.create(11, null, packetWrapper.user());
                            sneakPacket.write(Type.VAR_INT, tracker.getPlayerId());
                            sneakPacket.write(Type.VAR_INT, 0);
                            sneakPacket.write(Type.VAR_INT, 0);
                            final PacketWrapper unsneakPacket = PacketWrapper.create(11, null, packetWrapper.user());
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
                this.handler(packetWrapper -> {
                    final int x = packetWrapper.read((Type<Integer>)Type.INT);
                    final short y = packetWrapper.read((Type<Short>)Type.SHORT);
                    final int z = packetWrapper.read((Type<Integer>)Type.INT);
                    packetWrapper.write(Type.POSITION, new Position(x, y, z));
                    for (int i = 0; i < 4; ++i) {
                        final String line = packetWrapper.read(Type.STRING);
                        final String line2 = ChatUtil.legacyToJson(line);
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
                this.handler(packetWrapper -> {
                    final PlayerAbilities abilities = packetWrapper.user().get(PlayerAbilities.class);
                    if (abilities.isAllowFly()) {
                        final byte flags = packetWrapper.get((Type<Byte>)Type.BYTE, 0);
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
                this.handler(packetWrapper -> {
                    final String msg = packetWrapper.get(Type.STRING, 0);
                    if (msg.toLowerCase().startsWith("/stp ")) {
                        packetWrapper.cancel();
                        final String[] args = msg.split(" ");
                        if (args.length <= 2) {
                            final String prefix = (args.length == 1) ? "" : args[1];
                            final GameProfileStorage storage = packetWrapper.user().get(GameProfileStorage.class);
                            final List<GameProfileStorage.GameProfile> profiles = storage.getAllWithPrefix(prefix, true);
                            final PacketWrapper tabComplete = PacketWrapper.create(58, null, packetWrapper.user());
                            tabComplete.write(Type.VAR_INT, profiles.size());
                            profiles.iterator();
                            final Iterator iterator;
                            while (iterator.hasNext()) {
                                final GameProfileStorage.GameProfile profile = iterator.next();
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
                this.handler(packetWrapper -> {
                    final boolean cape = packetWrapper.read((Type<Boolean>)Type.BOOLEAN);
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
                //     at com.strobel.decompiler.languages.java.ast.NameVariables.generateNameForVariable(NameVariables.java:252)
                //     at com.strobel.decompiler.languages.java.ast.NameVariables.assignNamesToVariables(NameVariables.java:185)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.nameVariables(AstMethodBodyBuilder.java:1482)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.populateVariables(AstMethodBodyBuilder.java:1411)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:210)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:93)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:868)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:761)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:638)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:605)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:195)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:162)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1151)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:993)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:534)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:548)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:534)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:377)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:318)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:213)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:93)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:868)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:761)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:638)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:605)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:195)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:162)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:137)
                //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
                //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
                //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:333)
                //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:254)
                //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:144)
                // 
                throw new IllegalStateException("An error occurred while decompiling this method.");
            }
        });
    }
}
