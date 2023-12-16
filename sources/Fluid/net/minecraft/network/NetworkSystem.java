// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.network;

import net.minecraft.crash.CrashReportCategory;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.util.IChatComponent;
import net.minecraft.network.play.server.S40PacketDisconnect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ReportedException;
import java.util.concurrent.Callable;
import net.minecraft.crash.CrashReport;
import java.util.Iterator;
import io.netty.channel.local.LocalAddress;
import net.minecraft.client.network.NetHandlerHandshakeMemory;
import io.netty.channel.local.LocalServerChannel;
import java.net.SocketAddress;
import java.io.IOException;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.EventLoopGroup;
import net.minecraft.server.network.NetHandlerHandshakeTCP;
import net.minecraft.util.MessageSerializer;
import net.minecraft.util.MessageSerializer2;
import net.minecraft.util.MessageDeserializer;
import net.minecraft.util.MessageDeserializer2;
import io.netty.channel.ChannelHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelOption;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.epoll.Epoll;
import java.net.InetAddress;
import java.util.Collections;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.logging.log4j.LogManager;
import io.netty.channel.ChannelFuture;
import java.util.List;
import net.minecraft.server.MinecraftServer;
import io.netty.channel.local.LocalEventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import net.minecraft.util.LazyLoadBase;
import org.apache.logging.log4j.Logger;

public class NetworkSystem
{
    private static final Logger logger;
    public static final LazyLoadBase<NioEventLoopGroup> eventLoops;
    public static final LazyLoadBase<EpollEventLoopGroup> SERVER_EPOLL_EVENTLOOP;
    public static final LazyLoadBase<LocalEventLoopGroup> SERVER_LOCAL_EVENTLOOP;
    private final MinecraftServer mcServer;
    public volatile boolean isAlive;
    private final List<ChannelFuture> endpoints;
    private final List<NetworkManager> networkManagers;
    
    static {
        logger = LogManager.getLogger();
        eventLoops = new LazyLoadBase<NioEventLoopGroup>() {
            @Override
            protected NioEventLoopGroup load() {
                return new NioEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty Server IO #%d").setDaemon(true).build());
            }
        };
        SERVER_EPOLL_EVENTLOOP = new LazyLoadBase<EpollEventLoopGroup>() {
            @Override
            protected EpollEventLoopGroup load() {
                return new EpollEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty Epoll Server IO #%d").setDaemon(true).build());
            }
        };
        SERVER_LOCAL_EVENTLOOP = new LazyLoadBase<LocalEventLoopGroup>() {
            @Override
            protected LocalEventLoopGroup load() {
                return new LocalEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty Local Server IO #%d").setDaemon(true).build());
            }
        };
    }
    
    public NetworkSystem(final MinecraftServer server) {
        this.endpoints = Collections.synchronizedList((List<ChannelFuture>)Lists.newArrayList());
        this.networkManagers = Collections.synchronizedList((List<NetworkManager>)Lists.newArrayList());
        this.mcServer = server;
        this.isAlive = true;
    }
    
    public void addLanEndpoint(final InetAddress address, final int port) throws IOException {
        synchronized (this.endpoints) {
            Class<? extends ServerSocketChannel> oclass;
            LazyLoadBase<? extends EventLoopGroup> lazyloadbase;
            if (Epoll.isAvailable() && this.mcServer.shouldUseNativeTransport()) {
                oclass = (Class<? extends ServerSocketChannel>)EpollServerSocketChannel.class;
                lazyloadbase = (LazyLoadBase<? extends EventLoopGroup>)NetworkSystem.SERVER_EPOLL_EVENTLOOP;
                NetworkSystem.logger.info("Using epoll channel type");
            }
            else {
                oclass = (Class<? extends ServerSocketChannel>)NioServerSocketChannel.class;
                lazyloadbase = (LazyLoadBase<? extends EventLoopGroup>)NetworkSystem.eventLoops;
                NetworkSystem.logger.info("Using default channel type");
            }
            this.endpoints.add(((ServerBootstrap)((ServerBootstrap)new ServerBootstrap().channel((Class)oclass)).childHandler((ChannelHandler)new ChannelInitializer<Channel>() {
                protected void initChannel(final Channel p_initChannel_1_) throws Exception {
                    try {
                        p_initChannel_1_.config().setOption(ChannelOption.TCP_NODELAY, (Object)true);
                    }
                    catch (final ChannelException ex) {}
                    p_initChannel_1_.pipeline().addLast("timeout", (ChannelHandler)new ReadTimeoutHandler(30)).addLast("legacy_query", (ChannelHandler)new PingResponseHandler(NetworkSystem.this)).addLast("splitter", (ChannelHandler)new MessageDeserializer2()).addLast("decoder", (ChannelHandler)new MessageDeserializer(EnumPacketDirection.SERVERBOUND)).addLast("prepender", (ChannelHandler)new MessageSerializer2()).addLast("encoder", (ChannelHandler)new MessageSerializer(EnumPacketDirection.CLIENTBOUND));
                    final NetworkManager networkmanager = new NetworkManager(EnumPacketDirection.SERVERBOUND);
                    NetworkSystem.this.networkManagers.add(networkmanager);
                    p_initChannel_1_.pipeline().addLast("packet_handler", (ChannelHandler)networkmanager);
                    networkmanager.setNetHandler(new NetHandlerHandshakeTCP(NetworkSystem.this.mcServer, networkmanager));
                }
            }).group((EventLoopGroup)lazyloadbase.getValue()).localAddress(address, port)).bind().syncUninterruptibly());
            monitorexit(this.endpoints);
        }
    }
    
    public SocketAddress addLocalEndpoint() {
        final ChannelFuture channelfuture;
        synchronized (this.endpoints) {
            channelfuture = ((ServerBootstrap)((ServerBootstrap)new ServerBootstrap().channel((Class)LocalServerChannel.class)).childHandler((ChannelHandler)new ChannelInitializer<Channel>() {
                protected void initChannel(final Channel p_initChannel_1_) throws Exception {
                    final NetworkManager networkmanager = new NetworkManager(EnumPacketDirection.SERVERBOUND);
                    networkmanager.setNetHandler(new NetHandlerHandshakeMemory(NetworkSystem.this.mcServer, networkmanager));
                    NetworkSystem.this.networkManagers.add(networkmanager);
                    p_initChannel_1_.pipeline().addLast("packet_handler", (ChannelHandler)networkmanager);
                }
            }).group((EventLoopGroup)NetworkSystem.eventLoops.getValue()).localAddress((SocketAddress)LocalAddress.ANY)).bind().syncUninterruptibly();
            this.endpoints.add(channelfuture);
            monitorexit(this.endpoints);
        }
        return channelfuture.channel().localAddress();
    }
    
    public void terminateEndpoints() {
        this.isAlive = false;
        for (final ChannelFuture channelfuture : this.endpoints) {
            try {
                channelfuture.channel().close().sync();
            }
            catch (final InterruptedException var4) {
                NetworkSystem.logger.error("Interrupted whilst closing channel");
            }
        }
    }
    
    public void networkTick() {
        synchronized (this.networkManagers) {
            final Iterator<NetworkManager> iterator = this.networkManagers.iterator();
            while (iterator.hasNext()) {
                final NetworkManager networkmanager = iterator.next();
                if (!networkmanager.hasNoChannel()) {
                    if (!networkmanager.isChannelOpen()) {
                        iterator.remove();
                        networkmanager.checkDisconnected();
                    }
                    else {
                        try {
                            networkmanager.processReceivedPackets();
                        }
                        catch (final Exception exception) {
                            if (networkmanager.isLocalChannel()) {
                                final CrashReport crashreport = CrashReport.makeCrashReport(exception, "Ticking memory connection");
                                final CrashReportCategory crashreportcategory = crashreport.makeCategory("Ticking connection");
                                crashreportcategory.addCrashSectionCallable("Connection", new Callable<String>() {
                                    @Override
                                    public String call() throws Exception {
                                        return networkmanager.toString();
                                    }
                                });
                                throw new ReportedException(crashreport);
                            }
                            NetworkSystem.logger.warn("Failed to handle packet for " + networkmanager.getRemoteAddress(), (Throwable)exception);
                            final ChatComponentText chatcomponenttext = new ChatComponentText("Internal server error");
                            networkmanager.sendPacket(new S40PacketDisconnect(chatcomponenttext), (GenericFutureListener<? extends Future<? super Void>>)new GenericFutureListener<Future<? super Void>>() {
                                public void operationComplete(final Future<? super Void> p_operationComplete_1_) throws Exception {
                                    networkmanager.closeChannel(chatcomponenttext);
                                }
                            }, (GenericFutureListener<? extends Future<? super Void>>[])new GenericFutureListener[0]);
                            networkmanager.disableAutoRead();
                        }
                    }
                }
            }
            monitorexit(this.networkManagers);
        }
    }
    
    public MinecraftServer getServer() {
        return this.mcServer;
    }
}
