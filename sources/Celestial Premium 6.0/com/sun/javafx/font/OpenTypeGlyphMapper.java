/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font;

import com.sun.javafx.font.CMap;
import com.sun.javafx.font.CharToGlyphMapper;
import com.sun.javafx.font.PrismFontFile;

public class OpenTypeGlyphMapper
extends CharToGlyphMapper {
    PrismFontFile font;
    CMap cmap;

    public OpenTypeGlyphMapper(PrismFontFile prismFontFile) {
        this.font = prismFontFile;
        try {
            this.cmap = CMap.initialize(prismFontFile);
        }
        catch (Exception exception) {
            this.cmap = null;
        }
        if (this.cmap == null) {
            this.handleBadCMAP();
        }
        this.missingGlyph = 0;
    }

    @Override
    public int getGlyphCode(int n) {
        try {
            return this.cmap.getGlyph(n);
        }
        catch (Exception exception) {
            this.handleBadCMAP();
            return this.missingGlyph;
        }
    }

    private void handleBadCMAP() {
        this.cmap = CMap.theNullCmap;
    }

    boolean hasSupplementaryChars() {
        return this.cmap instanceof CMap.CMapFormat8 || this.cmap instanceof CMap.CMapFormat10 || this.cmap instanceof CMap.CMapFormat12;
    }
}

