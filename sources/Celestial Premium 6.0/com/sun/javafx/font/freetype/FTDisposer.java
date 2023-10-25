/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.freetype;

import com.sun.javafx.font.DisposerRecord;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.font.freetype.OSFreetype;

class FTDisposer
implements DisposerRecord {
    long library;
    long face;

    FTDisposer(long l, long l2) {
        this.library = l;
        this.face = l2;
    }

    @Override
    public synchronized void dispose() {
        if (this.face != 0L) {
            OSFreetype.FT_Done_Face(this.face);
            if (PrismFontFactory.debugFonts) {
                System.err.println("Done Face=" + this.face);
            }
            this.face = 0L;
        }
        if (this.library != 0L) {
            OSFreetype.FT_Done_FreeType(this.library);
            if (PrismFontFactory.debugFonts) {
                System.err.println("Done Library=" + this.library);
            }
            this.library = 0L;
        }
    }
}

