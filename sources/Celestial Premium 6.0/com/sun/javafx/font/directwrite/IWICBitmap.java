/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.directwrite;

import com.sun.javafx.font.directwrite.IUnknown;
import com.sun.javafx.font.directwrite.IWICBitmapLock;
import com.sun.javafx.font.directwrite.OS;

class IWICBitmap
extends IUnknown {
    IWICBitmap(long l) {
        super(l);
    }

    IWICBitmapLock Lock(int n, int n2, int n3, int n4, int n5) {
        long l = OS.Lock(this.ptr, n, n2, n3, n4, n5);
        return l != 0L ? new IWICBitmapLock(l) : null;
    }
}

