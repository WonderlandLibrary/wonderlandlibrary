/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Charsets
 *  com.mojang.authlib.GameProfile
 *  com.mojang.authlib.exceptions.AuthenticationUnavailableException
 *  org.apache.commons.lang3.Validate
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.server.network;

import com.google.common.base.Charsets;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.concurrent.GenericFutureListener;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import javax.crypto.SecretKey;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.login.INetHandlerLoginServer;
import net.minecraft.network.login.client.C00PacketLoginStart;
import net.minecraft.network.login.client.C01PacketEncryptionResponse;
import net.minecraft.network.login.server.S00PacketDisconnect;
import net.minecraft.network.login.server.S01PacketEncryptionRequest;
import net.minecraft.network.login.server.S02PacketLoginSuccess;
import net.minecraft.network.login.server.S03PacketEnableCompression;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.CryptManager;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ITickable;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NetHandlerLoginServer
implements ITickable,
INetHandlerLoginServer {
    private final MinecraftServer server;
    private String serverId = "";
    private static final AtomicInteger AUTHENTICATOR_THREAD_ID = new AtomicInteger(0);
    private LoginState currentLoginState;
    private final byte[] verifyToken = new byte[4];
    private static final Random RANDOM;
    private static final Logger logger;
    private EntityPlayerMP field_181025_l;
    public final NetworkManager networkManager;
    private SecretKey secretKey;
    private int connectionTimer;
    private GameProfile loginGameProfile;

    public NetHandlerLoginServer(MinecraftServer minecraftServer, NetworkManager networkManager) {
        this.currentLoginState = LoginState.HELLO;
        this.server = minecraftServer;
        this.networkManager = networkManager;
        RANDOM.nextBytes(this.verifyToken);
    }

    @Override
    public void update() {
        EntityPlayerMP entityPlayerMP;
        if (this.currentLoginState == LoginState.READY_TO_ACCEPT) {
            this.tryAcceptPlayer();
        } else if (this.currentLoginState == LoginState.DELAY_ACCEPT && (entityPlayerMP = this.server.getConfigurationManager().getPlayerByUUID(this.loginGameProfile.getId())) == null) {
            this.currentLoginState = LoginState.READY_TO_ACCEPT;
            this.server.getConfigurationManager().initializeConnectionToPlayer(this.networkManager, this.field_181025_l);
            this.field_181025_l = null;
        }
        if (this.connectionTimer++ == 600) {
            this.closeConnection("Took too long to log in");
        }
    }

    @Override
    public void processLoginStart(C00PacketLoginStart c00PacketLoginStart) {
        Validate.validState((this.currentLoginState == LoginState.HELLO ? 1 : 0) != 0, (String)"Unexpected hello packet", (Object[])new Object[0]);
        this.loginGameProfile = c00PacketLoginStart.getProfile();
        if (this.server.isServerInOnlineMode() && !this.networkManager.isLocalChannel()) {
            this.currentLoginState = LoginState.KEY;
            this.networkManager.sendPacket(new S01PacketEncryptionRequest(this.serverId, this.server.getKeyPair().getPublic(), this.verifyToken));
        } else {
            this.currentLoginState = LoginState.READY_TO_ACCEPT;
        }
    }

    protected GameProfile getOfflineProfile(GameProfile gameProfile) {
        UUID uUID = UUID.nameUUIDFromBytes(("OfflinePlayer:" + gameProfile.getName()).getBytes(Charsets.UTF_8));
        return new GameProfile(uUID, gameProfile.getName());
    }

    @Override
    public void processEncryptionResponse(C01PacketEncryptionResponse c01PacketEncryptionResponse) {
        Validate.validState((this.currentLoginState == LoginState.KEY ? 1 : 0) != 0, (String)"Unexpected key packet", (Object[])new Object[0]);
        PrivateKey privateKey = this.server.getKeyPair().getPrivate();
        if (!Arrays.equals(this.verifyToken, c01PacketEncryptionResponse.getVerifyToken(privateKey))) {
            throw new IllegalStateException("Invalid nonce!");
        }
        this.secretKey = c01PacketEncryptionResponse.getSecretKey(privateKey);
        this.currentLoginState = LoginState.AUTHENTICATING;
        this.networkManager.enableEncryption(this.secretKey);
        new Thread("User Authenticator #" + AUTHENTICATOR_THREAD_ID.incrementAndGet()){

            @Override
            public void run() {
                GameProfile gameProfile = NetHandlerLoginServer.this.loginGameProfile;
                try {
                    String string = new BigInteger(CryptManager.getServerIdHash(NetHandlerLoginServer.this.serverId, NetHandlerLoginServer.this.server.getKeyPair().getPublic(), NetHandlerLoginServer.this.secretKey)).toString(16);
                    NetHandlerLoginServer.this.loginGameProfile = NetHandlerLoginServer.this.server.getMinecraftSessionService().hasJoinedServer(new GameProfile(null, gameProfile.getName()), string);
                    if (NetHandlerLoginServer.this.loginGameProfile != null) {
                        logger.info("UUID of player " + NetHandlerLoginServer.this.loginGameProfile.getName() + " is " + NetHandlerLoginServer.this.loginGameProfile.getId());
                        NetHandlerLoginServer.this.currentLoginState = LoginState.READY_TO_ACCEPT;
                    } else if (NetHandlerLoginServer.this.server.isSinglePlayer()) {
                        logger.warn("Failed to verify username but will let them in anyway!");
                        NetHandlerLoginServer.this.loginGameProfile = NetHandlerLoginServer.this.getOfflineProfile(gameProfile);
                        NetHandlerLoginServer.this.currentLoginState = LoginState.READY_TO_ACCEPT;
                    } else {
                        NetHandlerLoginServer.this.closeConnection("Failed to verify username!");
                        logger.error("Username '" + NetHandlerLoginServer.this.loginGameProfile.getName() + "' tried to join with an invalid session");
                    }
                }
                catch (AuthenticationUnavailableException authenticationUnavailableException) {
                    if (NetHandlerLoginServer.this.server.isSinglePlayer()) {
                        logger.warn("Authentication servers are down but will let them in anyway!");
                        NetHandlerLoginServer.this.loginGameProfile = NetHandlerLoginServer.this.getOfflineProfile(gameProfile);
                        NetHandlerLoginServer.this.currentLoginState = LoginState.READY_TO_ACCEPT;
                    }
                    NetHandlerLoginServer.this.closeConnection("Authentication servers are down. Please try again later, sorry!");
                    logger.error("Couldn't verify username because servers are unavailable");
                }
            }
        }.start();
    }

    public void tryAcceptPlayer() {
        String string;
        if (!this.loginGameProfile.isComplete()) {
            this.loginGameProfile = this.getOfflineProfile(this.loginGameProfile);
        }
        if ((string = this.server.getConfigurationManager().allowUserToConnect(this.networkManager.getRemoteAddress(), this.loginGameProfile)) != null) {
            this.closeConnection(string);
        } else {
            this.currentLoginState = LoginState.ACCEPTED;
            if (this.server.getNetworkCompressionTreshold() >= 0 && !this.networkManager.isLocalChannel()) {
                this.networkManager.sendPacket(new S03PacketEnableCompression(this.server.getNetworkCompressionTreshold()), new ChannelFutureListener(){

                    @Override
                    public void operationComplete(ChannelFuture channelFuture) throws Exception {
                        NetHandlerLoginServer.this.networkManager.setCompressionTreshold(NetHandlerLoginServer.this.server.getNetworkCompressionTreshold());
                    }
                }, new GenericFutureListener[0]);
            }
            this.networkManager.sendPacket(new S02PacketLoginSuccess(this.loginGameProfile));
            EntityPlayerMP entityPlayerMP = this.server.getConfigurationManager().getPlayerByUUID(this.loginGameProfile.getId());
            if (entityPlayerMP != null) {
                this.currentLoginState = LoginState.DELAY_ACCEPT;
                this.field_181025_l = this.server.getConfigurationManager().createPlayerForUser(this.loginGameProfile);
            } else {
                this.server.getConfigurationManager().initializeConnectionToPlayer(this.networkManager, this.server.getConfigurationManager().createPlayerForUser(this.loginGameProfile));
            }
        }
    }

    static {
        logger = LogManager.getLogger();
        RANDOM = new Random();
    }

    public void closeConnection(String string) {
        try {
            logger.info("Disconnecting " + this.getConnectionInfo() + ": " + string);
            ChatComponentText chatComponentText = new ChatComponentText(string);
            this.networkManager.sendPacket(new S00PacketDisconnect(chatComponentText));
            this.networkManager.closeChannel(chatComponentText);
        }
        catch (Exception exception) {
            logger.error("Error whilst disconnecting player", (Throwable)exception);
        }
    }

    public String getConnectionInfo() {
        return this.loginGameProfile != null ? String.valueOf(this.loginGameProfile.toString()) + " (" + this.networkManager.getRemoteAddress().toString() + ")" : String.valueOf(this.networkManager.getRemoteAddress());
    }

    @Override
    public void onDisconnect(IChatComponent iChatComponent) {
        logger.info(String.valueOf(this.getConnectionInfo()) + " lost connection: " + iChatComponent.getUnformattedText());
    }

    static enum LoginState {
        HELLO,
        KEY,
        AUTHENTICATING,
        READY_TO_ACCEPT,
        DELAY_ACCEPT,
        ACCEPTED;

    }
}

