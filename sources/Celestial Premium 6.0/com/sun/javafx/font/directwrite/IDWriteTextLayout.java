/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.directwrite;

import com.sun.javafx.font.directwrite.IUnknown;
import com.sun.javafx.font.directwrite.JFXTextRenderer;
import com.sun.javafx.font.directwrite.OS;

class IDWriteTextLayout
extends IUnknown {
    IDWriteTextLayout(long l) {
        super(l);
    }

    int Draw(long l, JFXTextRenderer jFXTextRenderer, float f, float f2) {
        return OS.Draw(this.ptr, l, jFXTextRenderer.ptr, f, f2);
    }
}

