/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font;

import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.Metrics;
import com.sun.javafx.font.PGFont;
import com.sun.javafx.geom.transform.BaseTransform;

public class PrismFontUtils {
    private PrismFontUtils() {
    }

    static Metrics getFontMetrics(PGFont pGFont) {
        FontStrike fontStrike = pGFont.getStrike(BaseTransform.IDENTITY_TRANSFORM, 0);
        return fontStrike.getMetrics();
    }

    static double getCharWidth(PGFont pGFont, char c) {
        FontStrike fontStrike = pGFont.getStrike(BaseTransform.IDENTITY_TRANSFORM, 0);
        double d = fontStrike.getCharAdvance(c);
        if (d == 0.0) {
            d = (double)pGFont.getSize() / 2.0;
        }
        return d;
    }
}

