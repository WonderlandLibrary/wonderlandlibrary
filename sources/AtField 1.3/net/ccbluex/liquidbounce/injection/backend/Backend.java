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
    public static final String MINECRAFT_VERSION;
    public static final int MINECRAFT_VERSION_MINOR;
    public static final int MINECRAFT_VERSION_PATCH;
    public static final Backend INSTANCE;
    public static final int MINECRAFT_VERSION_MAJOR;
    private static final MinecraftVersion REPRESENTED_BACKEND_VERSION;

    public final MinecraftVersion getREPRESENTED_BACKEND_VERSION() {
        return REPRESENTED_BACKEND_VERSION;
    }

    private Backend() {
    }

    public final Void BACKEND_UNSUPPORTED() {
        boolean bl = false;
        throw (Throwable)new NotImplementedError("1.12.2 doesn't support this feature'");
    }

    static {
        Backend backend;
        MINECRAFT_VERSION_MAJOR = 1;
        MINECRAFT_VERSION_PATCH = 2;
        MINECRAFT_VERSION = "1.12.2";
        MINECRAFT_VERSION_MINOR = 12;
        INSTANCE = backend = new Backend();
        REPRESENTED_BACKEND_VERSION = MinecraftVersion.MC_1_12;
    }
}

