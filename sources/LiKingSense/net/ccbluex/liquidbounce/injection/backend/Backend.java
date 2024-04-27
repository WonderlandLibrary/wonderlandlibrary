/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.NotImplementedError
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.Metadata;
import kotlin.NotImplementedError;
import net.ccbluex.liquidbounce.api.MinecraftVersion;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0001\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\t\u0010\r\u001a\u00020\u000eH\u0086\bR\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\f\u00a8\u0006\u000f"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/Backend;", "", "()V", "MINECRAFT_VERSION", "", "MINECRAFT_VERSION_MAJOR", "", "MINECRAFT_VERSION_MINOR", "MINECRAFT_VERSION_PATCH", "REPRESENTED_BACKEND_VERSION", "Lnet/ccbluex/liquidbounce/api/MinecraftVersion;", "getREPRESENTED_BACKEND_VERSION", "()Lnet/ccbluex/liquidbounce/api/MinecraftVersion;", "BACKEND_UNSUPPORTED", "", "LiKingSense"})
public final class Backend {
    @NotNull
    public static final String MINECRAFT_VERSION = "1.12.2";
    public static final int MINECRAFT_VERSION_MAJOR = 1;
    public static final int MINECRAFT_VERSION_MINOR = 12;
    public static final int MINECRAFT_VERSION_PATCH = 2;
    @NotNull
    private static final MinecraftVersion REPRESENTED_BACKEND_VERSION;
    public static final Backend INSTANCE;

    @NotNull
    public final MinecraftVersion getREPRESENTED_BACKEND_VERSION() {
        return REPRESENTED_BACKEND_VERSION;
    }

    @NotNull
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

