/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.directwrite;

import com.sun.javafx.font.directwrite.IDWriteFont;
import com.sun.javafx.font.directwrite.IDWriteFontFace;
import com.sun.javafx.font.directwrite.IDWriteFontFamily;
import com.sun.javafx.font.directwrite.IUnknown;
import com.sun.javafx.font.directwrite.OS;

class IDWriteFontCollection
extends IUnknown {
    IDWriteFontCollection(long l) {
        super(l);
    }

    int GetFontFamilyCount() {
        return OS.GetFontFamilyCount(this.ptr);
    }

    IDWriteFontFamily GetFontFamily(int n) {
        long l = OS.GetFontFamily(this.ptr, n);
        return l != 0L ? new IDWriteFontFamily(l) : null;
    }

    int FindFamilyName(String string) {
        return OS.FindFamilyName(this.ptr, (string + "\u0000").toCharArray());
    }

    IDWriteFont GetFontFromFontFace(IDWriteFontFace iDWriteFontFace) {
        long l = OS.GetFontFromFontFace(this.ptr, iDWriteFontFace.ptr);
        return l != 0L ? new IDWriteFont(l) : null;
    }
}

