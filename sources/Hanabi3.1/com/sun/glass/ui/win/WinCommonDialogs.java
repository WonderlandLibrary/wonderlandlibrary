package com.sun.glass.ui.win;

import com.sun.glass.ui.CommonDialogs;
import com.sun.glass.ui.Window;
import java.io.File;

final class WinCommonDialogs {
   private static native void _initIDs();

   private static native CommonDialogs.FileChooserResult _showFileChooser(long var0, String var2, String var3, String var4, int var5, boolean var6, CommonDialogs.ExtensionFilter[] var7, int var8);

   private static native String _showFolderChooser(long var0, String var2, String var3);

   static CommonDialogs.FileChooserResult showFileChooser_impl(Window var0, String var1, String var2, String var3, int var4, boolean var5, CommonDialogs.ExtensionFilter[] var6, int var7) {
      if (var0 != null) {
         ((WinWindow)var0).setDeferredClosing(true);
      }

      CommonDialogs.FileChooserResult var8;
      try {
         var8 = _showFileChooser(var0 != null ? var0.getNativeWindow() : 0L, var1, var2, var3, var4, var5, var6, var7);
      } finally {
         if (var0 != null) {
            ((WinWindow)var0).setDeferredClosing(false);
         }

      }

      return var8;
   }

   static File showFolderChooser_impl(Window var0, String var1, String var2) {
      if (var0 != null) {
         ((WinWindow)var0).setDeferredClosing(true);
      }

      File var4;
      try {
         String var3 = _showFolderChooser(var0 != null ? var0.getNativeWindow() : 0L, var1, var2);
         var4 = var3 != null ? new File(var3) : null;
      } finally {
         if (var0 != null) {
            ((WinWindow)var0).setDeferredClosing(false);
         }

      }

      return var4;
   }

   static {
      _initIDs();
   }
}
