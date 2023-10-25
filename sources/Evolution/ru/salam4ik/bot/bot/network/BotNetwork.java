package ru.salam4ik.bot.bot.network;

import com.google.common.collect.Queues;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.local.LocalChannel;
import io.netty.channel.local.LocalServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.CodecException;
import io.netty.handler.proxy.Socks4ProxyHandler;
import io.netty.handler.proxy.Socks5ProxyHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.TimeoutException;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.*;
import net.minecraft.util.ITickable;
import net.minecraft.util.LazyLoadBase;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;
import wtf.evolution.bot.ProxyS;

import javax.annotation.Nullable;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.channels.ClosedChannelException;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static wtf.evolution.bot.ProxyS.isPrivate;

public class BotNetwork
        extends SimpleChannelInboundHandler<Packet<?>> {
    private boolean disconnected;
    private Channel channel;
    public String password;
    public static final AttributeKey<EnumConnectionState> PROTOCOL_ATTRIBUTE_KEY;
    public static final LazyLoadBase<NioEventLoopGroup> CLIENT_NIO_EVENTLOOP;
    private INetHandler packetListener;
    public boolean connection;
    public static final LazyLoadBase<EpollEventLoopGroup> CLIENT_EPOLL_EVENTLOOP;
    private final Queue<InboundHandlerTuplePacketListener> outboundPacketsQueue;
    private final EnumPacketDirection direction;
    private final ReentrantReadWriteLock readWriteLock;

    public ProxyS.Proxy p;

    public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {
        super.channelActive(channelHandlerContext);
        this.channel = channelHandlerContext.channel();
        try {
            this.setConnectionState(EnumConnectionState.HANDSHAKING);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    static {
        PROTOCOL_ATTRIBUTE_KEY = AttributeKey.valueOf((String)"protocol");
        CLIENT_NIO_EVENTLOOP = new LazyLoadBase<NioEventLoopGroup>(){

            @Override
            protected NioEventLoopGroup load() {
                return new NioEventLoopGroup();
            }
        };
        CLIENT_EPOLL_EVENTLOOP = new LazyLoadBase<EpollEventLoopGroup>(){

            @Override
            protected EpollEventLoopGroup load() {
                return new EpollEventLoopGroup();
            }
        };
    }

    public static BotNetwork createNetworkManagerAndConnect(InetAddress inetAddress, int n, final ProxyS.Proxy proxy) {
        final BotNetwork botNetwork = new BotNetwork(EnumPacketDirection.CLIENTBOUND, proxy);

        new Bootstrap().group(CLIENT_NIO_EVENTLOOP.getValue()).handler(new ChannelInitializer(){

            protected void initChannel(Channel channel) {
                try {
                    channel.config().setOption(ChannelOption.TCP_NODELAY, true);
                }
                catch (ChannelException channelException) {
                    // empty catch block
                }
                if (proxy != null) {
                    if (!isPrivate) {
                        if (proxy.getType() == ProxyS.ProxyType.SOCKS5) {
                            Socks5ProxyHandler socks5 = new Socks5ProxyHandler(proxy.getAddress());
                            socks5.setConnectTimeoutMillis(9500L);
                            channel.pipeline().addLast(socks5);
                        }
                        if (proxy.getType() == ProxyS.ProxyType.SOCKS4) {
                            Socks4ProxyHandler socks4 = new Socks4ProxyHandler(proxy.getAddress());
                            socks4.setConnectTimeoutMillis(9500L);
                            channel.pipeline().addLast(socks4);
                        }
                    } else {
                        if (proxy.getType() == ProxyS.ProxyType.SOCKS5) {
                            Socks5ProxyHandler socks5 = new Socks5ProxyHandler(proxy.getAddress(), "i700rp1020412", "z2zbuyfgtv");
                            socks5.setConnectTimeoutMillis(9500L);
                            channel.pipeline().addLast(socks5);
                        }
                    }
//                        Socks5ProxyHandler socks4 = new Socks5ProxyHandler(proxy.getAddress(), "ir3p1020327", "vN9ojZu8iI");
//                        socks4.setConnectTimeoutMillis(9500L);
//                        channel.pipeline().addLast(socks4);
                }
                channel.pipeline().addLast("timeout", (ChannelHandler)new ReadTimeoutHandler(30)).addLast("splitter", (ChannelHandler)new NettyVarint21FrameDecoder()).addLast("decoder", (ChannelHandler)new NettyPacketDecoder(EnumPacketDirection.CLIENTBOUND)).addLast("prepender", (ChannelHandler)new NettyVarint21FrameEncoder()).addLast("encoder", (ChannelHandler)new NettyPacketEncoder(EnumPacketDirection.SERVERBOUND)).addLast("packet_handler", (ChannelHandler)botNetwork);
            }
        }).channel(NioSocketChannel.class).connect(inetAddress, n).syncUninterruptibly();
        return botNetwork;
    }

    private void dispatchPacket(Packet<?> packet, @Nullable GenericFutureListener<? extends Future<? super Void>>[] genericFutureListenerArray) {
        EnumConnectionState enumConnectionState = EnumConnectionState.getFromPacket(packet);
        EnumConnectionState enumConnectionState2 = (EnumConnectionState)((Object)this.channel.attr(PROTOCOL_ATTRIBUTE_KEY).get());
        if (enumConnectionState2 != enumConnectionState) {
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
            channelFuture.addListener((GenericFutureListener)ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
        } else {
            this.channel.eventLoop().execute(() -> {
                if (enumConnectionState != enumConnectionState2) {
                    this.setConnectionState(enumConnectionState);
                }
                ChannelFuture channelFuture = this.channel.writeAndFlush((Object)packet);
                if (genericFutureListenerArray != null) {
                    channelFuture.addListeners(genericFutureListenerArray);
                }
                channelFuture.addListener((GenericFutureListener)ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
            });
        }
    }

    public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {
    }

    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Packet<?> packet) throws Exception {
        if (this.channel.isOpen()) {
            try {
                ((Packet<INetHandler>)packet).processPacket(this.packetListener);
            }
            catch (ThreadQuickExitException threadQuickExitException) {
                // empty catch block
            }
        }
    }

    public void sendPacket(Packet<?> packet) {
        if (this.isChannelOpen()) {
            this.flushOutboundQueue();
            this.dispatchPacket(packet, null);
        } else {
            this.readWriteLock.writeLock().lock();
            try {
                this.outboundPacketsQueue.add(new InboundHandlerTuplePacketListener(packet, new GenericFutureListener[0]));
            }
            finally {
                this.readWriteLock.writeLock().unlock();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @SafeVarargs
    public final void sendPacket(Packet<?> packet, GenericFutureListener<? extends Future<? super Void>> genericFutureListener, GenericFutureListener<? extends Future<? super Void>> ... genericFutureListenerArray) {
        if (this.isChannelOpen()) {
            this.flushOutboundQueue();
            this.dispatchPacket(packet, ArrayUtils.add(genericFutureListenerArray, 0, genericFutureListener));
        } else {
            this.readWriteLock.writeLock().lock();
            try {
                this.outboundPacketsQueue.add(new InboundHandlerTuplePacketListener(packet, ArrayUtils.add(genericFutureListenerArray, 0, genericFutureListener)));
            }
            finally {
                this.readWriteLock.writeLock().unlock();
            }
        }
    }

    public BotNetwork(EnumPacketDirection enumPacketDirection, ProxyS.Proxy p) {
        this.outboundPacketsQueue = Queues.newConcurrentLinkedQueue();
        this.readWriteLock = new ReentrantReadWriteLock();
        this.connection = false;
        this.direction = enumPacketDirection;
        this.p = p;
    }

    public void setConnectionState(EnumConnectionState enumConnectionState) {
        this.channel.attr(PROTOCOL_ATTRIBUTE_KEY).set(enumConnectionState);
        this.channel.config().setAutoRead(true);
    }

    public INetHandler getNetHandler() {
        return this.packetListener;
    }

    public void setCompressionThreshold(int n) {
        if (n >= 0) {
            if (this.channel.pipeline().get("decompress") instanceof NettyCompressionDecoder) {
                ((NettyCompressionDecoder)this.channel.pipeline().get("decompress")).setCompressionThreshold(n);
            } else {
                this.channel.pipeline().addBefore("decoder", "decompress", (ChannelHandler)new NettyCompressionDecoder(n));
            }
            if (this.channel.pipeline().get("compress") instanceof NettyCompressionEncoder) {
                ((NettyCompressionEncoder)this.channel.pipeline().get("compress")).setCompressionThreshold(n);
            } else {
                this.channel.pipeline().addBefore("encoder", "compress", (ChannelHandler)new NettyCompressionEncoder(n));
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

    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {
        if (throwable instanceof CodecException || throwable instanceof SocketException || throwable instanceof TimeoutException || !(throwable instanceof ClosedChannelException)) {
            // empty if block
        }
    }

    public boolean isLocalChannel() {
        return this.channel instanceof LocalChannel || this.channel instanceof LocalServerChannel;
    }

    public void disableAutoRead() {
        this.channel.config().setAutoRead(false);
    }

    public void tick() {
        this.flushOutboundQueue();
        if (this.packetListener instanceof ITickable) {
            ((ITickable)((Object)this.packetListener)).update();
        }
        if (this.channel != null) {
            this.channel.flush();
        }
    }

    public void handleDisconnection() {
        if (this.channel != null && !this.channel.isOpen() && !this.disconnected) {
            this.disconnected = true;
            this.getNetHandler().onDisconnect(null);
        }
    }

    public void setNetHandler(INetHandler iNetHandler) {
        Validate.notNull(iNetHandler, "packetListener", new Object[0]);
        this.packetListener = iNetHandler;
    }

    public boolean isChannelOpen() {
        return this.channel != null && this.channel.isOpen();
    }

    public void closeChannel() {
        if (this.channel.isOpen()) {
            try {
                try {
                    this.channel.close().sync();
                }
                catch (Exception exception) {
                    this.channel.close();
                }
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
    }

    public boolean hasNoChannel() {
        return this.channel == null;
    }

    private void flushOutboundQueue() {
        if (this.channel != null && this.channel.isOpen()) {
            this.readWriteLock.readLock().lock();
            try {
                while (!this.outboundPacketsQueue.isEmpty()) {
                    InboundHandlerTuplePacketListener inboundHandlerTuplePacketListener = this.outboundPacketsQueue.poll();
                    this.dispatchPacket(inboundHandlerTuplePacketListener.packet, inboundHandlerTuplePacketListener.listener);
                }
            }
            finally {
                this.readWriteLock.readLock().unlock();
            }
        }
    }

    static class InboundHandlerTuplePacketListener {
        private final GenericFutureListener<? extends Future<? super Void>>[] listener;
        private final Packet<?> packet;

        @SafeVarargs
        public InboundHandlerTuplePacketListener(Packet<?> packet, GenericFutureListener<? extends Future<? super Void>> ... genericFutureListenerArray) {
            this.packet = packet;
            this.listener = genericFutureListenerArray;
        }
    }
}