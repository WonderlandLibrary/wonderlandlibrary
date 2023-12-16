package moonsense.utils;

import net.minecraft.util.*;

public class MathUtil
{
    public static final double[] a;
    private static final double[] b;

    public static double getAngle(int nameInt) {
        nameInt %= 360;
        return MathUtil.b[nameInt];
    }

    public static double getRightAngle(int nameInt) {
        nameInt += 90;
        nameInt %= 360;
        return MathUtil.b[nameInt];
    }

    private static float snapToStep(float value, final float valueStep) {
        if (valueStep > 0.0f) {
            value = valueStep * Math.round(value / valueStep);
        }
        return value;
    }

    private static float snapToStepClamp(float value, final float valueMin, final float valueMax, final float valueStep) {
        value = snapToStep(value, valueStep);
        return MathHelper.clamp_float(value, valueMin, valueMax);
    }

    static {
        a = new double[65536];
        b = new double[360];
        for (int i = 0; i < 65536; ++i) {
            MathUtil.a[i] = Math.sin(i * 3.141592653589793 * 2.0 / 65536.0);
        }
        for (int i = 0; i < 360; ++i) {
            MathUtil.b[i] = Math.sin(Math.toRadians(i));
        }
    }
}