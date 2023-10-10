/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.bootstrap.Bootstrap
 *  io.netty.channel.Channel
 *  io.netty.channel.ChannelException
 *  io.netty.channel.ChannelFuture
 *  io.netty.channel.ChannelFutureListener
 *  io.netty.channel.ChannelHandler
 *  io.netty.channel.ChannelHandlerContext
 *  io.netty.channel.ChannelInitializer
 *  io.netty.channel.ChannelOption
 *  io.netty.channel.EventLoopGroup
 *  io.netty.channel.SimpleChannelInboundHandler
 *  io.netty.channel.epoll.Epoll
 *  io.netty.channel.epoll.EpollEventLoopGroup
 *  io.netty.channel.epoll.EpollSocketChannel
 *  io.netty.channel.local.LocalChannel
 *  io.netty.channel.local.LocalEventLoopGroup
 *  io.netty.channel.local.LocalServerChannel
 *  io.netty.channel.nio.NioEventLoopGroup
 *  io.netty.channel.socket.nio.NioSocketChannel
 *  io.netty.handler.timeout.ReadTimeoutHandler
 *  io.netty.handler.timeout.TimeoutException
 *  io.netty.util.AttributeKey
 *  io.netty.util.concurrent.Future
 *  io.netty.util.concurrent.GenericFutureListener
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 *  org.apache.logging.log4j.Marker
 *  org.apache.logging.log4j.MarkerManager
 */
package net.minecraft.network;

import com.google.common.collect.Queues;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.wallhacks.losebypass.LoseBypass;
import com.wallhacks.losebypass.event.events.PacketReceiveEvent;
import com.wallhacks.losebypass.event.events.PacketSendEvent;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.local.LocalChannel;
import io.netty.channel.local.LocalEventLoopGroup;
import io.netty.channel.local.LocalServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.TimeoutException;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.crypto.SecretKey;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NettyCompressionDecoder;
import net.minecraft.network.NettyCompressionEncoder;
import net.minecraft.network.NettyEncryptingDecoder;
import net.minecraft.network.NettyEncryptingEncoder;
import net.minecraft.network.Packet;
import net.minecraft.network.ThreadQuickExitException;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.CryptManager;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ITickable;
import net.minecraft.util.LazyLoadBase;
import net.minecraft.util.MessageDeserializer;
import net.minecraft.util.MessageDeserializer2;
import net.minecraft.util.MessageSerializer;
import net.minecraft.util.MessageSerializer2;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class NetworkManager
extends SimpleChannelInboundHandler<Packet> {
    public static final Marker logMarkerNetwork = MarkerManager.getMarker((String)"NETWORK");
    public static final Marker logMarkerPackets = MarkerManager.getMarker((String)"NETWORK_PACKETS", (Marker)logMarkerNetwork);
    public static final AttributeKey<EnumConnectionState> attrKeyConnectionState = AttributeKey.valueOf((String)"protocol");
    public static final LazyLoadBase<NioEventLoopGroup> CLIENT_NIO_EVENTLOOP = new LazyLoadBase<NioEventLoopGroup>(){

        @Override
        protected NioEventLoopGroup load() {
            return new NioEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty Client IO #%d").setDaemon(true).build());
        }
    };
    public static final LazyLoadBase<EpollEventLoopGroup> field_181125_e = new LazyLoadBase<EpollEventLoopGroup>(){

        @Override
        protected EpollEventLoopGroup load() {
            return new EpollEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty Epoll Client IO #%d").setDaemon(true).build());
        }
    };
    public static final LazyLoadBase<LocalEventLoopGroup> CLIENT_LOCAL_EVENTLOOP = new LazyLoadBase<LocalEventLoopGroup>(){

        @Override
        protected LocalEventLoopGroup load() {
            return new LocalEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty Local Client IO #%d").setDaemon(true).build());
        }
    };
    private static final Logger logger = LogManager.getLogger();
    private final EnumPacketDirection direction;
    private final Queue<InboundHandlerTuplePacketListener> outboundPacketsQueue = Queues.newConcurrentLinkedQueue();
    private final ReentrantReadWriteLock field_181680_j = new ReentrantReadWriteLock();
    private Channel channel;
    private SocketAddress socketAddress;
    private INetHandler packetListener;
    private IChatComponent terminationReason;
    private boolean isEncrypted;
    private boolean disconnected;

    public NetworkManager(EnumPacketDirection packetDirection) {
        this.direction = packetDirection;
    }

    public static NetworkManager func_181124_a(InetAddress p_181124_0_, int p_181124_1_, boolean p_181124_2_) {
        LazyLoadBase<NioEventLoopGroup> lazyloadbase;
        Class<NioSocketChannel> oclass;
        final NetworkManager networkmanager = new NetworkManager(EnumPacketDirection.CLIENTBOUND);
        if (Epoll.isAvailable() && p_181124_2_) {
            oclass = EpollSocketChannel.class;
            lazyloadbase = field_181125_e;
        } else {
            oclass = NioSocketChannel.class;
            lazyloadbase = CLIENT_NIO_EVENTLOOP;
        }
        ((Bootstrap)((Bootstrap)((Bootstrap)new Bootstrap().group((EventLoopGroup)lazyloadbase.getValue())).handler((ChannelHandler)new ChannelInitializer<Channel>(){

            protected void initChannel(Channel p_initChannel_1_) throws Exception {
                try {
                    p_initChannel_1_.config().setOption(ChannelOption.TCP_NODELAY, (Object)true);
                }
                catch (ChannelException channelException) {
                    // empty catch block
                }
                p_initChannel_1_.pipeline().addLast("timeout", (ChannelHandler)new ReadTimeoutHandler(30)).addLast("splitter", (ChannelHandler)new MessageDeserializer2()).addLast("decoder", (ChannelHandler)new MessageDeserializer(EnumPacketDirection.CLIENTBOUND)).addLast("prepender", (ChannelHandler)new MessageSerializer2()).addLast("encoder", (ChannelHandler)new MessageSerializer(EnumPacketDirection.SERVERBOUND)).addLast("packet_handler", (ChannelHandler)networkmanager);
            }
        })).channel(oclass)).connect(p_181124_0_, p_181124_1_).syncUninterruptibly();
        return networkmanager;
    }

    public static NetworkManager provideLocalClient(SocketAddress address) {
        final NetworkManager networkmanager = new NetworkManager(EnumPacketDirection.CLIENTBOUND);
        ((Bootstrap)((Bootstrap)((Bootstrap)new Bootstrap().group((EventLoopGroup)CLIENT_LOCAL_EVENTLOOP.getValue())).handler((ChannelHandler)new ChannelInitializer<Channel>(){

            protected void initChannel(Channel p_initChannel_1_) throws Exception {
                p_initChannel_1_.pipeline().addLast("packet_handler", (ChannelHandler)networkmanager);
            }
        })).channel(LocalChannel.class)).connect(address).syncUninterruptibly();
        return networkmanager;
    }

    public void channelActive(ChannelHandlerContext p_channelActive_1_) throws Exception {
        super.channelActive(p_channelActive_1_);
        this.channel = p_channelActive_1_.channel();
        this.socketAddress = this.channel.remoteAddress();
        try {
            this.setConnectionState(EnumConnectionState.HANDSHAKING);
            return;
        }
        catch (Throwable throwable) {
            logger.fatal((Object)throwable);
        }
    }

    public void setConnectionState(EnumConnectionState newState) {
        this.channel.attr(attrKeyConnectionState).set((Object)newState);
        this.channel.config().setAutoRead(true);
        logger.debug("Enabled auto read");
    }

    public void channelInactive(ChannelHandlerContext p_channelInactive_1_) throws Exception {
        this.closeChannel(new ChatComponentTranslation("disconnect.endOfStream", new Object[0]));
    }

    public void exceptionCaught(ChannelHandlerContext p_exceptionCaught_1_, Throwable p_exceptionCaught_2_) throws Exception {
        ChatComponentTranslation chatcomponenttranslation = p_exceptionCaught_2_ instanceof TimeoutException ? new ChatComponentTranslation("disconnect.timeout", new Object[0]) : new ChatComponentTranslation("disconnect.genericReason", "Internal Exception: " + p_exceptionCaught_2_);
        this.closeChannel(chatcomponenttranslation);
    }

    protected void channelRead0(ChannelHandlerContext p_channelRead0_1_, Packet p_channelRead0_2_) throws Exception {
        PacketReceiveEvent event = new PacketReceiveEvent(p_channelRead0_2_);
        LoseBypass.eventBus.post(event);
        if (event.isCancelled()) return;
        if (!this.channel.isOpen()) return;
        try {
            p_channelRead0_2_.processPacket(this.packetListener);
            return;
        }
        catch (ThreadQuickExitException threadQuickExitException) {
            // empty catch block
        }
    }

    public void sendPacket(Packet packetIn) {
        PacketSendEvent event = new PacketSendEvent(packetIn);
        LoseBypass.eventBus.post(event);
        if (event.isCancelled()) return;
        if (this.isChannelOpen()) {
            this.flushOutboundQueue();
            this.dispatchPacket(packetIn, null);
            return;
        }
        this.field_181680_j.writeLock().lock();
        try {
            this.outboundPacketsQueue.add(new InboundHandlerTuplePacketListener(packetIn, null));
            return;
        }
        finally {
            this.field_181680_j.writeLock().unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void sendPacket(Packet packetIn, GenericFutureListener<? extends Future<? super Void>> listener, GenericFutureListener<? extends Future<? super Void>> ... listeners) {
        PacketSendEvent event = new PacketSendEvent(packetIn);
        LoseBypass.eventBus.post(event);
        if (event.isCancelled()) return;
        if (this.isChannelOpen()) {
            this.flushOutboundQueue();
            this.dispatchPacket(packetIn, ArrayUtils.add(listeners, 0, listener));
            return;
        }
        this.field_181680_j.writeLock().lock();
        try {
            this.outboundPacketsQueue.add(new InboundHandlerTuplePacketListener(packetIn, ArrayUtils.add(listeners, 0, listener)));
            return;
        }
        finally {
            this.field_181680_j.writeLock().unlock();
        }
    }

    private void dispatchPacket(final Packet inPacket, final GenericFutureListener<? extends Future<? super Void>>[] futureListeners) {
        final EnumConnectionState enumconnectionstate = EnumConnectionState.getFromPacket(inPacket);
        final EnumConnectionState enumconnectionstate1 = (EnumConnectionState)((Object)this.channel.attr(attrKeyConnectionState).get());
        if (enumconnectionstate1 != enumconnectionstate) {
            logger.debug("Disabled auto read");
            this.channel.config().setAutoRead(false);
        }
        if (!this.channel.eventLoop().inEventLoop()) {
            this.channel.eventLoop().execute(new Runnable(){

                @Override
                public void run() {
                    if (enumconnectionstate != enumconnectionstate1) {
                        NetworkManager.this.setConnectionState(enumconnectionstate);
                    }
                    ChannelFuture channelfuture1 = NetworkManager.this.channel.writeAndFlush((Object)inPacket);
                    if (futureListeners != null) {
                        channelfuture1.addListeners(futureListeners);
                    }
                    channelfuture1.addListener((GenericFutureListener)ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
                }
            });
            return;
        }
        if (enumconnectionstate != enumconnectionstate1) {
            this.setConnectionState(enumconnectionstate);
        }
        ChannelFuture channelfuture = this.channel.writeAndFlush((Object)inPacket);
        if (futureListeners != null) {
            channelfuture.addListeners(futureListeners);
        }
        channelfuture.addListener((GenericFutureListener)ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    }

    private void flushOutboundQueue() {
        if (this.channel == null) return;
        if (!this.channel.isOpen()) return;
        this.field_181680_j.readLock().lock();
        try {
            while (!this.outboundPacketsQueue.isEmpty()) {
                InboundHandlerTuplePacketListener networkmanager$inboundhandlertuplepacketlistener = this.outboundPacketsQueue.poll();
                this.dispatchPacket(networkmanager$inboundhandlertuplepacketlistener.packet, networkmanager$inboundhandlertuplepacketlistener.futureListeners);
            }
            return;
        }
        finally {
            this.field_181680_j.readLock().unlock();
        }
    }

    public void processReceivedPackets() {
        this.flushOutboundQueue();
        if (this.packetListener instanceof ITickable) {
            ((ITickable)((Object)this.packetListener)).update();
        }
        this.channel.flush();
    }

    public SocketAddress getRemoteAddress() {
        return this.socketAddress;
    }

    public void closeChannel(IChatComponent message) {
        if (!this.channel.isOpen()) return;
        this.channel.close().awaitUninterruptibly();
        this.terminationReason = message;
    }

    public boolean isLocalChannel() {
        if (this.channel instanceof LocalChannel) return true;
        if (this.channel instanceof LocalServerChannel) return true;
        return false;
    }

    public void enableEncryption(SecretKey key) {
        this.isEncrypted = true;
        this.channel.pipeline().addBefore("splitter", "decrypt", (ChannelHandler)new NettyEncryptingDecoder(CryptManager.createNetCipherInstance(2, key)));
        this.channel.pipeline().addBefore("prepender", "encrypt", (ChannelHandler)new NettyEncryptingEncoder(CryptManager.createNetCipherInstance(1, key)));
    }

    public boolean getIsencrypted() {
        return this.isEncrypted;
    }

    public boolean isChannelOpen() {
        if (this.channel == null) return false;
        if (!this.channel.isOpen()) return false;
        return true;
    }

    public boolean hasNoChannel() {
        if (this.channel != null) return false;
        return true;
    }

    public INetHandler getNetHandler() {
        return this.packetListener;
    }

    public void setNetHandler(INetHandler handler) {
        Validate.notNull(handler, "packetListener", new Object[0]);
        logger.debug("Set listener of {} to {}", new Object[]{this, handler});
        this.packetListener = handler;
    }

    public IChatComponent getExitMessage() {
        return this.terminationReason;
    }

    public void disableAutoRead() {
        this.channel.config().setAutoRead(false);
    }

    public void setCompressionTreshold(int treshold) {
        if (treshold >= 0) {
            if (this.channel.pipeline().get("decompress") instanceof NettyCompressionDecoder) {
                ((NettyCompressionDecoder)this.channel.pipeline().get("decompress")).setCompressionTreshold(treshold);
            } else {
                this.channel.pipeline().addBefore("decoder", "decompress", (ChannelHandler)new NettyCompressionDecoder(treshold));
            }
            if (this.channel.pipeline().get("compress") instanceof NettyCompressionEncoder) {
                ((NettyCompressionEncoder)this.channel.pipeline().get("decompress")).setCompressionTreshold(treshold);
                return;
            }
            this.channel.pipeline().addBefore("encoder", "compress", (ChannelHandler)new NettyCompressionEncoder(treshold));
            return;
        }
        if (this.channel.pipeline().get("decompress") instanceof NettyCompressionDecoder) {
            this.channel.pipeline().remove("decompress");
        }
        if (!(this.channel.pipeline().get("compress") instanceof NettyCompressionEncoder)) return;
        this.channel.pipeline().remove("compress");
    }

    public void checkDisconnected() {
        if (this.channel == null) return;
        if (this.channel.isOpen()) return;
        if (this.disconnected) {
            logger.warn("handleDisconnection() called twice");
            return;
        }
        this.disconnected = true;
        if (this.getExitMessage() != null) {
            this.getNetHandler().onDisconnect(this.getExitMessage());
            return;
        }
        if (this.getNetHandler() == null) return;
        this.getNetHandler().onDisconnect(new ChatComponentText("Disconnected"));
    }

    static class InboundHandlerTuplePacketListener {
        private final Packet packet;
        private final GenericFutureListener<? extends Future<? super Void>>[] futureListeners;

        public InboundHandlerTuplePacketListener(Packet inPacket, GenericFutureListener<? extends Future<? super Void>> ... inFutureListeners) {
            this.packet = inPacket;
            this.futureListeners = inFutureListeners;
        }
    }
}

