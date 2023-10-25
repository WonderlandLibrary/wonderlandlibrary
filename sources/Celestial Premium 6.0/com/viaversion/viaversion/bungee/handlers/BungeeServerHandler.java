/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.connection.ProxiedPlayer
 *  net.md_5.bungee.api.event.ServerConnectEvent
 *  net.md_5.bungee.api.event.ServerConnectedEvent
 *  net.md_5.bungee.api.event.ServerSwitchEvent
 *  net.md_5.bungee.api.plugin.Listener
 *  net.md_5.bungee.api.score.Team
 *  net.md_5.bungee.event.EventHandler
 *  net.md_5.bungee.protocol.packet.PluginMessage
 */
package com.viaversion.viaversion.bungee.handlers;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.ProtocolInfo;
import com.viaversion.viaversion.api.connection.StorableObject;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.data.entity.ClientEntityIdChangeListener;
import com.viaversion.viaversion.api.data.entity.EntityTracker;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.protocol.ProtocolPathEntry;
import com.viaversion.viaversion.api.protocol.ProtocolPipeline;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.bungee.service.ProtocolDetectorService;
import com.viaversion.viaversion.bungee.storage.BungeeStorage;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.packets.InventoryPackets;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.ClientboundPackets1_9;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.Protocol1_9To1_8;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.providers.EntityIdProvider;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.storage.EntityTracker1_9;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.score.Team;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.protocol.packet.PluginMessage;

public class BungeeServerHandler
implements Listener {
    private static Method getHandshake;
    private static Method getRelayMessages;
    private static Method setProtocol;
    private static Method getEntityMap;
    private static Method setVersion;
    private static Field entityRewrite;
    private static Field channelWrapper;

    @EventHandler(priority=120)
    public void onServerConnect(ServerConnectEvent e) {
        if (e.isCancelled()) {
            return;
        }
        UserConnection user = Via.getManager().getConnectionManager().getConnectedClient(e.getPlayer().getUniqueId());
        if (user == null) {
            return;
        }
        if (!user.has(BungeeStorage.class)) {
            user.put(new BungeeStorage(e.getPlayer()));
        }
        int protocolId = ProtocolDetectorService.getProtocolId(e.getTarget().getName());
        List<ProtocolPathEntry> protocols = Via.getManager().getProtocolManager().getProtocolPath(user.getProtocolInfo().getProtocolVersion(), protocolId);
        try {
            Object handshake = getHandshake.invoke((Object)e.getPlayer().getPendingConnection(), new Object[0]);
            setProtocol.invoke(handshake, protocols == null ? user.getProtocolInfo().getProtocolVersion() : protocolId);
        }
        catch (IllegalAccessException | InvocationTargetException e1) {
            e1.printStackTrace();
        }
    }

    @EventHandler(priority=-120)
    public void onServerConnected(ServerConnectedEvent e) {
        try {
            this.checkServerChange(e, Via.getManager().getConnectionManager().getConnectedClient(e.getPlayer().getUniqueId()));
        }
        catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @EventHandler(priority=-120)
    public void onServerSwitch(ServerSwitchEvent e) {
        int playerId;
        UserConnection userConnection = Via.getManager().getConnectionManager().getConnectedClient(e.getPlayer().getUniqueId());
        if (userConnection == null) {
            return;
        }
        try {
            playerId = Via.getManager().getProviders().get(EntityIdProvider.class).getEntityId(userConnection);
        }
        catch (Exception ex) {
            return;
        }
        for (EntityTracker tracker : userConnection.getEntityTrackers()) {
            tracker.setClientEntityId(playerId);
        }
        for (StorableObject object : userConnection.getStoredObjects().values()) {
            if (!(object instanceof ClientEntityIdChangeListener)) continue;
            ((ClientEntityIdChangeListener)((Object)object)).setClientEntityId(playerId);
        }
    }

    public void checkServerChange(ServerConnectedEvent e, UserConnection user) throws Exception {
        if (user == null) {
            return;
        }
        if (user.has(BungeeStorage.class)) {
            BungeeStorage storage = user.get(BungeeStorage.class);
            ProxiedPlayer player = storage.getPlayer();
            if (e.getServer() != null && !e.getServer().getInfo().getName().equals(storage.getCurrentServer())) {
                EntityTracker1_9 oldEntityTracker = (EntityTracker1_9)user.getEntityTracker(Protocol1_9To1_8.class);
                if (oldEntityTracker != null && oldEntityTracker.isAutoTeam() && oldEntityTracker.isTeamExists()) {
                    oldEntityTracker.sendTeamPacket(false, true);
                }
                String serverName = e.getServer().getInfo().getName();
                storage.setCurrentServer(serverName);
                int protocolId = ProtocolDetectorService.getProtocolId(serverName);
                if (protocolId <= ProtocolVersion.v1_8.getVersion() && storage.getBossbar() != null) {
                    if (user.getProtocolInfo().getPipeline().contains(Protocol1_9To1_8.class)) {
                        for (UUID uuid : storage.getBossbar()) {
                            PacketWrapper wrapper = PacketWrapper.create(ClientboundPackets1_9.BOSSBAR, null, user);
                            wrapper.write(Type.UUID, uuid);
                            wrapper.write(Type.VAR_INT, 1);
                            wrapper.send(Protocol1_9To1_8.class);
                        }
                    }
                    storage.getBossbar().clear();
                }
                ProtocolInfo info = user.getProtocolInfo();
                int previousServerProtocol = info.getServerProtocolVersion();
                List<ProtocolPathEntry> protocolPath = Via.getManager().getProtocolManager().getProtocolPath(info.getProtocolVersion(), protocolId);
                ProtocolPipeline pipeline = user.getProtocolInfo().getPipeline();
                user.clearStoredObjects();
                pipeline.cleanPipes();
                if (protocolPath == null) {
                    protocolId = info.getProtocolVersion();
                } else {
                    ArrayList<Protocol> protocols = new ArrayList<Protocol>(protocolPath.size());
                    for (ProtocolPathEntry entry : protocolPath) {
                        protocols.add(entry.getProtocol());
                    }
                    pipeline.add(protocols);
                }
                info.setServerProtocolVersion(protocolId);
                pipeline.add(Via.getManager().getProtocolManager().getBaseProtocol(protocolId));
                Object relayMessages = getRelayMessages.invoke((Object)e.getPlayer().getPendingConnection(), new Object[0]);
                for (ProtocolPathEntry message : (List)relayMessages) {
                    PluginMessage plMsg = (PluginMessage)message;
                    String channel = plMsg.getTag();
                    int id1_13 = ProtocolVersion.v1_13.getVersion();
                    if (previousServerProtocol != -1) {
                        String oldChannel = channel;
                        if (previousServerProtocol < id1_13 && protocolId >= id1_13) {
                            if ((channel = InventoryPackets.getNewPluginChannelId(channel)) == null) {
                                throw new RuntimeException(oldChannel + " found in relayMessages");
                            }
                            if (channel.equals("minecraft:register")) {
                                plMsg.setData(Arrays.stream(new String(plMsg.getData(), StandardCharsets.UTF_8).split("\u0000")).map(InventoryPackets::getNewPluginChannelId).filter(Objects::nonNull).collect(Collectors.joining("\u0000")).getBytes(StandardCharsets.UTF_8));
                            }
                        } else if (previousServerProtocol >= id1_13 && protocolId < id1_13) {
                            if ((channel = InventoryPackets.getOldPluginChannelId(channel)) == null) {
                                throw new RuntimeException(oldChannel + " found in relayMessages");
                            }
                            if (channel.equals("REGISTER")) {
                                plMsg.setData(Arrays.stream(new String(plMsg.getData(), StandardCharsets.UTF_8).split("\u0000")).map(InventoryPackets::getOldPluginChannelId).filter(Objects::nonNull).collect(Collectors.joining("\u0000")).getBytes(StandardCharsets.UTF_8));
                            }
                        }
                    }
                    plMsg.setTag(channel);
                }
                user.put(storage);
                user.setActive(protocolPath != null);
                for (Protocol protocol : pipeline.pipes()) {
                    protocol.init(user);
                }
                EntityTracker1_9 newTracker = (EntityTracker1_9)user.getEntityTracker(Protocol1_9To1_8.class);
                if (newTracker != null && Via.getConfig().isAutoTeam()) {
                    String currentTeam = null;
                    for (Team team : player.getScoreboard().getTeams()) {
                        if (!team.getPlayers().contains(info.getUsername())) continue;
                        currentTeam = team.getName();
                    }
                    newTracker.setAutoTeam(true);
                    if (currentTeam == null) {
                        newTracker.sendTeamPacket(true, true);
                        newTracker.setCurrentTeam("viaversion");
                    } else {
                        newTracker.setAutoTeam(Via.getConfig().isAutoTeam());
                        newTracker.setCurrentTeam(currentTeam);
                    }
                }
                Object wrapper = channelWrapper.get((Object)player);
                setVersion.invoke(wrapper, protocolId);
                Object entityMap = getEntityMap.invoke(null, protocolId);
                entityRewrite.set((Object)player, entityMap);
            }
        }
    }

    static {
        getEntityMap = null;
        setVersion = null;
        entityRewrite = null;
        channelWrapper = null;
        try {
            getHandshake = Class.forName("net.md_5.bungee.connection.InitialHandler").getDeclaredMethod("getHandshake", new Class[0]);
            getRelayMessages = Class.forName("net.md_5.bungee.connection.InitialHandler").getDeclaredMethod("getRelayMessages", new Class[0]);
            setProtocol = Class.forName("net.md_5.bungee.protocol.packet.Handshake").getDeclaredMethod("setProtocolVersion", Integer.TYPE);
            getEntityMap = Class.forName("net.md_5.bungee.entitymap.EntityMap").getDeclaredMethod("getEntityMap", Integer.TYPE);
            setVersion = Class.forName("net.md_5.bungee.netty.ChannelWrapper").getDeclaredMethod("setVersion", Integer.TYPE);
            channelWrapper = Class.forName("net.md_5.bungee.UserConnection").getDeclaredField("ch");
            channelWrapper.setAccessible(true);
            entityRewrite = Class.forName("net.md_5.bungee.UserConnection").getDeclaredField("entityRewrite");
            entityRewrite.setAccessible(true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

