/*
 * Decompiled with CFR 0.150.
 */
package markgg.utilities;

public class Random {
    public static int randomNumber(double max, double min) {
        int ii = (int)(-min + (double)((int)(Math.random() * (max - -min + 1.0))));
        return ii;
    }
}

