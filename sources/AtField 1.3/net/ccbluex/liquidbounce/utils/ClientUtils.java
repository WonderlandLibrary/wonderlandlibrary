/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 *  kotlin.Unit
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.ccbluex.liquidbounce.utils;

import com.google.gson.JsonObject;
import java.security.PublicKey;
import javax.crypto.SecretKey;
import kotlin.Unit;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.INetworkManager;
import net.ccbluex.liquidbounce.api.minecraft.network.login.server.ISPacketEncryptionRequest;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SideOnly(value=Side.CLIENT)
public final class ClientUtils
extends MinecraftInstance {
    private static final Logger logger = LogManager.getLogger((String)"LiquidBounce");

    public static void disableFastRender() {
        LiquidBounce.wrapper.getFunctions().disableFastRender();
    }

    public static void displayChatMessage(String string) {
        if (mc.getThePlayer() == null) {
            ClientUtils.getLogger().info("(MCChat)" + string);
            return;
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("text", string);
        mc.getThePlayer().addChatMessage(LiquidBounce.wrapper.getFunctions().jsonToComponent(jsonObject.toString()));
    }

    public static Logger getLogger() {
        return logger;
    }

    private static Unit lambda$sendEncryption$0(INetworkManager iNetworkManager, SecretKey secretKey) {
        iNetworkManager.enableEncryption(secretKey);
        return null;
    }

    public static WVec3 getVectorForRotation(float f, float f2) {
        float f3 = (float)Math.cos(-f2 * ((float)Math.PI / 180) - (float)Math.PI);
        float f4 = (float)Math.sin(-f2 * ((float)Math.PI / 180) - (float)Math.PI);
        float f5 = (float)Math.cos(-f * ((float)Math.PI / 180));
        float f6 = (float)Math.sin(-f * ((float)Math.PI / 180));
        return new WVec3(f4 * f5, f6, f3 * f5);
    }

    public static void sendEncryption(INetworkManager iNetworkManager, SecretKey secretKey, PublicKey publicKey, ISPacketEncryptionRequest iSPacketEncryptionRequest) {
        iNetworkManager.sendPacket(classProvider.createCPacketEncryptionResponse(secretKey, publicKey, iSPacketEncryptionRequest.getVerifyToken()), () -> ClientUtils.lambda$sendEncryption$0(iNetworkManager, secretKey));
    }
}

