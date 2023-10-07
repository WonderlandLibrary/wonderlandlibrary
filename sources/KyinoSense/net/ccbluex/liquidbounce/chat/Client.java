/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.mojang.authlib.GameProfile
 *  com.mojang.authlib.minecraft.MinecraftSessionService
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
 *  io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker
 *  io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory
 *  io.netty.handler.codec.http.websocketx.WebSocketVersion
 *  io.netty.handler.ssl.SslContext
 *  io.netty.handler.ssl.util.InsecureTrustManagerFactory
 *  net.minecraft.client.Minecraft
 *  net.minecraft.util.Session
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.chat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftSessionService;
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
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.UUID;
import javax.net.ssl.TrustManagerFactory;
import kotlin.Metadata;
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
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\u0004\b&\u0018\u00002\u00020\u00012\u00020\u0002B\u0005\u00a2\u0006\u0002\u0010\u0003J\u000e\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u0018J\u0006\u0010 \u001a\u00020\u001eJ\u0006\u0010!\u001a\u00020\u001eJ\u0006\u0010\"\u001a\u00020\rJ\u000e\u0010#\u001a\u00020\u001e2\u0006\u0010$\u001a\u00020\u0018J\u0006\u0010%\u001a\u00020\u001eJ\u0015\u0010&\u001a\u00020\u001e2\u0006\u0010'\u001a\u00020\u0018H\u0000\u00a2\u0006\u0002\b(J\u000e\u0010)\u001a\u00020\u001e2\u0006\u0010'\u001a\u00020\u0018J\u000e\u0010*\u001a\u00020\u001e2\u0006\u0010+\u001a\u00020,J\u0016\u0010-\u001a\u00020\u001e2\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010'\u001a\u00020\u0018J\u0010\u0010.\u001a\u00020\u00182\u0006\u0010\u001f\u001a\u00020\u0018H\u0002J\u000e\u0010/\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u0018R\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u0080\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\tR\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\f\u001a\u00020\rX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u001a\u0010\u0012\u001a\u00020\rX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u000f\"\u0004\b\u0014\u0010\u0011R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0017\u001a\u00020\u0018X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u001a\"\u0004\b\u001b\u0010\u001c\u00a8\u00060"}, d2={"Lnet/ccbluex/liquidbounce/chat/Client;", "Lnet/ccbluex/liquidbounce/chat/ClientListener;", "Lnet/ccbluex/liquidbounce/utils/MinecraftInstance;", "()V", "channel", "Lio/netty/channel/Channel;", "getChannel$KyinoClient", "()Lio/netty/channel/Channel;", "setChannel$KyinoClient", "(Lio/netty/channel/Channel;)V", "deserializer", "Lnet/ccbluex/liquidbounce/chat/packet/PacketDeserializer;", "jwt", "", "getJwt", "()Z", "setJwt", "(Z)V", "loggedIn", "getLoggedIn", "setLoggedIn", "serializer", "Lnet/ccbluex/liquidbounce/chat/packet/PacketSerializer;", "username", "", "getUsername", "()Ljava/lang/String;", "setUsername", "(Ljava/lang/String;)V", "banUser", "", "target", "connect", "disconnect", "isConnected", "loginJWT", "token", "loginMojang", "onMessage", "message", "onMessage$KyinoClient", "sendMessage", "sendPacket", "packet", "Lnet/ccbluex/liquidbounce/chat/packet/packets/Packet;", "sendPrivateMessage", "toUUID", "unbanUser", "KyinoClient"})
public abstract class Client
extends MinecraftInstance
implements ClientListener {
    @Nullable
    private Channel channel;
    @NotNull
    private String username = "";
    private boolean jwt;
    private boolean loggedIn;
    private final PacketSerializer serializer = new PacketSerializer();
    private final PacketDeserializer deserializer = new PacketDeserializer();

    @Nullable
    public final Channel getChannel$KyinoClient() {
        return this.channel;
    }

    public final void setChannel$KyinoClient(@Nullable Channel channel) {
        this.channel = channel;
    }

    @NotNull
    public final String getUsername() {
        return this.username;
    }

    public final void setUsername(@NotNull String string) {
        Intrinsics.checkParameterIsNotNull(string, "<set-?>");
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
        boolean ssl = StringsKt.equals(uri.getScheme(), "wss", true);
        SslContext sslContext = ssl ? SslContext.newClientContext((TrustManagerFactory)InsecureTrustManagerFactory.INSTANCE) : null;
        NioEventLoopGroup group = new NioEventLoopGroup();
        WebSocketClientHandshaker webSocketClientHandshaker = WebSocketClientHandshakerFactory.newHandshaker((URI)uri, (WebSocketVersion)WebSocketVersion.V13, null, (boolean)true, (HttpHeaders)((HttpHeaders)new DefaultHttpHeaders()));
        Intrinsics.checkExpressionValueIsNotNull(webSocketClientHandshaker, "WebSocketClientHandshake\u2026ue, DefaultHttpHeaders())");
        ClientHandler handler = new ClientHandler(this, webSocketClientHandshaker);
        Bootstrap bootstrap = new Bootstrap();
        ((Bootstrap)((Bootstrap)bootstrap.group((EventLoopGroup)group)).channel(NioSocketChannel.class)).handler((ChannelHandler)new ChannelInitializer<SocketChannel>(sslContext, handler){
            final /* synthetic */ SslContext $sslContext;
            final /* synthetic */ ClientHandler $handler;

            protected void initChannel(@NotNull SocketChannel ch) {
                Intrinsics.checkParameterIsNotNull(ch, "ch");
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

    public final void loginJWT(@NotNull String token) {
        Intrinsics.checkParameterIsNotNull(token, "token");
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

    public final void onMessage$KyinoClient(@NotNull String message) {
        Intrinsics.checkParameterIsNotNull(message, "message");
        Gson gson = new GsonBuilder().registerTypeAdapter((Type)((Object)Packet.class), (Object)this.deserializer).create();
        Packet packet = (Packet)gson.fromJson(message, Packet.class);
        if (packet instanceof ClientMojangInfoPacket) {
            this.onLogon();
            try {
                String sessionHash = ((ClientMojangInfoPacket)packet).getSessionHash();
                Minecraft minecraft = Client.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                MinecraftSessionService minecraftSessionService = minecraft.func_152347_ac();
                Session session = Client.access$getMc$p$s1046033730().field_71449_j;
                Intrinsics.checkExpressionValueIsNotNull(session, "mc.session");
                GameProfile gameProfile = session.func_148256_e();
                Session session2 = Client.access$getMc$p$s1046033730().field_71449_j;
                Intrinsics.checkExpressionValueIsNotNull(session2, "mc.session");
                minecraftSessionService.joinServer(gameProfile, session2.func_148254_d(), sessionHash);
                Session session3 = Client.access$getMc$p$s1046033730().field_71449_j;
                Intrinsics.checkExpressionValueIsNotNull(session3, "mc.session");
                String string = session3.func_111285_a();
                Intrinsics.checkExpressionValueIsNotNull(string, "mc.session.username");
                this.username = string;
                this.jwt = false;
                Session session4 = Client.access$getMc$p$s1046033730().field_71449_j;
                Intrinsics.checkExpressionValueIsNotNull(session4, "mc.session");
                String string2 = session4.func_111285_a();
                Intrinsics.checkExpressionValueIsNotNull(string2, "mc.session.username");
                Session session5 = Client.access$getMc$p$s1046033730().field_71449_j;
                Intrinsics.checkExpressionValueIsNotNull(session5, "mc.session");
                GameProfile gameProfile2 = session5.func_148256_e();
                Intrinsics.checkExpressionValueIsNotNull(gameProfile2, "mc.session.profile");
                UUID uUID = gameProfile2.getId();
                Intrinsics.checkExpressionValueIsNotNull(uUID, "mc.session.profile.id");
                this.sendPacket(new ServerLoginMojangPacket(string2, uUID, true));
            }
            catch (Throwable throwable) {
                this.onError(throwable);
            }
            return;
        }
        Packet packet2 = packet;
        Intrinsics.checkExpressionValueIsNotNull(packet2, "packet");
        this.onPacket(packet2);
    }

    public final void sendPacket(@NotNull Packet packet) {
        block0: {
            Intrinsics.checkParameterIsNotNull(packet, "packet");
            Gson gson = new GsonBuilder().registerTypeAdapter((Type)((Object)Packet.class), (Object)this.serializer).create();
            Channel channel = this.channel;
            if (channel == null) break block0;
            channel.writeAndFlush((Object)new TextWebSocketFrame(gson.toJson((Object)packet, (Type)((Object)Packet.class))));
        }
    }

    public final void sendMessage(@NotNull String message) {
        Intrinsics.checkParameterIsNotNull(message, "message");
        this.sendPacket(new ServerMessagePacket(message));
    }

    public final void sendPrivateMessage(@NotNull String username, @NotNull String message) {
        Intrinsics.checkParameterIsNotNull(username, "username");
        Intrinsics.checkParameterIsNotNull(message, "message");
        this.sendPacket(new ServerPrivateMessagePacket(username, message));
    }

    public final void banUser(@NotNull String target) {
        Intrinsics.checkParameterIsNotNull(target, "target");
        this.sendPacket(new ServerBanUserPacket(this.toUUID(target)));
    }

    public final void unbanUser(@NotNull String target) {
        Intrinsics.checkParameterIsNotNull(target, "target");
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
            if (StringsKt.isBlank(incomingUUID)) {
                return "";
            }
            StringBuffer uuid = new StringBuffer(incomingUUID).insert(20, '-').insert(16, '-').insert(12, '-').insert(8, '-');
            String string2 = uuid.toString();
            Intrinsics.checkExpressionValueIsNotNull(string2, "uuid.toString()");
            string = string2;
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

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

