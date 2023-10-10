/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.number;

public interface AffixPatternProvider {
    public static final int FLAG_POS_PREFIX = 256;
    public static final int FLAG_POS_SUFFIX = 0;
    public static final int FLAG_NEG_PREFIX = 768;
    public static final int FLAG_NEG_SUFFIX = 512;

    public char charAt(int var1, int var2);

    public int length(int var1);

    public String getString(int var1);

    public boolean hasCurrencySign();

    public boolean positiveHasPlusSign();

    public boolean hasNegativeSubpattern();

    public boolean negativeHasMinusSign();

    public boolean containsSymbolType(int var1);

    public boolean hasBody();

    public static final class Flags {
        public static final int PLURAL_MASK = 255;
        public static final int PREFIX = 256;
        public static final int NEGATIVE_SUBPATTERN = 512;
        public static final int PADDING = 1024;
    }
}

