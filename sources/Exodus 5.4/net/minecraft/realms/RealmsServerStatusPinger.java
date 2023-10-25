/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.mojang.authlib.GameProfile
 *  org.apache.commons.lang3.ArrayUtils
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.realms;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.ServerStatusResponse;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.status.INetHandlerStatusClient;
import net.minecraft.network.status.client.C00PacketServerQuery;
import net.minecraft.network.status.client.C01PacketPing;
import net.minecraft.network.status.server.S00PacketServerInfo;
import net.minecraft.network.status.server.S01PacketPong;
import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsServerAddress;
import net.minecraft.realms.RealmsServerPing;
import net.minecraft.realms.RealmsSharedConstants;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RealmsServerStatusPinger {
    private final List<NetworkManager> connections = Collections.synchronizedList(Lists.newArrayList());
    private static final Logger LOGGER = LogManager.getLogger();

    public void pingServer(final String string, final RealmsServerPing realmsServerPing) throws UnknownHostException {
        if (string != null && !string.startsWith("0.0.0.0") && !string.isEmpty()) {
            RealmsServerAddress realmsServerAddress = RealmsServerAddress.parseString(string);
            final NetworkManager networkManager = NetworkManager.func_181124_a(InetAddress.getByName(realmsServerAddress.getHost()), realmsServerAddress.getPort(), false);
            this.connections.add(networkManager);
            networkManager.setNetHandler(new INetHandlerStatusClient(){
                private boolean field_154345_e = false;

                @Override
                public void handleServerInfo(S00PacketServerInfo s00PacketServerInfo) {
                    ServerStatusResponse serverStatusResponse = s00PacketServerInfo.getResponse();
                    if (serverStatusResponse.getPlayerCountData() != null) {
                        realmsServerPing.nrOfPlayers = String.valueOf(serverStatusResponse.getPlayerCountData().getOnlinePlayerCount());
                        if (ArrayUtils.isNotEmpty((Object[])serverStatusResponse.getPlayerCountData().getPlayers())) {
                            StringBuilder stringBuilder = new StringBuilder();
                            GameProfile[] gameProfileArray = serverStatusResponse.getPlayerCountData().getPlayers();
                            int n = gameProfileArray.length;
                            int n2 = 0;
                            while (n2 < n) {
                                GameProfile gameProfile = gameProfileArray[n2];
                                if (stringBuilder.length() > 0) {
                                    stringBuilder.append("\n");
                                }
                                stringBuilder.append(gameProfile.getName());
                                ++n2;
                            }
                            if (serverStatusResponse.getPlayerCountData().getPlayers().length < serverStatusResponse.getPlayerCountData().getOnlinePlayerCount()) {
                                if (stringBuilder.length() > 0) {
                                    stringBuilder.append("\n");
                                }
                                stringBuilder.append("... and ").append(serverStatusResponse.getPlayerCountData().getOnlinePlayerCount() - serverStatusResponse.getPlayerCountData().getPlayers().length).append(" more ...");
                            }
                            realmsServerPing.playerList = stringBuilder.toString();
                        }
                    } else {
                        realmsServerPing.playerList = "";
                    }
                    networkManager.sendPacket(new C01PacketPing(Realms.currentTimeMillis()));
                    this.field_154345_e = true;
                }

                @Override
                public void onDisconnect(IChatComponent iChatComponent) {
                    if (!this.field_154345_e) {
                        LOGGER.error("Can't ping " + string + ": " + iChatComponent.getUnformattedText());
                    }
                }

                @Override
                public void handlePong(S01PacketPong s01PacketPong) {
                    networkManager.closeChannel(new ChatComponentText("Finished"));
                }
            });
            try {
                networkManager.sendPacket(new C00Handshake(RealmsSharedConstants.NETWORK_PROTOCOL_VERSION, realmsServerAddress.getHost(), realmsServerAddress.getPort(), EnumConnectionState.STATUS));
                networkManager.sendPacket(new C00PacketServerQuery());
            }
            catch (Throwable throwable) {
                LOGGER.error((Object)throwable);
            }
        }
    }

    public void tick() {
        List<NetworkManager> list = this.connections;
        synchronized (list) {
            Iterator<NetworkManager> iterator = this.connections.iterator();
            while (iterator.hasNext()) {
                NetworkManager networkManager = iterator.next();
                if (networkManager.isChannelOpen()) {
                    networkManager.processReceivedPackets();
                    continue;
                }
                iterator.remove();
                networkManager.checkDisconnected();
            }
        }
    }

    public void removeAll() {
        List<NetworkManager> list = this.connections;
        synchronized (list) {
            Iterator<NetworkManager> iterator = this.connections.iterator();
            while (iterator.hasNext()) {
                NetworkManager networkManager = iterator.next();
                if (!networkManager.isChannelOpen()) continue;
                iterator.remove();
                networkManager.closeChannel(new ChatComponentText("Cancelled"));
            }
        }
    }
}

