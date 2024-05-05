package net.minecraft.client.network;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.status.client.C00PacketServerQuery;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OldServerPinger {
    private static final Splitter PING_RESPONSE_SPLITTER = Splitter.on((char)'\u0000').limit(6);
    private static final Logger logger = LogManager.getLogger();
    private final List<NetworkManager> pingDestinations = Collections.synchronizedList((List)Lists.newArrayList());

    public void ping(ServerData server) throws UnknownHostException {
        ServerAddress serveraddress = ServerAddress.fromString((String)server.serverIP);
        NetworkManager networkmanager = NetworkManager.createNetworkManagerAndConnect((InetAddress)InetAddress.getByName((String)serveraddress.getIP()), (int)serveraddress.getPort(), (boolean)false);
        this.pingDestinations.add((Object)networkmanager);
        server.serverMOTD = "Pinging...";
        server.pingToServer = -1L;
        server.playerList = null;
        networkmanager.setNetHandler((INetHandler)new /* Unavailable Anonymous Inner Class!! */);
        try {
            networkmanager.sendPacket((Packet)new C00Handshake(47, serveraddress.getIP(), serveraddress.getPort(), EnumConnectionState.STATUS));
            networkmanager.sendPacket((Packet)new C00PacketServerQuery());
        }
        catch (Throwable throwable) {
            logger.error((Object)throwable);
        }
    }

    private void tryCompatibilityPing(ServerData server) {
        ServerAddress serveraddress = ServerAddress.fromString((String)server.serverIP);
        ((Bootstrap)((Bootstrap)((Bootstrap)new Bootstrap().group((EventLoopGroup)NetworkManager.CLIENT_NIO_EVENTLOOP.getValue())).handler((ChannelHandler)new /* Unavailable Anonymous Inner Class!! */)).channel(NioSocketChannel.class)).connect(serveraddress.getIP(), serveraddress.getPort());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void pingPendingNetworks() {
        List<NetworkManager> list = this.pingDestinations;
        synchronized (list) {
            Iterator iterator = this.pingDestinations.iterator();
            while (iterator.hasNext()) {
                NetworkManager networkmanager = (NetworkManager)iterator.next();
                if (networkmanager.isChannelOpen()) {
                    networkmanager.processReceivedPackets();
                    continue;
                }
                iterator.remove();
                networkmanager.checkDisconnected();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void clearPendingNetworks() {
        List<NetworkManager> list = this.pingDestinations;
        synchronized (list) {
            Iterator iterator = this.pingDestinations.iterator();
            while (iterator.hasNext()) {
                NetworkManager networkmanager = (NetworkManager)iterator.next();
                if (!networkmanager.isChannelOpen()) continue;
                iterator.remove();
                networkmanager.closeChannel((IChatComponent)new ChatComponentText("Cancelled"));
            }
        }
    }

    static /* synthetic */ Logger access$000() {
        return logger;
    }

    static /* synthetic */ void access$100(OldServerPinger x0, ServerData x1) {
        x0.tryCompatibilityPing(x1);
    }

    static /* synthetic */ Splitter access$200() {
        return PING_RESPONSE_SPLITTER;
    }
}
