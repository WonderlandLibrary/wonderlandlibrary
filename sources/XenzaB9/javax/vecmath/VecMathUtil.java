// 
// Decompiled by Procyon v0.6.0
// 

package javax.vecmath;

class VecMathUtil
{
    static int floatToIntBits(final float n) {
        if (n == 0.0f) {
            return 0;
        }
        return Float.floatToIntBits(n);
    }
    
    static long doubleToLongBits(final double n) {
        if (n == 0.0) {
            return 0L;
        }
        return Double.doubleToLongBits(n);
    }
    
    private VecMathUtil() {
    }
}
