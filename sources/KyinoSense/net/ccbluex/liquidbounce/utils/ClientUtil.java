/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 *  net.minecraft.util.IChatComponent$Serializer
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.utils;

import com.google.gson.JsonObject;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.util.IChatComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\u0003\n\u0002\b\u0004\b\u00c6\u0002\u0018\u00002\u00020\u0001:\u0001\u001cB\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013J\u000e\u0010\u0014\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013J\u000e\u0010\u0015\u001a\u00020\u00112\u0006\u0010\u0016\u001a\u00020\u0013J\u000e\u0010\u0017\u001a\u00020\u00112\u0006\u0010\u0016\u001a\u00020\u0013J\u0016\u0010\u0017\u001a\u00020\u00112\u0006\u0010\u0016\u001a\u00020\u00132\u0006\u0010\u0018\u001a\u00020\u0019J\u000e\u0010\u001a\u001a\u00020\u00112\u0006\u0010\u0016\u001a\u00020\u0013J\u000e\u0010\u001b\u001a\u00020\u00112\u0006\u0010\u0016\u001a\u00020\u0013R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u0016\u0010\t\u001a\n \u000b*\u0004\u0018\u00010\n0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\f\u001a\u00020\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f\u00a8\u0006\u001d"}, d2={"Lnet/ccbluex/liquidbounce/utils/ClientUtil;", "Lnet/ccbluex/liquidbounce/utils/MinecraftInstance;", "()V", "inDevMode", "", "getInDevMode", "()Z", "setInDevMode", "(Z)V", "logger", "Lorg/apache/logging/log4j/Logger;", "kotlin.jvm.PlatformType", "osType", "Lnet/ccbluex/liquidbounce/utils/ClientUtil$EnumOSType;", "getOsType", "()Lnet/ccbluex/liquidbounce/utils/ClientUtil$EnumOSType;", "displayAlert", "", "message", "", "displayChatMessage", "logDebug", "msg", "logError", "t", "", "logInfo", "logWarn", "EnumOSType", "KyinoClient"})
public final class ClientUtil
extends MinecraftInstance {
    private static final Logger logger;
    @NotNull
    private static final EnumOSType osType;
    private static boolean inDevMode;
    public static final ClientUtil INSTANCE;

    @NotNull
    public final EnumOSType getOsType() {
        return osType;
    }

    public final boolean getInDevMode() {
        return inDevMode;
    }

    public final void setInDevMode(boolean bl) {
        inDevMode = bl;
    }

    public final void logInfo(@NotNull String msg) {
        Intrinsics.checkParameterIsNotNull(msg, "msg");
        logger.info(msg);
    }

    public final void logWarn(@NotNull String msg) {
        Intrinsics.checkParameterIsNotNull(msg, "msg");
        logger.warn(msg);
    }

    public final void logError(@NotNull String msg) {
        Intrinsics.checkParameterIsNotNull(msg, "msg");
        logger.error(msg);
    }

    public final void logError(@NotNull String msg, @NotNull Throwable t) {
        Intrinsics.checkParameterIsNotNull(msg, "msg");
        Intrinsics.checkParameterIsNotNull(t, "t");
        logger.error(msg, t);
    }

    public final void logDebug(@NotNull String msg) {
        Intrinsics.checkParameterIsNotNull(msg, "msg");
        logger.debug(msg);
    }

    public final void displayAlert(@NotNull String message) {
        Intrinsics.checkParameterIsNotNull(message, "message");
    }

    public final void displayChatMessage(@NotNull String message) {
        Intrinsics.checkParameterIsNotNull(message, "message");
        if (MinecraftInstance.mc.field_71439_g == null) {
            logger.info("(MCChat)" + message);
            return;
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("text", message);
        MinecraftInstance.mc.field_71439_g.func_145747_a(IChatComponent.Serializer.func_150699_a((String)jsonObject.toString()));
    }

    private ClientUtil() {
    }

    static {
        ClientUtil clientUtil;
        INSTANCE = clientUtil = new ClientUtil();
        logger = LogManager.getLogger((String)"LiquidBounce");
        inDevMode = System.getProperty("dev-mode") != null;
        String string = System.getProperty("os.name");
        Intrinsics.checkExpressionValueIsNotNull(string, "System.getProperty(\"os.name\")");
        String string2 = string;
        boolean bl = false;
        String string3 = string2;
        if (string3 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string4 = string3.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(string4, "(this as java.lang.String).toLowerCase()");
        String os = string4;
        osType = StringsKt.contains$default((CharSequence)os, "win", false, 2, null) ? EnumOSType.WINDOWS : (StringsKt.contains$default((CharSequence)os, "mac", false, 2, null) ? EnumOSType.MACOS : (StringsKt.contains$default((CharSequence)os, "nix", false, 2, null) || StringsKt.contains$default((CharSequence)os, "nux", false, 2, null) || StringsKt.contains$default((CharSequence)os, "aix", false, 2, null) ? EnumOSType.LINUX : EnumOSType.UNKNOWN));
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\u000e\n\u0002\b\b\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\n\u00a8\u0006\u000b"}, d2={"Lnet/ccbluex/liquidbounce/utils/ClientUtil$EnumOSType;", "", "friendlyName", "", "(Ljava/lang/String;ILjava/lang/String;)V", "getFriendlyName", "()Ljava/lang/String;", "WINDOWS", "LINUX", "MACOS", "UNKNOWN", "KyinoClient"})
    public static final class EnumOSType
    extends Enum<EnumOSType> {
        public static final /* enum */ EnumOSType WINDOWS;
        public static final /* enum */ EnumOSType LINUX;
        public static final /* enum */ EnumOSType MACOS;
        public static final /* enum */ EnumOSType UNKNOWN;
        private static final /* synthetic */ EnumOSType[] $VALUES;
        @NotNull
        private final String friendlyName;

        static {
            EnumOSType[] enumOSTypeArray = new EnumOSType[4];
            EnumOSType[] enumOSTypeArray2 = enumOSTypeArray;
            enumOSTypeArray[0] = WINDOWS = new EnumOSType("win");
            enumOSTypeArray[1] = LINUX = new EnumOSType("linux");
            enumOSTypeArray[2] = MACOS = new EnumOSType("mac");
            enumOSTypeArray[3] = UNKNOWN = new EnumOSType("unk");
            $VALUES = enumOSTypeArray;
        }

        @NotNull
        public final String getFriendlyName() {
            return this.friendlyName;
        }

        private EnumOSType(String friendlyName) {
            this.friendlyName = friendlyName;
        }

        public static EnumOSType[] values() {
            return (EnumOSType[])$VALUES.clone();
        }

        public static EnumOSType valueOf(String string) {
            return Enum.valueOf(EnumOSType.class, string);
        }
    }
}

