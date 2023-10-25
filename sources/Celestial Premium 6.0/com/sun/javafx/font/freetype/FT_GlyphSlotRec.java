/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.freetype;

import com.sun.javafx.font.freetype.FT_Bitmap;
import com.sun.javafx.font.freetype.FT_Glyph_Metrics;

class FT_GlyphSlotRec {
    FT_Glyph_Metrics metrics = new FT_Glyph_Metrics();
    long linearHoriAdvance;
    long linearVertAdvance;
    long advance_x;
    long advance_y;
    int format;
    FT_Bitmap bitmap = new FT_Bitmap();
    int bitmap_left;
    int bitmap_top;

    FT_GlyphSlotRec() {
    }
}

