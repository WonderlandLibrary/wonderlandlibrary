/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.utils.render;

import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b \b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H\u0007J\u0010\u0010\u0006\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H\u0007J\u0010\u0010\u0007\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H\u0007J\u0010\u0010\b\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H\u0007J\u0010\u0010\t\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H\u0007J\u0010\u0010\n\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H\u0007J\u0010\u0010\u000b\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H\u0007J\u0010\u0010\f\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H\u0007J\u0010\u0010\r\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H\u0007J\u0010\u0010\u000e\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H\u0007J\u0010\u0010\u000f\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H\u0007J\u0010\u0010\u0010\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H\u0007J\u0010\u0010\u0011\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H\u0007J\u0010\u0010\u0012\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H\u0007J\u0010\u0010\u0013\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H\u0007J\u0010\u0010\u0014\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H\u0007J\u0010\u0010\u0015\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H\u0007J\u0010\u0010\u0016\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H\u0007J\u0010\u0010\u0017\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H\u0007J\u0010\u0010\u0018\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H\u0007J\u0010\u0010\u0019\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H\u0007J\u0010\u0010\u001a\u001a\u00020\u00042\u0006\u0010\u001b\u001a\u00020\u0004H\u0007J\u0010\u0010\u001c\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H\u0007J\u0010\u0010\u001d\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H\u0007J\u0010\u0010\u001e\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H\u0007J\u0010\u0010\u001f\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H\u0007J\u0010\u0010 \u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H\u0007J\u0010\u0010!\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H\u0007J\u0010\u0010\"\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H\u0007J\u0010\u0010#\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H\u0007\u00a8\u0006$"}, d2={"Lnet/dev/important/utils/render/EaseUtils;", "", "()V", "easeInBack", "", "x", "easeInBounce", "easeInCirc", "easeInCubic", "easeInElastic", "easeInExpo", "easeInOutBack", "easeInOutBounce", "easeInOutCirc", "easeInOutCubic", "easeInOutElastic", "easeInOutExpo", "easeInOutQuad", "easeInOutQuart", "easeInOutQuint", "easeInOutSine", "easeInQuad", "easeInQuart", "easeInQuint", "easeInSine", "easeOutBack", "easeOutBounce", "animeX", "easeOutCirc", "easeOutCubic", "easeOutElastic", "easeOutExpo", "easeOutQuad", "easeOutQuart", "easeOutQuint", "easeOutSine", "LiquidBounce"})
public final class EaseUtils {
    @NotNull
    public static final EaseUtils INSTANCE = new EaseUtils();

    private EaseUtils() {
    }

    @JvmStatic
    public static final double easeInSine(double x) {
        return 1.0 - Math.cos(x * Math.PI / (double)2);
    }

    @JvmStatic
    public static final double easeOutSine(double x) {
        return Math.sin(x * Math.PI / (double)2);
    }

    @JvmStatic
    public static final double easeInOutSine(double x) {
        return -(Math.cos(Math.PI * x) - 1.0) / (double)2;
    }

    @JvmStatic
    public static final double easeInQuad(double x) {
        return x * x;
    }

    @JvmStatic
    public static final double easeOutQuad(double x) {
        return 1.0 - (1.0 - x) * (1.0 - x);
    }

    @JvmStatic
    public static final double easeInOutQuad(double x) {
        return x < 0.5 ? (double)2 * x * x : 1.0 - Math.pow((double)-2 * x + (double)2, 2) / (double)2;
    }

    @JvmStatic
    public static final double easeInCubic(double x) {
        return x * x * x;
    }

    @JvmStatic
    public static final double easeOutCubic(double x) {
        return 1.0 - Math.pow(1.0 - x, 3);
    }

    @JvmStatic
    public static final double easeInOutCubic(double x) {
        return x < 0.5 ? (double)4 * x * x * x : 1.0 - Math.pow((double)-2 * x + (double)2, 3) / (double)2;
    }

    @JvmStatic
    public static final double easeInQuart(double x) {
        return x * x * x * x;
    }

    @JvmStatic
    public static final double easeOutQuart(double x) {
        return 1.0 - Math.pow(1.0 - x, 4);
    }

    @JvmStatic
    public static final double easeInOutQuart(double x) {
        return x < 0.5 ? (double)8 * x * x * x * x : 1.0 - Math.pow((double)-2 * x + (double)2, 4) / (double)2;
    }

    @JvmStatic
    public static final double easeInQuint(double x) {
        return x * x * x * x * x;
    }

    @JvmStatic
    public static final double easeOutQuint(double x) {
        return 1.0 - Math.pow(1.0 - x, 5);
    }

    @JvmStatic
    public static final double easeInOutQuint(double x) {
        return x < 0.5 ? (double)16 * x * x * x * x * x : 1.0 - Math.pow((double)-2 * x + (double)2, 5) / (double)2;
    }

    @JvmStatic
    public static final double easeInExpo(double x) {
        return x == 0.0 ? 0.0 : Math.pow(2.0, (double)10 * x - (double)10);
    }

    @JvmStatic
    public static final double easeOutExpo(double x) {
        return x == 1.0 ? 1.0 : 1.0 - Math.pow(2.0, (double)-10 * x);
    }

    @JvmStatic
    public static final double easeInOutExpo(double x) {
        return x == 0.0 ? 0.0 : (x == 1.0 ? 1.0 : (x < 0.5 ? Math.pow(2.0, (double)20 * x - (double)10) / (double)2 : ((double)2 - Math.pow(2.0, (double)-20 * x + (double)10)) / (double)2));
    }

    @JvmStatic
    public static final double easeInCirc(double x) {
        return 1.0 - Math.sqrt(1.0 - Math.pow(x, 2));
    }

    @JvmStatic
    public static final double easeOutCirc(double x) {
        return Math.sqrt(1.0 - Math.pow(x - 1.0, 2));
    }

    @JvmStatic
    public static final double easeInOutCirc(double x) {
        return x < 0.5 ? (1.0 - Math.sqrt(1.0 - Math.pow((double)2 * x, 2))) / (double)2 : (Math.sqrt(1.0 - Math.pow((double)-2 * x + (double)2, 2)) + 1.0) / (double)2;
    }

    @JvmStatic
    public static final double easeInBack(double x) {
        double c1 = 1.70158;
        double c3 = c1 + 1.0;
        return c3 * x * x * x - c1 * x * x;
    }

    @JvmStatic
    public static final double easeOutBack(double x) {
        double c1 = 1.70158;
        double c3 = c1 + 1.0;
        return 1.0 + c3 * Math.pow(x - 1.0, 3) + c1 * Math.pow(x - 1.0, 2);
    }

    @JvmStatic
    public static final double easeInOutBack(double x) {
        double c1 = 1.70158;
        double c2 = c1 * 1.525;
        return x < 0.5 ? Math.pow((double)2 * x, 2) * ((c2 + 1.0) * (double)2 * x - c2) / (double)2 : (Math.pow((double)2 * x - (double)2, 2) * ((c2 + 1.0) * (x * (double)2 - (double)2) + c2) + (double)2) / (double)2;
    }

    @JvmStatic
    public static final double easeInElastic(double x) {
        double c4 = 2.0943951023931953;
        return x == 0.0 ? 0.0 : (x == 1.0 ? 1.0 : Math.pow(-2.0, (double)10 * x - (double)10) * Math.sin((x * (double)10 - 10.75) * c4));
    }

    @JvmStatic
    public static final double easeOutElastic(double x) {
        double c4 = 2.0943951023931953;
        return x == 0.0 ? 0.0 : (x == 1.0 ? 1.0 : Math.pow(2.0, (double)-10 * x) * Math.sin((x * (double)10 - 0.75) * c4) + 1.0);
    }

    @JvmStatic
    public static final double easeInOutElastic(double x) {
        double c5 = 1.3962634015954636;
        return x == 0.0 ? 0.0 : (x == 1.0 ? 1.0 : (x < 0.5 ? -(Math.pow(2.0, (double)20 * x - (double)10) * Math.sin(((double)20 * x - 11.125) * c5)) / (double)2 : Math.pow(2.0, (double)-20 * x + (double)10) * Math.sin(((double)20 * x - 11.125) * c5) / (double)2 + 1.0));
    }

    @JvmStatic
    public static final double easeInBounce(double x) {
        return 1.0 - EaseUtils.easeOutBounce(1.0 - x);
    }

    @JvmStatic
    public static final double easeOutBounce(double animeX) {
        double x = animeX;
        double n1 = 7.5625;
        double d1 = 2.75;
        if (x < 1.0 / d1) {
            return n1 * x * x;
        }
        if (x < (double)2 / d1) {
            return n1 * ((x -= 1.5) / d1) * x + 0.75;
        }
        if (x < 2.5 / d1) {
            return n1 * ((x -= 2.25) / d1) * x + 0.9375;
        }
        return n1 * ((x -= 2.625) / d1) * x + 0.984375;
    }

    @JvmStatic
    public static final double easeInOutBounce(double x) {
        return x < 0.5 ? (1.0 - EaseUtils.easeOutBounce(1.0 - (double)2 * x)) / (double)2 : (1.0 + EaseUtils.easeOutBounce((double)2 * x - 1.0)) / (double)2;
    }
}

