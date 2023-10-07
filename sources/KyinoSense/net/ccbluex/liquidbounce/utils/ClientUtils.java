/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 *  io.netty.util.concurrent.GenericFutureListener
 *  net.minecraft.client.settings.GameSettings
 *  net.minecraft.network.NetworkManager
 *  net.minecraft.network.Packet
 *  net.minecraft.network.login.client.C01PacketEncryptionResponse
 *  net.minecraft.network.login.server.S01PacketEncryptionRequest
 *  net.minecraft.util.IChatComponent$Serializer
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.ccbluex.liquidbounce.utils;

import com.google.gson.JsonObject;
import com.sun.management.OperatingSystemMXBean;
import io.netty.util.concurrent.GenericFutureListener;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.security.PublicKey;
import javax.crypto.SecretKey;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.login.client.C01PacketEncryptionResponse;
import net.minecraft.network.login.server.S01PacketEncryptionRequest;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SideOnly(value=Side.CLIENT)
public final class ClientUtils
extends MinecraftInstance {
    static OperatingSystemMXBean bean = (OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
    private static final Logger logger = LogManager.getLogger((String)"LiquidBounce");
    private static Field fastRenderField;

    public static double getCPUUse() {
        return bean.getSystemCpuLoad();
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void disableFastRender() {
        try {
            if (fastRenderField != null) {
                if (!fastRenderField.isAccessible()) {
                    fastRenderField.setAccessible(true);
                }
                fastRenderField.setBoolean(ClientUtils.mc.field_71474_y, false);
            }
        }
        catch (IllegalAccessException illegalAccessException) {
            // empty catch block
        }
    }

    public static void sendEncryption(NetworkManager networkManager, SecretKey secretKey, PublicKey publicKey, S01PacketEncryptionRequest encryptionRequest) {
        networkManager.func_179288_a((Packet)new C01PacketEncryptionResponse(secretKey, publicKey, encryptionRequest.func_149607_e()), p_operationComplete_1_ -> networkManager.func_150727_a(secretKey), new GenericFutureListener[0]);
    }

    public static void displayChatMessage(String message) {
        if (ClientUtils.mc.field_71439_g == null) {
            ClientUtils.getLogger().info("(MCChat)" + message);
            return;
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("text", message);
        ClientUtils.mc.field_71439_g.func_145747_a(IChatComponent.Serializer.func_150699_a((String)jsonObject.toString()));
    }

    static {
        try {
            fastRenderField = GameSettings.class.getDeclaredField("ofFastRender");
            if (!fastRenderField.isAccessible()) {
                fastRenderField.setAccessible(true);
            }
        }
        catch (NoSuchFieldException noSuchFieldException) {
            // empty catch block
        }
    }
}

