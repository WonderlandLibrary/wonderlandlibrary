// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_9to1_8.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.PlayerMovementMapper;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.providers.MainHandProvider;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.ServerboundPackets1_9;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.ClientboundPackets1_9;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.providers.CompressionProvider;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.storage.ClientChunks;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.ItemRewriter;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.providers.CommandBlockProvider;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.chat.GameMode;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_10Types;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.storage.EntityTracker1_9;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.chat.ChatRewriter;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.protocols.protocol1_8.ClientboundPackets1_8;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.Protocol1_9To1_8;

public class PlayerPackets
{
    public static void register(final Protocol1_9To1_8 protocol) {
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.CHAT_MESSAGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING, Protocol1_9To1_8.FIX_JSON);
                this.map(Type.BYTE);
                JsonObject obj;
                this.handler(wrapper -> {
                    try {
                        obj = wrapper.get((Type<JsonObject>)Type.COMPONENT, 0);
                        ChatRewriter.toClient(obj, wrapper.user());
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.TAB_LIST, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING, Protocol1_9To1_8.FIX_JSON);
                this.map(Type.STRING, Protocol1_9To1_8.FIX_JSON);
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.DISCONNECT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING, Protocol1_9To1_8.FIX_JSON);
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.TITLE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                final int action;
                this.handler(wrapper -> {
                    action = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    if (action == 0 || action == 1) {
                        Protocol1_9To1_8.FIX_JSON.write(wrapper, wrapper.read(Type.STRING));
                    }
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
                this.map(Type.BYTE);
                this.handler(wrapper -> wrapper.write(Type.VAR_INT, 0));
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.TEAMS, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                this.map(Type.BYTE);
                final byte mode;
                String[] players;
                EntityTracker1_9 entityTracker;
                String myName;
                String teamName;
                final String[] array;
                int length;
                int i = 0;
                String player;
                EntityTracker1_9 entityTracker2;
                String teamName2;
                this.handler(wrapper -> {
                    mode = wrapper.get((Type<Byte>)Type.BYTE, 0);
                    if (mode == 0 || mode == 2) {
                        wrapper.passthrough(Type.STRING);
                        wrapper.passthrough(Type.STRING);
                        wrapper.passthrough(Type.STRING);
                        wrapper.passthrough((Type<Object>)Type.BYTE);
                        wrapper.passthrough(Type.STRING);
                        wrapper.write(Type.STRING, Via.getConfig().isPreventCollision() ? "never" : "");
                        wrapper.passthrough((Type<Object>)Type.BYTE);
                    }
                    if (mode == 0 || mode == 3 || mode == 4) {
                        players = wrapper.passthrough(Type.STRING_ARRAY);
                        entityTracker = (EntityTracker1_9)wrapper.user().getEntityTracker(Protocol1_9To1_8.class);
                        myName = wrapper.user().getProtocolInfo().getUsername();
                        teamName = wrapper.get(Type.STRING, 0);
                        for (length = array.length; i < length; ++i) {
                            player = array[i];
                            if (entityTracker.isAutoTeam() && player.equalsIgnoreCase(myName)) {
                                if (mode == 4) {
                                    wrapper.send(Protocol1_9To1_8.class);
                                    wrapper.cancel();
                                    entityTracker.sendTeamPacket(true, true);
                                    entityTracker.setCurrentTeam("viaversion");
                                }
                                else {
                                    entityTracker.sendTeamPacket(false, true);
                                    entityTracker.setCurrentTeam(teamName);
                                }
                            }
                        }
                    }
                    if (mode == 1) {
                        entityTracker2 = (EntityTracker1_9)wrapper.user().getEntityTracker(Protocol1_9To1_8.class);
                        teamName2 = wrapper.get(Type.STRING, 0);
                        if (entityTracker2.isAutoTeam() && teamName2.equals(entityTracker2.getCurrentTeam())) {
                            wrapper.send(Protocol1_9To1_8.class);
                            wrapper.cancel();
                            entityTracker2.sendTeamPacket(true, true);
                            entityTracker2.setCurrentTeam("viaversion");
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.JOIN_GAME, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                final int entityId;
                final EntityTracker1_9 tracker;
                this.handler(wrapper -> {
                    entityId = wrapper.get((Type<Integer>)Type.INT, 0);
                    tracker = (EntityTracker1_9)wrapper.user().getEntityTracker(Protocol1_9To1_8.class);
                    tracker.addEntity(entityId, Entity1_10Types.EntityType.PLAYER);
                    tracker.setClientEntityId(entityId);
                    return;
                });
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.BYTE);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.STRING);
                this.map(Type.BOOLEAN);
                final EntityTracker1_9 tracker2;
                this.handler(wrapper -> {
                    tracker2 = (EntityTracker1_9)wrapper.user().getEntityTracker(Protocol1_9To1_8.class);
                    tracker2.setGameMode(GameMode.getById(wrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 0)));
                    return;
                });
                final ClientWorld clientWorld;
                final int dimensionId;
                this.handler(wrapper -> {
                    clientWorld = wrapper.user().get(ClientWorld.class);
                    dimensionId = wrapper.get((Type<Byte>)Type.BYTE, 0);
                    clientWorld.setEnvironment(dimensionId);
                    return;
                });
                final CommandBlockProvider provider;
                this.handler(wrapper -> {
                    provider = Via.getManager().getProviders().get(CommandBlockProvider.class);
                    provider.sendPermission(wrapper.user());
                    return;
                });
                final EntityTracker1_9 entityTracker;
                this.handler(wrapper -> {
                    entityTracker = (EntityTracker1_9)wrapper.user().getEntityTracker(Protocol1_9To1_8.class);
                    if (Via.getConfig().isAutoTeam()) {
                        entityTracker.setAutoTeam(true);
                        wrapper.send(Protocol1_9To1_8.class);
                        wrapper.cancel();
                        entityTracker.sendTeamPacket(true, true);
                        entityTracker.setCurrentTeam("viaversion");
                    }
                    else {
                        entityTracker.setAutoTeam(false);
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.PLAYER_INFO, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.VAR_INT);
                final int action;
                int count;
                int i;
                int properties;
                int j;
                boolean isSigned;
                boolean hasDisplayName;
                boolean hasDisplayName2;
                this.handler(wrapper -> {
                    action = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    for (count = wrapper.get((Type<Integer>)Type.VAR_INT, 1), i = 0; i < count; ++i) {
                        wrapper.passthrough(Type.UUID);
                        if (action == 0) {
                            wrapper.passthrough(Type.STRING);
                            for (properties = wrapper.passthrough((Type<Integer>)Type.VAR_INT), j = 0; j < properties; ++j) {
                                wrapper.passthrough(Type.STRING);
                                wrapper.passthrough(Type.STRING);
                                isSigned = wrapper.passthrough((Type<Boolean>)Type.BOOLEAN);
                                if (isSigned) {
                                    wrapper.passthrough(Type.STRING);
                                }
                            }
                            wrapper.passthrough((Type<Object>)Type.VAR_INT);
                            wrapper.passthrough((Type<Object>)Type.VAR_INT);
                            hasDisplayName = wrapper.passthrough((Type<Boolean>)Type.BOOLEAN);
                            if (hasDisplayName) {
                                Protocol1_9To1_8.FIX_JSON.write(wrapper, wrapper.read(Type.STRING));
                            }
                        }
                        else if (action == 1 || action == 2) {
                            wrapper.passthrough((Type<Object>)Type.VAR_INT);
                        }
                        else if (action == 3) {
                            hasDisplayName2 = wrapper.passthrough((Type<Boolean>)Type.BOOLEAN);
                            if (hasDisplayName2) {
                                Protocol1_9To1_8.FIX_JSON.write(wrapper, wrapper.read(Type.STRING));
                            }
                        }
                        else if (action == 4) {}
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.PLUGIN_MESSAGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                final String name;
                Short size;
                int i;
                Item item1;
                Item item2;
                boolean present;
                Item item3;
                this.handler(wrapper -> {
                    name = wrapper.get(Type.STRING, 0);
                    if (name.equalsIgnoreCase("MC|BOpen")) {
                        wrapper.read(Type.REMAINING_BYTES);
                        wrapper.write(Type.VAR_INT, 0);
                    }
                    if (name.equalsIgnoreCase("MC|TrList")) {
                        wrapper.passthrough((Type<Object>)Type.INT);
                        for (size = wrapper.passthrough((Type<Short>)Type.UNSIGNED_BYTE), i = 0; i < size; ++i) {
                            item1 = wrapper.passthrough(Type.ITEM);
                            ItemRewriter.toClient(item1);
                            item2 = wrapper.passthrough(Type.ITEM);
                            ItemRewriter.toClient(item2);
                            present = wrapper.passthrough((Type<Boolean>)Type.BOOLEAN);
                            if (present) {
                                item3 = wrapper.passthrough(Type.ITEM);
                                ItemRewriter.toClient(item3);
                            }
                            wrapper.passthrough((Type<Object>)Type.BOOLEAN);
                            wrapper.passthrough((Type<Object>)Type.INT);
                            wrapper.passthrough((Type<Object>)Type.INT);
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.RESPAWN, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.STRING);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final ClientWorld clientWorld = wrapper.user().get(ClientWorld.class);
                        final int dimensionId = wrapper.get((Type<Integer>)Type.INT, 0);
                        clientWorld.setEnvironment(dimensionId);
                    }
                });
                final int gamemode;
                final EntityTracker1_9 tracker;
                this.handler(wrapper -> {
                    wrapper.user().get(ClientChunks.class).getLoadedChunks().clear();
                    gamemode = wrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 0);
                    tracker = (EntityTracker1_9)wrapper.user().getEntityTracker(Protocol1_9To1_8.class);
                    tracker.setGameMode(GameMode.getById(gamemode));
                    return;
                });
                final CommandBlockProvider provider;
                this.handler(wrapper -> {
                    provider = Via.getManager().getProviders().get(CommandBlockProvider.class);
                    provider.sendPermission(wrapper.user());
                    provider.unloadChunks(wrapper.user());
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.GAME_EVENT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.FLOAT);
                final short reason;
                int gamemode;
                EntityTracker1_9 tracker;
                this.handler(wrapper -> {
                    reason = wrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 0);
                    if (reason == 3) {
                        gamemode = wrapper.get((Type<Float>)Type.FLOAT, 0).intValue();
                        tracker = (EntityTracker1_9)wrapper.user().getEntityTracker(Protocol1_9To1_8.class);
                        tracker.setGameMode(GameMode.getById(gamemode));
                    }
                    else if (reason == 4) {
                        wrapper.set(Type.FLOAT, 0, 1.0f);
                    }
                });
            }
        });
        ((Protocol<ClientboundPackets1_8, ClientboundPackets1_9, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.SET_COMPRESSION, null, new PacketRemapper() {
            @Override
            public void registerMap() {
                final CompressionProvider provider;
                this.handler(wrapper -> {
                    wrapper.cancel();
                    provider = Via.getManager().getProviders().get(CompressionProvider.class);
                    provider.handlePlayCompression(wrapper.user(), wrapper.read((Type<Integer>)Type.VAR_INT));
                });
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_9>)protocol).registerServerbound(ServerboundPackets1_9.TAB_COMPLETE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                this.map(Type.BOOLEAN, Type.NOTHING);
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_9>)protocol).registerServerbound(ServerboundPackets1_9.CLIENT_SETTINGS, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                this.map(Type.BYTE);
                this.map(Type.VAR_INT, Type.BYTE);
                this.map(Type.BOOLEAN);
                this.map(Type.UNSIGNED_BYTE);
                final int hand;
                this.handler(wrapper -> {
                    hand = wrapper.read((Type<Integer>)Type.VAR_INT);
                    if (Via.getConfig().isLeftHandedHandling() && hand == 0) {
                        wrapper.set(Type.UNSIGNED_BYTE, 0, (short)(wrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 0) | 0x80));
                    }
                    wrapper.sendToServer(Protocol1_9To1_8.class);
                    wrapper.cancel();
                    Via.getManager().getProviders().get(MainHandProvider.class).setMainHand(wrapper.user(), hand);
                });
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_9>)protocol).registerServerbound(ServerboundPackets1_9.ANIMATION, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT, Type.NOTHING);
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_9>)protocol).cancelServerbound(ServerboundPackets1_9.TELEPORT_CONFIRM);
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_9>)protocol).cancelServerbound(ServerboundPackets1_9.VEHICLE_MOVE);
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_9>)protocol).cancelServerbound(ServerboundPackets1_9.STEER_BOAT);
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_9>)protocol).registerServerbound(ServerboundPackets1_9.PLUGIN_MESSAGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                final String name;
                Item item;
                this.handler(wrapper -> {
                    name = wrapper.get(Type.STRING, 0);
                    if (name.equalsIgnoreCase("MC|BSign")) {
                        item = wrapper.passthrough(Type.ITEM);
                        if (item != null) {
                            item.setIdentifier(387);
                            ItemRewriter.rewriteBookToServer(item);
                        }
                    }
                    if (name.equalsIgnoreCase("MC|AutoCmd")) {
                        wrapper.set(Type.STRING, 0, "MC|AdvCdm");
                        wrapper.write(Type.BYTE, (Byte)0);
                        wrapper.passthrough((Type<Object>)Type.INT);
                        wrapper.passthrough((Type<Object>)Type.INT);
                        wrapper.passthrough((Type<Object>)Type.INT);
                        wrapper.passthrough(Type.STRING);
                        wrapper.passthrough((Type<Object>)Type.BOOLEAN);
                        wrapper.clearInputBuffer();
                    }
                    if (name.equalsIgnoreCase("MC|AdvCmd")) {
                        wrapper.set(Type.STRING, 0, "MC|AdvCdm");
                    }
                });
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_9>)protocol).registerServerbound(ServerboundPackets1_9.CLIENT_STATUS, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                final int action;
                EntityTracker1_9 tracker;
                this.handler(wrapper -> {
                    action = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    if (action == 2) {
                        tracker = (EntityTracker1_9)wrapper.user().getEntityTracker(Protocol1_9To1_8.class);
                        if (tracker.isBlocking()) {
                            if (!Via.getConfig().isShowShieldWhenSwordInHand()) {
                                tracker.setSecondHand(null);
                            }
                            tracker.setBlocking(false);
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_9>)protocol).registerServerbound(ServerboundPackets1_9.PLAYER_POSITION, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.BOOLEAN);
                this.handler(new PlayerMovementMapper());
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_9>)protocol).registerServerbound(ServerboundPackets1_9.PLAYER_POSITION_AND_ROTATION, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.BOOLEAN);
                this.handler(new PlayerMovementMapper());
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_9>)protocol).registerServerbound(ServerboundPackets1_9.PLAYER_ROTATION, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.BOOLEAN);
                this.handler(new PlayerMovementMapper());
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_9>)protocol).registerServerbound(ServerboundPackets1_9.PLAYER_MOVEMENT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.BOOLEAN);
                this.handler(new PlayerMovementMapper());
            }
        });
    }
}
