// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.misc;

public class RomanNumeralUtils
{
    private static final int MIN_VALUE = 1;
    private static final int MAX_VALUE = 3999;
    private static final String[] M;
    private static final String[] C;
    private static final String[] X;
    private static final String[] I;
    
    public static String generate(final int number) {
        if (number < 1 || number > 3999) {
            throw new IllegalArgumentException(String.format("The number must be in the range [%d, %d]", 1, 3999));
        }
        return RomanNumeralUtils.M[number / 1000] + RomanNumeralUtils.C[number % 1000 / 100] + RomanNumeralUtils.X[number % 100 / 10] + RomanNumeralUtils.I[number % 10];
    }
    
    static {
        M = new String[] { "", "M", "MM", "MMM", "MMMM" };
        C = new String[] { "", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM" };
        X = new String[] { "", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC" };
        I = new String[] { "", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX" };
    }
}
