/*
 * Decompiled with CFR 0.152.
 */
package de.enzaxd.viaforge.platform;

import com.viaversion.viaversion.api.platform.ViaInjector;
import com.viaversion.viaversion.libs.gson.JsonObject;

public class Injector
implements ViaInjector {
    @Override
    public void inject() {
    }

    @Override
    public void uninject() {
    }

    @Override
    public int getServerProtocolVersion() {
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
        return obj;
    }
}

