/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.directwrite;

import com.sun.javafx.font.directwrite.DWRITE_GLYPH_RUN;
import com.sun.javafx.font.directwrite.DWRITE_MATRIX;
import com.sun.javafx.font.directwrite.IDWriteFontCollection;
import com.sun.javafx.font.directwrite.IDWriteFontFace;
import com.sun.javafx.font.directwrite.IDWriteFontFile;
import com.sun.javafx.font.directwrite.IDWriteGlyphRunAnalysis;
import com.sun.javafx.font.directwrite.IDWriteTextAnalyzer;
import com.sun.javafx.font.directwrite.IDWriteTextFormat;
import com.sun.javafx.font.directwrite.IDWriteTextLayout;
import com.sun.javafx.font.directwrite.IUnknown;
import com.sun.javafx.font.directwrite.OS;

class IDWriteFactory
extends IUnknown {
    IDWriteFactory(long l) {
        super(l);
    }

    IDWriteFontCollection GetSystemFontCollection(boolean bl) {
        long l = OS.GetSystemFontCollection(this.ptr, bl);
        return l != 0L ? new IDWriteFontCollection(l) : null;
    }

    IDWriteTextAnalyzer CreateTextAnalyzer() {
        long l = OS.CreateTextAnalyzer(this.ptr);
        return l != 0L ? new IDWriteTextAnalyzer(l) : null;
    }

    IDWriteTextFormat CreateTextFormat(String string, IDWriteFontCollection iDWriteFontCollection, int n, int n2, int n3, float f, String string2) {
        long l = OS.CreateTextFormat(this.ptr, (string + "\u0000").toCharArray(), iDWriteFontCollection.ptr, n, n2, n3, f, (string2 + "\u0000").toCharArray());
        return l != 0L ? new IDWriteTextFormat(l) : null;
    }

    IDWriteTextLayout CreateTextLayout(char[] arrc, int n, int n2, IDWriteTextFormat iDWriteTextFormat, float f, float f2) {
        long l = OS.CreateTextLayout(this.ptr, arrc, n, n2, iDWriteTextFormat.ptr, f, f2);
        return l != 0L ? new IDWriteTextLayout(l) : null;
    }

    IDWriteGlyphRunAnalysis CreateGlyphRunAnalysis(DWRITE_GLYPH_RUN dWRITE_GLYPH_RUN, float f, DWRITE_MATRIX dWRITE_MATRIX, int n, int n2, float f2, float f3) {
        long l = OS.CreateGlyphRunAnalysis(this.ptr, dWRITE_GLYPH_RUN, f, dWRITE_MATRIX, n, n2, f2, f3);
        return l != 0L ? new IDWriteGlyphRunAnalysis(l) : null;
    }

    IDWriteFontFile CreateFontFileReference(String string) {
        long l = OS.CreateFontFileReference(this.ptr, (string + "\u0000").toCharArray());
        return l != 0L ? new IDWriteFontFile(l) : null;
    }

    IDWriteFontFace CreateFontFace(int n, IDWriteFontFile iDWriteFontFile, int n2, int n3) {
        long l = OS.CreateFontFace(this.ptr, n, iDWriteFontFile.ptr, n2, n3);
        return l != 0L ? new IDWriteFontFace(l) : null;
    }
}

