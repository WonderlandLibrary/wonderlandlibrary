/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.directwrite;

import com.sun.javafx.font.directwrite.IUnknown;
import com.sun.javafx.font.directwrite.OS;
import com.sun.javafx.font.directwrite.RECT;

class IDWriteGlyphRunAnalysis
extends IUnknown {
    IDWriteGlyphRunAnalysis(long l) {
        super(l);
    }

    byte[] CreateAlphaTexture(int n, RECT rECT) {
        return OS.CreateAlphaTexture(this.ptr, n, rECT);
    }

    RECT GetAlphaTextureBounds(int n) {
        return OS.GetAlphaTextureBounds(this.ptr, n);
    }
}

