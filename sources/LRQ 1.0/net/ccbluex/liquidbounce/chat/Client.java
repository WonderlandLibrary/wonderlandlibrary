/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  io.netty.bootstrap.Bootstrap
 *  io.netty.channel.Channel
 *  io.netty.channel.ChannelHandler
 *  io.netty.channel.ChannelInitializer
 *  io.netty.channel.ChannelPipeline
 *  io.netty.channel.EventLoopGroup
 *  io.netty.channel.nio.NioEventLoopGroup
 *  io.netty.channel.socket.SocketChannel
 *  io.netty.channel.socket.nio.NioSocketChannel
 *  io.netty.handler.codec.http.DefaultHttpHeaders
 *  io.netty.handler.codec.http.HttpClientCodec
 *  io.netty.handler.codec.http.HttpHeaders
 *  io.netty.handler.codec.http.HttpObjectAggregator
 *  io.netty.handler.codec.http.websocketx.TextWebSocketFrame
 *  io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory
 *  io.netty.handler.codec.http.websocketx.WebSocketVersion
 *  io.netty.handler.ssl.SslContext
 *  io.netty.handler.ssl.util.InsecureTrustManagerFactory
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.chat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.UUID;
import javax.net.ssl.TrustManagerFactory;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.chat.ClientHandler;
import net.ccbluex.liquidbounce.chat.ClientListener;
import net.ccbluex.liquidbounce.chat.packet.PacketDeserializer;
import net.ccbluex.liquidbounce.chat.packet.PacketSerializer;
import net.ccbluex.liquidbounce.chat.packet.packets.ClientErrorPacket;
import net.ccbluex.liquidbounce.chat.packet.packets.ClientMessagePacket;
import net.ccbluex.liquidbounce.chat.packet.packets.ClientMojangInfoPacket;
import net.ccbluex.liquidbounce.chat.packet.packets.ClientNewJWTPacket;
import net.ccbluex.liquidbounce.chat.packet.packets.ClientPrivateMessagePacket;
import net.ccbluex.liquidbounce.chat.packet.packets.ClientSuccessPacket;
import net.ccbluex.liquidbounce.chat.packet.packets.Packet;
import net.ccbluex.liquidbounce.chat.packet.packets.ServerBanUserPacket;
import net.ccbluex.liquidbounce.chat.packet.packets.ServerLoginJWTPacket;
import net.ccbluex.liquidbounce.chat.packet.packets.ServerLoginMojangPacket;
import net.ccbluex.liquidbounce.chat.packet.packets.ServerMessagePacket;
import net.ccbluex.liquidbounce.chat.packet.packets.ServerPrivateMessagePacket;
import net.ccbluex.liquidbounce.chat.packet.packets.ServerRequestJWTPacket;
import net.ccbluex.liquidbounce.chat.packet.packets.ServerRequestMojangInfoPacket;
import net.ccbluex.liquidbounce.chat.packet.packets.ServerUnbanUserPacket;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.login.UserUtils;
import org.jetbrains.annotations.Nullable;

public abstract class Client
extends MinecraftInstance
implements ClientListener {
    private Channel channel;
    private String username = "";
    private boolean jwt;
    private boolean loggedIn;
    private final PacketSerializer serializer = new PacketSerializer();
    private final PacketDeserializer deserializer = new PacketDeserializer();

    public final Channel getChannel$LiquidSense() {
        return this.channel;
    }

    public final void setChannel$LiquidSense(@Nullable Channel channel) {
        this.channel = channel;
    }

    public final String getUsername() {
        return this.username;
    }

    public final void setUsername(String string) {
        this.username = string;
    }

    public final boolean getJwt() {
        return this.jwt;
    }

    public final void setJwt(boolean bl) {
        this.jwt = bl;
    }

    public final boolean getLoggedIn() {
        return this.loggedIn;
    }

    public final void setLoggedIn(boolean bl) {
        this.loggedIn = bl;
    }

    public final void connect() {
        this.onConnect();
        URI uri = new URI("wss://chat.liquidbounce.net:7886/ws");
        boolean ssl = StringsKt.equals((String)uri.getScheme(), (String)"wss", (boolean)true);
        SslContext sslContext = ssl ? SslContext.newClientContext((TrustManagerFactory)InsecureTrustManagerFactory.INSTANCE) : null;
        NioEventLoopGroup group = new NioEventLoopGroup();
        ClientHandler handler = new ClientHandler(this, WebSocketClientHandshakerFactory.newHandshaker((URI)uri, (WebSocketVersion)WebSocketVersion.V13, null, (boolean)true, (HttpHeaders)((HttpHeaders)new DefaultHttpHeaders())));
        Bootstrap bootstrap = new Bootstrap();
        ((Bootstrap)((Bootstrap)bootstrap.group((EventLoopGroup)group)).channel(NioSocketChannel.class)).handler((ChannelHandler)new ChannelInitializer<SocketChannel>(sslContext, handler){
            final /* synthetic */ SslContext $sslContext;
            final /* synthetic */ ClientHandler $handler;

            protected void initChannel(SocketChannel ch) {
                ChannelPipeline pipeline = ch.pipeline();
                if (this.$sslContext != null) {
                    pipeline.addLast(new ChannelHandler[]{(ChannelHandler)this.$sslContext.newHandler(ch.alloc())});
                }
                pipeline.addLast(new ChannelHandler[]{(ChannelHandler)new HttpClientCodec(), (ChannelHandler)new HttpObjectAggregator(8192), (ChannelHandler)this.$handler});
            }
            {
                this.$sslContext = $captured_local_variable$0;
                this.$handler = $captured_local_variable$1;
            }
        });
        this.channel = bootstrap.connect(uri.getHost(), uri.getPort()).sync().channel();
        handler.getHandshakeFuture().sync();
        if (this.isConnected()) {
            this.onConnected();
        }
    }

    public final void disconnect() {
        Channel channel = this.channel;
        if (channel != null) {
            channel.close();
        }
        this.channel = null;
        this.username = "";
        this.jwt = false;
    }

    public final void loginMojang() {
        this.sendPacket(new ServerRequestMojangInfoPacket());
    }

    public final void loginJWT(String token) {
        this.onLogon();
        this.sendPacket(new ServerLoginJWTPacket(token, true));
        this.jwt = true;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public final boolean isConnected() {
        if (this.channel == null) return false;
        Channel channel = this.channel;
        if (channel == null) {
            Intrinsics.throwNpe();
        }
        if (!channel.isOpen()) return false;
        return true;
    }

    public final void onMessage$LiquidSense(String message) {
        Gson gson = new GsonBuilder().registerTypeAdapter((Type)((Object)Packet.class), (Object)this.deserializer).create();
        Packet packet = (Packet)gson.fromJson(message, Packet.class);
        if (packet instanceof ClientMojangInfoPacket) {
            this.onLogon();
            try {
                String sessionHash = ((ClientMojangInfoPacket)packet).getSessionHash();
                MinecraftInstance.functions.sessionServiceJoinServer(MinecraftInstance.mc.getSession().getProfile(), MinecraftInstance.mc.getSession().getToken(), sessionHash);
                this.username = MinecraftInstance.mc.getSession().getUsername();
                this.jwt = false;
                this.sendPacket(new ServerLoginMojangPacket(MinecraftInstance.mc.getSession().getUsername(), MinecraftInstance.mc.getSession().getProfile().getId(), true));
            }
            catch (Throwable throwable) {
                this.onError(throwable);
            }
            return;
        }
        this.onPacket(packet);
    }

    public final void sendPacket(Packet packet) {
        block0: {
            Gson gson = new GsonBuilder().registerTypeAdapter((Type)((Object)Packet.class), (Object)this.serializer).create();
            Channel channel = this.channel;
            if (channel == null) break block0;
            channel.writeAndFlush((Object)new TextWebSocketFrame(gson.toJson((Object)packet, (Type)((Object)Packet.class))));
        }
    }

    public final void sendMessage(String message) {
        this.sendPacket(new ServerMessagePacket(message));
    }

    public final void sendPrivateMessage(String username, String message) {
        this.sendPacket(new ServerPrivateMessagePacket(username, message));
    }

    public final void banUser(String target) {
        this.sendPacket(new ServerBanUserPacket(this.toUUID(target)));
    }

    public final void unbanUser(String target) {
        this.sendPacket(new ServerUnbanUserPacket(this.toUUID(target)));
    }

    private final String toUUID(String target) {
        String string;
        try {
            UUID.fromString(target);
            string = target;
        }
        catch (IllegalArgumentException _) {
            String incomingUUID = UserUtils.INSTANCE.getUUID(target);
            if (StringsKt.isBlank((CharSequence)incomingUUID)) {
                return "";
            }
            StringBuffer uuid = new StringBuffer(incomingUUID).insert(20, '-').insert(16, '-').insert(12, '-').insert(8, '-');
            string = uuid.toString();
        }
        return string;
    }

    public Client() {
        this.serializer.registerPacket("RequestMojangInfo", ServerRequestMojangInfoPacket.class);
        this.serializer.registerPacket("LoginMojang", ServerLoginMojangPacket.class);
        this.serializer.registerPacket("Message", ServerMessagePacket.class);
        this.serializer.registerPacket("PrivateMessage", ServerPrivateMessagePacket.class);
        this.serializer.registerPacket("BanUser", ServerBanUserPacket.class);
        this.serializer.registerPacket("UnbanUser", ServerUnbanUserPacket.class);
        this.serializer.registerPacket("RequestJWT", ServerRequestJWTPacket.class);
        this.serializer.registerPacket("LoginJWT", ServerLoginJWTPacket.class);
        this.deserializer.registerPacket("MojangInfo", ClientMojangInfoPacket.class);
        this.deserializer.registerPacket("NewJWT", ClientNewJWTPacket.class);
        this.deserializer.registerPacket("Message", ClientMessagePacket.class);
        this.deserializer.registerPacket("PrivateMessage", ClientPrivateMessagePacket.class);
        this.deserializer.registerPacket("Error", ClientErrorPacket.class);
        this.deserializer.registerPacket("Success", ClientSuccessPacket.class);
    }
}

