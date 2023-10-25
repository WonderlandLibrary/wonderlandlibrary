/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.directwrite;

import com.sun.javafx.font.DisposerRecord;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.font.directwrite.IUnknown;

class DWDisposer
implements DisposerRecord {
    IUnknown resource;

    DWDisposer(IUnknown iUnknown) {
        this.resource = iUnknown;
    }

    @Override
    public synchronized void dispose() {
        if (this.resource != null) {
            this.resource.Release();
            if (PrismFontFactory.debugFonts) {
                System.err.println("DisposerRecord=" + this.resource);
            }
            this.resource = null;
        }
    }
}

