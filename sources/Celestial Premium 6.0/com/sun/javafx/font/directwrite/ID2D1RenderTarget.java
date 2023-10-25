/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.directwrite;

import com.sun.javafx.font.directwrite.D2D1_COLOR_F;
import com.sun.javafx.font.directwrite.D2D1_MATRIX_3X2_F;
import com.sun.javafx.font.directwrite.D2D1_POINT_2F;
import com.sun.javafx.font.directwrite.DWRITE_GLYPH_RUN;
import com.sun.javafx.font.directwrite.ID2D1Brush;
import com.sun.javafx.font.directwrite.IUnknown;
import com.sun.javafx.font.directwrite.OS;

class ID2D1RenderTarget
extends IUnknown {
    ID2D1RenderTarget(long l) {
        super(l);
    }

    void BeginDraw() {
        OS.BeginDraw(this.ptr);
    }

    int EndDraw() {
        return OS.EndDraw(this.ptr);
    }

    void Clear(D2D1_COLOR_F d2D1_COLOR_F) {
        OS.Clear(this.ptr, d2D1_COLOR_F);
    }

    void SetTransform(D2D1_MATRIX_3X2_F d2D1_MATRIX_3X2_F) {
        OS.SetTransform(this.ptr, d2D1_MATRIX_3X2_F);
    }

    void SetTextAntialiasMode(int n) {
        OS.SetTextAntialiasMode(this.ptr, n);
    }

    void DrawGlyphRun(D2D1_POINT_2F d2D1_POINT_2F, DWRITE_GLYPH_RUN dWRITE_GLYPH_RUN, ID2D1Brush iD2D1Brush, int n) {
        OS.DrawGlyphRun(this.ptr, d2D1_POINT_2F, dWRITE_GLYPH_RUN, iD2D1Brush.ptr, n);
    }

    ID2D1Brush CreateSolidColorBrush(D2D1_COLOR_F d2D1_COLOR_F) {
        long l = OS.CreateSolidColorBrush(this.ptr, d2D1_COLOR_F);
        return l != 0L ? new ID2D1Brush(l) : null;
    }
}

