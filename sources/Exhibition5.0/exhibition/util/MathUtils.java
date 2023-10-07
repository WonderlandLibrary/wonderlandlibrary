// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.util;

import java.math.RoundingMode;
import java.math.BigDecimal;

public final class MathUtils
{
    public static double roundToPlace(final double value, final int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
    public static double getIncremental(final double val, final double inc) {
        final double one = 1.0 / inc;
        return Math.round(val * one) / one;
    }
    
    public static boolean isInteger(final Double variable) {
        return variable == Math.floor(variable) && !Double.isInfinite(variable);
    }
}
