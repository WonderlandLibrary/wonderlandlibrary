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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000D\n\n\n\n\b\n\n\b\n\n\u0000\n\n\b\b\n\n\u0000\n\n\b\n\n\b\r\n\n\b\b&\u00002020B¢J020J 0J!0J\"0\rJ#02$0J%0J&02'0H\u0000¢\b(J)02'0J*02+0,J-0202'0J.020HJ/020R0X¢\n\u0000\b\"\b\b\tR\n0X¢\n\u0000R\f0\rX¢\n\u0000\b\"\bR0\rX¢\n\u0000\b\"\bR0X¢\n\u0000R0X¢\n\u0000\b\"\b¨0"}, d2={"Lnet/ccbluex/liquidbounce/chat/Client;", "Lnet/ccbluex/liquidbounce/chat/ClientListener;", "Lnet/ccbluex/liquidbounce/utils/MinecraftInstance;", "()V", "channel", "Lio/netty/channel/Channel;", "getChannel$Pride", "()Lio/netty/channel/Channel;", "setChannel$Pride", "(Lio/netty/channel/Channel;)V", "deserializer", "Lnet/ccbluex/liquidbounce/chat/packet/PacketDeserializer;", "jwt", "", "getJwt", "()Z", "setJwt", "(Z)V", "loggedIn", "getLoggedIn", "setLoggedIn", "serializer", "Lnet/ccbluex/liquidbounce/chat/packet/PacketSerializer;", "username", "", "getUsername", "()Ljava/lang/String;", "setUsername", "(Ljava/lang/String;)V", "banUser", "", "target", "connect", "disconnect", "isConnected", "loginJWT", "token", "loginMojang", "onMessage", "message", "onMessage$Pride", "sendMessage", "sendPacket", "packet", "Lnet/ccbluex/liquidbounce/chat/packet/packets/Packet;", "sendPrivateMessage", "toUUID", "unbanUser", "Pride"})
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
    public final Channel getChannel$Pride() {
        return this.channel;
    }

    public final void setChannel$Pride(@Nullable Channel channel) {
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
        Intrinsics.checkExpressionValueIsNotNull(webSocketClientHandshaker, "WebSocketClientHandshake…ue, DefaultHttpHeaders())");
        ClientHandler handler = new ClientHandler(this, webSocketClientHandshaker);
        Bootstrap bootstrap = new Bootstrap();
        ((Bootstrap)((Bootstrap)bootstrap.group((EventLoopGroup)group)).channel(NioSocketChannel.class)).handler((ChannelHandler)new ChannelInitializer<SocketChannel>(sslContext, handler){
            final SslContext $sslContext;
            final ClientHandler $handler;

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

    public final void onMessage$Pride(@NotNull String message) {
        Intrinsics.checkParameterIsNotNull(message, "message");
        Gson gson = new GsonBuilder().registerTypeAdapter((Type)((Object)Packet.class), (Object)this.deserializer).create();
        Packet packet = (Packet)gson.fromJson(message, Packet.class);
        if (packet instanceof ClientMojangInfoPacket) {
            this.onLogon();
            try {
                String sessionHash = ((ClientMojangInfoPacket)packet).getSessionHash();
                MinecraftInstance.functions.sessionServiceJoinServer(MinecraftInstance.mc.getSession().getProfile(), MinecraftInstance.mc.getSession().getToken(), sessionHash);
                this.username = MinecraftInstance.mc.getSession().getUsername();
                this.jwt = false;
                String string = MinecraftInstance.mc.getSession().getUsername();
                UUID uUID = MinecraftInstance.mc.getSession().getProfile().getId();
                Intrinsics.checkExpressionValueIsNotNull(uUID, "mc.session.profile.id");
                this.sendPacket(new ServerLoginMojangPacket(string, uUID, true));
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
}
