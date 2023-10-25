/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.directwrite;

import com.sun.javafx.font.directwrite.IDWriteFontFace;
import com.sun.javafx.font.directwrite.IUnknown;
import com.sun.javafx.font.directwrite.OS;

class JFXTextRenderer
extends IUnknown {
    JFXTextRenderer(long l) {
        super(l);
    }

    boolean Next() {
        return OS.JFXTextRendererNext(this.ptr);
    }

    int GetStart() {
        return OS.JFXTextRendererGetStart(this.ptr);
    }

    int GetLength() {
        return OS.JFXTextRendererGetLength(this.ptr);
    }

    int GetGlyphCount() {
        return OS.JFXTextRendererGetGlyphCount(this.ptr);
    }

    int GetTotalGlyphCount() {
        return OS.JFXTextRendererGetTotalGlyphCount(this.ptr);
    }

    IDWriteFontFace GetFontFace() {
        long l = OS.JFXTextRendererGetFontFace(this.ptr);
        return l != 0L ? new IDWriteFontFace(l) : null;
    }

    int GetGlyphIndices(int[] arrn, int n, int n2) {
        return OS.JFXTextRendererGetGlyphIndices(this.ptr, arrn, n, n2);
    }

    int GetGlyphAdvances(float[] arrf, int n) {
        return OS.JFXTextRendererGetGlyphAdvances(this.ptr, arrf, n);
    }

    int GetGlyphOffsets(float[] arrf, int n) {
        return OS.JFXTextRendererGetGlyphOffsets(this.ptr, arrf, n);
    }

    int GetClusterMap(short[] arrs, int n, int n2) {
        return OS.JFXTextRendererGetClusterMap(this.ptr, arrs, n, n2);
    }
}

