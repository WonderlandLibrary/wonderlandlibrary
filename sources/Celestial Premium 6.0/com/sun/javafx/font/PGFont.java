/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font;

import com.sun.javafx.font.FontResource;
import com.sun.javafx.font.FontStrike;
import com.sun.javafx.geom.transform.BaseTransform;

public interface PGFont {
    public String getFullName();

    public String getFamilyName();

    public String getStyleName();

    public String getName();

    public float getSize();

    public FontResource getFontResource();

    public FontStrike getStrike(BaseTransform var1);

    public FontStrike getStrike(BaseTransform var1, int var2);

    public int getFeatures();
}

