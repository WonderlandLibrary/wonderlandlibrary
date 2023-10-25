/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font;

import com.sun.javafx.font.CharToGlyphMapper;
import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.FontStrikeDesc;
import com.sun.javafx.geom.transform.BaseTransform;
import java.lang.ref.WeakReference;
import java.util.Map;

public interface FontResource {
    public static final int AA_GREYSCALE = 0;
    public static final int AA_LCD = 1;
    public static final int KERN = 1;
    public static final int CLIG = 2;
    public static final int DLIG = 4;
    public static final int HLIG = 8;
    public static final int LIGA = 16;
    public static final int RLIG = 32;
    public static final int LIGATURES = 62;
    public static final int SMCP = 64;
    public static final int FRAC = 128;
    public static final int AFRC = 256;
    public static final int ZERO = 512;
    public static final int SWSH = 1024;
    public static final int CSWH = 2048;
    public static final int SALT = 4096;
    public static final int NALT = 8192;
    public static final int RUBY = 16384;
    public static final int SS01 = 32768;
    public static final int SS02 = 65536;
    public static final int SS03 = 131072;
    public static final int SS04 = 262144;
    public static final int SS05 = 524288;
    public static final int SS06 = 0x100000;
    public static final int SS07 = 0x200000;

    public String getFullName();

    public String getPSName();

    public String getFamilyName();

    public String getFileName();

    public String getStyleName();

    public String getLocaleFullName();

    public String getLocaleFamilyName();

    public String getLocaleStyleName();

    public int getFeatures();

    public boolean isBold();

    public boolean isItalic();

    public float getAdvance(int var1, float var2);

    public float[] getGlyphBoundingBox(int var1, float var2, float[] var3);

    public int getDefaultAAMode();

    public CharToGlyphMapper getGlyphMapper();

    public Map<FontStrikeDesc, WeakReference<FontStrike>> getStrikeMap();

    public FontStrike getStrike(float var1, BaseTransform var2);

    public FontStrike getStrike(float var1, BaseTransform var2, int var3);

    public Object getPeer();

    public void setPeer(Object var1);

    public boolean isEmbeddedFont();
}

