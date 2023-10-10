// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_8to1_9.packets;

import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import de.gerrygames.viarewind.ViaRewind;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
import java.util.List;
import com.viaversion.viaversion.api.type.types.version.Types1_8;
import com.viaversion.viaversion.api.minecraft.metadata.MetaType;
import com.viaversion.viaversion.api.minecraft.metadata.types.MetaType1_8;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import java.util.ArrayList;
import java.util.TimerTask;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage.Cooldown;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage.BlockPlaceDestroyTracker;
import de.gerrygames.viarewind.utils.PacketUtil;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.Protocol1_8TO1_9;
import com.viaversion.viaversion.api.minecraft.Position;
import io.netty.buffer.ByteBuf;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage.PlayerPosition;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_10Types;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage.EntityTracker;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.items.ItemRewriter;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import de.gerrygames.viarewind.utils.ChatUtil;
import com.viaversion.viaversion.libs.gson.JsonElement;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage.BossBarStorage;
import com.viaversion.viaversion.api.type.Type;
import java.util.UUID;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.protocols.protocol1_8.ServerboundPackets1_8;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.ServerboundPackets1_9;
import com.viaversion.viaversion.protocols.protocol1_8.ClientboundPackets1_8;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.ClientboundPackets1_9;
import com.viaversion.viaversion.api.protocol.Protocol;

public class PlayerPackets
{
    public static void register(final Protocol<ClientboundPackets1_9, ClientboundPackets1_8, ServerboundPackets1_9, ServerboundPackets1_8> protocol) {
        protocol.registerClientbound(ClientboundPackets1_9.BOSSBAR, null, new PacketRemapper() {
            @Override
            public void registerMap() {
                final UUID uuid;
                final int action;
                final BossBarStorage bossBarStorage;
                String title;
                this.handler(packetWrapper -> {
                    packetWrapper.cancel();
                    uuid = packetWrapper.read(Type.UUID);
                    action = packetWrapper.read((Type<Integer>)Type.VAR_INT);
                    bossBarStorage = packetWrapper.user().get(BossBarStorage.class);
                    if (action == 0) {
                        bossBarStorage.add(uuid, ChatUtil.jsonToLegacy(packetWrapper.read(Type.COMPONENT)), packetWrapper.read((Type<Float>)Type.FLOAT));
                        packetWrapper.read((Type<Object>)Type.VAR_INT);
                        packetWrapper.read((Type<Object>)Type.VAR_INT);
                        packetWrapper.read((Type<Object>)Type.UNSIGNED_BYTE);
                    }
                    else if (action == 1) {
                        bossBarStorage.remove(uuid);
                    }
                    else if (action == 2) {
                        bossBarStorage.updateHealth(uuid, packetWrapper.read((Type<Float>)Type.FLOAT));
                    }
                    else if (action == 3) {
                        title = ChatUtil.jsonToLegacy(packetWrapper.read(Type.COMPONENT));
                        bossBarStorage.updateTitle(uuid, title);
                    }
                });
            }
        });
        protocol.cancelClientbound(ClientboundPackets1_9.COOLDOWN);
        protocol.registerClientbound(ClientboundPackets1_9.PLUGIN_MESSAGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                final String channel;
                int size;
                int i;
                boolean has3Items;
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
                            packetWrapper.write(Type.ITEM, ItemRewriter.toClient(packetWrapper.read(Type.ITEM)));
                            packetWrapper.write(Type.ITEM, ItemRewriter.toClient(packetWrapper.read(Type.ITEM)));
                            has3Items = packetWrapper.passthrough((Type<Boolean>)Type.BOOLEAN);
                            if (has3Items) {
                                packetWrapper.write(Type.ITEM, ItemRewriter.toClient(packetWrapper.read(Type.ITEM)));
                            }
                            packetWrapper.passthrough((Type<Object>)Type.BOOLEAN);
                            packetWrapper.passthrough((Type<Object>)Type.INT);
                            packetWrapper.passthrough((Type<Object>)Type.INT);
                        }
                    }
                    else if (channel.equalsIgnoreCase("MC|BOpen")) {
                        packetWrapper.read((Type<Object>)Type.VAR_INT);
                    }
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_9.GAME_EVENT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.FLOAT);
                final int reason;
                this.handler(packetWrapper -> {
                    reason = packetWrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 0);
                    if (reason == 3) {
                        packetWrapper.user().get(EntityTracker.class).setPlayerGamemode(packetWrapper.get((Type<Float>)Type.FLOAT, 0).intValue());
                    }
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_9.JOIN_GAME, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.BYTE);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.STRING);
                this.map(Type.BOOLEAN);
                final EntityTracker tracker;
                this.handler(packetWrapper -> {
                    tracker = packetWrapper.user().get(EntityTracker.class);
                    tracker.setPlayerId(packetWrapper.get((Type<Integer>)Type.INT, 0));
                    tracker.setPlayerGamemode(packetWrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 0));
                    tracker.getClientEntityTypes().put(tracker.getPlayerId(), Entity1_10Types.EntityType.ENTITY_HUMAN);
                    return;
                });
                final ClientWorld world;
                this.handler(packetWrapper -> {
                    world = packetWrapper.user().get(ClientWorld.class);
                    world.setEnvironment(packetWrapper.get((Type<Byte>)Type.BYTE, 0));
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_9.PLAYER_POSITION, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.BYTE);
                final PlayerPosition pos;
                final int teleportId;
                final byte flags;
                double x;
                double y;
                double z;
                float yaw;
                float pitch;
                this.handler(packetWrapper -> {
                    pos = packetWrapper.user().get(PlayerPosition.class);
                    teleportId = packetWrapper.read((Type<Integer>)Type.VAR_INT);
                    pos.setConfirmId(teleportId);
                    flags = packetWrapper.get((Type<Byte>)Type.BYTE, 0);
                    x = packetWrapper.get((Type<Double>)Type.DOUBLE, 0);
                    y = packetWrapper.get((Type<Double>)Type.DOUBLE, 1);
                    z = packetWrapper.get((Type<Double>)Type.DOUBLE, 2);
                    yaw = packetWrapper.get((Type<Float>)Type.FLOAT, 0);
                    pitch = packetWrapper.get((Type<Float>)Type.FLOAT, 1);
                    packetWrapper.set(Type.BYTE, 0, (Byte)0);
                    if (flags != 0) {
                        if ((flags & 0x1) != 0x0) {
                            x += pos.getPosX();
                            packetWrapper.set(Type.DOUBLE, 0, x);
                        }
                        if ((flags & 0x2) != 0x0) {
                            y += pos.getPosY();
                            packetWrapper.set(Type.DOUBLE, 1, y);
                        }
                        if ((flags & 0x4) != 0x0) {
                            z += pos.getPosZ();
                            packetWrapper.set(Type.DOUBLE, 2, z);
                        }
                        if ((flags & 0x8) != 0x0) {
                            yaw += pos.getYaw();
                            packetWrapper.set(Type.FLOAT, 0, yaw);
                        }
                        if ((flags & 0x10) != 0x0) {
                            pitch += pos.getPitch();
                            packetWrapper.set(Type.FLOAT, 1, pitch);
                        }
                    }
                    pos.setPos(x, y, z);
                    pos.setYaw(yaw);
                    pos.setPitch(pitch);
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_9.RESPAWN, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.STRING);
                this.handler(packetWrapper -> packetWrapper.user().get(EntityTracker.class).setPlayerGamemode(packetWrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 1)));
                this.handler(packetWrapper -> {
                    packetWrapper.user().get(BossBarStorage.class).updateLocation();
                    packetWrapper.user().get(BossBarStorage.class).changeWorld();
                    return;
                });
                final ClientWorld world;
                this.handler(packetWrapper -> {
                    world = packetWrapper.user().get(ClientWorld.class);
                    world.setEnvironment(packetWrapper.get((Type<Integer>)Type.INT, 0));
                });
            }
        });
        protocol.registerServerbound(ServerboundPackets1_8.CHAT_MESSAGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                final String msg;
                PacketWrapper swapItems;
                this.handler(packetWrapper -> {
                    msg = packetWrapper.get(Type.STRING, 0);
                    if (msg.toLowerCase().startsWith("/offhand")) {
                        packetWrapper.cancel();
                        swapItems = PacketWrapper.create(19, null, packetWrapper.user());
                        swapItems.write(Type.VAR_INT, 6);
                        swapItems.write(Type.POSITION, new Position(0, (short)0, 0));
                        swapItems.write(Type.BYTE, (Byte)(-1));
                        PacketUtil.sendToServer(swapItems, Protocol1_8TO1_9.class, true, true);
                    }
                });
            }
        });
        protocol.registerServerbound(ServerboundPackets1_8.INTERACT_ENTITY, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.VAR_INT);
                final int type;
                this.handler(packetWrapper -> {
                    type = packetWrapper.get((Type<Integer>)Type.VAR_INT, 1);
                    if (type == 2) {
                        packetWrapper.passthrough((Type<Object>)Type.FLOAT);
                        packetWrapper.passthrough((Type<Object>)Type.FLOAT);
                        packetWrapper.passthrough((Type<Object>)Type.FLOAT);
                    }
                    if (type == 2 || type == 0) {
                        packetWrapper.write(Type.VAR_INT, 0);
                    }
                });
            }
        });
        protocol.registerServerbound(ServerboundPackets1_8.PLAYER_MOVEMENT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.BOOLEAN);
                final EntityTracker tracker;
                final int playerId;
                this.handler(packetWrapper -> {
                    tracker = packetWrapper.user().get(EntityTracker.class);
                    playerId = tracker.getPlayerId();
                    if (tracker.isInsideVehicle(playerId)) {
                        packetWrapper.cancel();
                    }
                });
            }
        });
        protocol.registerServerbound(ServerboundPackets1_8.PLAYER_POSITION, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.BOOLEAN);
                final PlayerPosition pos;
                this.handler(packetWrapper -> {
                    pos = packetWrapper.user().get(PlayerPosition.class);
                    if (pos.getConfirmId() != -1) {
                        return;
                    }
                    else {
                        pos.setPos(packetWrapper.get((Type<Double>)Type.DOUBLE, 0), packetWrapper.get((Type<Double>)Type.DOUBLE, 1), packetWrapper.get((Type<Double>)Type.DOUBLE, 2));
                        pos.setOnGround(packetWrapper.get((Type<Boolean>)Type.BOOLEAN, 0));
                        return;
                    }
                });
                this.handler(packetWrapper -> packetWrapper.user().get(BossBarStorage.class).updateLocation());
            }
        });
        protocol.registerServerbound(ServerboundPackets1_8.PLAYER_ROTATION, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.BOOLEAN);
                final PlayerPosition pos;
                this.handler(packetWrapper -> {
                    pos = packetWrapper.user().get(PlayerPosition.class);
                    if (pos.getConfirmId() != -1) {
                        return;
                    }
                    else {
                        pos.setYaw(packetWrapper.get((Type<Float>)Type.FLOAT, 0));
                        pos.setPitch(packetWrapper.get((Type<Float>)Type.FLOAT, 1));
                        pos.setOnGround(packetWrapper.get((Type<Boolean>)Type.BOOLEAN, 0));
                        return;
                    }
                });
                this.handler(packetWrapper -> packetWrapper.user().get(BossBarStorage.class).updateLocation());
            }
        });
        protocol.registerServerbound(ServerboundPackets1_8.PLAYER_POSITION_AND_ROTATION, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.BOOLEAN);
                final double x;
                final double y;
                final double z;
                final float yaw;
                final float pitch;
                final boolean onGround;
                final PlayerPosition pos;
                PacketWrapper confirmTeleport;
                this.handler(packetWrapper -> {
                    x = packetWrapper.get((Type<Double>)Type.DOUBLE, 0);
                    y = packetWrapper.get((Type<Double>)Type.DOUBLE, 1);
                    z = packetWrapper.get((Type<Double>)Type.DOUBLE, 2);
                    yaw = packetWrapper.get((Type<Float>)Type.FLOAT, 0);
                    pitch = packetWrapper.get((Type<Float>)Type.FLOAT, 1);
                    onGround = packetWrapper.get((Type<Boolean>)Type.BOOLEAN, 0);
                    pos = packetWrapper.user().get(PlayerPosition.class);
                    if (pos.getConfirmId() != -1) {
                        if (pos.getPosX() == x && pos.getPosY() == y && pos.getPosZ() == z && pos.getYaw() == yaw && pos.getPitch() == pitch) {
                            confirmTeleport = packetWrapper.create(0);
                            confirmTeleport.write(Type.VAR_INT, pos.getConfirmId());
                            PacketUtil.sendToServer(confirmTeleport, Protocol1_8TO1_9.class, true, true);
                            pos.setConfirmId(-1);
                        }
                    }
                    else {
                        pos.setPos(x, y, z);
                        pos.setYaw(yaw);
                        pos.setPitch(pitch);
                        pos.setOnGround(onGround);
                        packetWrapper.user().get(BossBarStorage.class).updateLocation();
                    }
                });
            }
        });
        protocol.registerServerbound(ServerboundPackets1_8.PLAYER_DIGGING, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.POSITION);
                final int state;
                BlockPlaceDestroyTracker tracker;
                BlockPlaceDestroyTracker tracker2;
                this.handler(packetWrapper -> {
                    state = packetWrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    if (state == 0) {
                        packetWrapper.user().get(BlockPlaceDestroyTracker.class).setMining(true);
                    }
                    else if (state == 2) {
                        tracker = packetWrapper.user().get(BlockPlaceDestroyTracker.class);
                        tracker.setMining(false);
                        tracker.setLastMining(System.currentTimeMillis() + 100L);
                        packetWrapper.user().get(Cooldown.class).setLastHit(0L);
                    }
                    else if (state == 1) {
                        tracker2 = packetWrapper.user().get(BlockPlaceDestroyTracker.class);
                        tracker2.setMining(false);
                        tracker2.setLastMining(0L);
                        packetWrapper.user().get(Cooldown.class).hit();
                    }
                });
            }
        });
        protocol.registerServerbound(ServerboundPackets1_8.PLAYER_BLOCK_PLACEMENT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.POSITION);
                this.map(Type.BYTE, Type.VAR_INT);
                this.map(Type.ITEM, Type.NOTHING);
                this.create(Type.VAR_INT, 0);
                this.map(Type.BYTE, Type.UNSIGNED_BYTE);
                this.map(Type.BYTE, Type.UNSIGNED_BYTE);
                this.map(Type.BYTE, Type.UNSIGNED_BYTE);
                PacketWrapper useItem;
                this.handler(packetWrapper -> {
                    if (packetWrapper.get((Type<Integer>)Type.VAR_INT, 0) == -1) {
                        packetWrapper.cancel();
                        useItem = PacketWrapper.create(29, null, packetWrapper.user());
                        useItem.write(Type.VAR_INT, 0);
                        PacketUtil.sendToServer(useItem, Protocol1_8TO1_9.class, true, true);
                    }
                    return;
                });
                this.handler(packetWrapper -> {
                    if (packetWrapper.get((Type<Integer>)Type.VAR_INT, 0) != -1) {
                        packetWrapper.user().get(BlockPlaceDestroyTracker.class).place();
                    }
                });
            }
        });
        protocol.registerServerbound(ServerboundPackets1_8.HELD_ITEM_CHANGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(packetWrapper -> packetWrapper.user().get(Cooldown.class).hit());
            }
        });
        protocol.registerServerbound(ServerboundPackets1_8.ANIMATION, new PacketRemapper() {
            @Override
            public void registerMap() {
                final PacketWrapper delayedPacket;
                this.handler(packetWrapper -> {
                    packetWrapper.cancel();
                    delayedPacket = PacketWrapper.create(26, null, packetWrapper.user());
                    delayedPacket.write(Type.VAR_INT, 0);
                    Protocol1_8TO1_9.TIMER.schedule(new TimerTask() {
                        final /* synthetic */ PacketWrapper val$delayedPacket;
                        
                        @Override
                        public void run() {
                            PacketUtil.sendToServer(this.val$delayedPacket, Protocol1_8TO1_9.class);
                        }
                    }, 5L);
                    return;
                });
                this.handler(packetWrapper -> {
                    packetWrapper.user().get(BlockPlaceDestroyTracker.class).updateMining();
                    packetWrapper.user().get(Cooldown.class).hit();
                });
            }
        });
        protocol.registerServerbound(ServerboundPackets1_8.ENTITY_ACTION, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.VAR_INT);
                this.map(Type.VAR_INT);
                final int action;
                PlayerPosition pos;
                PacketWrapper elytra;
                this.handler(packetWrapper -> {
                    action = packetWrapper.get((Type<Integer>)Type.VAR_INT, 1);
                    if (action == 6) {
                        packetWrapper.set(Type.VAR_INT, 1, 7);
                    }
                    else if (action == 0) {
                        pos = packetWrapper.user().get(PlayerPosition.class);
                        if (!pos.isOnGround()) {
                            elytra = PacketWrapper.create(20, null, packetWrapper.user());
                            elytra.write(Type.VAR_INT, (Integer)packetWrapper.get((Type<T>)Type.VAR_INT, 0));
                            elytra.write(Type.VAR_INT, 8);
                            elytra.write(Type.VAR_INT, 0);
                            PacketUtil.sendToServer(elytra, Protocol1_8TO1_9.class, true, false);
                        }
                    }
                });
            }
        });
        protocol.registerServerbound(ServerboundPackets1_8.STEER_VEHICLE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.UNSIGNED_BYTE);
                final EntityTracker tracker;
                final int playerId;
                final int vehicle;
                PacketWrapper steerBoat;
                float left;
                float forward;
                this.handler(packetWrapper -> {
                    tracker = packetWrapper.user().get(EntityTracker.class);
                    playerId = tracker.getPlayerId();
                    vehicle = tracker.getVehicle(playerId);
                    if (vehicle != -1 && tracker.getClientEntityTypes().get(vehicle) == Entity1_10Types.EntityType.BOAT) {
                        steerBoat = PacketWrapper.create(17, null, packetWrapper.user());
                        left = packetWrapper.get((Type<Float>)Type.FLOAT, 0);
                        forward = packetWrapper.get((Type<Float>)Type.FLOAT, 1);
                        steerBoat.write(Type.BOOLEAN, forward != 0.0f || left < 0.0f);
                        steerBoat.write(Type.BOOLEAN, forward != 0.0f || left > 0.0f);
                        PacketUtil.sendToServer(steerBoat, Protocol1_8TO1_9.class);
                    }
                });
            }
        });
        protocol.registerServerbound(ServerboundPackets1_8.UPDATE_SIGN, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.POSITION);
                int i;
                this.handler(packetWrapper -> {
                    for (i = 0; i < 4; ++i) {
                        packetWrapper.write(Type.STRING, ChatUtil.jsonToLegacy(packetWrapper.read(Type.COMPONENT)));
                    }
                });
            }
        });
        protocol.registerServerbound(ServerboundPackets1_8.TAB_COMPLETE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                this.handler(packetWrapper -> packetWrapper.write(Type.BOOLEAN, false));
                this.map(Type.OPTIONAL_POSITION);
            }
        });
        protocol.registerServerbound(ServerboundPackets1_8.CLIENT_SETTINGS, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                this.map(Type.BYTE);
                this.map(Type.BYTE, Type.VAR_INT);
                this.map(Type.BOOLEAN);
                this.map(Type.UNSIGNED_BYTE);
                this.create(Type.VAR_INT, 1);
                final short flags;
                final PacketWrapper updateSkin;
                final ArrayList<Metadata> metadata;
                this.handler(packetWrapper -> {
                    flags = packetWrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 0);
                    updateSkin = PacketWrapper.create(28, null, packetWrapper.user());
                    updateSkin.write(Type.VAR_INT, packetWrapper.user().get(EntityTracker.class).getPlayerId());
                    metadata = new ArrayList<Metadata>();
                    metadata.add(new Metadata(10, MetaType1_8.Byte, (byte)flags));
                    updateSkin.write(Types1_8.METADATA_LIST, metadata);
                    PacketUtil.sendPacket(updateSkin, Protocol1_8TO1_9.class);
                });
            }
        });
        protocol.registerServerbound(ServerboundPackets1_8.PLUGIN_MESSAGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                final String channel;
                Item book;
                CompoundTag tag;
                ListTag pages;
                int i;
                StringTag page;
                String value;
                String value2;
                Type<String> string;
                String channel2;
                final int n;
                final T t;
                this.handler(packetWrapper -> {
                    channel = packetWrapper.get(Type.STRING, 0);
                    if (channel.equalsIgnoreCase("MC|BEdit") || channel.equalsIgnoreCase("MC|BSign")) {
                        book = packetWrapper.passthrough(Type.ITEM);
                        book.setIdentifier(386);
                        tag = book.tag();
                        if (tag.contains("pages")) {
                            pages = tag.get("pages");
                            if (pages.size() > ViaRewind.getConfig().getMaxBookPages()) {
                                packetWrapper.user().disconnect("Too many book pages");
                            }
                            else {
                                i = 0;
                                while (i < pages.size()) {
                                    page = pages.get(i);
                                    value = page.getValue();
                                    if (value.length() > ViaRewind.getConfig().getMaxBookPageSize()) {
                                        packetWrapper.user().disconnect("Book page too large");
                                    }
                                    else {
                                        value2 = ChatUtil.jsonToLegacy(value);
                                        page.setValue(value2);
                                        ++i;
                                    }
                                }
                            }
                        }
                    }
                    else if (channel.equalsIgnoreCase("MC|AdvCdm")) {
                        string = Type.STRING;
                        channel2 = "MC|AdvCmd";
                        packetWrapper.set(string, n, (String)t);
                    }
                });
            }
        });
    }
}
