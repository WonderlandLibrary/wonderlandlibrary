// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network;

import io.netty.bootstrap.AbstractBootstrap;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.LogManager;
import net.minecraft.util.ChatComponentText;
import viamcp.utils.NettyUtil;
import java.security.Key;
import net.minecraft.util.CryptManager;
import javax.crypto.SecretKey;
import com.viaversion.viaversion.api.connection.UserConnection;
import viamcp.handler.MCPDecodeHandler;
import viamcp.handler.MCPEncodeHandler;
import com.viaversion.viaversion.protocol.ProtocolPipelineImpl;
import com.viaversion.viaversion.connection.UserConnectionImpl;
import viamcp.ViaMCP;
import io.netty.channel.socket.SocketChannel;
import viamcp.gui.GuiProtocolSelector;
import net.minecraft.util.MessageSerializer;
import net.minecraft.util.MessageSerializer2;
import net.minecraft.util.MessageDeserializer;
import net.minecraft.util.MessageDeserializer2;
import io.netty.channel.ChannelHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelInitializer;
import io.netty.bootstrap.ChannelFactory;
import net.augustus.utils.OioProxyChannelFactory;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import net.augustus.Augustus;
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
import org.apache.commons.lang3.ArrayUtils;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.util.Vec3;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.apache.commons.lang3.Validate;
import net.lenni0451.eventapi.events.IEvent;
import net.augustus.utils.EventHandler;
import net.augustus.events.EventReadPacket;
import io.netty.handler.timeout.TimeoutException;
import net.minecraft.util.ChatComponentTranslation;
import io.netty.channel.ChannelHandlerContext;
import com.google.common.collect.Queues;
import net.minecraft.util.IChatComponent;
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

public class NetworkManager extends SimpleChannelInboundHandler<Packet>
{
    private static final Logger logger;
    public static final Marker logMarkerNetwork;
    public static final Marker logMarkerPackets;
    public static final AttributeKey<EnumConnectionState> attrKeyConnectionState;
    public static final LazyLoadBase<NioEventLoopGroup> CLIENT_NIO_EVENTLOOP;
    public static final LazyLoadBase<EpollEventLoopGroup> field_181125_e;
    public static final LazyLoadBase<LocalEventLoopGroup> CLIENT_LOCAL_EVENTLOOP;
    private final EnumPacketDirection direction;
    private final Queue<InboundHandlerTuplePacketListener> outboundPacketsQueue;
    private final ReentrantReadWriteLock field_181680_j;
    private Channel channel;
    private SocketAddress socketAddress;
    private INetHandler packetListener;
    private IChatComponent terminationReason;
    private boolean isEncrypted;
    private boolean disconnected;
    
    public NetworkManager(final EnumPacketDirection packetDirection) {
        this.outboundPacketsQueue = (Queue<InboundHandlerTuplePacketListener>)Queues.newConcurrentLinkedQueue();
        this.field_181680_j = new ReentrantReadWriteLock();
        this.direction = packetDirection;
    }
    
    @Override
    public void channelActive(final ChannelHandlerContext p_channelActive_1_) throws Exception {
        super.channelActive(p_channelActive_1_);
        this.channel = p_channelActive_1_.channel();
        this.socketAddress = this.channel.remoteAddress();
        try {
            this.setConnectionState(EnumConnectionState.HANDSHAKING);
        }
        catch (Throwable throwable) {
            NetworkManager.logger.fatal(throwable);
        }
    }
    
    public void setConnectionState(final EnumConnectionState newState) {
        this.channel.attr(NetworkManager.attrKeyConnectionState).set(newState);
        this.channel.config().setAutoRead(true);
        NetworkManager.logger.debug("Enabled auto read");
    }
    
    @Override
    public void channelInactive(final ChannelHandlerContext p_channelInactive_1_) throws Exception {
        this.closeChannel(new ChatComponentTranslation("disconnect.endOfStream", new Object[0]));
    }
    
    @Override
    public void exceptionCaught(final ChannelHandlerContext p_exceptionCaught_1_, final Throwable p_exceptionCaught_2_) throws Exception {
        ChatComponentTranslation chatcomponenttranslation;
        if (p_exceptionCaught_2_ instanceof TimeoutException) {
            chatcomponenttranslation = new ChatComponentTranslation("disconnect.timeout", new Object[0]);
        }
        else {
            chatcomponenttranslation = new ChatComponentTranslation("disconnect.genericReason", new Object[] { "Internal Exception: " + p_exceptionCaught_2_ });
        }
        this.closeChannel(chatcomponenttranslation);
    }
    
    @Override
    protected void channelRead0(final ChannelHandlerContext p_channelRead0_1_, final Packet p_channelRead0_2_) throws Exception {
        if (this.channel.isOpen()) {
            try {
                final EventReadPacket eventReadPacket = new EventReadPacket(p_channelRead0_2_, this.packetListener, this.direction);
                EventHandler.call(eventReadPacket);
                if (eventReadPacket.isCanceled()) {
                    return;
                }
                p_channelRead0_2_.processPacket((Object)this.packetListener);
            }
            catch (ThreadQuickExitException ex) {}
        }
    }
    
    public void setNetHandler(final INetHandler handler) {
        Validate.notNull(handler, "packetListener", new Object[0]);
        NetworkManager.logger.debug("Set listener of {} to {}", new Object[] { this, handler });
        this.packetListener = handler;
    }
    
    public void sendPacket(final Packet packet) {
        if ((packet instanceof C03PacketPlayer.C04PacketPlayerPosition || packet instanceof C03PacketPlayer.C05PacketPlayerLook || packet instanceof C03PacketPlayer.C06PacketPlayerPosLook) && Minecraft.getMinecraft().theWorld != null && Minecraft.getMinecraft().thePlayer != null) {
            final C03PacketPlayer c03PacketPlayer = (C03PacketPlayer)packet;
            Minecraft.getMinecraft().thePlayer.rotIncrement = 3;
            if (!(packet instanceof C03PacketPlayer.C05PacketPlayerLook)) {
                Minecraft.getMinecraft().thePlayer.setLastServerPosition(Minecraft.getMinecraft().thePlayer.getSeverPosition());
                Minecraft.getMinecraft().thePlayer.setSeverPosition(new Vec3(c03PacketPlayer.getPositionX(), c03PacketPlayer.getPositionY(), c03PacketPlayer.getPositionZ()));
            }
        }
        if (this.isChannelOpen()) {
            this.flushOutboundQueue();
            this.dispatchPacket(packet, null);
        }
        else {
            this.field_181680_j.writeLock().lock();
            try {
                this.outboundPacketsQueue.add(new InboundHandlerTuplePacketListener(packet, (GenericFutureListener<? extends Future<? super Void>>[])null));
            }
            finally {
                this.field_181680_j.writeLock().unlock();
            }
        }
    }
    
    public void sendPacketDirect(final Packet packetIn) {
        if ((packetIn instanceof C03PacketPlayer.C04PacketPlayerPosition || packetIn instanceof C03PacketPlayer.C06PacketPlayerPosLook) && Minecraft.getMinecraft().theWorld != null && Minecraft.getMinecraft().thePlayer != null) {
            final C03PacketPlayer c03PacketPlayer = (C03PacketPlayer)packetIn;
            Minecraft.getMinecraft().thePlayer.setLastServerPosition(Minecraft.getMinecraft().thePlayer.getSeverPosition());
            Minecraft.getMinecraft().thePlayer.setSeverPosition(new Vec3(c03PacketPlayer.getPositionX(), c03PacketPlayer.getPositionY(), c03PacketPlayer.getPositionZ()));
        }
        if (this.isChannelOpen()) {
            this.flushOutboundQueue();
            this.dispatchPacket(packetIn, null);
        }
        else {
            this.field_181680_j.writeLock().lock();
            try {
                this.outboundPacketsQueue.add(new InboundHandlerTuplePacketListener(packetIn, (GenericFutureListener<? extends Future<? super Void>>[])null));
            }
            finally {
                this.field_181680_j.writeLock().unlock();
            }
        }
    }
    
    public void sendPacket(final Packet packetIn, final GenericFutureListener<? extends Future<? super Void>> listener, final GenericFutureListener<? extends Future<? super Void>>... listeners) {
        if (this.isChannelOpen()) {
            this.flushOutboundQueue();
            this.dispatchPacket(packetIn, ArrayUtils.add(listeners, 0, listener));
        }
        else {
            this.field_181680_j.writeLock().lock();
            try {
                this.outboundPacketsQueue.add(new InboundHandlerTuplePacketListener(packetIn, (GenericFutureListener<? extends Future<? super Void>>[])ArrayUtils.add(listeners, 0, listener)));
            }
            finally {
                this.field_181680_j.writeLock().unlock();
            }
        }
    }
    
    private void dispatchPacket(final Packet inPacket, final GenericFutureListener<? extends Future<? super Void>>[] futureListeners) {
        final EnumConnectionState enumconnectionstate = EnumConnectionState.getFromPacket(inPacket);
        final EnumConnectionState enumconnectionstate2 = this.channel.attr(NetworkManager.attrKeyConnectionState).get();
        if (enumconnectionstate2 != enumconnectionstate) {
            NetworkManager.logger.debug("Disabled auto read");
            this.channel.config().setAutoRead(false);
        }
        if (this.channel.eventLoop().inEventLoop()) {
            if (enumconnectionstate != enumconnectionstate2) {
                this.setConnectionState(enumconnectionstate);
            }
            final ChannelFuture channelfuture = this.channel.writeAndFlush(inPacket);
            if (futureListeners != null) {
                channelfuture.addListeners(futureListeners);
            }
            channelfuture.addListener((GenericFutureListener<? extends Future<? super Void>>)ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
        }
        else {
            this.channel.eventLoop().execute(new Runnable() {
                @Override
                public void run() {
                    if (enumconnectionstate != enumconnectionstate2) {
                        NetworkManager.this.setConnectionState(enumconnectionstate);
                    }
                    final ChannelFuture channelfuture1 = NetworkManager.this.channel.writeAndFlush(inPacket);
                    if (futureListeners != null) {
                        channelfuture1.addListeners((GenericFutureListener<? extends Future<? super Void>>[])futureListeners);
                    }
                    channelfuture1.addListener((GenericFutureListener<? extends Future<? super Void>>)ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
                }
            });
        }
    }
    
    private void flushOutboundQueue() {
        if (this.channel != null && this.channel.isOpen()) {
            this.field_181680_j.readLock().lock();
            try {
                while (!this.outboundPacketsQueue.isEmpty()) {
                    final InboundHandlerTuplePacketListener networkmanager$inboundhandlertuplepacketlistener = this.outboundPacketsQueue.poll();
                    this.dispatchPacket(networkmanager$inboundhandlertuplepacketlistener.packet, networkmanager$inboundhandlertuplepacketlistener.futureListeners);
                }
            }
            finally {
                this.field_181680_j.readLock().unlock();
            }
        }
    }
    
    public void processReceivedPackets() {
        this.flushOutboundQueue();
        if (this.packetListener instanceof ITickable) {
            ((ITickable)this.packetListener).update();
        }
        this.channel.flush();
    }
    
    public SocketAddress getRemoteAddress() {
        return this.socketAddress;
    }
    
    public void closeChannel(final IChatComponent message) {
        if (this.channel.isOpen()) {
            this.channel.close().awaitUninterruptibly();
            this.terminationReason = message;
        }
    }
    
    public boolean isLocalChannel() {
        return this.channel instanceof LocalChannel || this.channel instanceof LocalServerChannel;
    }
    
    public static NetworkManager func_181124_a(final InetAddress p_181124_0_, final int p_181124_1_, final boolean p_181124_2_) {
        final NetworkManager networkmanager = new NetworkManager(EnumPacketDirection.CLIENTBOUND);
        Class<? extends SocketChannel> oclass;
        LazyLoadBase<? extends EventLoopGroup> lazyloadbase;
        if (Epoll.isAvailable() && p_181124_2_) {
            oclass = EpollSocketChannel.class;
            lazyloadbase = NetworkManager.field_181125_e;
        }
        else {
            oclass = NioSocketChannel.class;
            lazyloadbase = NetworkManager.CLIENT_NIO_EVENTLOOP;
        }
        final Bootstrap bootstrap = new Bootstrap();
        if (Augustus.getInstance().getProxy() != null) {
            bootstrap.group(new OioEventLoopGroup());
            final OioProxyChannelFactory oioProxyChannelFactory = new OioProxyChannelFactory(Augustus.getInstance().getProxy());
            bootstrap.channelFactory(oioProxyChannelFactory);
        }
        else {
            bootstrap.group((EventLoopGroup)lazyloadbase.getValue());
            bootstrap.channel(oclass);
        }
        bootstrap.handler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(final Channel p_initChannel_1_) throws Exception {
                try {
                    p_initChannel_1_.config().setOption(ChannelOption.TCP_NODELAY, true);
                }
                catch (ChannelException ex) {}
                p_initChannel_1_.pipeline().addLast("timeout", new ReadTimeoutHandler(30)).addLast("splitter", new MessageDeserializer2()).addLast("decoder", new MessageDeserializer(EnumPacketDirection.CLIENTBOUND)).addLast("prepender", new MessageSerializer2()).addLast("encoder", new MessageSerializer(EnumPacketDirection.SERVERBOUND)).addLast("packet_handler", networkmanager);
                if (GuiProtocolSelector.active && p_initChannel_1_ instanceof SocketChannel && ViaMCP.getInstance().getVersion() != 47) {
                    final UserConnection user = new UserConnectionImpl(p_initChannel_1_, true);
                    new ProtocolPipelineImpl(user);
                    p_initChannel_1_.pipeline().addBefore("encoder", "via-encoder", new MCPEncodeHandler(user)).addBefore("decoder", "via-decoder", new MCPDecodeHandler(user));
                }
            }
        });
        bootstrap.connect(p_181124_0_, p_181124_1_).syncUninterruptibly();
        return networkmanager;
    }
    
    public static NetworkManager provideLocalClient(final SocketAddress address) {
        final NetworkManager networkmanager = new NetworkManager(EnumPacketDirection.CLIENTBOUND);
        ((AbstractBootstrap<Bootstrap, C>)((AbstractBootstrap<Bootstrap, C>)new Bootstrap()).group(NetworkManager.CLIENT_LOCAL_EVENTLOOP.getValue())).handler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(final Channel p_initChannel_1_) throws Exception {
                p_initChannel_1_.pipeline().addLast("packet_handler", networkmanager);
            }
        }).channel(LocalChannel.class).connect(address).syncUninterruptibly();
        return networkmanager;
    }
    
    public void enableEncryption(final SecretKey key) {
        this.isEncrypted = true;
        this.channel.pipeline().addBefore("splitter", "decrypt", new NettyEncryptingDecoder(CryptManager.createNetCipherInstance(2, key)));
        this.channel.pipeline().addBefore("prepender", "encrypt", new NettyEncryptingEncoder(CryptManager.createNetCipherInstance(1, key)));
    }
    
    public boolean getIsencrypted() {
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
    
    public IChatComponent getExitMessage() {
        return this.terminationReason;
    }
    
    public void disableAutoRead() {
        this.channel.config().setAutoRead(false);
    }
    
    public void setCompressionTreshold(final int treshold) {
        if (treshold >= 0) {
            if (this.channel.pipeline().get("decompress") instanceof NettyCompressionDecoder) {
                ((NettyCompressionDecoder)this.channel.pipeline().get("decompress")).setCompressionTreshold(treshold);
            }
            else if (GuiProtocolSelector.active) {
                NettyUtil.decodeEncodePlacement(this.channel.pipeline(), "decoder", "decompress", new NettyCompressionDecoder(treshold));
            }
            else {
                this.channel.pipeline().addBefore("decoder", "decompress", new NettyCompressionDecoder(treshold));
            }
            if (this.channel.pipeline().get("compress") instanceof NettyCompressionEncoder) {
                ((NettyCompressionEncoder)this.channel.pipeline().get("decompress")).setCompressionTreshold(treshold);
            }
            else if (GuiProtocolSelector.active) {
                NettyUtil.decodeEncodePlacement(this.channel.pipeline(), "encoder", "compress", new NettyCompressionEncoder(treshold));
            }
            else {
                this.channel.pipeline().addBefore("encoder", "compress", new NettyCompressionEncoder(treshold));
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
    
    public void checkDisconnected() {
        if (this.channel != null && !this.channel.isOpen()) {
            if (!this.disconnected) {
                this.disconnected = true;
                if (this.getExitMessage() != null) {
                    this.getNetHandler().onDisconnect(this.getExitMessage());
                }
                else if (this.getNetHandler() != null) {
                    this.getNetHandler().onDisconnect(new ChatComponentText("Disconnected"));
                }
            }
            else {
                NetworkManager.logger.warn("handleDisconnection() called twice");
            }
        }
    }
    
    static {
        logger = LogManager.getLogger();
        logMarkerNetwork = MarkerManager.getMarker("NETWORK");
        logMarkerPackets = MarkerManager.getMarker("NETWORK_PACKETS", NetworkManager.logMarkerNetwork);
        attrKeyConnectionState = AttributeKey.valueOf("protocol");
        CLIENT_NIO_EVENTLOOP = new LazyLoadBase<NioEventLoopGroup>() {
            @Override
            protected NioEventLoopGroup load() {
                return new NioEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty Client IO #%d").setDaemon(true).build());
            }
        };
        field_181125_e = new LazyLoadBase<EpollEventLoopGroup>() {
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
        private final Packet packet;
        private final GenericFutureListener<? extends Future<? super Void>>[] futureListeners;
        
        public InboundHandlerTuplePacketListener(final Packet inPacket, final GenericFutureListener<? extends Future<? super Void>>... inFutureListeners) {
            this.packet = inPacket;
            this.futureListeners = inFutureListeners;
        }
    }
}
