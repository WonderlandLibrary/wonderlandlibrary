/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.directwrite;

import com.sun.javafx.font.directwrite.IDWriteFontFace;
import com.sun.javafx.font.directwrite.IDWriteFontFamily;
import com.sun.javafx.font.directwrite.IDWriteLocalizedStrings;
import com.sun.javafx.font.directwrite.IUnknown;
import com.sun.javafx.font.directwrite.OS;

class IDWriteFont
extends IUnknown {
    IDWriteFont(long l) {
        super(l);
    }

    IDWriteFontFace CreateFontFace() {
        long l = OS.CreateFontFace(this.ptr);
        return l != 0L ? new IDWriteFontFace(l) : null;
    }

    IDWriteLocalizedStrings GetFaceNames() {
        long l = OS.GetFaceNames(this.ptr);
        return l != 0L ? new IDWriteLocalizedStrings(l) : null;
    }

    IDWriteFontFamily GetFontFamily() {
        long l = OS.GetFontFamily(this.ptr);
        return l != 0L ? new IDWriteFontFamily(l) : null;
    }

    IDWriteLocalizedStrings GetInformationalStrings(int n) {
        long l = OS.GetInformationalStrings(this.ptr, n);
        return l != 0L ? new IDWriteLocalizedStrings(l) : null;
    }

    int GetSimulations() {
        return OS.GetSimulations(this.ptr);
    }

    int GetStretch() {
        return OS.GetStretch(this.ptr);
    }

    int GetStyle() {
        return OS.GetStyle(this.ptr);
    }

    int GetWeight() {
        return OS.GetWeight(this.ptr);
    }
}

