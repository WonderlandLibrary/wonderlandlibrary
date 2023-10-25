/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font;

import com.sun.javafx.font.FontResource;
import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.PGFont;
import com.sun.javafx.geom.transform.BaseTransform;

class PrismFont
implements PGFont {
    private String name;
    private float fontSize;
    protected FontResource fontResource;
    private int features;
    private int hash;

    PrismFont(FontResource fontResource, String string, float f) {
        this.fontResource = fontResource;
        this.name = string;
        this.fontSize = f;
    }

    @Override
    public String getFullName() {
        return this.fontResource.getFullName();
    }

    @Override
    public String getFamilyName() {
        return this.fontResource.getFamilyName();
    }

    @Override
    public String getStyleName() {
        return this.fontResource.getStyleName();
    }

    @Override
    public int getFeatures() {
        return this.features;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public float getSize() {
        return this.fontSize;
    }

    @Override
    public FontStrike getStrike(BaseTransform baseTransform) {
        return this.fontResource.getStrike(this.fontSize, baseTransform);
    }

    @Override
    public FontStrike getStrike(BaseTransform baseTransform, int n) {
        return this.fontResource.getStrike(this.fontSize, baseTransform, n);
    }

    @Override
    public FontResource getFontResource() {
        return this.fontResource;
    }

    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (!(object instanceof PrismFont)) {
            return false;
        }
        PrismFont prismFont = (PrismFont)object;
        return this.fontSize == prismFont.fontSize && this.fontResource.equals(prismFont.fontResource);
    }

    public int hashCode() {
        if (this.hash != 0) {
            return this.hash;
        }
        this.hash = 497 + Float.floatToIntBits(this.fontSize);
        this.hash = 71 * this.hash + this.fontResource.hashCode();
        return this.hash;
    }
}

