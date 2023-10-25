/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.directwrite;

import com.sun.javafx.font.directwrite.DWRITE_SCRIPT_ANALYSIS;
import com.sun.javafx.font.directwrite.IUnknown;
import com.sun.javafx.font.directwrite.OS;

class JFXTextAnalysisSink
extends IUnknown {
    JFXTextAnalysisSink(long l) {
        super(l);
    }

    boolean Next() {
        return OS.Next(this.ptr);
    }

    int GetStart() {
        return OS.GetStart(this.ptr);
    }

    int GetLength() {
        return OS.GetLength(this.ptr);
    }

    DWRITE_SCRIPT_ANALYSIS GetAnalysis() {
        return OS.GetAnalysis(this.ptr);
    }
}

