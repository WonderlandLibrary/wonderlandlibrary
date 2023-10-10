// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.ranges;

public class RangesKt___RangesKt extends Companion
{
    public static final int coerceAtLeast(final int $this$coerceAtLeast, final int minimumValue) {
        if ($this$coerceAtLeast < minimumValue) {
            return minimumValue;
        }
        return $this$coerceAtLeast;
    }
    
    public static final long coerceAtLeast(final long $this$coerceAtLeast, final long minimumValue) {
        if ($this$coerceAtLeast < minimumValue) {
            return minimumValue;
        }
        return $this$coerceAtLeast;
    }
    
    public static final int coerceAtMost(final int $this$coerceAtMost, final int maximumValue) {
        if ($this$coerceAtMost > maximumValue) {
            return maximumValue;
        }
        return $this$coerceAtMost;
    }
    
    public static final long coerceAtMost(final long $this$coerceAtMost, final long maximumValue) {
        if ($this$coerceAtMost > maximumValue) {
            return maximumValue;
        }
        return $this$coerceAtMost;
    }
}
