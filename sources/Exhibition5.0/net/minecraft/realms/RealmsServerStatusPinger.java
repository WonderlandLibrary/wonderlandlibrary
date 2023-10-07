// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.realms;

import org.apache.logging.log4j.LogManager;
import java.util.Iterator;
import java.net.UnknownHostException;
import net.minecraft.network.status.client.C00PacketServerQuery;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.INetHandler;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.network.status.server.S01PacketPong;
import net.minecraft.network.ServerStatusResponse;
import net.minecraft.network.Packet;
import net.minecraft.network.status.client.C01PacketPing;
import net.minecraft.network.status.server.S00PacketServerInfo;
import net.minecraft.network.status.INetHandlerStatusClient;
import net.minecraft.network.NetworkManager;
import java.net.InetAddress;
import java.util.Collections;
import com.google.common.collect.Lists;
import java.util.List;
import org.apache.logging.log4j.Logger;

public class RealmsServerStatusPinger
{
    private static final Logger LOGGER;
    private final List connections;
    private static final String __OBFID = "CL_00001854";
    
    public RealmsServerStatusPinger() {
        this.connections = Collections.synchronizedList((List<Object>)Lists.newArrayList());
    }
    
    public void pingServer(final String p_pingServer_1_, final RealmsServerPing p_pingServer_2_) throws UnknownHostException {
        if (p_pingServer_1_ != null && !p_pingServer_1_.startsWith("0.0.0.0") && !p_pingServer_1_.isEmpty()) {
            final RealmsServerAddress var3 = RealmsServerAddress.parseString(p_pingServer_1_);
            final NetworkManager var4 = NetworkManager.provideLanClient(InetAddress.getByName(var3.getHost()), var3.getPort());
            this.connections.add(var4);
            var4.setNetHandler(new INetHandlerStatusClient() {
                private boolean field_154345_e = false;
                private static final String __OBFID = "CL_00001807";
                
                @Override
                public void handleServerInfo(final S00PacketServerInfo packetIn) {
                    final ServerStatusResponse var2 = packetIn.func_149294_c();
                    if (var2.getPlayerCountData() != null) {
                        p_pingServer_2_.nrOfPlayers = String.valueOf(var2.getPlayerCountData().getOnlinePlayerCount());
                    }
                    var4.sendPacket(new C01PacketPing(Realms.currentTimeMillis()));
                    this.field_154345_e = true;
                }
                
                @Override
                public void handlePong(final S01PacketPong packetIn) {
                    var4.closeChannel(new ChatComponentText("Finished"));
                }
                
                @Override
                public void onDisconnect(final IChatComponent reason) {
                    if (!this.field_154345_e) {
                        RealmsServerStatusPinger.LOGGER.error("Can't ping " + p_pingServer_1_ + ": " + reason.getUnformattedText());
                    }
                }
            });
            try {
                var4.sendPacket(new C00Handshake(RealmsSharedConstants.NETWORK_PROTOCOL_VERSION, var3.getHost(), var3.getPort(), EnumConnectionState.STATUS));
                var4.sendPacket(new C00PacketServerQuery());
            }
            catch (Throwable var5) {
                RealmsServerStatusPinger.LOGGER.error((Object)var5);
            }
        }
    }
    
    public void tick() {
        final List var1 = this.connections;
        synchronized (this.connections) {
            final Iterator var2 = this.connections.iterator();
            while (var2.hasNext()) {
                final NetworkManager var3 = var2.next();
                if (var3.isChannelOpen()) {
                    var3.processReceivedPackets();
                }
                else {
                    var2.remove();
                    var3.checkDisconnected();
                }
            }
        }
    }
    
    public void removeAll() {
        final List var1 = this.connections;
        synchronized (this.connections) {
            final Iterator var2 = this.connections.iterator();
            while (var2.hasNext()) {
                final NetworkManager var3 = var2.next();
                if (var3.isChannelOpen()) {
                    var2.remove();
                    var3.closeChannel(new ChatComponentText("Cancelled"));
                }
            }
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
