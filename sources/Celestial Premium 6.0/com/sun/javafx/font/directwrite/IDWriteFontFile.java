/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.directwrite;

import com.sun.javafx.font.directwrite.IUnknown;
import com.sun.javafx.font.directwrite.OS;

class IDWriteFontFile
extends IUnknown {
    IDWriteFontFile(long l) {
        super(l);
    }

    int Analyze(boolean[] arrbl, int[] arrn, int[] arrn2, int[] arrn3) {
        return OS.Analyze(this.ptr, arrbl, arrn, arrn2, arrn3);
    }
}

