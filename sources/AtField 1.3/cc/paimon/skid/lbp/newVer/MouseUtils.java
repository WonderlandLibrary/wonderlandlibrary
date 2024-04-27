/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.JvmStatic
 */
package cc.paimon.skid.lbp.newVer;

import kotlin.jvm.JvmStatic;

public final class MouseUtils {
    public static final MouseUtils INSTANCE;

    @JvmStatic
    public static final boolean mouseWithinBounds(int n, int n2, float f, float f2, float f3, float f4) {
        return (float)n >= f && (float)n < f3 && (float)n2 >= f2 && (float)n2 < f4;
    }

    private MouseUtils() {
    }

    static {
        MouseUtils mouseUtils;
        INSTANCE = mouseUtils = new MouseUtils();
    }
}

