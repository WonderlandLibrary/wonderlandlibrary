/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 *  kotlin.Unit
 *  kotlin.jvm.functions.Function0
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.ccbluex.liquidbounce.utils;

import com.google.gson.JsonObject;
import java.awt.Color;
import java.security.PublicKey;
import javax.crypto.SecretKey;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
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

    public static Logger getLogger() {
        return logger;
    }

    public static void disableFastRender() {
        LiquidBounce.wrapper.getFunctions().disableFastRender();
    }

    public static int reAlpha(int color, float alpha) {
        Color c = new Color(color);
        float r = 0.003921569f * (float)c.getRed();
        float g = 0.003921569f * (float)c.getGreen();
        float b = 0.003921569f * (float)c.getBlue();
        return new Color(r, g, b, alpha).getRGB();
    }

    public static void sendEncryption(INetworkManager networkManager, SecretKey secretKey, PublicKey publicKey, ISPacketEncryptionRequest encryptionRequest) {
        networkManager.sendPacket(classProvider.createCPacketEncryptionResponse(secretKey, publicKey, encryptionRequest.getVerifyToken()), (Function0<Unit>)((Function0)() -> {
            networkManager.enableEncryption(secretKey);
            return null;
        }));
    }

    public static WVec3 getVectorForRotation(float p_getVectorForRotation_1_, float p_getVectorForRotation_2_) {
        float f = (float)Math.cos(-p_getVectorForRotation_2_ * ((float)Math.PI / 180) - (float)Math.PI);
        float f1 = (float)Math.sin(-p_getVectorForRotation_2_ * ((float)Math.PI / 180) - (float)Math.PI);
        float f2 = (float)Math.cos(-p_getVectorForRotation_1_ * ((float)Math.PI / 180));
        float f3 = (float)Math.sin(-p_getVectorForRotation_1_ * ((float)Math.PI / 180));
        return new WVec3(f1 * f2, f3, f * f2);
    }

    public static void displayChatMessage(String message) {
        if (mc.getThePlayer() == null) {
            ClientUtils.getLogger().info("(MCChat)" + message);
            return;
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("text", message);
        mc.getThePlayer().addChatMessage(LiquidBounce.wrapper.getFunctions().jsonToComponent(jsonObject.toString()));
    }
}

