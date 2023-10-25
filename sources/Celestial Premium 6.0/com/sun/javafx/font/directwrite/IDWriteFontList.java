/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.directwrite;

import com.sun.javafx.font.directwrite.IDWriteFont;
import com.sun.javafx.font.directwrite.IUnknown;
import com.sun.javafx.font.directwrite.OS;

class IDWriteFontList
extends IUnknown {
    IDWriteFontList(long l) {
        super(l);
    }

    int GetFontCount() {
        return OS.GetFontCount(this.ptr);
    }

    IDWriteFont GetFont(int n) {
        long l = OS.GetFont(this.ptr, n);
        return l != 0L ? new IDWriteFont(l) : null;
    }
}

