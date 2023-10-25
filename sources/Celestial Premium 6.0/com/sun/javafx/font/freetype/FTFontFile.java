/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.freetype;

import com.sun.javafx.font.Disposer;
import com.sun.javafx.font.FontStrikeDesc;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.font.PrismFontFile;
import com.sun.javafx.font.PrismFontStrike;
import com.sun.javafx.font.freetype.FTDisposer;
import com.sun.javafx.font.freetype.FTFactory;
import com.sun.javafx.font.freetype.FTFontStrike;
import com.sun.javafx.font.freetype.FTGlyph;
import com.sun.javafx.font.freetype.FT_Bitmap;
import com.sun.javafx.font.freetype.FT_GlyphSlotRec;
import com.sun.javafx.font.freetype.FT_Glyph_Metrics;
import com.sun.javafx.font.freetype.FT_Matrix;
import com.sun.javafx.font.freetype.OSFreetype;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.transform.BaseTransform;

class FTFontFile
extends PrismFontFile {
    private long library;
    private long face;
    private FTDisposer disposer;

    FTFontFile(String string, String string2, int n, boolean bl, boolean bl2, boolean bl3, boolean bl4) throws Exception {
        super(string, string2, n, bl, bl2, bl3, bl4);
        this.init();
    }

    private synchronized void init() throws Exception {
        long[] arrl = new long[1];
        int n = OSFreetype.FT_Init_FreeType(arrl);
        if (n != 0) {
            throw new Exception("FT_Init_FreeType Failed error " + n);
        }
        this.library = arrl[0];
        if (FTFactory.LCD_SUPPORT) {
            OSFreetype.FT_Library_SetLcdFilter(this.library, 1);
        }
        String string = this.getFileName();
        int n2 = this.getFontIndex();
        byte[] arrby = (string + "\u0000").getBytes();
        n = OSFreetype.FT_New_Face(this.library, arrby, n2, arrl);
        if (n != 0) {
            throw new Exception("FT_New_Face Failed error " + n + " Font File " + string + " Font Index " + n2);
        }
        this.face = arrl[0];
        if (!this.isRegistered()) {
            this.disposer = new FTDisposer(this.library, this.face);
            Disposer.addRecord(this, this.disposer);
        }
    }

    @Override
    protected PrismFontStrike<?> createStrike(float f, BaseTransform baseTransform, int n, FontStrikeDesc fontStrikeDesc) {
        return new FTFontStrike(this, f, baseTransform, n, fontStrikeDesc);
    }

    @Override
    protected synchronized int[] createGlyphBoundingBox(int n) {
        int n2 = 1;
        OSFreetype.FT_Load_Glyph(this.face, n, n2);
        int[] arrn = new int[4];
        FT_GlyphSlotRec fT_GlyphSlotRec = OSFreetype.getGlyphSlot(this.face);
        if (fT_GlyphSlotRec != null && fT_GlyphSlotRec.metrics != null) {
            FT_Glyph_Metrics fT_Glyph_Metrics = fT_GlyphSlotRec.metrics;
            arrn[0] = (int)fT_Glyph_Metrics.horiBearingX;
            arrn[1] = (int)(fT_Glyph_Metrics.horiBearingY - fT_Glyph_Metrics.height);
            arrn[2] = (int)(fT_Glyph_Metrics.horiBearingX + fT_Glyph_Metrics.width);
            arrn[3] = (int)fT_Glyph_Metrics.horiBearingY;
        }
        return arrn;
    }

    synchronized Path2D createGlyphOutline(int n, float f) {
        int n2 = (int)(f * 64.0f);
        OSFreetype.FT_Set_Char_Size(this.face, 0L, n2, 72, 72);
        int n3 = 2058;
        OSFreetype.FT_Load_Glyph(this.face, n, n3);
        return OSFreetype.FT_Outline_Decompose(this.face);
    }

    synchronized void initGlyph(FTGlyph fTGlyph, FTFontStrike fTFontStrike) {
        byte[] arrby;
        float f = fTFontStrike.getSize();
        if (f == 0.0f) {
            fTGlyph.buffer = new byte[0];
            fTGlyph.bitmap = new FT_Bitmap();
            return;
        }
        int n = (int)(f * 64.0f);
        OSFreetype.FT_Set_Char_Size(this.face, 0L, n, 72, 72);
        boolean bl = fTFontStrike.getAAMode() == 1 && FTFactory.LCD_SUPPORT;
        int n2 = 14;
        FT_Matrix fT_Matrix = fTFontStrike.matrix;
        if (fT_Matrix != null) {
            OSFreetype.FT_Set_Transform(this.face, fT_Matrix, 0L, 0L);
        } else {
            n2 |= 0x800;
        }
        n2 = bl ? (n2 |= 0x30000) : (n2 |= 0);
        int n3 = fTGlyph.getGlyphCode();
        int n4 = OSFreetype.FT_Load_Glyph(this.face, n3, n2);
        if (n4 != 0) {
            if (PrismFontFactory.debugFonts) {
                System.err.println("FT_Load_Glyph failed " + n4 + " glyph code " + n3 + " load falgs " + n2);
            }
            return;
        }
        FT_GlyphSlotRec fT_GlyphSlotRec = OSFreetype.getGlyphSlot(this.face);
        if (fT_GlyphSlotRec == null) {
            return;
        }
        FT_Bitmap fT_Bitmap = fT_GlyphSlotRec.bitmap;
        if (fT_Bitmap == null) {
            return;
        }
        byte by = fT_Bitmap.pixel_mode;
        int n5 = fT_Bitmap.width;
        int n6 = fT_Bitmap.rows;
        int n7 = fT_Bitmap.pitch;
        if (by != 2 && by != 5) {
            if (PrismFontFactory.debugFonts) {
                System.err.println("Unexpected pixel mode: " + by + " glyph code " + n3 + " load falgs " + n2);
            }
            return;
        }
        if (n5 != 0 && n6 != 0) {
            arrby = OSFreetype.getBitmapData(this.face);
            if (arrby != null && n7 != n5) {
                byte[] arrby2 = new byte[n5 * n6];
                int n8 = 0;
                int n9 = 0;
                for (int i = 0; i < n6; ++i) {
                    for (int j = 0; j < n5; ++j) {
                        arrby2[n9 + j] = arrby[n8 + j];
                    }
                    n9 += n5;
                    n8 += n7;
                }
                arrby = arrby2;
            }
        } else {
            arrby = new byte[]{};
        }
        fTGlyph.buffer = arrby;
        fTGlyph.bitmap = fT_Bitmap;
        fTGlyph.bitmap_left = fT_GlyphSlotRec.bitmap_left;
        fTGlyph.bitmap_top = fT_GlyphSlotRec.bitmap_top;
        fTGlyph.advanceX = (float)fT_GlyphSlotRec.advance_x / 64.0f;
        fTGlyph.advanceY = (float)fT_GlyphSlotRec.advance_y / 64.0f;
        fTGlyph.userAdvance = (float)fT_GlyphSlotRec.linearHoriAdvance / 65536.0f;
        fTGlyph.lcd = bl;
    }
}

