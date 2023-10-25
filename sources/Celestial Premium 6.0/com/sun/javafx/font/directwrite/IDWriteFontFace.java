/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.directwrite;

import com.sun.javafx.font.directwrite.DWRITE_GLYPH_METRICS;
import com.sun.javafx.font.directwrite.IUnknown;
import com.sun.javafx.font.directwrite.OS;
import com.sun.javafx.geom.Path2D;

class IDWriteFontFace
extends IUnknown {
    IDWriteFontFace(long l) {
        super(l);
    }

    DWRITE_GLYPH_METRICS GetDesignGlyphMetrics(short s, boolean bl) {
        return OS.GetDesignGlyphMetrics(this.ptr, s, bl);
    }

    Path2D GetGlyphRunOutline(float f, short s, boolean bl) {
        return OS.GetGlyphRunOutline(this.ptr, f, s, bl);
    }
}

