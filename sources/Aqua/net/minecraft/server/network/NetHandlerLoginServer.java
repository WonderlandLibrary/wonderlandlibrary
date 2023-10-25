package net.minecraft.server.network;

import com.google.common.base.Charsets;
import com.mojang.authlib.GameProfile;
import io.netty.util.concurrent.GenericFutureListener;
import java.security.PrivateKey;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import javax.crypto.SecretKey;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.login.INetHandlerLoginServer;
import net.minecraft.network.login.client.C00PacketLoginStart;
import net.minecraft.network.login.client.C01PacketEncryptionResponse;
import net.minecraft.network.login.server.S00PacketDisconnect;
import net.minecraft.network.login.server.S01PacketEncryptionRequest;
import net.minecraft.network.login.server.S02PacketLoginSuccess;
import net.minecraft.network.login.server.S03PacketEnableCompression;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.NetHandlerLoginServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ITickable;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NetHandlerLoginServer
implements INetHandlerLoginServer,
ITickable {
    private static final AtomicInteger AUTHENTICATOR_THREAD_ID = new AtomicInteger(0);
    private static final Logger logger = LogManager.getLogger();
    private static final Random RANDOM = new Random();
    private final byte[] verifyToken = new byte[4];
    private final MinecraftServer server;
    public final NetworkManager networkManager;
    private LoginState currentLoginState = LoginState.HELLO;
    private int connectionTimer;
    private GameProfile loginGameProfile;
    private String serverId = "";
    private SecretKey secretKey;
    private EntityPlayerMP player;

    public NetHandlerLoginServer(MinecraftServer serverIn, NetworkManager networkManagerIn) {
        this.server = serverIn;
        this.networkManager = networkManagerIn;
        RANDOM.nextBytes(this.verifyToken);
    }

    public void update() {
        EntityPlayerMP entityplayermp;
        if (this.currentLoginState == LoginState.READY_TO_ACCEPT) {
            this.tryAcceptPlayer();
        } else if (this.currentLoginState == LoginState.DELAY_ACCEPT && (entityplayermp = this.server.getConfigurationManager().getPlayerByUUID(this.loginGameProfile.getId())) == null) {
            this.currentLoginState = LoginState.READY_TO_ACCEPT;
            this.server.getConfigurationManager().initializeConnectionToPlayer(this.networkManager, this.player);
            this.player = null;
        }
        if (this.connectionTimer++ == 600) {
            this.closeConnection("Took too long to log in");
        }
    }

    public void closeConnection(String reason) {
        try {
            logger.info("Disconnecting " + this.getConnectionInfo() + ": " + reason);
            ChatComponentText chatcomponenttext = new ChatComponentText(reason);
            this.networkManager.sendPacket((Packet)new S00PacketDisconnect((IChatComponent)chatcomponenttext));
            this.networkManager.closeChannel((IChatComponent)chatcomponenttext);
        }
        catch (Exception exception) {
            logger.error("Error whilst disconnecting player", (Throwable)exception);
        }
    }

    public void tryAcceptPlayer() {
        String s;
        if (!this.loginGameProfile.isComplete()) {
            this.loginGameProfile = this.getOfflineProfile(this.loginGameProfile);
        }
        if ((s = this.server.getConfigurationManager().allowUserToConnect(this.networkManager.getRemoteAddress(), this.loginGameProfile)) != null) {
            this.closeConnection(s);
        } else {
            this.currentLoginState = LoginState.ACCEPTED;
            if (this.server.getNetworkCompressionTreshold() >= 0 && !this.networkManager.isLocalChannel()) {
                this.networkManager.sendPacket((Packet)new S03PacketEnableCompression(this.server.getNetworkCompressionTreshold()), (GenericFutureListener)new /* Unavailable Anonymous Inner Class!! */, new GenericFutureListener[0]);
            }
            this.networkManager.sendPacket((Packet)new S02PacketLoginSuccess(this.loginGameProfile));
            EntityPlayerMP entityplayermp = this.server.getConfigurationManager().getPlayerByUUID(this.loginGameProfile.getId());
            if (entityplayermp != null) {
                this.currentLoginState = LoginState.DELAY_ACCEPT;
                this.player = this.server.getConfigurationManager().createPlayerForUser(this.loginGameProfile);
            } else {
                this.server.getConfigurationManager().initializeConnectionToPlayer(this.networkManager, this.server.getConfigurationManager().createPlayerForUser(this.loginGameProfile));
            }
        }
    }

    public void onDisconnect(IChatComponent reason) {
        logger.info(this.getConnectionInfo() + " lost connection: " + reason.getUnformattedText());
    }

    public String getConnectionInfo() {
        return this.loginGameProfile != null ? this.loginGameProfile.toString() + " (" + this.networkManager.getRemoteAddress().toString() + ")" : String.valueOf((Object)this.networkManager.getRemoteAddress());
    }

    public void processLoginStart(C00PacketLoginStart packetIn) {
        Validate.validState((this.currentLoginState == LoginState.HELLO ? 1 : 0) != 0, (String)"Unexpected hello packet", (Object[])new Object[0]);
        this.loginGameProfile = packetIn.getProfile();
        if (this.server.isServerInOnlineMode() && !this.networkManager.isLocalChannel()) {
            this.currentLoginState = LoginState.KEY;
            this.networkManager.sendPacket((Packet)new S01PacketEncryptionRequest(this.serverId, this.server.getKeyPair().getPublic(), this.verifyToken));
        } else {
            this.currentLoginState = LoginState.READY_TO_ACCEPT;
        }
    }

    public void processEncryptionResponse(C01PacketEncryptionResponse packetIn) {
        Validate.validState((this.currentLoginState == LoginState.KEY ? 1 : 0) != 0, (String)"Unexpected key packet", (Object[])new Object[0]);
        PrivateKey privatekey = this.server.getKeyPair().getPrivate();
        if (!Arrays.equals((byte[])this.verifyToken, (byte[])packetIn.getVerifyToken(privatekey))) {
            throw new IllegalStateException("Invalid nonce!");
        }
        this.secretKey = packetIn.getSecretKey(privatekey);
        this.currentLoginState = LoginState.AUTHENTICATING;
        this.networkManager.enableEncryption(this.secretKey);
        new /* Unavailable Anonymous Inner Class!! */.start();
    }

    protected GameProfile getOfflineProfile(GameProfile original) {
        UUID uuid = UUID.nameUUIDFromBytes((byte[])("OfflinePlayer:" + original.getName()).getBytes(Charsets.UTF_8));
        return new GameProfile(uuid, original.getName());
    }

    static /* synthetic */ MinecraftServer access$000(NetHandlerLoginServer x0) {
        return x0.server;
    }

    static /* synthetic */ GameProfile access$100(NetHandlerLoginServer x0) {
        return x0.loginGameProfile;
    }

    static /* synthetic */ String access$200(NetHandlerLoginServer x0) {
        return x0.serverId;
    }

    static /* synthetic */ SecretKey access$300(NetHandlerLoginServer x0) {
        return x0.secretKey;
    }

    static /* synthetic */ GameProfile access$102(NetHandlerLoginServer x0, GameProfile x1) {
        x0.loginGameProfile = x1;
        return x0.loginGameProfile;
    }

    static /* synthetic */ Logger access$400() {
        return logger;
    }

    static /* synthetic */ LoginState access$502(NetHandlerLoginServer x0, LoginState x1) {
        x0.currentLoginState = x1;
        return x0.currentLoginState;
    }
}
