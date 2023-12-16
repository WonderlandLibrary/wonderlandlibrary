// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

public class MathUtils
{
    public static int getAverage(final int[] vals) {
        if (vals.length <= 0) {
            return 0;
        }
        int sum = 0;
        for (int avg = 0; avg < vals.length; ++avg) {
            final int val = vals[avg];
            sum += val;
        }
        int avg = sum / vals.length;
        return avg;
    }
}
