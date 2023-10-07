// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network;

import io.netty.channel.local.LocalEventLoopGroup;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.channel.nio.NioEventLoopGroup;
import org.apache.logging.log4j.LogManager;
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
import io.netty.channel.ChannelFuture;
import io.netty.channel.local.LocalAddress;
import net.minecraft.client.network.NetHandlerHandshakeMemory;
import io.netty.channel.local.LocalServerChannel;
import java.net.SocketAddress;
import java.io.IOException;
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
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.bootstrap.ServerBootstrap;
import java.net.InetAddress;
import java.util.Collections;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.LazyLoadBase;
import org.apache.logging.log4j.Logger;

public class NetworkSystem
{
    private static final Logger logger;
    public static final LazyLoadBase eventLoops;
    public static final LazyLoadBase SERVER_LOCAL_EVENTLOOP;
    private final MinecraftServer mcServer;
    public volatile boolean isAlive;
    private final List endpoints;
    private final List networkManagers;
    private static final String __OBFID = "CL_00001447";
    
    public NetworkSystem(final MinecraftServer server) {
        this.endpoints = Collections.synchronizedList((List<Object>)Lists.newArrayList());
        this.networkManagers = Collections.synchronizedList((List<Object>)Lists.newArrayList());
        this.mcServer = server;
        this.isAlive = true;
    }
    
    public void addLanEndpoint(final InetAddress address, final int port) throws IOException {
        final List var3 = this.endpoints;
        synchronized (this.endpoints) {
            this.endpoints.add(((ServerBootstrap)((ServerBootstrap)new ServerBootstrap().channel((Class)NioServerSocketChannel.class)).childHandler((ChannelHandler)new ChannelInitializer() {
                private static final String __OBFID = "CL_00001450";
                
                protected void initChannel(final Channel p_initChannel_1_) {
                    try {
                        p_initChannel_1_.config().setOption(ChannelOption.IP_TOS, (Object)24);
                    }
                    catch (ChannelException ex) {}
                    try {
                        p_initChannel_1_.config().setOption(ChannelOption.TCP_NODELAY, (Object)false);
                    }
                    catch (ChannelException ex2) {}
                    p_initChannel_1_.pipeline().addLast("timeout", (ChannelHandler)new ReadTimeoutHandler(30)).addLast("legacy_query", (ChannelHandler)new PingResponseHandler(NetworkSystem.this)).addLast("splitter", (ChannelHandler)new MessageDeserializer2()).addLast("decoder", (ChannelHandler)new MessageDeserializer(EnumPacketDirection.SERVERBOUND)).addLast("prepender", (ChannelHandler)new MessageSerializer2()).addLast("encoder", (ChannelHandler)new MessageSerializer(EnumPacketDirection.CLIENTBOUND));
                    final NetworkManager var2 = new NetworkManager(EnumPacketDirection.SERVERBOUND);
                    NetworkSystem.this.networkManagers.add(var2);
                    p_initChannel_1_.pipeline().addLast("packet_handler", (ChannelHandler)var2);
                    var2.setNetHandler(new NetHandlerHandshakeTCP(NetworkSystem.this.mcServer, var2));
                }
            }).group((EventLoopGroup)NetworkSystem.eventLoops.getValue()).localAddress(address, port)).bind().syncUninterruptibly());
        }
    }
    
    public SocketAddress addLocalEndpoint() {
        final List var2 = this.endpoints;
        final ChannelFuture var3;
        synchronized (this.endpoints) {
            var3 = ((ServerBootstrap)((ServerBootstrap)new ServerBootstrap().channel((Class)LocalServerChannel.class)).childHandler((ChannelHandler)new ChannelInitializer() {
                private static final String __OBFID = "CL_00001451";
                
                protected void initChannel(final Channel p_initChannel_1_) {
                    final NetworkManager var2 = new NetworkManager(EnumPacketDirection.SERVERBOUND);
                    var2.setNetHandler(new NetHandlerHandshakeMemory(NetworkSystem.this.mcServer, var2));
                    NetworkSystem.this.networkManagers.add(var2);
                    p_initChannel_1_.pipeline().addLast("packet_handler", (ChannelHandler)var2);
                }
            }).group((EventLoopGroup)NetworkSystem.eventLoops.getValue()).localAddress((SocketAddress)LocalAddress.ANY)).bind().syncUninterruptibly();
            this.endpoints.add(var3);
        }
        return var3.channel().localAddress();
    }
    
    public void terminateEndpoints() {
        this.isAlive = false;
        for (final ChannelFuture var2 : this.endpoints) {
            try {
                var2.channel().close().sync();
            }
            catch (InterruptedException var3) {
                NetworkSystem.logger.error("Interrupted whilst closing channel");
            }
        }
    }
    
    public void networkTick() {
        final List var1 = this.networkManagers;
        synchronized (this.networkManagers) {
            final Iterator var2 = this.networkManagers.iterator();
            while (var2.hasNext()) {
                final NetworkManager var3 = var2.next();
                if (!var3.hasNoChannel()) {
                    if (!var3.isChannelOpen()) {
                        var2.remove();
                        var3.checkDisconnected();
                    }
                    else {
                        try {
                            var3.processReceivedPackets();
                        }
                        catch (Exception var5) {
                            if (var3.isLocalChannel()) {
                                final CrashReport var4 = CrashReport.makeCrashReport(var5, "Ticking memory connection");
                                final CrashReportCategory var6 = var4.makeCategory("Ticking connection");
                                var6.addCrashSectionCallable("Connection", new Callable() {
                                    private static final String __OBFID = "CL_00002272";
                                    
                                    public String func_180229_a() {
                                        return var3.toString();
                                    }
                                    
                                    @Override
                                    public Object call() {
                                        return this.func_180229_a();
                                    }
                                });
                                throw new ReportedException(var4);
                            }
                            NetworkSystem.logger.warn("Failed to handle packet for " + var3.getRemoteAddress(), (Throwable)var5);
                            final ChatComponentText var7 = new ChatComponentText("Internal server error");
                            var3.sendPacket(new S40PacketDisconnect(var7), (GenericFutureListener)new GenericFutureListener() {
                                private static final String __OBFID = "CL_00002271";
                                
                                public void operationComplete(final Future p_operationComplete_1_) {
                                    var3.closeChannel(var7);
                                }
                            }, new GenericFutureListener[0]);
                            var3.disableAutoRead();
                        }
                    }
                }
            }
        }
    }
    
    public MinecraftServer getServer() {
        return this.mcServer;
    }
    
    static {
        logger = LogManager.getLogger();
        eventLoops = new LazyLoadBase() {
            private static final String __OBFID = "CL_00001448";
            
            protected NioEventLoopGroup genericLoad() {
                return new NioEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty Server IO #%d").setDaemon(true).build());
            }
            
            @Override
            protected Object load() {
                return this.genericLoad();
            }
        };
        SERVER_LOCAL_EVENTLOOP = new LazyLoadBase() {
            private static final String __OBFID = "CL_00001449";
            
            protected LocalEventLoopGroup genericLoad() {
                return new LocalEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty Local Server IO #%d").setDaemon(true).build());
            }
            
            @Override
            protected Object load() {
                return this.genericLoad();
            }
        };
    }
}
