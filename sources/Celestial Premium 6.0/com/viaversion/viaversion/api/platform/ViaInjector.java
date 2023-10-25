/*
 * Decompiled with CFR 0.150.
 */
package com.viaversion.viaversion.api.platform;

import com.viaversion.viaversion.libs.fastutil.ints.IntSortedSet;
import com.viaversion.viaversion.libs.fastutil.ints.IntSortedSets;
import com.viaversion.viaversion.libs.gson.JsonObject;

public interface ViaInjector {
    public void inject() throws Exception;

    public void uninject() throws Exception;

    default public boolean lateProtocolVersionSetting() {
        return false;
    }

    public int getServerProtocolVersion() throws Exception;

    default public IntSortedSet getServerProtocolVersions() throws Exception {
        return IntSortedSets.singleton(this.getServerProtocolVersion());
    }

    public String getEncoderName();

    public String getDecoderName();

    public JsonObject getDump();
}

