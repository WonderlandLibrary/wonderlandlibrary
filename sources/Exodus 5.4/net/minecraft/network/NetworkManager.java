/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Queues
 *  com.google.common.util.concurrent.ThreadFactoryBuilder
 *  org.apache.commons.lang3.ArrayUtils
 *  org.apache.commons.lang3.Validate
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 *  org.apache.logging.log4j.Marker
 *  org.apache.logging.log4j.MarkerManager
 */
package net.minecraft.network;

import com.google.common.collect.Queues;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.MultithreadEventLoopGroup;
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
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.event.events.EventPacket;
import me.Tengoku.Terror.event.events.EventSendPacket;
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
    private static final Logger logger = LogManager.getLogger();
    public static final LazyLoadBase<NioEventLoopGroup> CLIENT_NIO_EVENTLOOP;
    public static final Marker logMarkerNetwork;
    private SocketAddress socketAddress;
    private final Queue<InboundHandlerTuplePacketListener> outboundPacketsQueue = Queues.newConcurrentLinkedQueue();
    private boolean isEncrypted;
    private final ReentrantReadWriteLock field_181680_j = new ReentrantReadWriteLock();
    private final EnumPacketDirection direction;
    private boolean disconnected;
    public static final LazyLoadBase<LocalEventLoopGroup> CLIENT_LOCAL_EVENTLOOP;
    public static final AttributeKey<EnumConnectionState> attrKeyConnectionState;
    private IChatComponent terminationReason;
    public static final LazyLoadBase<EpollEventLoopGroup> field_181125_e;
    public static final Marker logMarkerPackets;
    private INetHandler packetListener;
    private Channel channel;

    public void checkDisconnected() {
        if (this.channel != null && !this.channel.isOpen()) {
            if (!this.disconnected) {
                this.disconnected = true;
                if (this.getExitMessage() != null) {
                    this.getNetHandler().onDisconnect(this.getExitMessage());
                } else if (this.getNetHandler() != null) {
                    this.getNetHandler().onDisconnect(new ChatComponentText("Disconnected"));
                }
            } else {
                logger.warn("handleDisconnection() called twice");
            }
        }
    }

    public static NetworkManager provideLocalClient(SocketAddress socketAddress) {
        final NetworkManager networkManager = new NetworkManager(EnumPacketDirection.CLIENTBOUND);
        ((Bootstrap)((Bootstrap)((Bootstrap)new Bootstrap().group(CLIENT_LOCAL_EVENTLOOP.getValue())).handler(new ChannelInitializer<Channel>(){

            @Override
            protected void initChannel(Channel channel) throws Exception {
                channel.pipeline().addLast("packet_handler", (ChannelHandler)networkManager);
            }
        })).channel(LocalChannel.class)).connect(socketAddress).syncUninterruptibly();
        return networkManager;
    }

    public boolean isLocalChannel() {
        return this.channel instanceof LocalChannel || this.channel instanceof LocalServerChannel;
    }

    public boolean isChannelOpen() {
        return this.channel != null && this.channel.isOpen();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Packet packet) throws Exception {
        if (this.channel.isOpen()) {
            try {
                EventPacket eventPacket = new EventPacket(packet, true);
                eventPacket.call();
                if (!eventPacket.isCancelled()) {
                    packet.processPacket(this.packetListener);
                }
            }
            catch (ThreadQuickExitException threadQuickExitException) {
                // empty catch block
            }
        }
    }

    private void flushOutboundQueue() {
        if (this.channel != null && this.channel.isOpen()) {
            this.field_181680_j.readLock().lock();
            while (!this.outboundPacketsQueue.isEmpty()) {
                InboundHandlerTuplePacketListener inboundHandlerTuplePacketListener = this.outboundPacketsQueue.poll();
                this.dispatchPacket(inboundHandlerTuplePacketListener.packet, inboundHandlerTuplePacketListener.futureListeners);
            }
            this.field_181680_j.readLock().unlock();
        }
    }

    public IChatComponent getExitMessage() {
        return this.terminationReason;
    }

    public void enableEncryption(SecretKey secretKey) {
        this.isEncrypted = true;
        this.channel.pipeline().addBefore("splitter", "decrypt", new NettyEncryptingDecoder(CryptManager.createNetCipherInstance(2, secretKey)));
        this.channel.pipeline().addBefore("prepender", "encrypt", new NettyEncryptingEncoder(CryptManager.createNetCipherInstance(1, secretKey)));
    }

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {
        super.channelActive(channelHandlerContext);
        this.channel = channelHandlerContext.channel();
        this.socketAddress = this.channel.remoteAddress();
        try {
            this.setConnectionState(EnumConnectionState.HANDSHAKING);
        }
        catch (Throwable throwable) {
            logger.fatal((Object)throwable);
        }
    }

    public boolean getIsencrypted() {
        return this.isEncrypted;
    }

    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {
        this.closeChannel(new ChatComponentTranslation("disconnect.endOfStream", new Object[0]));
    }

    public SocketAddress getRemoteAddress() {
        return this.socketAddress;
    }

    public void processReceivedPackets() {
        this.flushOutboundQueue();
        if (this.packetListener instanceof ITickable) {
            ((ITickable)((Object)this.packetListener)).update();
        }
        this.channel.flush();
    }

    public void setNetHandler(INetHandler iNetHandler) {
        Validate.notNull((Object)iNetHandler, (String)"packetListener", (Object[])new Object[0]);
        logger.debug("Set listener of {} to {}", new Object[]{this, iNetHandler});
        this.packetListener = iNetHandler;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {
        ChatComponentTranslation chatComponentTranslation = throwable instanceof TimeoutException ? new ChatComponentTranslation("disconnect.timeout", new Object[0]) : new ChatComponentTranslation("disconnect.genericReason", "Internal Exception: " + throwable);
        this.closeChannel(chatComponentTranslation);
    }

    static {
        logMarkerNetwork = MarkerManager.getMarker((String)"NETWORK");
        logMarkerPackets = MarkerManager.getMarker((String)"NETWORK_PACKETS", (Marker)logMarkerNetwork);
        attrKeyConnectionState = AttributeKey.valueOf("protocol");
        CLIENT_NIO_EVENTLOOP = new LazyLoadBase<NioEventLoopGroup>(){

            @Override
            protected NioEventLoopGroup load() {
                return new NioEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty Client IO #%d").setDaemon(true).build());
            }
        };
        field_181125_e = new LazyLoadBase<EpollEventLoopGroup>(){

            @Override
            protected EpollEventLoopGroup load() {
                return new EpollEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty Epoll Client IO #%d").setDaemon(true).build());
            }
        };
        CLIENT_LOCAL_EVENTLOOP = new LazyLoadBase<LocalEventLoopGroup>(){

            @Override
            protected LocalEventLoopGroup load() {
                return new LocalEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty Local Client IO #%d").setDaemon(true).build());
            }
        };
    }

    public void disableAutoRead() {
        this.channel.config().setAutoRead(false);
    }

    private void dispatchPacket(final Packet packet, final GenericFutureListener<? extends Future<? super Void>>[] genericFutureListenerArray) {
        final EnumConnectionState enumConnectionState = EnumConnectionState.getFromPacket(packet);
        final EnumConnectionState enumConnectionState2 = this.channel.attr(attrKeyConnectionState).get();
        if (enumConnectionState2 != enumConnectionState) {
            logger.debug("Disabled auto read");
            this.channel.config().setAutoRead(false);
        }
        if (this.channel.eventLoop().inEventLoop()) {
            if (enumConnectionState != enumConnectionState2) {
                this.setConnectionState(enumConnectionState);
            }
            ChannelFuture channelFuture = this.channel.writeAndFlush(packet);
            if (genericFutureListenerArray != null) {
                channelFuture.addListeners(genericFutureListenerArray);
            }
            channelFuture.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
        } else {
            this.channel.eventLoop().execute(new Runnable(){

                @Override
                public void run() {
                    if (enumConnectionState != enumConnectionState2) {
                        NetworkManager.this.setConnectionState(enumConnectionState);
                    }
                    ChannelFuture channelFuture = NetworkManager.this.channel.writeAndFlush(packet);
                    if (genericFutureListenerArray != null) {
                        channelFuture.addListeners(genericFutureListenerArray);
                    }
                    channelFuture.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
                }
            });
        }
    }

    public void setCompressionTreshold(int n) {
        if (n >= 0) {
            if (this.channel.pipeline().get("decompress") instanceof NettyCompressionDecoder) {
                ((NettyCompressionDecoder)this.channel.pipeline().get("decompress")).setCompressionTreshold(n);
            } else {
                this.channel.pipeline().addBefore("decoder", "decompress", new NettyCompressionDecoder(n));
            }
            if (this.channel.pipeline().get("compress") instanceof NettyCompressionEncoder) {
                ((NettyCompressionEncoder)this.channel.pipeline().get("decompress")).setCompressionTreshold(n);
            } else {
                this.channel.pipeline().addBefore("encoder", "compress", new NettyCompressionEncoder(n));
            }
        } else {
            if (this.channel.pipeline().get("decompress") instanceof NettyCompressionDecoder) {
                this.channel.pipeline().remove("decompress");
            }
            if (this.channel.pipeline().get("compress") instanceof NettyCompressionEncoder) {
                this.channel.pipeline().remove("compress");
            }
        }
    }

    public void sendPacket(Packet packet, GenericFutureListener<? extends Future<? super Void>> genericFutureListener, GenericFutureListener<? extends Future<? super Void>> ... genericFutureListenerArray) {
        if (this.isChannelOpen()) {
            this.flushOutboundQueue();
            this.dispatchPacket(packet, (GenericFutureListener[])ArrayUtils.add((Object[])genericFutureListenerArray, (int)0, genericFutureListener));
        } else {
            this.field_181680_j.writeLock().lock();
            this.outboundPacketsQueue.add(new InboundHandlerTuplePacketListener(packet, (GenericFutureListener[])ArrayUtils.add((Object[])genericFutureListenerArray, (int)0, genericFutureListener)));
            this.field_181680_j.writeLock().unlock();
        }
    }

    public void sendPacket(Packet packet) {
        EventPacket eventPacket = new EventPacket(packet, false);
        eventPacket.call();
        if (this.isChannelOpen()) {
            EventSendPacket eventSendPacket = new EventSendPacket(packet);
            eventSendPacket.call();
            Exodus.onEvent(eventSendPacket);
            this.flushOutboundQueue();
            if (!eventSendPacket.isCancelled()) {
                this.dispatchPacket(packet, null);
            }
        } else {
            this.field_181680_j.writeLock().lock();
            this.outboundPacketsQueue.add(new InboundHandlerTuplePacketListener(packet, null));
            this.field_181680_j.writeLock().unlock();
        }
    }

    public NetworkManager(EnumPacketDirection enumPacketDirection) {
        this.direction = enumPacketDirection;
    }

    public void setConnectionState(EnumConnectionState enumConnectionState) {
        this.channel.attr(attrKeyConnectionState).set(enumConnectionState);
        this.channel.config().setAutoRead(true);
        logger.debug("Enabled auto read");
    }

    public static NetworkManager func_181124_a(InetAddress inetAddress, int n, boolean bl) {
        LazyLoadBase<MultithreadEventLoopGroup> lazyLoadBase;
        Class clazz;
        final NetworkManager networkManager = new NetworkManager(EnumPacketDirection.CLIENTBOUND);
        if (Epoll.isAvailable() && bl) {
            clazz = EpollSocketChannel.class;
            lazyLoadBase = field_181125_e;
        } else {
            clazz = NioSocketChannel.class;
            lazyLoadBase = CLIENT_NIO_EVENTLOOP;
        }
        ((Bootstrap)((Bootstrap)((Bootstrap)new Bootstrap().group(lazyLoadBase.getValue())).handler(new ChannelInitializer<Channel>(){

            @Override
            protected void initChannel(Channel channel) throws Exception {
                try {
                    channel.config().setOption(ChannelOption.TCP_NODELAY, true);
                }
                catch (ChannelException channelException) {
                    // empty catch block
                }
                channel.pipeline().addLast("timeout", (ChannelHandler)new ReadTimeoutHandler(30)).addLast("splitter", (ChannelHandler)new MessageDeserializer2()).addLast("decoder", (ChannelHandler)new MessageDeserializer(EnumPacketDirection.CLIENTBOUND)).addLast("prepender", (ChannelHandler)new MessageSerializer2()).addLast("encoder", (ChannelHandler)new MessageSerializer(EnumPacketDirection.SERVERBOUND)).addLast("packet_handler", (ChannelHandler)networkManager);
            }
        })).channel(clazz)).connect(inetAddress, n).syncUninterruptibly();
        return networkManager;
    }

    public INetHandler getNetHandler() {
        return this.packetListener;
    }

    public void closeChannel(IChatComponent iChatComponent) {
        if (this.channel.isOpen()) {
            this.channel.close().awaitUninterruptibly();
            this.terminationReason = iChatComponent;
        }
    }

    public boolean hasNoChannel() {
        return this.channel == null;
    }

    static class InboundHandlerTuplePacketListener {
        private final GenericFutureListener<? extends Future<? super Void>>[] futureListeners;
        private final Packet packet;

        public InboundHandlerTuplePacketListener(Packet packet, GenericFutureListener<? extends Future<? super Void>> ... genericFutureListenerArray) {
            this.packet = packet;
            this.futureListeners = genericFutureListenerArray;
        }
    }
}

