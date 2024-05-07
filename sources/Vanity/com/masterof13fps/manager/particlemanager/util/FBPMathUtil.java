package com.masterof13fps.manager.particlemanager.util;

public class FBPMathUtil
{
    public static double add(final double d, final double add) {
        if (d < 0.0) {
            return d - add;
        }
        return d + add;
    }

    public static double round(final double d, final int decimals) {
        final int i = (int)Math.round(d * Math.pow(10.0, decimals));
        return i / Math.pow(10.0, decimals);
    }
}
