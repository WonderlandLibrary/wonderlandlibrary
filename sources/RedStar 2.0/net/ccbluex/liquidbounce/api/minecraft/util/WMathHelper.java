package net.ccbluex.liquidbounce.api.minecraft.util;

import kotlin.Metadata;
import kotlin.jvm.JvmStatic;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\"\n\n\u0000\n\b\n\n\b\n\n\u0000\n\b\n\b\bÆ\u000020B\b¢J!0202020H\bJ!\b0\t20\t20\t20\tH\bJ\n02\f0HJ\r0\t20\tH¨"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/util/WMathHelper;", "", "()V", "clamp_double", "", "num", "min", "max", "clamp_float", "", "floor_double", "", "value", "wrapAngleTo180_float", "angle", "Pride"})
public final class WMathHelper {
    public static final WMathHelper INSTANCE;

    @JvmStatic
    public static final float wrapAngleTo180_float(float angle) {
        float value = angle % 360.0f;
        if (value >= 180.0f) {
            value -= 360.0f;
        }
        if (value < -180.0f) {
            value += 360.0f;
        }
        return value;
    }

    @JvmStatic
    public static final float clamp_float(float num, float min, float max) {
        int $i$f$clamp_float = 0;
        return num < min ? min : (num > max ? max : num);
    }

    @JvmStatic
    public static final double clamp_double(double num, double min, double max) {
        int $i$f$clamp_double = 0;
        return num < min ? min : (num > max ? max : num);
    }

    @JvmStatic
    public static final int floor_double(double value) {
        int i = (int)value;
        return value < (double)i ? i - 1 : i;
    }

    private WMathHelper() {
    }

    static {
        WMathHelper wMathHelper;
        INSTANCE = wMathHelper = new WMathHelper();
    }
}
