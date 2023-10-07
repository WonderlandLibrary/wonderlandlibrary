package net.minecraft.network;

import cc.novoline.Novoline;
import cc.novoline.events.EventManager;
import cc.novoline.events.events.PacketEvent;
import cc.novoline.utils.java.Checks;
import cc.novoline.viaversion.utils.Util;
import com.google.common.collect.Queues;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.local.LocalChannel;
import io.netty.channel.local.LocalEventLoopGroup;
import io.netty.channel.local.LocalServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.TimeoutException;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.util.*;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import viaversion.viafabric.ViaFabric;
import viaversion.viafabric.handler.clientside.VRDecodeHandler;
import viaversion.viafabric.handler.clientside.VREncodeHandler;
import viaversion.viafabric.platform.VRClientSideUserConnection;
import viaversion.viafabric.protocol.ViaFabricHostnameProtocol;
import viaversion.viaversion.api.Via;
import viaversion.viaversion.api.data.UserConnection;
import viaversion.viaversion.api.platform.ViaInjector;
import viaversion.viaversion.api.protocol.ProtocolPipeline;

import javax.crypto.SecretKey;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;

import static java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;
import static net.minecraft.network.EnumPacketDirection.CLIENTBOUND;
import static net.minecraft.network.EnumPacketDirection.SERVERBOUND;

public class NetworkManager extends SimpleChannelInboundHandler<Packet> {

    private static final Logger logger = LogManager.getLogger();
    public static final Marker logMarkerNetwork = MarkerManager.getMarker("NETWORK");
    public static final Marker logMarkerPackets = MarkerManager.getMarker("NETWORK_PACKETS", logMarkerNetwork);
    public static final AttributeKey<EnumConnectionState> attrKeyConnectionState = AttributeKey.valueOf("protocol");
    public static final LazyLoadBase<NioEventLoopGroup> CLIENT_NIO_EVENT_LOOP = new LazyLoadBase<NioEventLoopGroup>() {

        protected NioEventLoopGroup load() {
            return new NioEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty Client IO #%d").setDaemon(true).build());
        }
    };
    public static final LazyLoadBase<EpollEventLoopGroup> CLIENT_EPOLL_EVENT_LOOP = new LazyLoadBase<EpollEventLoopGroup>() {

        protected EpollEventLoopGroup load() {
            return new EpollEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty Epoll Client IO #%d").setDaemon(true).build());
        }
    };
    public static final LazyLoadBase<LocalEventLoopGroup> CLIENT_LOCAL_EVENT_LOOP = new LazyLoadBase<LocalEventLoopGroup>() {

        protected LocalEventLoopGroup load() {
            return new LocalEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty Local Client IO #%d").setDaemon(true).build());
        }
    };
    private final EnumPacketDirection direction;
    private final Queue<NetworkManager.InboundHandlerTuplePacketListener> outboundPacketsQueue = Queues.newConcurrentLinkedQueue();
    private final ReentrantReadWriteLock field_181680_j = new ReentrantReadWriteLock();

    /**
     * The active channel
     */
    private Channel channel;

    /**
     * The address of the remote party
     */
    private SocketAddress socketAddress;

    /**
     * The INetHandler instance responsible for processing received packets
     */
    private INetHandler packetListener;

    /**
     * A String indicating why the network has shutdown.
     */
    private IChatComponent terminationReason;
    private boolean isEncrypted;
    private boolean disconnected;

    public NetworkManager(EnumPacketDirection packetDirection) {
        this.direction = packetDirection;
    }

    public void channelActive(ChannelHandlerContext p_channelActive_1_) throws Exception {
        super.channelActive(p_channelActive_1_);
        this.channel = p_channelActive_1_.channel();
        this.socketAddress = channel.remoteAddress();

        try {
            setConnectionState(EnumConnectionState.HANDSHAKING);
        } catch (Throwable throwable) {
            logger.fatal(throwable);
        }
    }

    /**
     * Sets the new connection state and registers which packets this channel may send and receive
     */
    public void setConnectionState(EnumConnectionState newState) {
        channel.attr(attrKeyConnectionState).set(newState);
        channel.config().setAutoRead(true);
        logger.debug("Enabled auto read");
    }

    public void channelInactive(ChannelHandlerContext p_channelInactive_1_) {
        closeChannel(new ChatComponentTranslation("disconnect.endOfStream"));
    }

    public void exceptionCaught(ChannelHandlerContext p_exceptionCaught_1_, Throwable p_exceptionCaught_2_) {
        ChatComponentTranslation chatcomponenttranslation;

        if (p_exceptionCaught_2_ instanceof TimeoutException) {
            chatcomponenttranslation = new ChatComponentTranslation("disconnect.timeout");
        } else {
            chatcomponenttranslation = new ChatComponentTranslation("disconnect.genericReason", "Internal Exception: " + p_exceptionCaught_2_);
        }

        closeChannel(chatcomponenttranslation);
    }

    protected void channelRead0(ChannelHandlerContext p_channelRead0_1_, Packet p_channelRead0_2_) {
        PacketEvent event = new PacketEvent(p_channelRead0_2_, PacketEvent.State.INCOMING);
        if (direction == CLIENTBOUND) EventManager.call(event);

        if (!event.isCancelled()) {
            if (channel.isOpen()) {
                try {
                    p_channelRead0_2_.processPacket(packetListener);
                } catch (ThreadQuickExitException ignored) {
                }
            }
        }
    }

    public void sendPacket(Packet packetIn) {
        PacketEvent event = new PacketEvent(packetIn, PacketEvent.State.OUTGOING);
        if (direction == CLIENTBOUND) EventManager.call(event);

        if (!event.isCancelled()) {
            if (isChannelOpen()) {
                flushOutboundQueue();
                dispatchPacket(packetIn, null);
            } else {
                WriteLock writeLock = field_181680_j.writeLock();
                writeLock.lock();

                try {
                    outboundPacketsQueue.add(new InboundHandlerTuplePacketListener(packetIn, (GenericFutureListener[]) null));
                } finally {
                    writeLock.unlock();
                }
            }
        }
    }

    public void sendPacketNoEvent(Packet packetIn) {
        if (isChannelOpen()) {
            flushOutboundQueue();
            dispatchPacket(packetIn, null);
        } else {
            WriteLock writeLock = field_181680_j.writeLock();
            writeLock.lock();

            try {
                outboundPacketsQueue.add(new NetworkManager.InboundHandlerTuplePacketListener(packetIn, (GenericFutureListener[]) null));
            } finally {
                writeLock.unlock();
            }
        }
    }

    @SafeVarargs
    public final void sendPacket(Packet packetIn, GenericFutureListener<? extends Future<? super Void>>
            listener, GenericFutureListener<? extends Future<? super Void>>... listeners) {
        if (isChannelOpen()) {
            flushOutboundQueue();
            dispatchPacket(packetIn, ArrayUtils.add(listeners, 0, listener));
        } else {
            WriteLock writeLock = field_181680_j.writeLock();
            writeLock.lock();

            try {
                outboundPacketsQueue
                        .add(new NetworkManager.InboundHandlerTuplePacketListener(packetIn, ArrayUtils.add(listeners, 0, listener)));
            } finally {
                writeLock.unlock();
            }
        }
    }

    /**
     * Will commit the packet to the channel. If the current thread 'owns' the channel it will write and flush the
     * packet, otherwise it will add a task for the channel eventloop thread to do that.
     */

    private void dispatchPacket(Packet inPacket,
                                GenericFutureListener<? extends Future<? super Void>>[] futureListeners) {
        Channel channel = this.channel;

        EnumConnectionState packetProtocol = EnumConnectionState.getFromPacket(inPacket);
        EnumConnectionState channelProtocol = channel.attr(attrKeyConnectionState).get();

        if (channelProtocol != packetProtocol) {
            logger.debug("Disabled auto read");
            channel.config().setAutoRead(false);
        }

        if (channel.eventLoop().inEventLoop()) {
            writeUndFlash(inPacket, futureListeners, packetProtocol, channelProtocol);
        } else {
            channel.eventLoop().execute(() -> writeUndFlash(inPacket, futureListeners, packetProtocol, channelProtocol));
        }
    }

    private void writeUndFlash(Packet inPacket,
                               GenericFutureListener<? extends Future<? super Void>>[] futureListeners,
                               EnumConnectionState packetProtocol,
                               EnumConnectionState channelProtocol) {
        if (packetProtocol != channelProtocol) {
            setConnectionState(packetProtocol);
        }

        ChannelFuture channelFuture = channel.writeAndFlush(inPacket);

        if (futureListeners != null) {
            channelFuture.addListeners(futureListeners);
        }

        channelFuture.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    }

    /**
     * Will iterate through the outboundPacketQueue and dispatch all Packets
     */
    private void flushOutboundQueue() {
        if (channel != null && channel.isOpen()) {
            ReadLock readLock = field_181680_j.readLock();
            readLock.lock();

            try {
                while (!outboundPacketsQueue.isEmpty()) {
                    NetworkManager.InboundHandlerTuplePacketListener inboundHandlerTuplePacketListener = outboundPacketsQueue.poll();
                    dispatchPacket(inboundHandlerTuplePacketListener.packet, inboundHandlerTuplePacketListener.futureListeners);
                }
            } finally {
                readLock.unlock();
            }
        }
    }

    /**
     * Checks timeouts and processes all packets received
     */
    public void processReceivedPackets() {
        flushOutboundQueue();
        INetHandler packetListener = this.packetListener;

        if (packetListener instanceof ITickable) {
            ((ITickable) packetListener).update();
        }

        channel.flush();
    }

    /**
     * Returns the socket address of the remote side. Server-only.
     */
    public SocketAddress getRemoteAddress() {
        return socketAddress;
    }

    /**
     * Closes the channel, the parameter can be used for an exit message (not certain how it gets sent)
     */
    public void closeChannel(IChatComponent message) {
        Channel channel = this.channel;

        if (channel.isOpen()) {
            channel.close().awaitUninterruptibly();
            this.terminationReason = message;
        }
    }

    /**
     * True if this NetworkManager uses a memory connection (single player game). False may imply both an active TCP
     * connection or simply no active connection at all
     */
    public boolean isLocalChannel() {
        return channel instanceof LocalChannel || channel instanceof LocalServerChannel;
    }

    public static NetworkManager createNetworkManagerAndConnect(InetAddress address, int port, boolean useNativeTransport) {
        if (ViaFabric.clientSideVersion == Novoline.getInstance().viaVersion()) {
            return linkingVia(address, port, useNativeTransport);
        } else {
            return linking(address, port, useNativeTransport);
        }
    }

    private static @NotNull NetworkManager linking(InetAddress address, int port, boolean useNativeTransport) {
        NetworkManager networkManager = new NetworkManager(CLIENTBOUND);
        LazyLoadBase<? extends EventLoopGroup> lazyLoadBase;
        Class<? extends SocketChannel> socketClass;

        if (Epoll.isAvailable() && useNativeTransport) {
            socketClass = EpollSocketChannel.class;
            lazyLoadBase = CLIENT_EPOLL_EVENT_LOOP;
        } else {
            socketClass = NioSocketChannel.class;
            lazyLoadBase = CLIENT_NIO_EVENT_LOOP;
        }

        new Bootstrap().group(lazyLoadBase.getValue()).handler(new ChannelInitializer<Channel>() {

            @Override
            protected void initChannel(Channel channel) {
                try {
                    channel.config().setOption(ChannelOption.TCP_NODELAY, Boolean.TRUE);
                } catch (ChannelException ignored) {
                }

                ChannelPipeline pipeline = channel.pipeline();

                pipeline.addLast("timeout", new ReadTimeoutHandler(30)) // @off
                        .addLast("splitter", new MessageDeserializer2())
                        .addLast("decoder", new MessageDeserializer(CLIENTBOUND))
                        .addLast("prepender", new MessageSerializer2())
                        .addLast("encoder", new MessageSerializer(SERVERBOUND))
                        .addLast("packet_handler", networkManager); // @on

            }
        }).channel(socketClass).connect(address, port).syncUninterruptibly();

        return networkManager;
    }

    private static @NotNull NetworkManager linkingVia(InetAddress address, int port, boolean useNativeTransport) {
        Class<? extends SocketChannel> socketClass;
        LazyLoadBase<? extends EventLoopGroup> lazyLoadBase;

        if (Epoll.isAvailable() && useNativeTransport) {
            socketClass = EpollSocketChannel.class;
            lazyLoadBase = CLIENT_EPOLL_EVENT_LOOP;
        } else {
            socketClass = NioSocketChannel.class;
            lazyLoadBase = CLIENT_NIO_EVENT_LOOP;
        }

        ViaNetworkManager networkManager = ViaNetworkManager.create(CLIENTBOUND);

        new Bootstrap().group(lazyLoadBase.getValue()).handler(new ChannelInitializer<Channel>() {

            @Override
            protected void initChannel(Channel channel) {
                if (!(channel instanceof SocketChannel)) {
                    throw new IllegalStateException();
                }

                UserConnection userConnection = new VRClientSideUserConnection(channel);
                networkManager.setUserConnection(userConnection);
                new ProtocolPipeline(userConnection).add(ViaFabricHostnameProtocol.INSTANCE);

                ViaInjector injector = Via.getManager().getInjector();

                try {
                    channel.config().setOption(ChannelOption.TCP_NODELAY, Boolean.TRUE);
                } catch (ChannelException ignored) {
                }

                ChannelPipeline pipeline = channel.pipeline();

                pipeline.addLast("timeout", new ReadTimeoutHandler(30)) // @off
                        .addLast("splitter", new MessageDeserializer2())
                        //custom decoder
                        .addLast("decoder", new MessageDeserializer(CLIENTBOUND))
                        .addLast("prepender", new MessageSerializer2())
                        //custom encoder
                        .addLast("encoder", new MessageSerializer(SERVERBOUND))
                        .addLast("packet_handler", networkManager); // @on

                channel.pipeline().addBefore("encoder", injector.getEncoderName(), new VREncodeHandler(userConnection))
                        .addBefore("decoder", injector.getDecoderName(), new VRDecodeHandler(userConnection));
                // TODO 28 Jan 2021: addBefore -> addLast.
            }
        }).channel(socketClass).connect(address, port).syncUninterruptibly();

        return networkManager;
    }

    public static class ViaNetworkManager extends NetworkManager {

        private UserConnection userConnection;
        private final Lock readLock, writeLock;

        private ViaNetworkManager(@NotNull EnumPacketDirection packetDirection,
                                  @NotNull Lock readLock,
                                  @NotNull Lock writeLock) {
            super(packetDirection);
            this.readLock = readLock;
            this.writeLock = writeLock;
        }

        @Contract("_ -> new")
        public static @NotNull ViaNetworkManager create(@NotNull EnumPacketDirection packetDirection) {
            Checks.notNull(packetDirection, "Direction");

            ReadWriteLock rwLock = new ReentrantReadWriteLock();
            return new ViaNetworkManager(packetDirection, rwLock.readLock(), rwLock.writeLock());
        }

        @Contract(mutates = "this")
        public void setUserConnection(@NotNull UserConnection userConnection) {
            Checks.notNull(userConnection, "User connection");

            Lock readLock = this.readLock;
            readLock.lock();

            UserConnection local;

            try {
                local = this.userConnection;
            } finally {
                readLock.unlock();
            }

            if (local != null) {
                throw new IllegalStateException("User connection cannot be set twice");
            } else {
                Lock writeLock = this.writeLock;
                writeLock.lock();

                try {
                    local = this.userConnection;

                    //noinspection VariableNotUsedInsideIf
                    if (local != null) {
                        throw new IllegalStateException("User connection cannot be set twice");
                    } else {
                        this.userConnection = userConnection;
                    }
                } finally {
                    writeLock.unlock();
                }
            }
        }

        @Contract(pure = true)
        public @NotNull UserConnection getUserConnection() {
            Lock readLock = this.readLock;
            readLock.lock();

            try {
                return userConnection;
            } finally {
                readLock.unlock();
            }
        }
    }

    /**
     * Prepares a client-side NetworkManager: establishes a connection to the socket supplied and configures the channel
     * pipeline. Returns the newly created instance.
     */
    public static @NotNull NetworkManager provideLocalClient(@NotNull SocketAddress address) {
        NetworkManager networkManager = new NetworkManager(CLIENTBOUND);

        new Bootstrap().group(CLIENT_LOCAL_EVENT_LOOP.getValue()).handler(new ChannelInitializer<Channel>() {

            @Override
            protected void initChannel(Channel channel) {
                channel.pipeline().addLast("packet_handler", networkManager);
            }
        }).channel(LocalChannel.class).connect(address).syncUninterruptibly();

        return networkManager;
    }

    /**
     * Adds an encoder+decoder to the channel pipeline. The parameter is the secret key used for encrypted communication
     */
    public void enableEncryption(SecretKey key) {
        this.isEncrypted = true;
        channel.pipeline().addBefore("splitter", "decrypt", new NettyEncryptingDecoder(CryptManager.createNetCipherInstance(2, key)));
        channel.pipeline().addBefore("prepender", "encrypt", new NettyEncryptingEncoder(CryptManager.createNetCipherInstance(1, key)));
    }

    public boolean getIsencrypted() {
        return isEncrypted;
    }

    /**
     * Returns true if this NetworkManager has an active channel, false otherwise
     */
    public boolean isChannelOpen() {
        return channel != null && channel.isOpen();
    }

    public boolean hasNoChannel() {
        return channel == null;
    }

    /**
     * Gets the current handler for processing packets
     */
    public INetHandler getNetHandler() {
        return packetListener;
    }

    /**
     * Sets the NetHandler for this NetworkManager, no checks are made if this handler is suitable for the particular
     * connection state (protocol)
     */
    public void setNetHandler(INetHandler handler) {
        Validate.notNull(handler, "packetListener");
        logger.debug("Set listener of {} to {}", this, handler);
        this.packetListener = handler;
    }

    /**
     * If this channel is closed, returns the exit message, null otherwise.
     */
    public IChatComponent getExitMessage() {
        return terminationReason;
    }

    /**
     * Switches the channel to manual reading modus
     */
    public void disableAutoRead() {
        channel.config().setAutoRead(false);
    }

    public void setCompressionTreshold(int treshold) {
        if (ViaFabric.clientSideVersion == Novoline.getInstance().viaVersion()) {
            setCompressionTresholdVia(treshold);
        } else {
            setCompressionTresholdVanilla(treshold);
        }
    }

    public void setCompressionTresholdVanilla(int treshold) {
        if (treshold >= 0) {
            if (channel.pipeline().get("decompress") instanceof NettyCompressionDecoder) {
                ((NettyCompressionDecoder) channel.pipeline().get("decompress")).setCompressionTreshold(treshold);
            } else {
                channel.pipeline().addBefore("decoder", "decompress", new NettyCompressionDecoder(treshold));
            }

            if (channel.pipeline().get("compress") instanceof NettyCompressionEncoder) {
                ((NettyCompressionEncoder) channel.pipeline().get("decompress")).setCompressionTreshold(treshold);
            } else {
                channel.pipeline().addBefore("encoder", "compress", new NettyCompressionEncoder(treshold));
            }

        } else {
            if (channel.pipeline().get("decompress") instanceof NettyCompressionDecoder) {
                channel.pipeline().remove("decompress");
            }

            if (channel.pipeline().get("compress") instanceof NettyCompressionEncoder) {
                channel.pipeline().remove("compress");
            }
        }
    }

    public void setCompressionTresholdVia(int treshold) {
        if (treshold >= 0) {
            if (channel.pipeline().get("decompress") instanceof NettyCompressionDecoder) {
                ((NettyCompressionDecoder) channel.pipeline().get("decompress")).setCompressionTreshold(treshold);
            } else {
                Util.decodeEncodePlacement(channel.pipeline(), "decoder", "decompress", new NettyCompressionDecoder(treshold));
            }

            if (channel.pipeline().get("compress") instanceof NettyCompressionEncoder) {
                ((NettyCompressionEncoder) channel.pipeline().get("decompress")).setCompressionTreshold(treshold);
            } else {
                Util.decodeEncodePlacement(channel.pipeline(), "encoder", "compress", new NettyCompressionEncoder(treshold));
            }

        } else {
            if (channel.pipeline().get("decompress") instanceof NettyCompressionDecoder) {
                channel.pipeline().remove("decompress");
            }

            if (channel.pipeline().get("compress") instanceof NettyCompressionEncoder) {
                channel.pipeline().remove("compress");
            }
        }
    }

    public void checkDisconnected() {
        if (channel != null && !channel.isOpen()) {
            if (!disconnected) {
                this.disconnected = true;

                if (getExitMessage() != null) {
                    getNetHandler().onDisconnect(getExitMessage());
                } else if (getNetHandler() != null) {
                    getNetHandler().onDisconnect(new ChatComponentText("Disconnected"));
                }
            } else {
                logger.warn("handleDisconnection() called twice");
            }
        }
    }

    static class InboundHandlerTuplePacketListener {

        private final Packet packet;
        private final GenericFutureListener<? extends Future<? super Void>>[] futureListeners;

        @SafeVarargs
        public InboundHandlerTuplePacketListener(Packet inPacket, GenericFutureListener<? extends Future<? super Void>>... inFutureListeners) {
            this.packet = inPacket;
            this.futureListeners = inFutureListeners;
        }
    }
}
