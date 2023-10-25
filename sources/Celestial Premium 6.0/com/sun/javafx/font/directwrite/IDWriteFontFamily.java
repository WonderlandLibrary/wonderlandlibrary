/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.directwrite;

import com.sun.javafx.font.directwrite.IDWriteFont;
import com.sun.javafx.font.directwrite.IDWriteFontList;
import com.sun.javafx.font.directwrite.IDWriteLocalizedStrings;
import com.sun.javafx.font.directwrite.OS;

class IDWriteFontFamily
extends IDWriteFontList {
    IDWriteFontFamily(long l) {
        super(l);
    }

    IDWriteLocalizedStrings GetFamilyNames() {
        long l = OS.GetFamilyNames(this.ptr);
        return l != 0L ? new IDWriteLocalizedStrings(l) : null;
    }

    IDWriteFont GetFirstMatchingFont(int n, int n2, int n3) {
        long l = OS.GetFirstMatchingFont(this.ptr, n, n2, n3);
        return l != 0L ? new IDWriteFont(l) : null;
    }
}

