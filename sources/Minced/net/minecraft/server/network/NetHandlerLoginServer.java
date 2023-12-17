// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server.network;

import org.apache.logging.log4j.LogManager;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import javax.annotation.Nullable;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import java.net.InetAddress;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import java.util.UUID;
import java.math.BigInteger;
import net.minecraft.util.CryptManager;
import java.util.Arrays;
import net.minecraft.network.login.client.CPacketEncryptionResponse;
import net.minecraft.network.login.server.SPacketEncryptionRequest;
import org.apache.commons.lang3.Validate;
import net.minecraft.network.login.client.CPacketLoginStart;
import net.minecraft.network.login.server.SPacketLoginSuccess;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Future;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import net.minecraft.network.login.server.SPacketEnableCompression;
import net.minecraft.network.Packet;
import net.minecraft.network.login.server.SPacketDisconnect;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.entity.player.EntityPlayerMP;
import javax.crypto.SecretKey;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.NetworkManager;
import net.minecraft.server.MinecraftServer;
import java.util.Random;
import org.apache.logging.log4j.Logger;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.util.ITickable;
import net.minecraft.network.login.INetHandlerLoginServer;

public class NetHandlerLoginServer implements INetHandlerLoginServer, ITickable
{
    private static final AtomicInteger AUTHENTICATOR_THREAD_ID;
    private static final Logger LOGGER;
    private static final Random RANDOM;
    private final byte[] verifyToken;
    private final MinecraftServer server;
    public final NetworkManager networkManager;
    private LoginState currentLoginState;
    private int connectionTimer;
    private GameProfile loginGameProfile;
    private final String serverId = "";
    private SecretKey secretKey;
    private EntityPlayerMP player;
    
    public NetHandlerLoginServer(final MinecraftServer serverIn, final NetworkManager networkManagerIn) {
        this.verifyToken = new byte[4];
        this.currentLoginState = LoginState.HELLO;
        this.server = serverIn;
        this.networkManager = networkManagerIn;
        NetHandlerLoginServer.RANDOM.nextBytes(this.verifyToken);
    }
    
    @Override
    public void update() {
        if (this.currentLoginState == LoginState.READY_TO_ACCEPT) {
            this.tryAcceptPlayer();
        }
        else if (this.currentLoginState == LoginState.DELAY_ACCEPT) {
            final EntityPlayerMP entityplayermp = this.server.getPlayerList().getPlayerByUUID(this.loginGameProfile.getId());
            if (entityplayermp == null) {
                this.currentLoginState = LoginState.READY_TO_ACCEPT;
                this.server.getPlayerList().initializeConnectionToPlayer(this.networkManager, this.player);
                this.player = null;
            }
        }
        if (this.connectionTimer++ == 600) {
            this.disconnect(new TextComponentTranslation("multiplayer.disconnect.slow_login", new Object[0]));
        }
    }
    
    public void disconnect(final ITextComponent reason) {
        try {
            NetHandlerLoginServer.LOGGER.info("Disconnecting {}: {}", (Object)this.getConnectionInfo(), (Object)reason.getUnformattedText());
            this.networkManager.sendPacket(new SPacketDisconnect(reason));
            this.networkManager.closeChannel(reason);
        }
        catch (Exception exception) {
            NetHandlerLoginServer.LOGGER.error("Error whilst disconnecting player", (Throwable)exception);
        }
    }
    
    public void tryAcceptPlayer() {
        if (!this.loginGameProfile.isComplete()) {
            this.loginGameProfile = this.getOfflineProfile(this.loginGameProfile);
        }
        final String s = this.server.getPlayerList().allowUserToConnect(this.networkManager.getRemoteAddress(), this.loginGameProfile);
        if (s != null) {
            this.disconnect(new TextComponentTranslation(s, new Object[0]));
        }
        else {
            this.currentLoginState = LoginState.ACCEPTED;
            if (this.server.getNetworkCompressionThreshold() >= 0 && !this.networkManager.isLocalChannel()) {
                this.networkManager.sendPacket(new SPacketEnableCompression(this.server.getNetworkCompressionThreshold()), (GenericFutureListener<? extends Future<? super Void>>)new ChannelFutureListener() {
                    public void operationComplete(final ChannelFuture p_operationComplete_1_) throws Exception {
                        NetHandlerLoginServer.this.networkManager.setCompressionThreshold(NetHandlerLoginServer.this.server.getNetworkCompressionThreshold());
                    }
                }, (GenericFutureListener<? extends Future<? super Void>>[])new GenericFutureListener[0]);
            }
            this.networkManager.sendPacket(new SPacketLoginSuccess(this.loginGameProfile));
            final EntityPlayerMP entityplayermp = this.server.getPlayerList().getPlayerByUUID(this.loginGameProfile.getId());
            if (entityplayermp != null) {
                this.currentLoginState = LoginState.DELAY_ACCEPT;
                this.player = this.server.getPlayerList().createPlayerForUser(this.loginGameProfile);
            }
            else {
                this.server.getPlayerList().initializeConnectionToPlayer(this.networkManager, this.server.getPlayerList().createPlayerForUser(this.loginGameProfile));
            }
        }
    }
    
    @Override
    public void onDisconnect(final ITextComponent reason) {
        NetHandlerLoginServer.LOGGER.info("{} lost connection: {}", (Object)this.getConnectionInfo(), (Object)reason.getUnformattedText());
    }
    
    public String getConnectionInfo() {
        return (this.loginGameProfile != null) ? (this.loginGameProfile + " (" + this.networkManager.getRemoteAddress() + ")") : String.valueOf(this.networkManager.getRemoteAddress());
    }
    
    @Override
    public void processLoginStart(final CPacketLoginStart packetIn) {
        Validate.validState(this.currentLoginState == LoginState.HELLO, "Unexpected hello packet", new Object[0]);
        this.loginGameProfile = packetIn.getProfile();
        if (this.server.isServerInOnlineMode() && !this.networkManager.isLocalChannel()) {
            this.currentLoginState = LoginState.KEY;
            this.networkManager.sendPacket(new SPacketEncryptionRequest("", this.server.getKeyPair().getPublic(), this.verifyToken));
        }
        else {
            this.currentLoginState = LoginState.READY_TO_ACCEPT;
        }
    }
    
    @Override
    public void processEncryptionResponse(final CPacketEncryptionResponse packetIn) {
        Validate.validState(this.currentLoginState == LoginState.KEY, "Unexpected key packet", new Object[0]);
        final PrivateKey privatekey = this.server.getKeyPair().getPrivate();
        if (!Arrays.equals(this.verifyToken, packetIn.getVerifyToken(privatekey))) {
            throw new IllegalStateException("Invalid nonce!");
        }
        this.secretKey = packetIn.getSecretKey(privatekey);
        this.currentLoginState = LoginState.AUTHENTICATING;
        this.networkManager.enableEncryption(this.secretKey);
        new Thread("User Authenticator #" + NetHandlerLoginServer.AUTHENTICATOR_THREAD_ID.incrementAndGet()) {
            @Override
            public void run() {
                final GameProfile gameprofile = NetHandlerLoginServer.this.loginGameProfile;
                try {
                    final String s = new BigInteger(CryptManager.getServerIdHash("", NetHandlerLoginServer.this.server.getKeyPair().getPublic(), NetHandlerLoginServer.this.secretKey)).toString(16);
                    NetHandlerLoginServer.this.loginGameProfile = NetHandlerLoginServer.this.server.getMinecraftSessionService().hasJoinedServer(new GameProfile((UUID)null, gameprofile.getName()), s, this.getAddress());
                    if (NetHandlerLoginServer.this.loginGameProfile != null) {
                        NetHandlerLoginServer.LOGGER.info("UUID of player {} is {}", (Object)NetHandlerLoginServer.this.loginGameProfile.getName(), (Object)NetHandlerLoginServer.this.loginGameProfile.getId());
                        NetHandlerLoginServer.this.currentLoginState = LoginState.READY_TO_ACCEPT;
                    }
                    else if (NetHandlerLoginServer.this.server.isSinglePlayer()) {
                        NetHandlerLoginServer.LOGGER.warn("Failed to verify username but will let them in anyway!");
                        NetHandlerLoginServer.this.loginGameProfile = NetHandlerLoginServer.this.getOfflineProfile(gameprofile);
                        NetHandlerLoginServer.this.currentLoginState = LoginState.READY_TO_ACCEPT;
                    }
                    else {
                        NetHandlerLoginServer.this.disconnect(new TextComponentTranslation("multiplayer.disconnect.unverified_username", new Object[0]));
                        NetHandlerLoginServer.LOGGER.error("Username '{}' tried to join with an invalid session", (Object)gameprofile.getName());
                    }
                }
                catch (AuthenticationUnavailableException var3) {
                    if (NetHandlerLoginServer.this.server.isSinglePlayer()) {
                        NetHandlerLoginServer.LOGGER.warn("Authentication servers are down but will let them in anyway!");
                        NetHandlerLoginServer.this.loginGameProfile = NetHandlerLoginServer.this.getOfflineProfile(gameprofile);
                        NetHandlerLoginServer.this.currentLoginState = LoginState.READY_TO_ACCEPT;
                    }
                    else {
                        NetHandlerLoginServer.this.disconnect(new TextComponentTranslation("multiplayer.disconnect.authservers_down", new Object[0]));
                        NetHandlerLoginServer.LOGGER.error("Couldn't verify username because servers are unavailable");
                    }
                }
            }
            
            @Nullable
            private InetAddress getAddress() {
                final SocketAddress socketaddress = NetHandlerLoginServer.this.networkManager.getRemoteAddress();
                return (NetHandlerLoginServer.this.server.getPreventProxyConnections() && socketaddress instanceof InetSocketAddress) ? ((InetSocketAddress)socketaddress).getAddress() : null;
            }
        }.start();
    }
    
    protected GameProfile getOfflineProfile(final GameProfile original) {
        final UUID uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + original.getName()).getBytes(StandardCharsets.UTF_8));
        return new GameProfile(uuid, original.getName());
    }
    
    static {
        AUTHENTICATOR_THREAD_ID = new AtomicInteger(0);
        LOGGER = LogManager.getLogger();
        RANDOM = new Random();
    }
    
    enum LoginState
    {
        HELLO, 
        KEY, 
        AUTHENTICATING, 
        READY_TO_ACCEPT, 
        DELAY_ACCEPT, 
        ACCEPTED;
    }
}
