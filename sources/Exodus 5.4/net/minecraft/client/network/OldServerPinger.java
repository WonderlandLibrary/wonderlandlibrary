/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Charsets
 *  com.google.common.base.Splitter
 *  com.google.common.collect.Iterables
 *  com.google.common.collect.Lists
 *  com.mojang.authlib.GameProfile
 *  org.apache.commons.lang3.ArrayUtils
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.client.network;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.ServerStatusResponse;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.status.INetHandlerStatusClient;
import net.minecraft.network.status.client.C00PacketServerQuery;
import net.minecraft.network.status.client.C01PacketPing;
import net.minecraft.network.status.server.S00PacketServerInfo;
import net.minecraft.network.status.server.S01PacketPong;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OldServerPinger {
    private final List<NetworkManager> pingDestinations = Collections.synchronizedList(Lists.newArrayList());
    private static final Splitter PING_RESPONSE_SPLITTER = Splitter.on((char)'\u0000').limit(6);
    private static final Logger logger = LogManager.getLogger();

    public void ping(final ServerData serverData) throws UnknownHostException {
        ServerAddress serverAddress = ServerAddress.func_78860_a(serverData.serverIP);
        final NetworkManager networkManager = NetworkManager.func_181124_a(InetAddress.getByName(serverAddress.getIP()), serverAddress.getPort(), false);
        this.pingDestinations.add(networkManager);
        serverData.serverMOTD = "Pinging...";
        serverData.pingToServer = -1L;
        serverData.playerList = null;
        networkManager.setNetHandler(new INetHandlerStatusClient(){
            private boolean field_183009_e = false;
            private boolean field_147403_d = false;
            private long field_175092_e = 0L;

            @Override
            public void handleServerInfo(S00PacketServerInfo s00PacketServerInfo) {
                if (this.field_183009_e) {
                    networkManager.closeChannel(new ChatComponentText("Received unrequested status"));
                } else {
                    CharSequence charSequence;
                    this.field_183009_e = true;
                    ServerStatusResponse serverStatusResponse = s00PacketServerInfo.getResponse();
                    serverData.serverMOTD = serverStatusResponse.getServerDescription() != null ? serverStatusResponse.getServerDescription().getFormattedText() : "";
                    if (serverStatusResponse.getProtocolVersionInfo() != null) {
                        serverData.gameVersion = serverStatusResponse.getProtocolVersionInfo().getName();
                        serverData.version = serverStatusResponse.getProtocolVersionInfo().getProtocol();
                    } else {
                        serverData.gameVersion = "Old";
                        serverData.version = 0;
                    }
                    if (serverStatusResponse.getPlayerCountData() != null) {
                        serverData.populationInfo = (Object)((Object)EnumChatFormatting.GRAY) + serverStatusResponse.getPlayerCountData().getOnlinePlayerCount() + (Object)((Object)EnumChatFormatting.DARK_GRAY) + "/" + (Object)((Object)EnumChatFormatting.GRAY) + serverStatusResponse.getPlayerCountData().getMaxPlayers();
                        if (ArrayUtils.isNotEmpty((Object[])serverStatusResponse.getPlayerCountData().getPlayers())) {
                            charSequence = new StringBuilder();
                            GameProfile[] gameProfileArray = serverStatusResponse.getPlayerCountData().getPlayers();
                            int n = gameProfileArray.length;
                            int n2 = 0;
                            while (n2 < n) {
                                GameProfile gameProfile = gameProfileArray[n2];
                                if (((StringBuilder)charSequence).length() > 0) {
                                    ((StringBuilder)charSequence).append("\n");
                                }
                                ((StringBuilder)charSequence).append(gameProfile.getName());
                                ++n2;
                            }
                            if (serverStatusResponse.getPlayerCountData().getPlayers().length < serverStatusResponse.getPlayerCountData().getOnlinePlayerCount()) {
                                if (((StringBuilder)charSequence).length() > 0) {
                                    ((StringBuilder)charSequence).append("\n");
                                }
                                ((StringBuilder)charSequence).append("... and ").append(serverStatusResponse.getPlayerCountData().getOnlinePlayerCount() - serverStatusResponse.getPlayerCountData().getPlayers().length).append(" more ...");
                            }
                            serverData.playerList = ((StringBuilder)charSequence).toString();
                        }
                    } else {
                        serverData.populationInfo = (Object)((Object)EnumChatFormatting.DARK_GRAY) + "???";
                    }
                    if (serverStatusResponse.getFavicon() != null) {
                        charSequence = serverStatusResponse.getFavicon();
                        if (((String)charSequence).startsWith("data:image/png;base64,")) {
                            serverData.setBase64EncodedIconData(((String)charSequence).substring(22));
                        } else {
                            logger.error("Invalid server icon (unknown format)");
                        }
                    } else {
                        serverData.setBase64EncodedIconData(null);
                    }
                    this.field_175092_e = Minecraft.getSystemTime();
                    networkManager.sendPacket(new C01PacketPing(this.field_175092_e));
                    this.field_147403_d = true;
                }
            }

            @Override
            public void handlePong(S01PacketPong s01PacketPong) {
                long l = this.field_175092_e;
                long l2 = Minecraft.getSystemTime();
                serverData.pingToServer = l2 - l;
                networkManager.closeChannel(new ChatComponentText("Finished"));
            }

            @Override
            public void onDisconnect(IChatComponent iChatComponent) {
                if (!this.field_147403_d) {
                    logger.error("Can't ping " + serverData.serverIP + ": " + iChatComponent.getUnformattedText());
                    serverData.serverMOTD = (Object)((Object)EnumChatFormatting.DARK_RED) + "Can't connect to server.";
                    serverData.populationInfo = "";
                    OldServerPinger.this.tryCompatibilityPing(serverData);
                }
            }
        });
        try {
            networkManager.sendPacket(new C00Handshake(47, serverAddress.getIP(), serverAddress.getPort(), EnumConnectionState.STATUS));
            networkManager.sendPacket(new C00PacketServerQuery());
        }
        catch (Throwable throwable) {
            logger.error((Object)throwable);
        }
    }

    private void tryCompatibilityPing(final ServerData serverData) {
        final ServerAddress serverAddress = ServerAddress.func_78860_a(serverData.serverIP);
        ((Bootstrap)((Bootstrap)((Bootstrap)new Bootstrap().group(NetworkManager.CLIENT_NIO_EVENTLOOP.getValue())).handler(new ChannelInitializer<Channel>(){

            @Override
            protected void initChannel(Channel channel) throws Exception {
                try {
                    channel.config().setOption(ChannelOption.TCP_NODELAY, true);
                }
                catch (ChannelException channelException) {
                    // empty catch block
                }
                channel.pipeline().addLast(new SimpleChannelInboundHandler<ByteBuf>(){

                    @Override
                    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {
                        channelHandlerContext.close();
                    }

                    @Override
                    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
                        short s = byteBuf.readUnsignedByte();
                        if (s == 255) {
                            String string = new String(byteBuf.readBytes(byteBuf.readShort() * 2).array(), Charsets.UTF_16BE);
                            String[] stringArray = (String[])Iterables.toArray((Iterable)PING_RESPONSE_SPLITTER.split((CharSequence)string), String.class);
                            if ("\u00a71".equals(stringArray[0])) {
                                int n = MathHelper.parseIntWithDefault(stringArray[1], 0);
                                String string2 = stringArray[2];
                                String string3 = stringArray[3];
                                int n2 = MathHelper.parseIntWithDefault(stringArray[4], -1);
                                int n3 = MathHelper.parseIntWithDefault(stringArray[5], -1);
                                serverData.version = -1;
                                serverData.gameVersion = string2;
                                serverData.serverMOTD = string3;
                                serverData.populationInfo = (Object)((Object)EnumChatFormatting.GRAY) + n2 + (Object)((Object)EnumChatFormatting.DARK_GRAY) + "/" + (Object)((Object)EnumChatFormatting.GRAY) + n3;
                            }
                        }
                        channelHandlerContext.close();
                    }

                    @Override
                    public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {
                        char c;
                        super.channelActive(channelHandlerContext);
                        ByteBuf byteBuf = Unpooled.buffer();
                        byteBuf.writeByte(254);
                        byteBuf.writeByte(1);
                        byteBuf.writeByte(250);
                        char[] cArray = "MC|PingHost".toCharArray();
                        byteBuf.writeShort(cArray.length);
                        char[] cArray2 = cArray;
                        int n = cArray.length;
                        int n2 = 0;
                        while (n2 < n) {
                            c = cArray2[n2];
                            byteBuf.writeChar(c);
                            ++n2;
                        }
                        byteBuf.writeShort(7 + 2 * serverAddress.getIP().length());
                        byteBuf.writeByte(127);
                        cArray = serverAddress.getIP().toCharArray();
                        byteBuf.writeShort(cArray.length);
                        cArray2 = cArray;
                        n = cArray.length;
                        n2 = 0;
                        while (n2 < n) {
                            c = cArray2[n2];
                            byteBuf.writeChar(c);
                            ++n2;
                        }
                        byteBuf.writeInt(serverAddress.getPort());
                        channelHandlerContext.channel().writeAndFlush(byteBuf).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                        byteBuf.release();
                    }
                });
            }
        })).channel(NioSocketChannel.class)).connect(serverAddress.getIP(), serverAddress.getPort());
    }

    public void clearPendingNetworks() {
        List<NetworkManager> list = this.pingDestinations;
        synchronized (list) {
            Iterator<NetworkManager> iterator = this.pingDestinations.iterator();
            while (iterator.hasNext()) {
                NetworkManager networkManager = iterator.next();
                if (!networkManager.isChannelOpen()) continue;
                iterator.remove();
                networkManager.closeChannel(new ChatComponentText("Cancelled"));
            }
        }
    }

    public void pingPendingNetworks() {
        List<NetworkManager> list = this.pingDestinations;
        synchronized (list) {
            Iterator<NetworkManager> iterator = this.pingDestinations.iterator();
            while (iterator.hasNext()) {
                NetworkManager networkManager = iterator.next();
                if (networkManager.isChannelOpen()) {
                    networkManager.processReceivedPackets();
                    continue;
                }
                iterator.remove();
                networkManager.checkDisconnected();
            }
        }
    }
}

