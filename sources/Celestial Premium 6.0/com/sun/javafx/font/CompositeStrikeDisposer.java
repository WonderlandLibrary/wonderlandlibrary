/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font;

import com.sun.javafx.font.DisposerRecord;
import com.sun.javafx.font.FontResource;
import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.FontStrikeDesc;
import java.lang.ref.WeakReference;

class CompositeStrikeDisposer
implements DisposerRecord {
    FontResource fontResource;
    FontStrikeDesc desc;
    boolean disposed = false;

    public CompositeStrikeDisposer(FontResource fontResource, FontStrikeDesc fontStrikeDesc) {
        this.fontResource = fontResource;
        this.desc = fontStrikeDesc;
    }

    @Override
    public synchronized void dispose() {
        if (!this.disposed) {
            Object t;
            WeakReference<FontStrike> weakReference = this.fontResource.getStrikeMap().get(this.desc);
            if (weakReference != null && (t = weakReference.get()) == null) {
                this.fontResource.getStrikeMap().remove(this.desc);
            }
            this.disposed = true;
        }
    }
}

