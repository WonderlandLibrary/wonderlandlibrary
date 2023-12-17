// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.network;

import org.apache.logging.log4j.LogManager;
import java.util.Iterator;
import io.netty.channel.socket.nio.NioSocketChannel;
import net.minecraft.util.math.MathHelper;
import com.google.common.collect.Iterables;
import java.nio.charset.StandardCharsets;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.channel.ChannelFutureListener;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.buffer.ByteBuf;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelOption;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.bootstrap.Bootstrap;
import java.net.UnknownHostException;
import net.minecraft.network.status.client.CPacketServerQuery;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.INetHandler;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.network.status.server.SPacketPong;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.ServerStatusResponse;
import net.minecraft.network.Packet;
import net.minecraft.network.status.client.CPacketPing;
import net.minecraft.client.Minecraft;
import org.apache.commons.lang3.ArrayUtils;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.network.status.server.SPacketServerInfo;
import net.minecraft.network.status.INetHandlerStatusClient;
import net.minecraft.client.resources.I18n;
import java.net.InetAddress;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.multiplayer.ServerData;
import java.util.Collections;
import com.google.common.collect.Lists;
import net.minecraft.network.NetworkManager;
import java.util.List;
import org.apache.logging.log4j.Logger;
import com.google.common.base.Splitter;

public class ServerPinger
{
    private static final Splitter PING_RESPONSE_SPLITTER;
    private static final Logger LOGGER;
    private final List<NetworkManager> pingDestinations;
    
    public ServerPinger() {
        this.pingDestinations = Collections.synchronizedList((List<NetworkManager>)Lists.newArrayList());
    }
    
    public void ping(final ServerData server) throws UnknownHostException {
        final ServerAddress serveraddress = ServerAddress.fromString(server.serverIP);
        final NetworkManager networkmanager = NetworkManager.createNetworkManagerAndConnect(InetAddress.getByName(serveraddress.getIP()), serveraddress.getPort(), false);
        this.pingDestinations.add(networkmanager);
        server.serverMOTD = I18n.format("multiplayer.status.pinging", new Object[0]);
        server.pingToServer = -1L;
        server.playerList = null;
        networkmanager.setNetHandler(new INetHandlerStatusClient() {
            private boolean successful;
            private boolean receivedStatus;
            private long pingSentAt;
            
            @Override
            public void handleServerInfo(final SPacketServerInfo packetIn) {
                if (this.receivedStatus) {
                    networkmanager.closeChannel(new TextComponentTranslation("multiplayer.status.unrequested", new Object[0]));
                }
                else {
                    this.receivedStatus = true;
                    final ServerStatusResponse serverstatusresponse = packetIn.getResponse();
                    if (serverstatusresponse.getServerDescription() != null) {
                        server.serverMOTD = serverstatusresponse.getServerDescription().getFormattedText();
                    }
                    else {
                        server.serverMOTD = "";
                    }
                    if (serverstatusresponse.getVersion() != null) {
                        server.gameVersion = serverstatusresponse.getVersion().getName();
                        server.version = serverstatusresponse.getVersion().getProtocol();
                    }
                    else {
                        server.gameVersion = I18n.format("multiplayer.status.old", new Object[0]);
                        server.version = 0;
                    }
                    if (serverstatusresponse.getPlayers() != null) {
                        server.populationInfo = TextFormatting.GRAY + "" + serverstatusresponse.getPlayers().getOnlinePlayerCount() + "" + TextFormatting.DARK_GRAY + "/" + TextFormatting.GRAY + serverstatusresponse.getPlayers().getMaxPlayers();
                        if (ArrayUtils.isNotEmpty((Object[])serverstatusresponse.getPlayers().getPlayers())) {
                            final StringBuilder stringbuilder = new StringBuilder();
                            for (final GameProfile gameprofile : serverstatusresponse.getPlayers().getPlayers()) {
                                if (stringbuilder.length() > 0) {
                                    stringbuilder.append("\n");
                                }
                                stringbuilder.append(gameprofile.getName());
                            }
                            if (serverstatusresponse.getPlayers().getPlayers().length < serverstatusresponse.getPlayers().getOnlinePlayerCount()) {
                                if (stringbuilder.length() > 0) {
                                    stringbuilder.append("\n");
                                }
                                stringbuilder.append(I18n.format("multiplayer.status.and_more", serverstatusresponse.getPlayers().getOnlinePlayerCount() - serverstatusresponse.getPlayers().getPlayers().length));
                            }
                            server.playerList = stringbuilder.toString();
                        }
                    }
                    else {
                        server.populationInfo = TextFormatting.DARK_GRAY + I18n.format("multiplayer.status.unknown", new Object[0]);
                    }
                    if (serverstatusresponse.getFavicon() != null) {
                        final String s = serverstatusresponse.getFavicon();
                        if (s.startsWith("data:image/png;base64,")) {
                            server.setBase64EncodedIconData(s.substring("data:image/png;base64,".length()));
                        }
                        else {
                            ServerPinger.LOGGER.error("Invalid server icon (unknown format)");
                        }
                    }
                    else {
                        server.setBase64EncodedIconData(null);
                    }
                    this.pingSentAt = Minecraft.getSystemTime();
                    networkmanager.sendPacket(new CPacketPing(this.pingSentAt));
                    this.successful = true;
                }
            }
            
            @Override
            public void handlePong(final SPacketPong packetIn) {
                final long i = this.pingSentAt;
                final long j = Minecraft.getSystemTime();
                server.pingToServer = j - i;
                networkmanager.closeChannel(new TextComponentString("Finished"));
            }
            
            @Override
            public void onDisconnect(final ITextComponent reason) {
                if (!this.successful) {
                    ServerPinger.LOGGER.error("Can't ping {}: {}", (Object)server.serverIP, (Object)reason.getUnformattedText());
                    server.serverMOTD = TextFormatting.DARK_RED + I18n.format("multiplayer.status.cannot_connect", new Object[0]);
                    server.populationInfo = "";
                    ServerPinger.this.tryCompatibilityPing(server);
                }
            }
        });
        try {
            networkmanager.sendPacket(new C00Handshake(serveraddress.getIP(), serveraddress.getPort(), EnumConnectionState.STATUS));
            networkmanager.sendPacket(new CPacketServerQuery());
        }
        catch (Throwable throwable) {
            ServerPinger.LOGGER.error((Object)throwable);
        }
    }
    
    private void tryCompatibilityPing(final ServerData server) {
        final ServerAddress serveraddress = ServerAddress.fromString(server.serverIP);
        ((Bootstrap)((Bootstrap)((Bootstrap)new Bootstrap().group((EventLoopGroup)NetworkManager.CLIENT_NIO_EVENTLOOP.getValue())).handler((ChannelHandler)new ChannelInitializer<Channel>() {
            protected void initChannel(final Channel p_initChannel_1_) throws Exception {
                try {
                    p_initChannel_1_.config().setOption(ChannelOption.TCP_NODELAY, (Object)true);
                }
                catch (ChannelException ex) {}
                p_initChannel_1_.pipeline().addLast(new ChannelHandler[] { (ChannelHandler)new SimpleChannelInboundHandler<ByteBuf>() {
                        public void channelActive(final ChannelHandlerContext p_channelActive_1_) throws Exception {
                            super.channelActive(p_channelActive_1_);
                            final ByteBuf bytebuf = Unpooled.buffer();
                            try {
                                bytebuf.writeByte(254);
                                bytebuf.writeByte(1);
                                bytebuf.writeByte(250);
                                char[] achar = "MC|PingHost".toCharArray();
                                bytebuf.writeShort(achar.length);
                                for (final char c0 : achar) {
                                    bytebuf.writeChar((int)c0);
                                }
                                bytebuf.writeShort(7 + 2 * serveraddress.getIP().length());
                                bytebuf.writeByte(127);
                                achar = serveraddress.getIP().toCharArray();
                                bytebuf.writeShort(achar.length);
                                for (final char c2 : achar) {
                                    bytebuf.writeChar((int)c2);
                                }
                                bytebuf.writeInt(serveraddress.getPort());
                                p_channelActive_1_.channel().writeAndFlush((Object)bytebuf).addListener((GenericFutureListener)ChannelFutureListener.CLOSE_ON_FAILURE);
                            }
                            finally {
                                bytebuf.release();
                            }
                        }
                        
                        protected void channelRead0(final ChannelHandlerContext p_channelRead0_1_, final ByteBuf p_channelRead0_2_) throws Exception {
                            final short short1 = p_channelRead0_2_.readUnsignedByte();
                            if (short1 == 255) {
                                final String s = new String(p_channelRead0_2_.readBytes(p_channelRead0_2_.readShort() * 2).array(), StandardCharsets.UTF_16BE);
                                final String[] astring = (String[])Iterables.toArray(ServerPinger.PING_RESPONSE_SPLITTER.split((CharSequence)s), (Class)String.class);
                                if ("§1".equals(astring[0])) {
                                    final int i = MathHelper.getInt(astring[1], 0);
                                    final String s2 = astring[2];
                                    final String s3 = astring[3];
                                    final int j = MathHelper.getInt(astring[4], -1);
                                    final int k = MathHelper.getInt(astring[5], -1);
                                    server.version = -1;
                                    server.gameVersion = s2;
                                    server.serverMOTD = s3;
                                    server.populationInfo = TextFormatting.GRAY + "" + j + "" + TextFormatting.DARK_GRAY + "/" + TextFormatting.GRAY + k;
                                }
                            }
                            p_channelRead0_1_.close();
                        }
                        
                        public void exceptionCaught(final ChannelHandlerContext p_exceptionCaught_1_, final Throwable p_exceptionCaught_2_) throws Exception {
                            p_exceptionCaught_1_.close();
                        }
                    } });
            }
        })).channel((Class)NioSocketChannel.class)).connect(serveraddress.getIP(), serveraddress.getPort());
    }
    
    public void pingPendingNetworks() {
        synchronized (this.pingDestinations) {
            final Iterator<NetworkManager> iterator = this.pingDestinations.iterator();
            while (iterator.hasNext()) {
                final NetworkManager networkmanager = iterator.next();
                if (networkmanager.isChannelOpen()) {
                    networkmanager.processReceivedPackets();
                }
                else {
                    iterator.remove();
                    networkmanager.handleDisconnection();
                }
            }
        }
    }
    
    public void clearPendingNetworks() {
        synchronized (this.pingDestinations) {
            final Iterator<NetworkManager> iterator = this.pingDestinations.iterator();
            while (iterator.hasNext()) {
                final NetworkManager networkmanager = iterator.next();
                if (networkmanager.isChannelOpen()) {
                    iterator.remove();
                    networkmanager.closeChannel(new TextComponentTranslation("multiplayer.status.cancelled", new Object[0]));
                }
            }
        }
    }
    
    static {
        PING_RESPONSE_SPLITTER = Splitter.on('\0').limit(6);
        LOGGER = LogManager.getLogger();
    }
}
