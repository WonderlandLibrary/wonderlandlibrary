/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.JvmStatic
 */
package net.ccbluex.liquidbounce.utils;

import kotlin.Metadata;
import kotlin.jvm.JvmStatic;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0004\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J8\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000bH\u0007J8\u0010\u0003\u001a\u00020\u00042\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000b2\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\r\u001a\u00020\u00062\u0006\u0010\u000e\u001a\u00020\u0006H\u0007\u00a8\u0006\u000f"}, d2={"Lnet/ccbluex/liquidbounce/utils/MouseUtils;", "", "()V", "mouseWithinBounds", "", "x", "", "y", "width", "height", "mouseX", "", "mouseY", "x2", "y2", "LiKingSense"})
public final class MouseUtils {
    public static final MouseUtils INSTANCE;

    @JvmStatic
    public static final boolean mouseWithinBounds(int mouseX, int mouseY, float x, float y, float x2, float y2) {
        return (float)mouseX >= x && (float)mouseX < x2 && (float)mouseY >= y && (float)mouseY < y2;
    }

    @JvmStatic
    public static final boolean mouseWithinBounds(float x, float y, float width, float height, int mouseX, int mouseY) {
        return (float)mouseX >= x && (float)mouseX <= x + width && (float)mouseY >= y && (float)mouseY <= y + height;
    }

    private MouseUtils() {
    }

    static {
        MouseUtils mouseUtils;
        INSTANCE = mouseUtils = new MouseUtils();
    }
}

