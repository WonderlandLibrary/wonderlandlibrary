/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.JsonElement
 *  net.minecraft.client.network.NetHandlerLoginClient
 *  net.minecraft.network.NetworkManager
 *  net.minecraft.network.login.server.S01PacketEncryptionRequest
 *  net.minecraft.util.ChatComponentText
 *  net.minecraft.util.CryptManager
 *  net.minecraft.util.IChatComponent
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.network;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import javax.crypto.SecretKey;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.mcleaks.MCLeaks;
import net.mcleaks.Session;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.login.server.S01PacketEncryptionRequest;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.CryptManager;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SideOnly(value=Side.CLIENT)
@Mixin(value={NetHandlerLoginClient.class})
public class MixinNetHandlerLoginClient {
    @Shadow
    @Final
    private NetworkManager field_147393_d;

    @Inject(method={"handleEncryptionRequest"}, at={@At(value="HEAD")}, cancellable=true)
    private void handleEncryptionRequest(S01PacketEncryptionRequest packetIn, CallbackInfo callbackInfo) {
        if (MCLeaks.isAltActive()) {
            SecretKey secretkey = CryptManager.func_75890_a();
            String s = packetIn.func_149609_c();
            PublicKey publickey = packetIn.func_149608_d();
            String s1 = new BigInteger(CryptManager.func_75895_a((String)s, (PublicKey)publickey, (SecretKey)secretkey)).toString(16);
            Session session = MCLeaks.getSession();
            String server = ((InetSocketAddress)this.field_147393_d.func_74430_c()).getHostName() + ":" + ((InetSocketAddress)this.field_147393_d.func_74430_c()).getPort();
            try {
                String line;
                String jsonBody = "{\"session\":\"" + session.getToken() + "\",\"mcname\":\"" + session.getUsername() + "\",\"serverhash\":\"" + s1 + "\",\"server\":\"" + server + "\"}";
                HttpURLConnection connection = (HttpURLConnection)new URL("https://auth.mcleaks.net/v1/joinserver").openConnection();
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
                connection.setDoOutput(true);
                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                outputStream.write(jsonBody.getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
                outputStream.close();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder outputBuilder = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    outputBuilder.append(line);
                }
                reader.close();
                JsonElement jsonElement = (JsonElement)new Gson().fromJson(outputBuilder.toString(), JsonElement.class);
                if (!jsonElement.isJsonObject() || !jsonElement.getAsJsonObject().has("success")) {
                    this.field_147393_d.func_150718_a((IChatComponent)new ChatComponentText("Invalid response from MCLeaks API"));
                    callbackInfo.cancel();
                    return;
                }
                if (!jsonElement.getAsJsonObject().get("success").getAsBoolean()) {
                    String errorMessage = "Received success=false from MCLeaks API";
                    if (jsonElement.getAsJsonObject().has("errorMessage")) {
                        errorMessage = jsonElement.getAsJsonObject().get("errorMessage").getAsString();
                    }
                    this.field_147393_d.func_150718_a((IChatComponent)new ChatComponentText(errorMessage));
                    callbackInfo.cancel();
                    return;
                }
            }
            catch (Exception e) {
                this.field_147393_d.func_150718_a((IChatComponent)new ChatComponentText("Error whilst contacting MCLeaks API: " + e.toString()));
                callbackInfo.cancel();
                return;
            }
            ClientUtils.sendEncryption(this.field_147393_d, secretkey, publickey, packetIn);
            callbackInfo.cancel();
        }
    }
}

