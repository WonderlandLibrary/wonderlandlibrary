/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.directwrite;

import com.sun.javafx.font.directwrite.IUnknown;
import com.sun.javafx.font.directwrite.IWICBitmap;
import com.sun.javafx.font.directwrite.OS;

class IWICImagingFactory
extends IUnknown {
    IWICImagingFactory(long l) {
        super(l);
    }

    IWICBitmap CreateBitmap(int n, int n2, int n3, int n4) {
        long l = OS.CreateBitmap(this.ptr, n, n2, n3, n4);
        return l != 0L ? new IWICBitmap(l) : null;
    }
}

