// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.LogManager;
import java.security.Key;
import net.minecraft.util.CryptManager;
import javax.crypto.SecretKey;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.ChannelHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.epoll.Epoll;
import java.net.InetAddress;
import io.netty.channel.local.LocalServerChannel;
import io.netty.channel.local.LocalChannel;
import net.minecraft.util.ITickable;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ArrayUtils;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.apache.commons.lang3.Validate;
import ru.tuskevich.event.events.Event;
import ru.tuskevich.event.EventManager;
import ru.tuskevich.event.events.impl.EventPacket;
import io.netty.handler.timeout.TimeoutException;
import net.minecraft.util.text.TextComponentTranslation;
import io.netty.channel.ChannelHandlerContext;
import com.google.common.collect.Queues;
import net.minecraft.util.text.ITextComponent;
import java.net.SocketAddress;
import io.netty.channel.Channel;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.Queue;
import io.netty.channel.local.LocalEventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import net.minecraft.util.LazyLoadBase;
import io.netty.util.AttributeKey;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.Logger;
import io.netty.channel.SimpleChannelInboundHandler;

public class NetworkManager extends SimpleChannelInboundHandler<Packet<?>>
{
    private static final Logger LOGGER;
    public static final Marker NETWORK_MARKER;
    public static final Marker NETWORK_PACKETS_MARKER;
    public static final AttributeKey<EnumConnectionState> PROTOCOL_ATTRIBUTE_KEY;
    public static final LazyLoadBase<NioEventLoopGroup> CLIENT_NIO_EVENTLOOP;
    public static final LazyLoadBase<EpollEventLoopGroup> CLIENT_EPOLL_EVENTLOOP;
    public static final LazyLoadBase<LocalEventLoopGroup> CLIENT_LOCAL_EVENTLOOP;
    private final EnumPacketDirection direction;
    private final Queue<InboundHandlerTuplePacketListener> outboundPacketsQueue;
    private final ReentrantReadWriteLock readWriteLock;
    private Channel channel;
    private SocketAddress socketAddress;
    private INetHandler packetListener;
    private ITextComponent terminationReason;
    private boolean isEncrypted;
    private boolean disconnected;
    
    public NetworkManager(final EnumPacketDirection packetDirection) {
        this.outboundPacketsQueue = (Queue<InboundHandlerTuplePacketListener>)Queues.newConcurrentLinkedQueue();
        this.readWriteLock = new ReentrantReadWriteLock();
        this.direction = packetDirection;
    }
    
    public void channelActive(final ChannelHandlerContext p_channelActive_1_) throws Exception {
        super.channelActive(p_channelActive_1_);
        this.channel = p_channelActive_1_.channel();
        this.socketAddress = this.channel.remoteAddress();
        try {
            this.setConnectionState(EnumConnectionState.HANDSHAKING);
        }
        catch (Throwable throwable) {
            NetworkManager.LOGGER.fatal((Object)throwable);
        }
    }
    
    public void setConnectionState(final EnumConnectionState newState) {
        this.channel.attr((AttributeKey)NetworkManager.PROTOCOL_ATTRIBUTE_KEY).set((Object)newState);
        this.channel.config().setAutoRead(true);
        NetworkManager.LOGGER.debug("Enabled auto read");
    }
    
    public void channelInactive(final ChannelHandlerContext p_channelInactive_1_) throws Exception {
        this.closeChannel(new TextComponentTranslation("disconnect.endOfStream", new Object[0]));
    }
    
    public void exceptionCaught(final ChannelHandlerContext p_exceptionCaught_1_, final Throwable p_exceptionCaught_2_) throws Exception {
        TextComponentTranslation textcomponenttranslation;
        if (p_exceptionCaught_2_ instanceof TimeoutException) {
            textcomponenttranslation = new TextComponentTranslation("disconnect.timeout", new Object[0]);
        }
        else {
            textcomponenttranslation = new TextComponentTranslation("disconnect.genericReason", new Object[] { "Internal Exception: " + p_exceptionCaught_2_ });
        }
        NetworkManager.LOGGER.debug(textcomponenttranslation.getUnformattedText(), p_exceptionCaught_2_);
        this.closeChannel(textcomponenttranslation);
    }
    
    protected void channelRead0(final ChannelHandlerContext p_channelRead0_1_, final Packet<?> p_channelRead0_2_) throws Exception {
        if (this.channel.isOpen()) {
            final EventPacket event = new EventPacket(p_channelRead0_2_);
            EventManager.call(event);
            if (event.isCanceled()) {
                return;
            }
            try {
                p_channelRead0_2_.processPacket(this.packetListener);
            }
            catch (ThreadQuickExitException ex) {}
        }
    }
    
    public void setNetHandler(final INetHandler handler) {
        Validate.notNull((Object)handler, "packetListener", new Object[0]);
        NetworkManager.LOGGER.debug("Set listener of {} to {}", (Object)this, (Object)handler);
        this.packetListener = handler;
    }
    
    public void sendPacket(final Packet<?> packetIn) {
        final EventPacket event = new EventPacket(packetIn);
        EventManager.call(event);
        if (event.isCanceled()) {
            return;
        }
        if (this.isChannelOpen()) {
            this.flushOutboundQueue();
            this.dispatchPacket(packetIn, null);
        }
        else {
            this.readWriteLock.writeLock().lock();
            try {
                this.outboundPacketsQueue.add(new InboundHandlerTuplePacketListener(packetIn, (GenericFutureListener<? extends Future<? super Void>>[])new GenericFutureListener[0]));
            }
            finally {
                this.readWriteLock.writeLock().unlock();
            }
        }
    }
    
    public void sendPacket(final Packet<?> packetIn, final GenericFutureListener<? extends Future<? super Void>> listener, final GenericFutureListener<? extends Future<? super Void>>... listeners) {
        if (this.isChannelOpen()) {
            this.flushOutboundQueue();
            this.dispatchPacket(packetIn, (GenericFutureListener<? extends Future<? super Void>>[])ArrayUtils.add((Object[])listeners, 0, (Object)listener));
        }
        else {
            this.readWriteLock.writeLock().lock();
            try {
                this.outboundPacketsQueue.add(new InboundHandlerTuplePacketListener(packetIn, (GenericFutureListener<? extends Future<? super Void>>[])ArrayUtils.add((Object[])listeners, 0, (Object)listener)));
            }
            finally {
                this.readWriteLock.writeLock().unlock();
            }
        }
    }
    
    private void dispatchPacket(final Packet<?> inPacket, @Nullable final GenericFutureListener<? extends Future<? super Void>>[] futureListeners) {
        final EnumConnectionState enumconnectionstate = EnumConnectionState.getFromPacket(inPacket);
        final EnumConnectionState enumconnectionstate2 = (EnumConnectionState)this.channel.attr((AttributeKey)NetworkManager.PROTOCOL_ATTRIBUTE_KEY).get();
        if (enumconnectionstate2 != enumconnectionstate) {
            NetworkManager.LOGGER.debug("Disabled auto read");
            this.channel.config().setAutoRead(false);
        }
        if (this.channel.eventLoop().inEventLoop()) {
            if (enumconnectionstate != enumconnectionstate2) {
                this.setConnectionState(enumconnectionstate);
            }
            final ChannelFuture channelfuture = this.channel.writeAndFlush((Object)inPacket);
            if (futureListeners != null) {
                channelfuture.addListeners((GenericFutureListener[])futureListeners);
            }
            channelfuture.addListener((GenericFutureListener)ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
        }
        else {
            this.channel.eventLoop().execute((Runnable)new Runnable() {
                @Override
                public void run() {
                    if (enumconnectionstate != enumconnectionstate2) {
                        NetworkManager.this.setConnectionState(enumconnectionstate);
                    }
                    final ChannelFuture channelfuture1 = NetworkManager.this.channel.writeAndFlush((Object)inPacket);
                    if (futureListeners != null) {
                        channelfuture1.addListeners(futureListeners);
                    }
                    channelfuture1.addListener((GenericFutureListener)ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
                }
            });
        }
    }
    
    private void flushOutboundQueue() {
        if (this.channel != null && this.channel.isOpen()) {
            this.readWriteLock.readLock().lock();
            try {
                while (!this.outboundPacketsQueue.isEmpty()) {
                    final InboundHandlerTuplePacketListener networkmanager$inboundhandlertuplepacketlistener = this.outboundPacketsQueue.poll();
                    this.dispatchPacket(networkmanager$inboundhandlertuplepacketlistener.packet, networkmanager$inboundhandlertuplepacketlistener.futureListeners);
                }
            }
            finally {
                this.readWriteLock.readLock().unlock();
            }
        }
    }
    
    public void processReceivedPackets() {
        this.flushOutboundQueue();
        if (this.packetListener instanceof ITickable) {
            ((ITickable)this.packetListener).update();
        }
        if (this.channel != null) {
            this.channel.flush();
        }
    }
    
    public SocketAddress getRemoteAddress() {
        return this.socketAddress;
    }
    
    public void closeChannel(final ITextComponent message) {
        if (this.channel.isOpen()) {
            this.channel.close().awaitUninterruptibly();
            this.terminationReason = message;
        }
    }
    
    public boolean isLocalChannel() {
        return this.channel instanceof LocalChannel || this.channel instanceof LocalServerChannel;
    }
    
    public static NetworkManager createNetworkManagerAndConnect(final InetAddress address, final int serverPort, final boolean useNativeTransport) {
        final NetworkManager networkmanager = new NetworkManager(EnumPacketDirection.CLIENTBOUND);
        Class<? extends SocketChannel> oclass;
        LazyLoadBase<? extends EventLoopGroup> lazyloadbase;
        if (Epoll.isAvailable() && useNativeTransport) {
            oclass = (Class<? extends SocketChannel>)EpollSocketChannel.class;
            lazyloadbase = (LazyLoadBase<? extends EventLoopGroup>)NetworkManager.CLIENT_EPOLL_EVENTLOOP;
        }
        else {
            oclass = (Class<? extends SocketChannel>)NioSocketChannel.class;
            lazyloadbase = (LazyLoadBase<? extends EventLoopGroup>)NetworkManager.CLIENT_NIO_EVENTLOOP;
        }
        ((Bootstrap)((Bootstrap)((Bootstrap)new Bootstrap().group((EventLoopGroup)lazyloadbase.getValue())).handler((ChannelHandler)new ChannelInitializer<Channel>() {
            protected void initChannel(final Channel p_initChannel_1_) throws Exception {
                try {
                    p_initChannel_1_.config().setOption(ChannelOption.TCP_NODELAY, (Object)true);
                }
                catch (ChannelException ex) {}
                p_initChannel_1_.pipeline().addLast("timeout", (ChannelHandler)new ReadTimeoutHandler(30)).addLast("splitter", (ChannelHandler)new NettyVarint21FrameDecoder()).addLast("decoder", (ChannelHandler)new NettyPacketDecoder(EnumPacketDirection.CLIENTBOUND)).addLast("prepender", (ChannelHandler)new NettyVarint21FrameEncoder()).addLast("encoder", (ChannelHandler)new NettyPacketEncoder(EnumPacketDirection.SERVERBOUND)).addLast("packet_handler", (ChannelHandler)networkmanager);
            }
        })).channel((Class)oclass)).connect(address, serverPort).syncUninterruptibly();
        return networkmanager;
    }
    
    public static NetworkManager provideLocalClient(final SocketAddress address) {
        final NetworkManager networkmanager = new NetworkManager(EnumPacketDirection.CLIENTBOUND);
        ((Bootstrap)((Bootstrap)((Bootstrap)new Bootstrap().group((EventLoopGroup)NetworkManager.CLIENT_LOCAL_EVENTLOOP.getValue())).handler((ChannelHandler)new ChannelInitializer<Channel>() {
            protected void initChannel(final Channel p_initChannel_1_) throws Exception {
                p_initChannel_1_.pipeline().addLast("packet_handler", (ChannelHandler)networkmanager);
            }
        })).channel((Class)LocalChannel.class)).connect(address).syncUninterruptibly();
        return networkmanager;
    }
    
    public void enableEncryption(final SecretKey key) {
        this.isEncrypted = true;
        this.channel.pipeline().addBefore("splitter", "decrypt", (ChannelHandler)new NettyEncryptingDecoder(CryptManager.createNetCipherInstance(2, key)));
        this.channel.pipeline().addBefore("prepender", "encrypt", (ChannelHandler)new NettyEncryptingEncoder(CryptManager.createNetCipherInstance(1, key)));
    }
    
    public boolean isEncrypted() {
        return this.isEncrypted;
    }
    
    public boolean isChannelOpen() {
        return this.channel != null && this.channel.isOpen();
    }
    
    public boolean hasNoChannel() {
        return this.channel == null;
    }
    
    public INetHandler getNetHandler() {
        return this.packetListener;
    }
    
    public ITextComponent getExitMessage() {
        return this.terminationReason;
    }
    
    public void disableAutoRead() {
        this.channel.config().setAutoRead(false);
    }
    
    public void setCompressionThreshold(final int threshold) {
        if (threshold >= 0) {
            if (this.channel.pipeline().get("decompress") instanceof NettyCompressionDecoder) {
                ((NettyCompressionDecoder)this.channel.pipeline().get("decompress")).setCompressionThreshold(threshold);
            }
            else {
                this.channel.pipeline().addBefore("decoder", "decompress", (ChannelHandler)new NettyCompressionDecoder(threshold));
            }
            if (this.channel.pipeline().get("compress") instanceof NettyCompressionEncoder) {
                ((NettyCompressionEncoder)this.channel.pipeline().get("compress")).setCompressionThreshold(threshold);
            }
            else {
                this.channel.pipeline().addBefore("encoder", "compress", (ChannelHandler)new NettyCompressionEncoder(threshold));
            }
        }
        else {
            if (this.channel.pipeline().get("decompress") instanceof NettyCompressionDecoder) {
                this.channel.pipeline().remove("decompress");
            }
            if (this.channel.pipeline().get("compress") instanceof NettyCompressionEncoder) {
                this.channel.pipeline().remove("compress");
            }
        }
    }
    
    public void handleDisconnection() {
        if (this.channel != null && !this.channel.isOpen()) {
            if (this.disconnected) {
                NetworkManager.LOGGER.warn("handleDisconnection() called twice");
            }
            else {
                this.disconnected = true;
                if (this.getExitMessage() != null) {
                    this.getNetHandler().onDisconnect(this.getExitMessage());
                }
                else if (this.getNetHandler() != null) {
                    this.getNetHandler().onDisconnect(new TextComponentTranslation("multiplayer.disconnect.generic", new Object[0]));
                }
            }
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
        NETWORK_MARKER = MarkerManager.getMarker("NETWORK");
        NETWORK_PACKETS_MARKER = MarkerManager.getMarker("NETWORK_PACKETS", NetworkManager.NETWORK_MARKER);
        PROTOCOL_ATTRIBUTE_KEY = AttributeKey.valueOf("protocol");
        CLIENT_NIO_EVENTLOOP = new LazyLoadBase<NioEventLoopGroup>() {
            @Override
            protected NioEventLoopGroup load() {
                return new NioEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty Client IO #%d").setDaemon(true).build());
            }
        };
        CLIENT_EPOLL_EVENTLOOP = new LazyLoadBase<EpollEventLoopGroup>() {
            @Override
            protected EpollEventLoopGroup load() {
                return new EpollEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty Epoll Client IO #%d").setDaemon(true).build());
            }
        };
        CLIENT_LOCAL_EVENTLOOP = new LazyLoadBase<LocalEventLoopGroup>() {
            @Override
            protected LocalEventLoopGroup load() {
                return new LocalEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty Local Client IO #%d").setDaemon(true).build());
            }
        };
    }
    
    static class InboundHandlerTuplePacketListener
    {
        private final Packet<?> packet;
        private final GenericFutureListener<? extends Future<? super Void>>[] futureListeners;
        
        public InboundHandlerTuplePacketListener(final Packet<?> inPacket, final GenericFutureListener<? extends Future<? super Void>>... inFutureListeners) {
            this.packet = inPacket;
            this.futureListeners = inFutureListeners;
        }
    }
}
