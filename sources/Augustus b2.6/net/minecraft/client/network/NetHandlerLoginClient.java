// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.network;

import org.apache.logging.log4j.LogManager;
import net.minecraft.network.login.server.S03PacketEnableCompression;
import net.minecraft.network.login.server.S00PacketDisconnect;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.network.INetHandler;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.login.server.S02PacketLoginSuccess;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import net.mcleaks.Session;
import java.security.PublicKey;
import com.mojang.authlib.exceptions.InvalidCredentialsException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentTranslation;
import com.mojang.authlib.exceptions.AuthenticationException;
import net.minecraft.network.Packet;
import javax.crypto.SecretKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.login.client.C01PacketEncryptionResponse;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.io.DataOutputStream;
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import net.mcleaks.MCLeaks;
import java.math.BigInteger;
import net.minecraft.util.CryptManager;
import net.minecraft.network.login.server.S01PacketEncryptionRequest;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.NetworkManager;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.Logger;
import net.minecraft.network.login.INetHandlerLoginClient;

public class NetHandlerLoginClient implements INetHandlerLoginClient
{
    private static final Logger logger;
    private final Minecraft mc;
    private final GuiScreen previousGuiScreen;
    private final NetworkManager networkManager;
    private GameProfile gameProfile;
    
    public NetHandlerLoginClient(final NetworkManager p_i45059_1_, final Minecraft mcIn, final GuiScreen p_i45059_3_) {
        this.networkManager = p_i45059_1_;
        this.mc = mcIn;
        this.previousGuiScreen = p_i45059_3_;
    }
    
    @Override
    public void handleEncryptionRequest(final S01PacketEncryptionRequest packetIn) {
        final SecretKey secretkey = CryptManager.createNewSharedKey();
        final String s = packetIn.getServerId();
        final PublicKey publickey = packetIn.getPublicKey();
        final String s2 = new BigInteger(CryptManager.getServerIdHash(s, publickey, secretkey)).toString(16);
        if (MCLeaks.isAltActive()) {
            final Session session = MCLeaks.getSession();
            final String server = ((InetSocketAddress)this.networkManager.getRemoteAddress()).getHostName() + ":" + ((InetSocketAddress)this.networkManager.getRemoteAddress()).getPort();
            try {
                final String jsonBody = "{\"session\":\"" + session.getToken() + "\",\"mcname\":\"" + session.getUsername() + "\",\"serverhash\":\"" + s2 + "\",\"server\":\"" + server + "\"}";
                final HttpURLConnection connection = (HttpURLConnection)new URL("https://auth.mcleaks.net/v1/joinserver").openConnection();
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
                connection.setDoOutput(true);
                final DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                outputStream.write(jsonBody.getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
                outputStream.close();
                final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                final StringBuilder outputBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    outputBuilder.append(line);
                }
                reader.close();
                final JsonElement jsonElement = new Gson().fromJson(outputBuilder.toString(), JsonElement.class);
                if (!jsonElement.isJsonObject() || !jsonElement.getAsJsonObject().has("success")) {
                    return;
                }
                if (!jsonElement.getAsJsonObject().get("success").getAsBoolean()) {
                    String errorMessage = "Received success=false from MCLeaks API";
                    if (jsonElement.getAsJsonObject().has("errorMessage")) {
                        errorMessage = jsonElement.getAsJsonObject().get("errorMessage").getAsString();
                    }
                    return;
                }
            }
            catch (Exception e) {
                return;
            }
            this.networkManager.sendPacket(new C01PacketEncryptionResponse(secretkey, publickey, packetIn.getVerifyToken()), new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(final Future<? super Void> p_operationComplete_1_) throws Exception {
                    NetHandlerLoginClient.this.networkManager.enableEncryption(secretkey);
                }
            }, (GenericFutureListener<? extends Future<? super Void>>[])new GenericFutureListener[0]);
        }
        if (this.mc.getCurrentServerData() != null && this.mc.getCurrentServerData().func_181041_d()) {
            try {
                this.getSessionService().joinServer(this.mc.getSession().getProfile(), this.mc.getSession().getToken(), s2);
            }
            catch (AuthenticationException var10) {
                NetHandlerLoginClient.logger.warn("Couldn't connect to auth servers but will continue to join LAN");
            }
        }
        else {
            try {
                this.getSessionService().joinServer(this.mc.getSession().getProfile(), this.mc.getSession().getToken(), s2);
            }
            catch (AuthenticationUnavailableException var11) {
                this.networkManager.closeChannel(new ChatComponentTranslation("disconnect.loginFailedInfo", new Object[] { new ChatComponentTranslation("disconnect.loginFailedInfo.serversUnavailable", new Object[0]) }));
                return;
            }
            catch (InvalidCredentialsException var12) {
                this.networkManager.closeChannel(new ChatComponentTranslation("disconnect.loginFailedInfo", new Object[] { new ChatComponentTranslation("disconnect.loginFailedInfo.invalidSession", new Object[0]) }));
                return;
            }
            catch (AuthenticationException authenticationexception) {
                this.networkManager.closeChannel(new ChatComponentTranslation("disconnect.loginFailedInfo", new Object[] { authenticationexception.getMessage() }));
                return;
            }
        }
        this.networkManager.sendPacket(new C01PacketEncryptionResponse(secretkey, publickey, packetIn.getVerifyToken()), new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(final Future<? super Void> p_operationComplete_1_) throws Exception {
                NetHandlerLoginClient.this.networkManager.enableEncryption(secretkey);
            }
        }, (GenericFutureListener<? extends Future<? super Void>>[])new GenericFutureListener[0]);
    }
    
    private MinecraftSessionService getSessionService() {
        return this.mc.getSessionService();
    }
    
    @Override
    public void handleLoginSuccess(final S02PacketLoginSuccess packetIn) {
        this.gameProfile = packetIn.getProfile();
        this.networkManager.setConnectionState(EnumConnectionState.PLAY);
        this.networkManager.setNetHandler(new NetHandlerPlayClient(this.mc, this.previousGuiScreen, this.networkManager, this.gameProfile));
    }
    
    @Override
    public void onDisconnect(final IChatComponent reason) {
        this.mc.displayGuiScreen(new GuiDisconnected(this.previousGuiScreen, "connect.failed", reason));
    }
    
    @Override
    public void handleDisconnect(final S00PacketDisconnect packetIn) {
        this.networkManager.closeChannel(packetIn.func_149603_c());
    }
    
    @Override
    public void handleEnableCompression(final S03PacketEnableCompression packetIn) {
        if (!this.networkManager.isLocalChannel()) {
            this.networkManager.setCompressionTreshold(packetIn.getCompressionTreshold());
        }
    }
    
    static {
        logger = LogManager.getLogger();
    }
}
