package com.sun.javafx.font;

import com.sun.glass.utils.NativeLibLoader;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

class MacFontFinder {
   private static final int SystemFontType = 2;
   private static final int MonospacedFontType = 1;

   private static native String getFont(int var0);

   public static String getSystemFont() {
      return getFont(2);
   }

   public static String getMonospacedFont() {
      return getFont(1);
   }

   static native float getSystemFontSize();

   public static boolean populateFontFileNameMap(HashMap var0, HashMap var1, HashMap var2, Locale var3) {
      if (var0 != null && var1 != null && var2 != null) {
         if (var3 == null) {
            var3 = Locale.ENGLISH;
         }

         String[] var4 = getFontData();
         if (var4 == null) {
            return false;
         } else {
            String var6;
            ArrayList var11;
            for(int var5 = 0; var5 < var4.length; var11.add(var6)) {
               var6 = var4[var5++];
               String var7 = var4[var5++];
               String var8 = var4[var5++];
               if (PrismFontFactory.debugFonts) {
                  System.err.println("[MacFontFinder] Name=" + var6);
                  System.err.println("\tFamily=" + var7);
                  System.err.println("\tFile=" + var8);
               }

               String var9 = var6.toLowerCase(var3);
               String var10 = var7.toLowerCase(var3);
               var0.put(var9, var8);
               var1.put(var9, var7);
               var11 = (ArrayList)var2.get(var10);
               if (var11 == null) {
                  var11 = new ArrayList();
                  var2.put(var10, var11);
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }

   private static native String[] getFontData();

   static {
      AccessController.doPrivileged(() -> {
         NativeLibLoader.loadLibrary("javafx_font");
         return null;
      });
   }
}
