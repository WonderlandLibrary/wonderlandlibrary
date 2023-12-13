// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.server.network;

import org.apache.logging.log4j.LogManager;
import com.google.common.base.Charsets;
import java.security.PrivateKey;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import java.util.UUID;
import java.math.BigInteger;
import net.minecraft.util.CryptManager;
import java.util.Arrays;
import net.minecraft.network.login.client.C01PacketEncryptionResponse;
import net.minecraft.network.login.server.S01PacketEncryptionRequest;
import org.apache.commons.lang3.Validate;
import net.minecraft.network.login.client.C00PacketLoginStart;
import net.minecraft.network.login.server.S02PacketLoginSuccess;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Future;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import net.minecraft.network.login.server.S03PacketEnableCompression;
import net.minecraft.network.Packet;
import net.minecraft.util.IChatComponent;
import net.minecraft.network.login.server.S00PacketDisconnect;
import net.minecraft.util.ChatComponentText;
import javax.crypto.SecretKey;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.NetworkManager;
import net.minecraft.server.MinecraftServer;
import java.util.Random;
import org.apache.logging.log4j.Logger;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.network.login.INetHandlerLoginServer;

public class NetHandlerLoginServer implements INetHandlerLoginServer, IUpdatePlayerListBox
{
    private static final AtomicInteger AUTHENTICATOR_THREAD_ID;
    private static final Logger logger;
    private static final Random RANDOM;
    private final byte[] field_147330_e;
    private final MinecraftServer server;
    public final NetworkManager networkManager;
    private LoginState currentLoginState;
    private int connectionTimer;
    private GameProfile loginGameProfile;
    private String serverId;
    private SecretKey secretKey;
    private static final String __OBFID = "CL_00001458";
    
    public NetHandlerLoginServer(final MinecraftServer p_i45298_1_, final NetworkManager p_i45298_2_) {
        this.field_147330_e = new byte[4];
        this.currentLoginState = LoginState.HELLO;
        this.serverId = "";
        this.server = p_i45298_1_;
        this.networkManager = p_i45298_2_;
        NetHandlerLoginServer.RANDOM.nextBytes(this.field_147330_e);
    }
    
    @Override
    public void update() {
        if (this.currentLoginState == LoginState.READY_TO_ACCEPT) {
            this.func_147326_c();
        }
        if (this.connectionTimer++ == 600) {
            this.closeConnection("Took too long to log in");
        }
    }
    
    public void closeConnection(final String reason) {
        try {
            NetHandlerLoginServer.logger.info("Disconnecting " + this.func_147317_d() + ": " + reason);
            final ChatComponentText var2 = new ChatComponentText(reason);
            this.networkManager.sendPacket(new S00PacketDisconnect(var2));
            this.networkManager.closeChannel(var2);
        }
        catch (Exception var3) {
            NetHandlerLoginServer.logger.error("Error whilst disconnecting player", (Throwable)var3);
        }
    }
    
    public void func_147326_c() {
        if (!this.loginGameProfile.isComplete()) {
            this.loginGameProfile = this.getOfflineProfile(this.loginGameProfile);
        }
        final String var1 = this.server.getConfigurationManager().allowUserToConnect(this.networkManager.getRemoteAddress(), this.loginGameProfile);
        if (var1 != null) {
            this.closeConnection(var1);
        }
        else {
            this.currentLoginState = LoginState.ACCEPTED;
            if (this.server.getNetworkCompressionTreshold() >= 0 && !this.networkManager.isLocalChannel()) {
                this.networkManager.sendPacket(new S03PacketEnableCompression(this.server.getNetworkCompressionTreshold()), (GenericFutureListener)new ChannelFutureListener() {
                    private static final String __OBFID = "CL_00001459";
                    
                    public void operationComplete(final ChannelFuture p_operationComplete_1_) {
                        NetHandlerLoginServer.this.networkManager.setCompressionTreshold(NetHandlerLoginServer.this.server.getNetworkCompressionTreshold());
                    }
                }, new GenericFutureListener[0]);
            }
            this.networkManager.sendPacket(new S02PacketLoginSuccess(this.loginGameProfile));
            this.server.getConfigurationManager().initializeConnectionToPlayer(this.networkManager, this.server.getConfigurationManager().createPlayerForUser(this.loginGameProfile));
        }
    }
    
    @Override
    public void onDisconnect(final IChatComponent reason) {
        NetHandlerLoginServer.logger.info(this.func_147317_d() + " lost connection: " + reason.getUnformattedText());
    }
    
    public String func_147317_d() {
        return (this.loginGameProfile != null) ? (this.loginGameProfile.toString() + " (" + this.networkManager.getRemoteAddress().toString() + ")") : String.valueOf(this.networkManager.getRemoteAddress());
    }
    
    @Override
    public void processLoginStart(final C00PacketLoginStart packetIn) {
        Validate.validState(this.currentLoginState == LoginState.HELLO, "Unexpected hello packet", new Object[0]);
        this.loginGameProfile = packetIn.getProfile();
        if (this.server.isServerInOnlineMode() && !this.networkManager.isLocalChannel()) {
            this.currentLoginState = LoginState.KEY;
            this.networkManager.sendPacket(new S01PacketEncryptionRequest(this.serverId, this.server.getKeyPair().getPublic(), this.field_147330_e));
        }
        else {
            this.currentLoginState = LoginState.READY_TO_ACCEPT;
        }
    }
    
    @Override
    public void processEncryptionResponse(final C01PacketEncryptionResponse packetIn) {
        Validate.validState(this.currentLoginState == LoginState.KEY, "Unexpected key packet", new Object[0]);
        final PrivateKey var2 = this.server.getKeyPair().getPrivate();
        if (!Arrays.equals(this.field_147330_e, packetIn.func_149299_b(var2))) {
            throw new IllegalStateException("Invalid nonce!");
        }
        this.secretKey = packetIn.func_149300_a(var2);
        this.currentLoginState = LoginState.AUTHENTICATING;
        this.networkManager.enableEncryption(this.secretKey);
        new Thread("User Authenticator #" + NetHandlerLoginServer.AUTHENTICATOR_THREAD_ID.incrementAndGet()) {
            private static final String __OBFID = "CL_00002268";
            
            @Override
            public void run() {
                final GameProfile var1 = NetHandlerLoginServer.this.loginGameProfile;
                try {
                    final String var2 = new BigInteger(CryptManager.getServerIdHash(NetHandlerLoginServer.this.serverId, NetHandlerLoginServer.this.server.getKeyPair().getPublic(), NetHandlerLoginServer.this.secretKey)).toString(16);
                    NetHandlerLoginServer.this.loginGameProfile = NetHandlerLoginServer.this.server.getMinecraftSessionService().hasJoinedServer(new GameProfile((UUID)null, var1.getName()), var2);
                    if (NetHandlerLoginServer.this.loginGameProfile != null) {
                        NetHandlerLoginServer.logger.info("UUID of player " + NetHandlerLoginServer.this.loginGameProfile.getName() + " is " + NetHandlerLoginServer.this.loginGameProfile.getId());
                        NetHandlerLoginServer.this.currentLoginState = LoginState.READY_TO_ACCEPT;
                    }
                    else if (NetHandlerLoginServer.this.server.isSinglePlayer()) {
                        NetHandlerLoginServer.logger.warn("Failed to verify username but will let them in anyway!");
                        NetHandlerLoginServer.this.loginGameProfile = NetHandlerLoginServer.this.getOfflineProfile(var1);
                        NetHandlerLoginServer.this.currentLoginState = LoginState.READY_TO_ACCEPT;
                    }
                    else {
                        NetHandlerLoginServer.this.closeConnection("Failed to verify username!");
                        NetHandlerLoginServer.logger.error("Username '" + NetHandlerLoginServer.this.loginGameProfile.getName() + "' tried to join with an invalid session");
                    }
                }
                catch (AuthenticationUnavailableException var3) {
                    if (NetHandlerLoginServer.this.server.isSinglePlayer()) {
                        NetHandlerLoginServer.logger.warn("Authentication servers are down but will let them in anyway!");
                        NetHandlerLoginServer.this.loginGameProfile = NetHandlerLoginServer.this.getOfflineProfile(var1);
                        NetHandlerLoginServer.this.currentLoginState = LoginState.READY_TO_ACCEPT;
                    }
                    else {
                        NetHandlerLoginServer.this.closeConnection("Authentication servers are down. Please try again later, sorry!");
                        NetHandlerLoginServer.logger.error("Couldn't verify username because servers are unavailable");
                    }
                }
            }
        }.start();
    }
    
    protected GameProfile getOfflineProfile(final GameProfile original) {
        final UUID var2 = UUID.nameUUIDFromBytes(("OfflinePlayer:" + original.getName()).getBytes(Charsets.UTF_8));
        return new GameProfile(var2, original.getName());
    }
    
    static {
        AUTHENTICATOR_THREAD_ID = new AtomicInteger(0);
        logger = LogManager.getLogger();
        RANDOM = new Random();
    }
    
    enum LoginState
    {
        HELLO("HELLO", 0), 
        KEY("KEY", 1), 
        AUTHENTICATING("AUTHENTICATING", 2), 
        READY_TO_ACCEPT("READY_TO_ACCEPT", 3), 
        ACCEPTED("ACCEPTED", 4);
        
        private static final LoginState[] $VALUES;
        private static final String __OBFID = "CL_00001463";
        
        private LoginState(final String p_i45297_1_, final int p_i45297_2_) {
        }
        
        static {
            $VALUES = new LoginState[] { LoginState.HELLO, LoginState.KEY, LoginState.AUTHENTICATING, LoginState.READY_TO_ACCEPT, LoginState.ACCEPTED };
        }
    }
}
