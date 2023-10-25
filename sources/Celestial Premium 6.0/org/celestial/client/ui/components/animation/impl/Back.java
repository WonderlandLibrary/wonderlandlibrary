/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.ui.components.animation.impl;

import org.celestial.client.ui.components.animation.Animation;

public class Back
extends Animation {
    public static double easeIn(double progression, double startValue, double endValue, double duration) {
        return -endValue * Math.cos(progression / duration * 1.5707963267948966) + endValue + startValue;
    }

    public static double easeOut(double progression, double startValue, double endValue, double duration) {
        return endValue * Math.sin(progression / duration * 1.5707963267948966) + startValue;
    }

    public static double easeInOut(double progression, double startValue, double endValue, double duration) {
        return -endValue / 2.0 * (Math.cos(Math.PI * progression / duration) - 1.0) + startValue;
    }
}

