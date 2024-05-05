package net.minecraft.network;

import com.google.common.collect.Lists;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.local.LocalAddress;
import io.netty.channel.local.LocalEventLoopGroup;
import io.netty.channel.local.LocalServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.GenericFutureListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S40PacketDisconnect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.LazyLoadBase;
import net.minecraft.util.ReportedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NetworkSystem {
    private static final Logger logger = LogManager.getLogger();
    public static final LazyLoadBase<NioEventLoopGroup> eventLoops = new /* Unavailable Anonymous Inner Class!! */;
    public static final LazyLoadBase<EpollEventLoopGroup> SERVER_EPOLL_EVENTLOOP = new /* Unavailable Anonymous Inner Class!! */;
    public static final LazyLoadBase<LocalEventLoopGroup> SERVER_LOCAL_EVENTLOOP = new /* Unavailable Anonymous Inner Class!! */;
    private final MinecraftServer mcServer;
    public volatile boolean isAlive;
    private final List<ChannelFuture> endpoints = Collections.synchronizedList((List)Lists.newArrayList());
    private final List<NetworkManager> networkManagers = Collections.synchronizedList((List)Lists.newArrayList());

    public NetworkSystem(MinecraftServer server) {
        this.mcServer = server;
        this.isAlive = true;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void addLanEndpoint(InetAddress address, int port) throws IOException {
        List<ChannelFuture> list = this.endpoints;
        synchronized (list) {
            LazyLoadBase<NioEventLoopGroup> lazyloadbase;
            Class<NioServerSocketChannel> oclass;
            if (Epoll.isAvailable() && this.mcServer.shouldUseNativeTransport()) {
                oclass = EpollServerSocketChannel.class;
                lazyloadbase = SERVER_EPOLL_EVENTLOOP;
                logger.info("Using epoll channel type");
            } else {
                oclass = NioServerSocketChannel.class;
                lazyloadbase = eventLoops;
                logger.info("Using default channel type");
            }
            this.endpoints.add((Object)((ServerBootstrap)((ServerBootstrap)new ServerBootstrap().channel(oclass)).childHandler((ChannelHandler)new /* Unavailable Anonymous Inner Class!! */).group((EventLoopGroup)lazyloadbase.getValue()).localAddress(address, port)).bind().syncUninterruptibly());
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public SocketAddress addLocalEndpoint() {
        ChannelFuture channelfuture;
        List<ChannelFuture> list = this.endpoints;
        synchronized (list) {
            channelfuture = ((ServerBootstrap)((ServerBootstrap)new ServerBootstrap().channel(LocalServerChannel.class)).childHandler((ChannelHandler)new /* Unavailable Anonymous Inner Class!! */).group((EventLoopGroup)eventLoops.getValue()).localAddress((SocketAddress)LocalAddress.ANY)).bind().syncUninterruptibly();
            this.endpoints.add((Object)channelfuture);
        }
        return channelfuture.channel().localAddress();
    }

    public void terminateEndpoints() {
        this.isAlive = false;
        for (ChannelFuture channelfuture : this.endpoints) {
            try {
                channelfuture.channel().close().sync();
            }
            catch (InterruptedException var4) {
                logger.error("Interrupted whilst closing channel");
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void networkTick() {
        List<NetworkManager> list = this.networkManagers;
        synchronized (list) {
            Iterator iterator = this.networkManagers.iterator();
            while (iterator.hasNext()) {
                NetworkManager networkmanager = (NetworkManager)iterator.next();
                if (networkmanager.hasNoChannel()) continue;
                if (!networkmanager.isChannelOpen()) {
                    iterator.remove();
                    networkmanager.checkDisconnected();
                    continue;
                }
                try {
                    networkmanager.processReceivedPackets();
                }
                catch (Exception exception) {
                    if (networkmanager.isLocalChannel()) {
                        CrashReport crashreport = CrashReport.makeCrashReport((Throwable)exception, (String)"Ticking memory connection");
                        CrashReportCategory crashreportcategory = crashreport.makeCategory("Ticking connection");
                        crashreportcategory.addCrashSectionCallable("Connection", (Callable)new /* Unavailable Anonymous Inner Class!! */);
                        throw new ReportedException(crashreport);
                    }
                    logger.warn("Failed to handle packet for " + networkmanager.getRemoteAddress(), (Throwable)exception);
                    ChatComponentText chatcomponenttext = new ChatComponentText("Internal server error");
                    networkmanager.sendPacket((Packet)new S40PacketDisconnect((IChatComponent)chatcomponenttext), (GenericFutureListener)new /* Unavailable Anonymous Inner Class!! */, new GenericFutureListener[0]);
                    networkmanager.disableAutoRead();
                }
            }
        }
    }

    public MinecraftServer getServer() {
        return this.mcServer;
    }

    static /* synthetic */ List access$000(NetworkSystem x0) {
        return x0.networkManagers;
    }

    static /* synthetic */ MinecraftServer access$100(NetworkSystem x0) {
        return x0.mcServer;
    }
}
