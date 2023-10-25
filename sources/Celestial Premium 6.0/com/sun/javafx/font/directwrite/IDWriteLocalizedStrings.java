/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.directwrite;

import com.sun.javafx.font.directwrite.IUnknown;
import com.sun.javafx.font.directwrite.OS;

class IDWriteLocalizedStrings
extends IUnknown {
    IDWriteLocalizedStrings(long l) {
        super(l);
    }

    int FindLocaleName(String string) {
        return OS.FindLocaleName(this.ptr, (string + "\u0000").toCharArray());
    }

    int GetStringLength(int n) {
        return OS.GetStringLength(this.ptr, n);
    }

    String GetString(int n, int n2) {
        char[] arrc = OS.GetString(this.ptr, n, n2 + 1);
        return arrc != null ? new String(arrc, 0, n2) : null;
    }
}

