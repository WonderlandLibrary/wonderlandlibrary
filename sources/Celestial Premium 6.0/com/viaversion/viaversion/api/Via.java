/*
 * Decompiled with CFR 0.150.
 */
package com.viaversion.viaversion.api;

import com.google.common.base.Preconditions;
import com.viaversion.viaversion.api.ViaAPI;
import com.viaversion.viaversion.api.ViaManager;
import com.viaversion.viaversion.api.configuration.ViaVersionConfig;
import com.viaversion.viaversion.api.platform.ViaPlatform;

public final class Via {
    private static ViaManager manager;

    public static ViaAPI getAPI() {
        return Via.manager().getPlatform().getApi();
    }

    public static ViaManager getManager() {
        return Via.manager();
    }

    public static ViaVersionConfig getConfig() {
        return Via.manager().getPlatform().getConf();
    }

    public static ViaPlatform getPlatform() {
        return Via.manager().getPlatform();
    }

    public static void init(ViaManager viaManager) {
        Preconditions.checkArgument(manager == null, "ViaManager is already set");
        manager = viaManager;
    }

    private static ViaManager manager() {
        Preconditions.checkArgument(manager != null, "ViaVersion has not loaded the platform yet");
        return manager;
    }
}

