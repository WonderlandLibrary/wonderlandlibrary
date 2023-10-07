// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.network;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.apache.logging.log4j.LogManager;
import java.util.Iterator;
import io.netty.channel.socket.nio.NioSocketChannel;
import net.minecraft.util.MathHelper;
import com.google.common.collect.Iterables;
import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.channel.ChannelFutureListener;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelOption;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.bootstrap.Bootstrap;
import java.net.UnknownHostException;
import net.minecraft.network.status.client.C00PacketServerQuery;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.INetHandler;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.network.status.server.S01PacketPong;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.ServerStatusResponse;
import net.minecraft.network.Packet;
import net.minecraft.network.status.client.C01PacketPing;
import net.minecraft.client.Minecraft;
import org.apache.commons.lang3.ArrayUtils;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.network.status.server.S00PacketServerInfo;
import net.minecraft.network.status.INetHandlerStatusClient;
import net.minecraft.network.NetworkManager;
import java.net.InetAddress;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.multiplayer.ServerData;
import java.util.Collections;
import com.google.common.collect.Lists;
import java.util.List;
import org.apache.logging.log4j.Logger;
import com.google.common.base.Splitter;

public class OldServerPinger
{
    private static final Splitter PING_RESPONSE_SPLITTER;
    private static final Logger logger;
    private final List pingDestinations;
    private static final String __OBFID = "CL_00000892";
    
    public OldServerPinger() {
        this.pingDestinations = Collections.synchronizedList((List<Object>)Lists.newArrayList());
    }
    
    public void ping(final ServerData server) throws UnknownHostException {
        final ServerAddress var2 = ServerAddress.func_78860_a(server.serverIP);
        final NetworkManager var3 = NetworkManager.provideLanClient(InetAddress.getByName(var2.getIP()), var2.getPort());
        this.pingDestinations.add(var3);
        server.serverMOTD = "Pinging...";
        ServerData.pingToServer = -1L;
        server.playerList = null;
        var3.setNetHandler(new INetHandlerStatusClient() {
            private boolean field_147403_d = false;
            private long field_175092_e = 0L;
            private static final String __OBFID = "CL_00000893";
            
            @Override
            public void handleServerInfo(final S00PacketServerInfo packetIn) {
                final ServerStatusResponse var2 = packetIn.func_149294_c();
                if (var2.getServerDescription() != null) {
                    server.serverMOTD = var2.getServerDescription().getFormattedText();
                }
                else {
                    server.serverMOTD = "";
                }
                if (var2.getProtocolVersionInfo() != null) {
                    server.gameVersion = var2.getProtocolVersionInfo().getName();
                    server.version = var2.getProtocolVersionInfo().getProtocol();
                }
                else {
                    server.gameVersion = "Old";
                    server.version = 0;
                }
                if (var2.getPlayerCountData() != null) {
                    server.populationInfo = EnumChatFormatting.GRAY + "" + var2.getPlayerCountData().getOnlinePlayerCount() + "" + EnumChatFormatting.DARK_GRAY + "/" + EnumChatFormatting.GRAY + var2.getPlayerCountData().getMaxPlayers();
                    if (ArrayUtils.isNotEmpty((Object[])var2.getPlayerCountData().getPlayers())) {
                        final StringBuilder var3x = new StringBuilder();
                        for (final GameProfile var6 : var2.getPlayerCountData().getPlayers()) {
                            if (var3x.length() > 0) {
                                var3x.append("\n");
                            }
                            var3x.append(var6.getName());
                        }
                        if (var2.getPlayerCountData().getPlayers().length < var2.getPlayerCountData().getOnlinePlayerCount()) {
                            if (var3x.length() > 0) {
                                var3x.append("\n");
                            }
                            var3x.append("... and ").append(var2.getPlayerCountData().getOnlinePlayerCount() - var2.getPlayerCountData().getPlayers().length).append(" more ...");
                        }
                        server.playerList = var3x.toString();
                    }
                }
                else {
                    server.populationInfo = EnumChatFormatting.DARK_GRAY + "???";
                }
                if (var2.getFavicon() != null) {
                    final String var7 = var2.getFavicon();
                    if (var7.startsWith("data:image/png;base64,")) {
                        server.setBase64EncodedIconData(var7.substring("data:image/png;base64,".length()));
                    }
                    else {
                        OldServerPinger.logger.error("Invalid server icon (unknown format)");
                    }
                }
                else {
                    server.setBase64EncodedIconData(null);
                }
                this.field_175092_e = Minecraft.getSystemTime();
                var3.sendPacket(new C01PacketPing(this.field_175092_e));
                this.field_147403_d = true;
            }
            
            @Override
            public void handlePong(final S01PacketPong packetIn) {
                final long var2 = this.field_175092_e;
                final long var3 = Minecraft.getSystemTime();
                final ServerData val$server = server;
                ServerData.pingToServer = var3 - var2;
                var3.closeChannel(new ChatComponentText("Finished"));
            }
            
            @Override
            public void onDisconnect(final IChatComponent reason) {
                if (!this.field_147403_d) {
                    OldServerPinger.logger.error("Can't ping " + server.serverIP + ": " + reason.getUnformattedText());
                    server.serverMOTD = EnumChatFormatting.DARK_RED + "Can't connect to server.";
                    server.populationInfo = "";
                    OldServerPinger.this.tryCompatibilityPing(server);
                }
            }
        });
        try {
            var3.sendPacket(new C00Handshake(47, var2.getIP(), var2.getPort(), EnumConnectionState.STATUS));
            var3.sendPacket(new C00PacketServerQuery());
        }
        catch (Throwable var4) {
            OldServerPinger.logger.error((Object)var4);
        }
    }
    
    private void tryCompatibilityPing(final ServerData server) {
        final ServerAddress var2 = ServerAddress.func_78860_a(server.serverIP);
        ((Bootstrap)((Bootstrap)((Bootstrap)new Bootstrap().group((EventLoopGroup)NetworkManager.CLIENT_NIO_EVENTLOOP.getValue())).handler((ChannelHandler)new ChannelInitializer() {
            private static final String __OBFID = "CL_00000894";
            
            protected void initChannel(final Channel p_initChannel_1_) {
                try {
                    p_initChannel_1_.config().setOption(ChannelOption.IP_TOS, (Object)24);
                }
                catch (ChannelException ex) {}
                try {
                    p_initChannel_1_.config().setOption(ChannelOption.TCP_NODELAY, (Object)false);
                }
                catch (ChannelException ex2) {}
                p_initChannel_1_.pipeline().addLast(new ChannelHandler[] { new SimpleChannelInboundHandler() {
                        private static final String __OBFID = "CL_00000895";
                        
                        public void channelActive(final ChannelHandlerContext p_channelActive_1_) throws Exception {
                            super.channelActive(p_channelActive_1_);
                            final ByteBuf var2x = Unpooled.buffer();
                            try {
                                var2x.writeByte(254);
                                var2x.writeByte(1);
                                var2x.writeByte(250);
                                char[] var3 = "MC|PingHost".toCharArray();
                                var2x.writeShort(var3.length);
                                char[] var4 = var3;
                                for (int var5 = var3.length, var6 = 0; var6 < var5; ++var6) {
                                    final char var7 = var4[var6];
                                    var2x.writeChar((int)var7);
                                }
                                var2x.writeShort(7 + 2 * var2.getIP().length());
                                var2x.writeByte(127);
                                var3 = var2.getIP().toCharArray();
                                var2x.writeShort(var3.length);
                                var4 = var3;
                                for (int var5 = var3.length, var6 = 0; var6 < var5; ++var6) {
                                    final char var7 = var4[var6];
                                    var2x.writeChar((int)var7);
                                }
                                var2x.writeInt(var2.getPort());
                                p_channelActive_1_.channel().writeAndFlush((Object)var2x).addListener((GenericFutureListener)ChannelFutureListener.CLOSE_ON_FAILURE);
                            }
                            finally {
                                var2x.release();
                            }
                        }
                        
                        protected void channelRead0(final ChannelHandlerContext p_channelRead0_1_, final ByteBuf p_channelRead0_2_) {
                            final short var3 = p_channelRead0_2_.readUnsignedByte();
                            if (var3 == 255) {
                                final String var4 = new String(p_channelRead0_2_.readBytes(p_channelRead0_2_.readShort() * 2).array(), Charsets.UTF_16BE);
                                final String[] var5 = (String[])Iterables.toArray(OldServerPinger.PING_RESPONSE_SPLITTER.split((CharSequence)var4), (Class)String.class);
                                if ("§1".equals(var5[0])) {
                                    final int var6 = MathHelper.parseIntWithDefault(var5[1], 0);
                                    final String var7 = var5[2];
                                    final String var8 = var5[3];
                                    final int var9 = MathHelper.parseIntWithDefault(var5[4], -1);
                                    final int var10 = MathHelper.parseIntWithDefault(var5[5], -1);
                                    server.version = -1;
                                    server.gameVersion = var7;
                                    server.serverMOTD = var8;
                                    server.populationInfo = EnumChatFormatting.GRAY + "" + var9 + "" + EnumChatFormatting.DARK_GRAY + "/" + EnumChatFormatting.GRAY + var10;
                                }
                            }
                            p_channelRead0_1_.close();
                        }
                        
                        public void exceptionCaught(final ChannelHandlerContext p_exceptionCaught_1_, final Throwable p_exceptionCaught_2_) {
                            p_exceptionCaught_1_.close();
                        }
                        
                        protected void channelRead0(final ChannelHandlerContext p_channelRead0_1_, final Object p_channelRead0_2_) {
                            this.channelRead0(p_channelRead0_1_, (ByteBuf)p_channelRead0_2_);
                        }
                    } });
            }
        })).channel((Class)NioSocketChannel.class)).connect(var2.getIP(), var2.getPort());
    }
    
    public void pingPendingNetworks() {
        final List var1 = this.pingDestinations;
        synchronized (this.pingDestinations) {
            final Iterator var2 = this.pingDestinations.iterator();
            while (var2.hasNext()) {
                final NetworkManager var3 = var2.next();
                if (var3.isChannelOpen()) {
                    var3.processReceivedPackets();
                }
                else {
                    var2.remove();
                    var3.checkDisconnected();
                }
            }
        }
    }
    
    public void clearPendingNetworks() {
        final List var1 = this.pingDestinations;
        synchronized (this.pingDestinations) {
            final Iterator var2 = this.pingDestinations.iterator();
            while (var2.hasNext()) {
                final NetworkManager var3 = var2.next();
                if (var3.isChannelOpen()) {
                    var2.remove();
                    var3.closeChannel(new ChatComponentText("Cancelled"));
                }
            }
        }
    }
    
    static {
        PING_RESPONSE_SPLITTER = Splitter.on('\0').limit(6);
        logger = LogManager.getLogger();
    }
}
