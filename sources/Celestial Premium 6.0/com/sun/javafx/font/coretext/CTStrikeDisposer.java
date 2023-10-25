/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.coretext;

import com.sun.javafx.font.DisposerRecord;
import com.sun.javafx.font.FontResource;
import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.FontStrikeDesc;
import com.sun.javafx.font.coretext.OS;
import java.lang.ref.WeakReference;

class CTStrikeDisposer
implements DisposerRecord {
    private FontResource fontResource;
    private FontStrikeDesc desc;
    private long fontRef = 0L;
    private boolean disposed = false;

    public CTStrikeDisposer(FontResource fontResource, FontStrikeDesc fontStrikeDesc, long l) {
        this.fontResource = fontResource;
        this.desc = fontStrikeDesc;
        this.fontRef = l;
    }

    @Override
    public synchronized void dispose() {
        if (!this.disposed) {
            Object t;
            WeakReference<FontStrike> weakReference = this.fontResource.getStrikeMap().get(this.desc);
            if (weakReference != null && (t = weakReference.get()) == null) {
                this.fontResource.getStrikeMap().remove(this.desc);
            }
            if (this.fontRef != 0L) {
                OS.CFRelease(this.fontRef);
                this.fontRef = 0L;
            }
            this.disposed = true;
        }
    }
}

