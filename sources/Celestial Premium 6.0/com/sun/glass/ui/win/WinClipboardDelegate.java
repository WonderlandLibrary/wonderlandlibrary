/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui.win;

import com.sun.glass.ui.Clipboard;
import com.sun.glass.ui.delegate.ClipboardDelegate;
import com.sun.glass.ui.win.WinDnDClipboard;
import com.sun.glass.ui.win.WinSystemClipboard;

final class WinClipboardDelegate
implements ClipboardDelegate {
    WinClipboardDelegate() {
    }

    @Override
    public Clipboard createClipboard(String string) {
        if ("SYSTEM".equals(string)) {
            return new WinSystemClipboard(string);
        }
        if ("DND".equals(string)) {
            return new WinDnDClipboard(string);
        }
        return null;
    }
}

