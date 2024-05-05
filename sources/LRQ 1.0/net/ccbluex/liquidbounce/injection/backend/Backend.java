/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.NotImplementedError
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.NotImplementedError;
import net.ccbluex.liquidbounce.api.MinecraftVersion;

public final class Backend {
    public static final String MINECRAFT_VERSION = "1.12.2";
    public static final int MINECRAFT_VERSION_MAJOR = 1;
    public static final int MINECRAFT_VERSION_MINOR = 12;
    public static final int MINECRAFT_VERSION_PATCH = 2;
    private static final MinecraftVersion REPRESENTED_BACKEND_VERSION;
    public static final Backend INSTANCE;

    public final MinecraftVersion getREPRESENTED_BACKEND_VERSION() {
        return REPRESENTED_BACKEND_VERSION;
    }

    public final Void BACKEND_UNSUPPORTED() {
        int $i$f$BACKEND_UNSUPPORTED = 0;
        throw (Throwable)new NotImplementedError("1.12.2 doesn't support this feature'");
    }

    private Backend() {
    }

    static {
        Backend backend;
        INSTANCE = backend = new Backend();
        REPRESENTED_BACKEND_VERSION = MinecraftVersion.MC_1_12;
    }
}

