/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.util.concurrent.ThreadFactoryBuilder
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.network;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.MultithreadEventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.local.LocalAddress;
import io.netty.channel.local.LocalEventLoopGroup;
import io.netty.channel.local.LocalServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import net.minecraft.client.network.NetHandlerHandshakeMemory;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PingResponseHandler;
import net.minecraft.network.play.server.S40PacketDisconnect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.NetHandlerHandshakeTCP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.LazyLoadBase;
import net.minecraft.util.MessageDeserializer;
import net.minecraft.util.MessageDeserializer2;
import net.minecraft.util.MessageSerializer;
import net.minecraft.util.MessageSerializer2;
import net.minecraft.util.ReportedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NetworkSystem {
    private static final Logger logger = LogManager.getLogger();
    public static final LazyLoadBase<LocalEventLoopGroup> SERVER_LOCAL_EVENTLOOP;
    public static final LazyLoadBase<EpollEventLoopGroup> field_181141_b;
    private final List<ChannelFuture> endpoints = Collections.synchronizedList(Lists.newArrayList());
    public static final LazyLoadBase<NioEventLoopGroup> eventLoops;
    private final List<NetworkManager> networkManagers = Collections.synchronizedList(Lists.newArrayList());
    private final MinecraftServer mcServer;
    public volatile boolean isAlive;

    static {
        eventLoops = new LazyLoadBase<NioEventLoopGroup>(){

            @Override
            protected NioEventLoopGroup load() {
                return new NioEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty Server IO #%d").setDaemon(true).build());
            }
        };
        field_181141_b = new LazyLoadBase<EpollEventLoopGroup>(){

            @Override
            protected EpollEventLoopGroup load() {
                return new EpollEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty Epoll Server IO #%d").setDaemon(true).build());
            }
        };
        SERVER_LOCAL_EVENTLOOP = new LazyLoadBase<LocalEventLoopGroup>(){

            @Override
            protected LocalEventLoopGroup load() {
                return new LocalEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty Local Server IO #%d").setDaemon(true).build());
            }
        };
    }

    public MinecraftServer getServer() {
        return this.mcServer;
    }

    public NetworkSystem(MinecraftServer minecraftServer) {
        this.mcServer = minecraftServer;
        this.isAlive = true;
    }

    public SocketAddress addLocalEndpoint() {
        ChannelFuture channelFuture;
        List<ChannelFuture> list = this.endpoints;
        synchronized (list) {
            channelFuture = ((ServerBootstrap)((ServerBootstrap)new ServerBootstrap().channel(LocalServerChannel.class)).childHandler(new ChannelInitializer<Channel>(){

                @Override
                protected void initChannel(Channel channel) throws Exception {
                    NetworkManager networkManager = new NetworkManager(EnumPacketDirection.SERVERBOUND);
                    networkManager.setNetHandler(new NetHandlerHandshakeMemory(NetworkSystem.this.mcServer, networkManager));
                    NetworkSystem.this.networkManagers.add(networkManager);
                    channel.pipeline().addLast("packet_handler", (ChannelHandler)networkManager);
                }
            }).group(eventLoops.getValue()).localAddress(LocalAddress.ANY)).bind().syncUninterruptibly();
            this.endpoints.add(channelFuture);
        }
        return channelFuture.channel().localAddress();
    }

    public void addLanEndpoint(InetAddress inetAddress, int n) throws IOException {
        List<ChannelFuture> list = this.endpoints;
        synchronized (list) {
            LazyLoadBase<MultithreadEventLoopGroup> lazyLoadBase;
            Class clazz;
            if (Epoll.isAvailable() && this.mcServer.func_181035_ah()) {
                clazz = EpollServerSocketChannel.class;
                lazyLoadBase = field_181141_b;
                logger.info("Using epoll channel type");
            } else {
                clazz = NioServerSocketChannel.class;
                lazyLoadBase = eventLoops;
                logger.info("Using default channel type");
            }
            this.endpoints.add(((ServerBootstrap)((ServerBootstrap)new ServerBootstrap().channel(clazz)).childHandler(new ChannelInitializer<Channel>(){

                @Override
                protected void initChannel(Channel channel) throws Exception {
                    try {
                        channel.config().setOption(ChannelOption.TCP_NODELAY, true);
                    }
                    catch (ChannelException channelException) {
                        // empty catch block
                    }
                    channel.pipeline().addLast("timeout", (ChannelHandler)new ReadTimeoutHandler(30)).addLast("legacy_query", (ChannelHandler)new PingResponseHandler(NetworkSystem.this)).addLast("splitter", (ChannelHandler)new MessageDeserializer2()).addLast("decoder", (ChannelHandler)new MessageDeserializer(EnumPacketDirection.SERVERBOUND)).addLast("prepender", (ChannelHandler)new MessageSerializer2()).addLast("encoder", (ChannelHandler)new MessageSerializer(EnumPacketDirection.CLIENTBOUND));
                    NetworkManager networkManager = new NetworkManager(EnumPacketDirection.SERVERBOUND);
                    NetworkSystem.this.networkManagers.add(networkManager);
                    channel.pipeline().addLast("packet_handler", (ChannelHandler)networkManager);
                    networkManager.setNetHandler(new NetHandlerHandshakeTCP(NetworkSystem.this.mcServer, networkManager));
                }
            }).group(lazyLoadBase.getValue()).localAddress(inetAddress, n)).bind().syncUninterruptibly());
        }
    }

    public void terminateEndpoints() {
        this.isAlive = false;
        for (ChannelFuture channelFuture : this.endpoints) {
            try {
                channelFuture.channel().close().sync();
            }
            catch (InterruptedException interruptedException) {
                logger.error("Interrupted whilst closing channel");
            }
        }
    }

    public void networkTick() {
        List<NetworkManager> list = this.networkManagers;
        synchronized (list) {
            Iterator<NetworkManager> iterator = this.networkManagers.iterator();
            while (iterator.hasNext()) {
                final NetworkManager networkManager = iterator.next();
                if (networkManager.hasNoChannel()) continue;
                if (!networkManager.isChannelOpen()) {
                    iterator.remove();
                    networkManager.checkDisconnected();
                    continue;
                }
                try {
                    networkManager.processReceivedPackets();
                }
                catch (Exception exception) {
                    Object object;
                    if (networkManager.isLocalChannel()) {
                        object = CrashReport.makeCrashReport(exception, "Ticking memory connection");
                        CrashReportCategory crashReportCategory = ((CrashReport)object).makeCategory("Ticking connection");
                        crashReportCategory.addCrashSectionCallable("Connection", new Callable<String>(){

                            @Override
                            public String call() throws Exception {
                                return networkManager.toString();
                            }
                        });
                        throw new ReportedException((CrashReport)object);
                    }
                    logger.warn("Failed to handle packet for " + networkManager.getRemoteAddress(), (Throwable)exception);
                    object = new ChatComponentText("Internal server error");
                    networkManager.sendPacket(new S40PacketDisconnect((IChatComponent)object), (GenericFutureListener<? extends Future<? super Void>>)new GenericFutureListener<Future<? super Void>>((ChatComponentText)object){
                        private final /* synthetic */ ChatComponentText val$chatcomponenttext;

                        @Override
                        public void operationComplete(Future<? super Void> future) throws Exception {
                            networkManager.closeChannel(this.val$chatcomponenttext);
                        }
                        {
                            this.val$chatcomponenttext = chatComponentText;
                        }
                    }, new GenericFutureListener[0]);
                    networkManager.disableAutoRead();
                }
            }
        }
    }
}

