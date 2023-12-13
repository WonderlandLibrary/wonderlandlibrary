/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.viamcp.viafabric.platform;

import java.lang.reflect.Method;
import java.util.Arrays;
import us.myles.ViaVersion.api.platform.ViaInjector;
import us.myles.ViaVersion.util.GsonUtil;
import us.myles.viaversion.libs.gson.JsonObject;

public class VRInjector
implements ViaInjector {
    @Override
    public void inject() {
    }

    @Override
    public void uninject() {
    }

    @Override
    public int getServerProtocolVersion() throws NoSuchFieldException, IllegalAccessException {
        return this.getClientProtocol();
    }

    private int getClientProtocol() throws NoSuchFieldException, IllegalAccessException {
        return 47;
    }

    @Override
    public String getEncoderName() {
        return "via-encoder";
    }

    @Override
    public String getDecoderName() {
        return "via-decoder";
    }

    @Override
    public JsonObject getDump() {
        JsonObject obj = new JsonObject();
        try {
            obj.add("serverNetworkIOChInit", GsonUtil.getGson().toJsonTree(Arrays.stream(Class.forName("net.minecraft.class_3242$1").getDeclaredMethods()).map(Method::toString).toArray(String[]::new)));
        }
        catch (ClassNotFoundException classNotFoundException) {
            // empty catch block
        }
        try {
            obj.add("clientConnectionChInit", GsonUtil.getGson().toJsonTree(Arrays.stream(Class.forName("net.minecraft.class_2535$1").getDeclaredMethods()).map(Method::toString).toArray(String[]::new)));
        }
        catch (ClassNotFoundException classNotFoundException) {
            // empty catch block
        }
        return obj;
    }
}

