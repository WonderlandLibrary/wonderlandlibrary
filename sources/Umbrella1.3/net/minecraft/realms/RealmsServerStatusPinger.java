/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.realms;

import com.google.common.collect.Lists;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RealmsServerStatusPinger {
    private static final Logger LOGGER = LogManager.getLogger();
    private final List connections = Collections.synchronizedList(Lists.newArrayList());
    private static final String __OBFID = "CL_00001854";

    public void pingServer(final String p_pingServer_1_, final RealmsServerPing p_pingServer_2_) throws UnknownHostException {
        if (p_pingServer_1_ != null && !p_pingServer_1_.startsWith("0.0.0.0") && !p_pingServer_1_.isEmpty()) {
            RealmsServerAddress var3 = RealmsServerAddress.parseString(p_pingServer_1_);
            final NetworkManager var4 = NetworkManager.provideLanClient(InetAddress.getByName(var3.getHost()), var3.getPort());
            this.connections.add(var4);
            var4.setNetHandler(new INetHandlerStatusClient(){
                private boolean field_154345_e = false;
                private static final String __OBFID = "CL_00001807";

                @Override
                public void handleServerInfo(S00PacketServerInfo packetIn) {
                    ServerStatusResponse var2 = packetIn.func_149294_c();
                    if (var2.getPlayerCountData() != null) {
                        p_pingServer_2_.nrOfPlayers = String.valueOf(var2.getPlayerCountData().getOnlinePlayerCount());
                    }
                    var4.sendPacket(new C01PacketPing(Realms.currentTimeMillis()));
                    this.field_154345_e = true;
                }

                @Override
                public void handlePong(S01PacketPong packetIn) {
                    var4.closeChannel(new ChatComponentText("Finished"));
                }

                @Override
                public void onDisconnect(IChatComponent reason) {
                    if (!this.field_154345_e) {
                        LOGGER.error("Can't ping " + p_pingServer_1_ + ": " + reason.getUnformattedText());
                    }
                }
            });
            try {
                var4.sendPacket(new C00Handshake(RealmsSharedConstants.NETWORK_PROTOCOL_VERSION, var3.getHost(), var3.getPort(), EnumConnectionState.STATUS));
                var4.sendPacket(new C00PacketServerQuery());
            }
            catch (Throwable var6) {
                LOGGER.error((Object)var6);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void tick() {
        List var1 = this.connections;
        List list = this.connections;
        synchronized (list) {
            Iterator var2 = this.connections.iterator();
            while (var2.hasNext()) {
                NetworkManager var3 = (NetworkManager)var2.next();
                if (var3.isChannelOpen()) {
                    var3.processReceivedPackets();
                    continue;
                }
                var2.remove();
                var3.checkDisconnected();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void removeAll() {
        List var1 = this.connections;
        List list = this.connections;
        synchronized (list) {
            Iterator var2 = this.connections.iterator();
            while (var2.hasNext()) {
                NetworkManager var3 = (NetworkManager)var2.next();
                if (!var3.isChannelOpen()) continue;
                var2.remove();
                var3.closeChannel(new ChatComponentText("Cancelled"));
            }
        }
    }
}

