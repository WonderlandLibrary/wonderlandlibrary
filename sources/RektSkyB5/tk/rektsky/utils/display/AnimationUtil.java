/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.utils.display;

public class AnimationUtil {
    static double PI = Math.PI;

    public static double easeInSine(double t2) {
        return 1.0 - Math.cos(t2 * Math.PI / 2.0);
    }

    public static double easeOutSine(double t2) {
        return Math.cos(t2 * Math.PI / 2.0);
    }

    public static double easeInOutSine(double t2) {
        return -(Math.cos(PI * t2) - 1.0) / 2.0;
    }

    public static double easeInQuad(double t2) {
        return t2 * t2;
    }

    public static double easeOutQuad(double t2) {
        return 1.0 - (1.0 - t2) * (1.0 - t2);
    }

    public static double easeInOutQuad(double t2) {
        return t2 < 0.5 ? 2.0 * t2 * t2 : 1.0 - Math.pow(-2.0 * t2 + 2.0, 2.0) / 2.0;
    }

    public static double easeInCubic(double t2) {
        return t2 * t2 * t2;
    }

    public static double easeOutCubic(double t2) {
        return 1.0 - Math.pow(1.0 - t2, 3.0);
    }

    public static double easeInOutCubic(double t2) {
        return t2 < 0.5 ? 4.0 * t2 * t2 * t2 : 1.0 - Math.pow(-2.0 * t2 + 2.0, 3.0) / 2.0;
    }

    public static double easeInQuart(double t2) {
        return t2 * t2 * t2 * t2;
    }

    public static double easeOutQuart(double t2) {
        return 1.0 - Math.pow(1.0 - t2, 4.0);
    }

    public static double easeInOutQuart(double t2) {
        return t2 < 0.5 ? 8.0 * t2 * t2 * t2 * t2 : 1.0 - Math.pow(-2.0 * t2 + 2.0, 4.0) / 2.0;
    }

    public static double easeInQuint(double t2) {
        return t2 * t2 * t2 * t2 * t2;
    }

    public static double easeOutQuint(double t2) {
        return 1.0 - Math.pow(1.0 - t2, 5.0);
    }

    public static double easeInOutQuint(double t2) {
        return t2 < 0.5 ? 16.0 * t2 * t2 * t2 * t2 * t2 : 1.0 - Math.pow(-2.0 * t2 + 2.0, 5.0) / 2.0;
    }

    public static double easeInExpo(double t2) {
        return t2 == 0.0 ? 0.0 : Math.pow(2.0, 10.0 * t2 - 10.0);
    }

    public static double easeOutExpo(double t2) {
        return t2 == 1.0 ? 1.0 : 1.0 - Math.pow(2.0, -10.0 * t2);
    }

    public static double easeInOutExpo(double t2) {
        return t2 == 0.0 ? 0.0 : (t2 == 1.0 ? 1.0 : (t2 < 0.5 ? Math.pow(2.0, 20.0 * t2 - 10.0) / 2.0 : (2.0 - Math.pow(2.0, -20.0 * t2 + 10.0)) / 2.0));
    }

    public static double easeInCirc(double t2) {
        return 1.0 - Math.sqrt(1.0 - Math.pow(t2, 2.0));
    }

    public static double easeOutCirc(double t2) {
        return Math.sqrt(1.0 - Math.pow(t2 - 1.0, 2.0));
    }

    public static double easeInOutCirc(double t2) {
        return t2 < 0.5 ? (1.0 - Math.sqrt(1.0 - Math.pow(2.0 * t2, 2.0))) / 2.0 : (Math.sqrt(1.0 - Math.pow(-2.0 * t2 + 2.0, 2.0)) + 1.0) / 2.0;
    }

    public static double easeInBack(double t2) {
        double c1 = 1.70158;
        double c3 = c1 + 1.0;
        return c3 * t2 * t2 * t2 - c1 * t2 * t2;
    }

    public static double easeOutBack(double t2) {
        double c1 = 1.70158;
        double c3 = c1 + 1.0;
        return 1.0 + c3 * Math.pow(t2 - 1.0, 3.0) + c1 * Math.pow(t2 - 1.0, 2.0);
    }

    public static double easeInOutBack(double t2) {
        double c1 = 1.70158;
        double c2 = c1 * 1.525;
        return t2 < 0.5 ? Math.pow(2.0 * t2, 2.0) * ((c2 + 1.0) * 2.0 * t2 - c2) / 2.0 : (Math.pow(2.0 * t2 - 2.0, 2.0) * ((c2 + 1.0) * (t2 * 2.0 - 2.0) + c2) + 2.0) / 2.0;
    }

    public static double easeInElastic(double t2) {
        double c4 = 2.0943951023931953;
        return t2 == 0.0 ? 0.0 : (t2 == 1.0 ? 1.0 : -Math.pow(2.0, 10.0 * t2 - 10.0) * Math.sin((t2 * 10.0 - 10.75) * c4));
    }

    public static double easeOutElastic(double t2) {
        double c4 = 2.0943951023931953;
        return t2 == 0.0 ? 0.0 : (t2 == 1.0 ? 1.0 : Math.pow(2.0, -10.0 * t2) * Math.sin((t2 * 10.0 - 0.75) * c4) + 1.0);
    }

    public static double easeInOutElastic(double t2) {
        double c5 = 1.3962634015954636;
        return t2 == 0.0 ? 0.0 : (t2 == 1.0 ? 1.0 : (t2 < 0.5 ? -(Math.pow(2.0, 20.0 * t2 - 10.0) * Math.sin((20.0 * t2 - 11.125) * c5)) / 2.0 : Math.pow(2.0, -20.0 * t2 + 10.0) * Math.sin((20.0 * t2 - 11.125) * c5) / 2.0 + 1.0));
    }

    public static double easeInBounce(double t2) {
        return 1.0 - AnimationUtil.easeOutBounce(1.0 - t2);
    }

    public static double easeOutBounce(double t2) {
        double n1 = 7.5625;
        double d1 = 2.75;
        if (t2 < 1.0 / d1) {
            return n1 * t2 * t2;
        }
        if (t2 < 2.0 / d1) {
            return n1 * (t2 -= 1.5 / d1) * t2 + 0.75;
        }
        if (t2 < 2.5 / d1) {
            return n1 * (t2 -= 2.25 / d1) * t2 + 0.9375;
        }
        return n1 * (t2 -= 2.625 / d1) * t2 + 0.984375;
    }

    public static double easeInOutBounce(double t2) {
        return t2 < 0.5 ? (1.0 - AnimationUtil.easeOutBounce(1.0 - 2.0 * t2)) / 2.0 : (1.0 + AnimationUtil.easeOutBounce(2.0 * t2 - 1.0)) / 2.0;
    }
}

