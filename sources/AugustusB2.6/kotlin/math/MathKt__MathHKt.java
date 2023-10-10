// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.math;

class MathKt__MathHKt
{
    public static int roundToInt(final double $this$roundToInt) {
        if (Double.isNaN($this$roundToInt)) {
            throw new IllegalArgumentException("Cannot round NaN value.");
        }
        if ($this$roundToInt > 2.147483647E9) {
            return Integer.MAX_VALUE;
        }
        if ($this$roundToInt < -2.147483648E9) {
            return Integer.MIN_VALUE;
        }
        return (int)Math.round($this$roundToInt);
    }
}
