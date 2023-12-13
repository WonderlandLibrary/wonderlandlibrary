// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network;

import io.netty.channel.local.LocalEventLoopGroup;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.channel.nio.NioEventLoopGroup;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.LogManager;
import net.minecraft.util.ChatComponentText;
import java.security.Key;
import net.minecraft.util.CryptManager;
import javax.crypto.SecretKey;
import io.netty.channel.socket.nio.NioSocketChannel;
import net.minecraft.util.MessageSerializer;
import net.minecraft.util.MessageSerializer2;
import net.minecraft.util.MessageDeserializer;
import net.minecraft.util.MessageDeserializer2;
import io.netty.channel.ChannelHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.bootstrap.Bootstrap;
import java.net.InetAddress;
import io.netty.channel.local.LocalServerChannel;
import io.netty.channel.local.LocalChannel;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.apache.commons.lang3.ArrayUtils;
import io.netty.util.concurrent.GenericFutureListener;
import org.apache.commons.lang3.Validate;
import exhibition.management.notifications.Notifications;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S02PacketChat;
import exhibition.event.EventSystem;
import exhibition.event.impl.EventPacket;
import net.minecraft.util.ChatComponentTranslation;
import io.netty.channel.ChannelHandlerContext;
import com.google.common.collect.Queues;
import net.minecraft.util.IChatComponent;
import java.net.SocketAddress;
import io.netty.channel.Channel;
import java.util.Queue;
import net.minecraft.util.LazyLoadBase;
import io.netty.util.AttributeKey;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.Logger;
import io.netty.channel.SimpleChannelInboundHandler;

public class NetworkManager extends SimpleChannelInboundHandler
{
    private static final Logger logger;
    public static final Marker logMarkerNetwork;
    public static final Marker logMarkerPackets;
    public static final AttributeKey attrKeyConnectionState;
    public static final LazyLoadBase CLIENT_NIO_EVENTLOOP;
    public static final LazyLoadBase CLIENT_LOCAL_EVENTLOOP;
    private final EnumPacketDirection direction;
    private final Queue outboundPacketsQueue;
    private Channel channel;
    private SocketAddress socketAddress;
    private INetHandler packetListener;
    private IChatComponent terminationReason;
    private boolean isEncrypted;
    private boolean disconnected;
    private static final String __OBFID = "CL_00001240";
    
    public NetworkManager(final EnumPacketDirection packetDirection) {
        this.outboundPacketsQueue = Queues.newConcurrentLinkedQueue();
        this.direction = packetDirection;
    }
    
    public void channelActive(final ChannelHandlerContext p_channelActive_1_) throws Exception {
        super.channelActive(p_channelActive_1_);
        this.channel = p_channelActive_1_.channel();
        this.socketAddress = this.channel.remoteAddress();
        try {
            this.setConnectionState(EnumConnectionState.HANDSHAKING);
        }
        catch (Throwable var3) {
            NetworkManager.logger.fatal((Object)var3);
        }
    }
    
    public void setConnectionState(final EnumConnectionState newState) {
        this.channel.attr(NetworkManager.attrKeyConnectionState).set((Object)newState);
        this.channel.config().setAutoRead(true);
        NetworkManager.logger.debug("Enabled auto read");
    }
    
    public void channelInactive(final ChannelHandlerContext p_channelInactive_1_) {
        this.closeChannel(new ChatComponentTranslation("disconnect.endOfStream", new Object[0]));
    }
    
    public void exceptionCaught(final ChannelHandlerContext p_exceptionCaught_1_, final Throwable p_exceptionCaught_2_) {
        NetworkManager.logger.debug("Disconnecting " + this.getRemoteAddress(), p_exceptionCaught_2_);
        this.closeChannel(new ChatComponentTranslation("disconnect.genericReason", new Object[] { "Internal Exception: " + p_exceptionCaught_2_ }));
    }
    
    protected void channelRead0(final ChannelHandlerContext context, final Packet packet) {
        if (this.channel.isOpen()) {
            try {
                final EventPacket ep = (EventPacket)EventSystem.getInstance(EventPacket.class);
                ep.fire(packet, false);
                if (!ep.isCancelled()) {
                    if (packet instanceof S02PacketChat) {
                        final S02PacketChat packet2 = (S02PacketChat)packet;
                        final String formattedText = packet2.func_148915_c().getFormattedText();
                        if ((formattedText.contains("hack") || formattedText.contains("hax") || formattedText.contains("hqr") || formattedText.contains("hkr") || formattedText.contains("recorded") || formattedText.contains("reported") || formattedText.contains("ticket") || formattedText.contains("fly") || formattedText.contains("cheater") || formattedText.contains("cheat")) && formattedText.contains(Minecraft.getMinecraft().thePlayer.getName()) && !packet2.func_148915_c().getUnformattedText().contains("TestNCP")) {
                            Notifications.getManager().post("Player Warning", "Some one called you a hacker.", 0L, 2500L, Notifications.Type.NOTIFY);
                        }
                        final boolean contains = formattedText.contains("Ground items will be removed in");
                        if (contains) {
                            final String message = formattedText.substring(formattedText.lastIndexOf("in "));
                            Notifications.getManager().post("Server Warning", "Clearlag " + message, 0L, 2500L, Notifications.Type.NOTIFY);
                        }
                        if (formattedText.contains("You are now in ")) {
                            final String message = formattedText.substring(formattedText.lastIndexOf("in ") + 3);
                            Notifications.getManager().post("Faction Warning", "Chunk: " + message, 0L, 2500L, Notifications.Type.INFO);
                        }
                    }
                    packet.processPacket(this.packetListener);
                }
            }
            catch (ThreadQuickExitException ex) {}
        }
    }
    
    public void setNetHandler(final INetHandler handler) {
        Validate.notNull((Object)handler, "packetListener", new Object[0]);
        NetworkManager.logger.debug("Set listener of {} to {}", new Object[] { this, handler });
        this.packetListener = handler;
    }
    
    public void sendPacket(final Packet packet) {
        final EventPacket ep = (EventPacket)EventSystem.getInstance(EventPacket.class);
        ep.fire(packet, true);
        if (ep.isCancelled()) {
            return;
        }
        if (this.channel != null && this.channel.isOpen()) {
            this.flushOutboundQueue();
            this.dispatchPacket(packet, null);
        }
        else {
            this.outboundPacketsQueue.add(new InboundHandlerTuplePacketListener(packet, (GenericFutureListener[])null));
        }
    }
    
    public void sendPacketNoEvent(final Packet packet) {
        if (this.channel != null && this.channel.isOpen()) {
            this.flushOutboundQueue();
            this.dispatchPacket(packet, null);
        }
        else {
            this.outboundPacketsQueue.add(new InboundHandlerTuplePacketListener(packet, (GenericFutureListener[])null));
        }
    }
    
    public void sendPacket(final Packet packetIn, final GenericFutureListener listener, final GenericFutureListener... listeners) {
        if (this.channel != null && this.channel.isOpen()) {
            this.flushOutboundQueue();
            this.dispatchPacket(packetIn, (GenericFutureListener[])ArrayUtils.add((Object[])listeners, 0, (Object)listener));
        }
        else {
            this.outboundPacketsQueue.add(new InboundHandlerTuplePacketListener(packetIn, (GenericFutureListener[])ArrayUtils.add((Object[])listeners, 0, (Object)listener)));
        }
    }
    
    private void dispatchPacket(final Packet inPacket, final GenericFutureListener[] futureListeners) {
        final EnumConnectionState var3 = EnumConnectionState.getFromPacket(inPacket);
        final EnumConnectionState var4 = (EnumConnectionState)this.channel.attr(NetworkManager.attrKeyConnectionState).get();
        if (var4 != var3) {
            NetworkManager.logger.debug("Disabled auto read");
            this.channel.config().setAutoRead(false);
        }
        if (this.channel.eventLoop().inEventLoop()) {
            if (var3 != var4) {
                this.setConnectionState(var3);
            }
            final ChannelFuture var5 = this.channel.writeAndFlush((Object)inPacket);
            if (futureListeners != null) {
                var5.addListeners(futureListeners);
            }
            var5.addListener((GenericFutureListener)ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
        }
        else {
            this.channel.eventLoop().execute((Runnable)new Runnable() {
                private static final String __OBFID = "CL_00001243";
                
                @Override
                public void run() {
                    if (var3 != var4) {
                        NetworkManager.this.setConnectionState(var3);
                    }
                    final ChannelFuture var1 = NetworkManager.this.channel.writeAndFlush((Object)inPacket);
                    if (futureListeners != null) {
                        var1.addListeners(futureListeners);
                    }
                    var1.addListener((GenericFutureListener)ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
                }
            });
        }
    }
    
    private void flushOutboundQueue() {
        if (this.channel != null && this.channel.isOpen()) {
            while (!this.outboundPacketsQueue.isEmpty()) {
                final InboundHandlerTuplePacketListener var1 = this.outboundPacketsQueue.poll();
                this.dispatchPacket(var1.packet, var1.futureListeners);
            }
        }
    }
    
    public void processReceivedPackets() {
        this.flushOutboundQueue();
        if (this.packetListener instanceof IUpdatePlayerListBox) {
            ((IUpdatePlayerListBox)this.packetListener).update();
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
    
    public static NetworkManager provideLanClient(final InetAddress p_150726_0_, final int p_150726_1_) {
        final NetworkManager var2 = new NetworkManager(EnumPacketDirection.CLIENTBOUND);
        ((Bootstrap)((Bootstrap)((Bootstrap)new Bootstrap().group((EventLoopGroup)NetworkManager.CLIENT_NIO_EVENTLOOP.getValue())).handler((ChannelHandler)new ChannelInitializer() {
            private static final String __OBFID = "CL_00002312";
            
            protected void initChannel(final Channel p_initChannel_1_) {
                try {
                    p_initChannel_1_.config().setOption(ChannelOption.IP_TOS, (Object)24);
                }
                catch (ChannelException ex) {}
                try {
                    p_initChannel_1_.config().setOption(ChannelOption.TCP_NODELAY, (Object)false);
                }
                catch (ChannelException ex2) {}
                p_initChannel_1_.pipeline().addLast("timeout", (ChannelHandler)new ReadTimeoutHandler(20)).addLast("splitter", (ChannelHandler)new MessageDeserializer2()).addLast("decoder", (ChannelHandler)new MessageDeserializer(EnumPacketDirection.CLIENTBOUND)).addLast("prepender", (ChannelHandler)new MessageSerializer2()).addLast("encoder", (ChannelHandler)new MessageSerializer(EnumPacketDirection.SERVERBOUND)).addLast("packet_handler", (ChannelHandler)var2);
            }
        })).channel((Class)NioSocketChannel.class)).connect(p_150726_0_, p_150726_1_).syncUninterruptibly();
        return var2;
    }
    
    public static NetworkManager provideLocalClient(final SocketAddress p_150722_0_) {
        final NetworkManager var1 = new NetworkManager(EnumPacketDirection.CLIENTBOUND);
        ((Bootstrap)((Bootstrap)((Bootstrap)new Bootstrap().group((EventLoopGroup)NetworkManager.CLIENT_LOCAL_EVENTLOOP.getValue())).handler((ChannelHandler)new ChannelInitializer() {
            private static final String __OBFID = "CL_00002311";
            
            protected void initChannel(final Channel p_initChannel_1_) {
                p_initChannel_1_.pipeline().addLast("packet_handler", (ChannelHandler)var1);
            }
        })).channel((Class)LocalChannel.class)).connect(p_150722_0_).syncUninterruptibly();
        return var1;
    }
    
    public void enableEncryption(final SecretKey key) {
        this.isEncrypted = true;
        this.channel.pipeline().addBefore("splitter", "decrypt", (ChannelHandler)new NettyEncryptingDecoder(CryptManager.func_151229_a(2, key)));
        this.channel.pipeline().addBefore("prepender", "encrypt", (ChannelHandler)new NettyEncryptingEncoder(CryptManager.func_151229_a(1, key)));
    }
    
    public boolean func_179292_f() {
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
            else {
                this.channel.pipeline().addBefore("decoder", "decompress", (ChannelHandler)new NettyCompressionDecoder(treshold));
            }
            if (this.channel.pipeline().get("compress") instanceof NettyCompressionEncoder) {
                ((NettyCompressionEncoder)this.channel.pipeline().get("decompress")).setCompressionTreshold(treshold);
            }
            else {
                this.channel.pipeline().addBefore("encoder", "compress", (ChannelHandler)new NettyCompressionEncoder(treshold));
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
        if (!this.hasNoChannel() && !this.isChannelOpen() && !this.disconnected) {
            this.disconnected = true;
            if (this.getExitMessage() != null) {
                this.getNetHandler().onDisconnect(this.getExitMessage());
            }
            else if (this.getNetHandler() != null) {
                this.getNetHandler().onDisconnect(new ChatComponentText("Disconnected"));
            }
        }
    }
    
    protected void channelRead0(final ChannelHandlerContext p_channelRead0_1_, final Object p_channelRead0_2_) {
        this.channelRead0(p_channelRead0_1_, (Packet)p_channelRead0_2_);
    }
    
    static {
        logger = LogManager.getLogger();
        logMarkerNetwork = MarkerManager.getMarker("NETWORK");
        logMarkerPackets = MarkerManager.getMarker("NETWORK_PACKETS", NetworkManager.logMarkerNetwork);
        attrKeyConnectionState = AttributeKey.valueOf("protocol");
        CLIENT_NIO_EVENTLOOP = new LazyLoadBase() {
            private static final String __OBFID = "CL_00001241";
            
            protected NioEventLoopGroup genericLoad() {
                return new NioEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty Client IO #%d").setDaemon(true).build());
            }
            
            @Override
            protected Object load() {
                return this.genericLoad();
            }
        };
        CLIENT_LOCAL_EVENTLOOP = new LazyLoadBase() {
            private static final String __OBFID = "CL_00001242";
            
            protected LocalEventLoopGroup genericLoad() {
                return new LocalEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty Local Client IO #%d").setDaemon(true).build());
            }
            
            @Override
            protected Object load() {
                return this.genericLoad();
            }
        };
    }
    
    static class InboundHandlerTuplePacketListener
    {
        private final Packet packet;
        private final GenericFutureListener[] futureListeners;
        private static final String __OBFID = "CL_00001244";
        
        public InboundHandlerTuplePacketListener(final Packet inPacket, final GenericFutureListener... inFutureListeners) {
            this.packet = inPacket;
            this.futureListeners = inFutureListeners;
        }
    }
}
