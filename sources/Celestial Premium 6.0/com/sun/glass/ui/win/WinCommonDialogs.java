/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui.win;

import com.sun.glass.ui.CommonDialogs;
import com.sun.glass.ui.Window;
import com.sun.glass.ui.win.WinWindow;
import java.io.File;

final class WinCommonDialogs {
    WinCommonDialogs() {
    }

    private static native void _initIDs();

    private static native CommonDialogs.FileChooserResult _showFileChooser(long var0, String var2, String var3, String var4, int var5, boolean var6, CommonDialogs.ExtensionFilter[] var7, int var8);

    private static native String _showFolderChooser(long var0, String var2, String var3);

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    static CommonDialogs.FileChooserResult showFileChooser_impl(Window window, String string, String string2, String string3, int n, boolean bl, CommonDialogs.ExtensionFilter[] arrextensionFilter, int n2) {
        if (window != null) {
            ((WinWindow)window).setDeferredClosing(true);
        }
        try {
            CommonDialogs.FileChooserResult fileChooserResult = WinCommonDialogs._showFileChooser(window != null ? window.getNativeWindow() : 0L, string, string2, string3, n, bl, arrextensionFilter, n2);
            return fileChooserResult;
        }
        finally {
            if (window != null) {
                ((WinWindow)window).setDeferredClosing(false);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    static File showFolderChooser_impl(Window window, String string, String string2) {
        if (window != null) {
            ((WinWindow)window).setDeferredClosing(true);
        }
        try {
            String string3 = WinCommonDialogs._showFolderChooser(window != null ? window.getNativeWindow() : 0L, string, string2);
            File file = string3 != null ? new File(string3) : null;
            return file;
        }
        finally {
            if (window != null) {
                ((WinWindow)window).setDeferredClosing(false);
            }
        }
    }

    static {
        WinCommonDialogs._initIDs();
    }
}

