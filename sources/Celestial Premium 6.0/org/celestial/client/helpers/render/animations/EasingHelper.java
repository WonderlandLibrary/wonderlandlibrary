/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.helpers.render.animations;

public class EasingHelper {
    public static double easeInSine(int n) {
        return 1.0 - Math.cos((double)n * Math.PI / 2.0);
    }

    public static double easeOutSine(int n) {
        return Math.sin((double)n * Math.PI / 2.0);
    }

    public static double easeInOutSine(int n) {
        return -(Math.cos(Math.PI * (double)n) - 1.0) / 2.0;
    }

    public static double easeInCubic(int n) {
        return n ^ 3;
    }

    public static double easeOutCubic(int n) {
        return 1.0 - Math.pow(1 - n, 3.0);
    }

    public static double easeInOutCubic(int n) {
        return (double)n < 0.5 ? (double)(4 * n ^ 3) : 1.0 - Math.pow(-2 * n + 2, 3.0) / 2.0;
    }

    public static double easeInQuint(int n) {
        return n ^ 5;
    }

    public static double easeOutQuint(int n) {
        return 1.0 - Math.pow(1 - n, 5.0);
    }

    public static double easeInOutQuint(int n) {
        return (double)n < 0.5 ? (double)(16 * n ^ 5) : 1.0 - Math.pow(-2 * n + 2, 5.0) / 2.0;
    }

    public static double easeInCirc(int n) {
        return 1.0 - Math.sqrt(1.0 - Math.pow(n, 2.0));
    }

    public static double easeOutCirc(int n) {
        return Math.sqrt(1.0 - Math.pow(n - 1, 2.0));
    }

    public static double easeInOutCirc(int n) {
        return (double)n < 0.5 ? (1.0 - Math.sqrt(1.0 - Math.pow(2 * n, 2.0))) / 2.0 : (Math.sqrt(1.0 - Math.pow(-2 * n + 2, 2.0)) + 1.0) / 2.0;
    }

    public static double easeInElastic(int n) {
        double c4 = 2.0943951023931953;
        return n == 0 ? 0.0 : (n == 1 ? 1.0 : -Math.pow(2.0, 10 * n - 10) * Math.sin(((double)(n * 10) - 10.75) * 2.0943951023931953));
    }

    public static double easeOutElastic(int n) {
        double c4 = 2.0943951023931953;
        return n == 0 ? 0.0 : (n == 1 ? 1.0 : Math.pow(2.0, -10 * n - 10) * Math.sin(((double)(n * 10) - 0.75) * 2.0943951023931953) + 1.0);
    }

    public static double easeInOutElastic(int n) {
        double c5 = 1.3962634015954636;
        return n == 0 ? 0.0 : (n == 1 ? 1.0 : ((double)n < 0.5 ? -(Math.pow(2.0, 20 * n - 10) * Math.sin(((double)(20 * n) - 11.125) * 1.3962634015954636)) / 2.0 : Math.pow(2.0, -20 * n + 10) * Math.sin(((double)(20 * n) - 11.125) * 1.3962634015954636) / 2.0 + 1.0));
    }

    public static double easeInQuad(int n) {
        return n ^ 2;
    }

    public static double easeOutQuad(int n) {
        return 1 - (1 - n) * (1 - n);
    }

    public static double easeInOutQuad(int n) {
        return (double)n < 0.5 ? (double)(2 * n ^ 2) : 1.0 - Math.pow(-2 * n + 2, 2.0) / 2.0;
    }

    public static double easeInQuart(int n) {
        return n ^ 4;
    }

    public static double easeOutQuart(float n) {
        return 1.0 - Math.pow(1.0f - n, 4.0);
    }

    public static double easeInOutQuart(int n) {
        return (double)n < 0.5 ? (double)(8 * n ^ 4) : 1.0 - Math.pow(-2 * n + 2, 4.0) / 2.0;
    }

    public static double easeInExpo(int n) {
        return n == 0 ? 0.0 : Math.pow(2.0, 10 * n - 10);
    }

    public static double easeOutExpo(int n) {
        return n == 1 ? 1.0 : 1.0 - Math.pow(2.0, -10 * n);
    }

    public static double easeInOutExpo(int n) {
        return n == 0 ? 0.0 : (n == 1 ? 1.0 : ((double)n < 0.5 ? Math.pow(2.0, 20 * n - 10) / 2.0 : (2.0 - Math.pow(2.0, -20 * n + 10)) / 2.0));
    }

    public static double easeInBack(int n) {
        double c1 = 1.70158;
        double c3 = 2.70158;
        return 2.70158 * (double)n * (double)n * (double)n - 1.70158 * (double)n * (double)n;
    }

    public static double easeOutBack(int n) {
        double c1 = 1.70158;
        double c3 = 2.70158;
        return 1.0 + 2.70158 * Math.pow(n - 1, 3.0) + 1.70158 * Math.pow(n - 1, 2.0);
    }

    public static double easeInOutBack(int n) {
        double c1 = 1.70158;
        double c2 = 2.5949095;
        return (double)n < 0.5 ? Math.pow(2 * n, 2.0) * (7.189819 * (double)n - 2.5949095) / 2.0 : (Math.pow(2 * n - 2, 2.0) * (3.5949095 * (double)(n * 2 - 2) + 2.5949095) + 2.0) / 2.0;
    }

    public static double easeOutBounce(int n) {
        double n1 = 7.5625;
        double d1 = 2.75;
        if ((double)n < 0.36363636363636365) {
            return 7.5625 * (double)n * (double)n;
        }
        if ((double)n < 0.7272727272727273) {
            n = (int)((double)n - 0.5454545454545454);
            return 7.5625 * (double)n * (double)n + 0.75;
        }
        if ((double)n < 0.9090909090909091) {
            n = (int)((double)n - 0.8181818181818182);
            return 7.5625 * (double)n * (double)n + 0.9375;
        }
        n = (int)((double)n - 0.9545454545454546);
        return 7.5625 * (double)n * (double)n + 0.984375;
    }

    public static double easeInBounce(int n) {
        return 1.0 - EasingHelper.easeOutBounce(1 - n);
    }

    public static double easeInOutBounce(int n) {
        return (double)n < 0.5 ? (1.0 - EasingHelper.easeOutBounce(1 - 2 * n)) / 2.0 : (1.0 + EasingHelper.easeOutBounce(2 * n - 1)) / 2.0;
    }
}

