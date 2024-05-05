// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.helpers.render.animations;

public class EasingHelper
{
    public static double easeInSine(final int n) {
        return 1.0 - Math.cos(n * 3.141592653589793 / 2.0);
    }
    
    public static double easeOutSine(final int n) {
        return Math.sin(n * 3.141592653589793 / 2.0);
    }
    
    public static double easeInOutSine(final int n) {
        return -(Math.cos(3.141592653589793 * n) - 1.0) / 2.0;
    }
    
    public static double easeInCubic(final int n) {
        return n ^ 0x3;
    }
    
    public static double easeOutCubic(final int n) {
        return 1.0 - Math.pow(1 - n, 3.0);
    }
    
    public static double easeInOutCubic(final int n) {
        return (n < 0.5) ? (4 * n ^ 0x3) : (1.0 - Math.pow(-2 * n + 2, 3.0) / 2.0);
    }
    
    public static double easeInQuint(final int n) {
        return n ^ 0x5;
    }
    
    public static double easeOutQuint(final int n) {
        return 1.0 - Math.pow(1 - n, 5.0);
    }
    
    public static double easeInOutQuint(final int n) {
        return (n < 0.5) ? (16 * n ^ 0x5) : (1.0 - Math.pow(-2 * n + 2, 5.0) / 2.0);
    }
    
    public static double easeInCirc(final int n) {
        return 1.0 - Math.sqrt(1.0 - Math.pow(n, 2.0));
    }
    
    public static double easeOutCirc(final int n) {
        return Math.sqrt(1.0 - Math.pow(n - 1, 2.0));
    }
    
    public static double easeInOutCirc(final int n) {
        return (n < 0.5) ? ((1.0 - Math.sqrt(1.0 - Math.pow(2 * n, 2.0))) / 2.0) : ((Math.sqrt(1.0 - Math.pow(-2 * n + 2, 2.0)) + 1.0) / 2.0);
    }
    
    public static double easeInElastic(final int n) {
        final double c4 = 2.0943951023931953;
        return (n == 0) ? 0.0 : ((n == 1) ? 1.0 : (-Math.pow(2.0, 10 * n - 10) * Math.sin((n * 10 - 10.75) * 2.0943951023931953)));
    }
    
    public static double easeOutElastic(final int n) {
        final double c4 = 2.0943951023931953;
        return (n == 0) ? 0.0 : ((n == 1) ? 1.0 : (Math.pow(2.0, -10 * n - 10) * Math.sin((n * 10 - 0.75) * 2.0943951023931953) + 1.0));
    }
    
    public static double easeInOutElastic(final int n) {
        final double c5 = 1.3962634015954636;
        return (n == 0) ? 0.0 : ((n == 1) ? 1.0 : ((n < 0.5) ? (-(Math.pow(2.0, 20 * n - 10) * Math.sin((20 * n - 11.125) * 1.3962634015954636)) / 2.0) : (Math.pow(2.0, -20 * n + 10) * Math.sin((20 * n - 11.125) * 1.3962634015954636) / 2.0 + 1.0)));
    }
    
    public static double easeInQuad(final int n) {
        return n ^ 0x2;
    }
    
    public static double easeOutQuad(final int n) {
        return 1 - (1 - n) * (1 - n);
    }
    
    public static double easeInOutQuad(final int n) {
        return (n < 0.5) ? (2 * n ^ 0x2) : (1.0 - Math.pow(-2 * n + 2, 2.0) / 2.0);
    }
    
    public static double easeInQuart(final int n) {
        return n ^ 0x4;
    }
    
    public static double easeOutQuart(final float n) {
        return 1.0 - Math.pow(1.0f - n, 4.0);
    }
    
    public static double easeInOutQuart(final int n) {
        return (n < 0.5) ? (8 * n ^ 0x4) : (1.0 - Math.pow(-2 * n + 2, 4.0) / 2.0);
    }
    
    public static double easeInExpo(final int n) {
        return (n == 0) ? 0.0 : Math.pow(2.0, 10 * n - 10);
    }
    
    public static double easeOutExpo(final int n) {
        return (n == 1) ? 1.0 : (1.0 - Math.pow(2.0, -10 * n));
    }
    
    public static double easeInOutExpo(final int n) {
        return (n == 0) ? 0.0 : ((n == 1) ? 1.0 : ((n < 0.5) ? (Math.pow(2.0, 20 * n - 10) / 2.0) : ((2.0 - Math.pow(2.0, -20 * n + 10)) / 2.0)));
    }
    
    public static double easeInBack(final int n) {
        final double c1 = 1.70158;
        final double c2 = 2.70158;
        return 2.70158 * n * n * n - 1.70158 * n * n;
    }
    
    public static double easeOutBack(final int n) {
        final double c1 = 1.70158;
        final double c2 = 2.70158;
        return 1.0 + 2.70158 * Math.pow(n - 1, 3.0) + 1.70158 * Math.pow(n - 1, 2.0);
    }
    
    public static double easeInOutBack(final int n) {
        final double c1 = 1.70158;
        final double c2 = 2.5949095;
        return (n < 0.5) ? (Math.pow(2 * n, 2.0) * (7.189819 * n - 2.5949095) / 2.0) : ((Math.pow(2 * n - 2, 2.0) * (3.5949095 * (n * 2 - 2) + 2.5949095) + 2.0) / 2.0);
    }
    
    public static double easeOutBounce(int n) {
        final double n2 = 7.5625;
        final double d1 = 2.75;
        if (n < 0.36363636363636365) {
            return 7.5625 * n * n;
        }
        if (n < 0.7272727272727273) {
            n -= (int)0.5454545454545454;
            return 7.5625 * n * n + 0.75;
        }
        if (n < 0.9090909090909091) {
            n -= (int)0.8181818181818182;
            return 7.5625 * n * n + 0.9375;
        }
        n -= (int)0.9545454545454546;
        return 7.5625 * n * n + 0.984375;
    }
    
    public static double easeInBounce(final int n) {
        return 1.0 - easeOutBounce(1 - n);
    }
    
    public static double easeInOutBounce(final int n) {
        return (n < 0.5) ? ((1.0 - easeOutBounce(1 - 2 * n)) / 2.0) : ((1.0 + easeOutBounce(2 * n - 1)) / 2.0);
    }
}
