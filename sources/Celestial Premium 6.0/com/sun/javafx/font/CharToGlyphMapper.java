/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font;

public abstract class CharToGlyphMapper {
    public static final int HI_SURROGATE_SHIFT = 10;
    public static final int HI_SURROGATE_START = 55296;
    public static final int HI_SURROGATE_END = 56319;
    public static final int LO_SURROGATE_START = 56320;
    public static final int LO_SURROGATE_END = 57343;
    public static final int SURROGATES_START = 65536;
    public static final int MISSING_GLYPH = 0;
    public static final int INVISIBLE_GLYPH_ID = 65535;
    protected int missingGlyph = 0;

    public boolean canDisplay(char c) {
        int n = this.charToGlyph(c);
        return n != this.missingGlyph;
    }

    public int getMissingGlyphCode() {
        return this.missingGlyph;
    }

    public abstract int getGlyphCode(int var1);

    public int charToGlyph(char c) {
        return this.getGlyphCode(c);
    }

    public int charToGlyph(int n) {
        return this.getGlyphCode(n);
    }

    public void charsToGlyphs(int n, int n2, char[] arrc, int[] arrn, int n3) {
        for (int i = 0; i < n2; ++i) {
            char c;
            int n4 = arrc[n + i];
            if (n4 >= 55296 && n4 <= 56319 && i + 1 < n2 && (c = arrc[n + i + 1]) >= '\udc00' && c <= '\udfff') {
                n4 = (n4 - 55296 << 10) + c - 56320 + 65536;
                arrn[n3 + i] = this.getGlyphCode(n4);
                arrn[n3 + ++i] = 65535;
                continue;
            }
            arrn[n3 + i] = this.getGlyphCode(n4);
        }
    }

    public void charsToGlyphs(int n, int n2, char[] arrc, int[] arrn) {
        this.charsToGlyphs(n, n2, arrc, arrn, 0);
    }

    public void charsToGlyphs(int n, char[] arrc, int[] arrn) {
        this.charsToGlyphs(0, n, arrc, arrn, 0);
    }
}

