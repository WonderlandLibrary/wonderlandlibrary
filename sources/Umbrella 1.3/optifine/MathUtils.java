/*
 * Decompiled with CFR 0.150.
 */
package optifine;

public class MathUtils {
    public static int getAverage(int[] vals) {
        int avg;
        if (vals.length <= 0) {
            return 0;
        }
        int sum = 0;
        for (avg = 0; avg < vals.length; ++avg) {
            int val = vals[avg];
            sum += val;
        }
        avg = sum / vals.length;
        return avg;
    }
}

