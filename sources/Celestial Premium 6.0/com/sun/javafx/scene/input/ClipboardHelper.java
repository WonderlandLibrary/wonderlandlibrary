/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.input.Clipboard
 */
package com.sun.javafx.scene.input;

import com.sun.javafx.util.Utils;
import javafx.scene.input.Clipboard;

public class ClipboardHelper {
    private static ClipboardAccessor clipboardAccessor;

    private ClipboardHelper() {
    }

    public static boolean contentPut(Clipboard clipboard) {
        return clipboardAccessor.contentPut(clipboard);
    }

    public static void setClipboardAccessor(ClipboardAccessor clipboardAccessor) {
        if (ClipboardHelper.clipboardAccessor != null) {
            throw new IllegalStateException();
        }
        ClipboardHelper.clipboardAccessor = clipboardAccessor;
    }

    static {
        Utils.forceInit(Clipboard.class);
    }

    public static interface ClipboardAccessor {
        public boolean contentPut(Clipboard var1);
    }
}

