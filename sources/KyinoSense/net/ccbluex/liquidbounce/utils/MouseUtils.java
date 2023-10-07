/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils;

import kotlin.Metadata;
import kotlin.jvm.JvmStatic;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0004\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J8\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\t2\u0006\u0010\f\u001a\u00020\tH\u0007\u00a8\u0006\r"}, d2={"Lnet/ccbluex/liquidbounce/utils/MouseUtils;", "", "()V", "mouseWithinBounds", "", "mouseX", "", "mouseY", "x", "", "y", "x2", "y2", "KyinoClient"})
public final class MouseUtils {
    public static final MouseUtils INSTANCE;

    @JvmStatic
    public static final boolean mouseWithinBounds(int mouseX, int mouseY, float x, float y, float x2, float y2) {
        return (float)mouseX >= x && (float)mouseX < x2 && (float)mouseY >= y && (float)mouseY < y2;
    }

    private MouseUtils() {
    }

    static {
        MouseUtils mouseUtils;
        INSTANCE = mouseUtils = new MouseUtils();
    }
}

