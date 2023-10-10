package org.lwjgl.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.opengl.DisplayMode;

public final class Display {
   private static final boolean DEBUG = false;
   static final boolean $assertionsDisabled = !Display.class.desiredAssertionStatus();

   public static DisplayMode[] getAvailableDisplayModes(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) throws LWJGLException {
      DisplayMode[] var8 = org.lwjgl.opengl.Display.getAvailableDisplayModes();
      int var10;
      if (LWJGLUtil.DEBUG) {
         System.out.println("Available screen modes:");
         DisplayMode[] var9 = var8;
         var10 = var8.length;

         for(int var11 = 0; var11 < var10; ++var11) {
            DisplayMode var12 = var9[var11];
            System.out.println(var12);
         }
      }

      ArrayList var13 = new ArrayList(var8.length);

      for(var10 = 0; var10 < var8.length; ++var10) {
         if (!$assertionsDisabled && var8[var10] == null) {
            throw new AssertionError("" + var10 + " " + var8.length);
         }

         if ((var0 == -1 || var8[var10].getWidth() >= var0) && (var2 == -1 || var8[var10].getWidth() <= var2) && (var1 == -1 || var8[var10].getHeight() >= var1) && (var3 == -1 || var8[var10].getHeight() <= var3) && (var4 == -1 || var8[var10].getBitsPerPixel() >= var4) && (var5 == -1 || var8[var10].getBitsPerPixel() <= var5) && (var8[var10].getFrequency() == 0 || (var6 == -1 || var8[var10].getFrequency() >= var6) && (var7 == -1 || var8[var10].getFrequency() <= var7))) {
            var13.add(var8[var10]);
         }
      }

      DisplayMode[] var14 = new DisplayMode[var13.size()];
      var13.toArray(var14);
      if (LWJGLUtil.DEBUG) {
      }

      return var14;
   }

   public static DisplayMode setDisplayMode(DisplayMode[] var0, String[] var1) throws Exception {
      class Sorter implements Comparator {
         final FieldAccessor[] accessors;
         final String[] val$param;

         Sorter(String[] var1) {
            this.val$param = var1;

            class FieldAccessor {
               final String fieldName;
               final int order;
               final int preferred;
               final boolean usePreferred;

               FieldAccessor(String var1, int var2, int var3, boolean var4) {
                  this.fieldName = var1;
                  this.order = var2;
                  this.preferred = var3;
                  this.usePreferred = var4;
               }

               int getInt(DisplayMode var1) {
                  if ("width".equals(this.fieldName)) {
                     return var1.getWidth();
                  } else if ("height".equals(this.fieldName)) {
                     return var1.getHeight();
                  } else if ("freq".equals(this.fieldName)) {
                     return var1.getFrequency();
                  } else if ("bpp".equals(this.fieldName)) {
                     return var1.getBitsPerPixel();
                  } else {
                     throw new IllegalArgumentException("Unknown field " + this.fieldName);
                  }
               }
            }

            this.accessors = new FieldAccessor[this.val$param.length];

            for(int var2 = 0; var2 < this.accessors.length; ++var2) {
               int var3 = this.val$param[var2].indexOf(61);
               if (var3 > 0) {
                  this.accessors[var2] = new FieldAccessor(this.val$param[var2].substring(0, var3), 0, Integer.parseInt(this.val$param[var2].substring(var3 + 1, this.val$param[var2].length())), true);
               } else if (this.val$param[var2].charAt(0) == '-') {
                  this.accessors[var2] = new FieldAccessor(this.val$param[var2].substring(1), -1, 0, false);
               } else {
                  this.accessors[var2] = new FieldAccessor(this.val$param[var2], 1, 0, false);
               }
            }

         }

         public int compare(DisplayMode var1, DisplayMode var2) {
            FieldAccessor[] var3 = this.accessors;
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               FieldAccessor var6 = var3[var5];
               int var7 = var6.getInt(var1);
               int var8 = var6.getInt(var2);
               if (var6.usePreferred && var7 != var8) {
                  if (var7 == var6.preferred) {
                     return -1;
                  }

                  if (var8 == var6.preferred) {
                     return 1;
                  }

                  int var9 = Math.abs(var7 - var6.preferred);
                  int var10 = Math.abs(var8 - var6.preferred);
                  if (var9 < var10) {
                     return -1;
                  }

                  if (var9 > var10) {
                     return 1;
                  }
               } else {
                  if (var7 < var8) {
                     return var6.order;
                  }

                  if (var7 != var8) {
                     return -var6.order;
                  }
               }
            }

            return 0;
         }

         public int compare(Object var1, Object var2) {
            return this.compare((DisplayMode)var1, (DisplayMode)var2);
         }
      }

      Arrays.sort(var0, new Sorter(var1));
      DisplayMode[] var2;
      int var3;
      int var4;
      DisplayMode var5;
      if (LWJGLUtil.DEBUG) {
         System.out.println("Sorted display modes:");
         var2 = var0;
         var3 = var0.length;

         for(var4 = 0; var4 < var3; ++var4) {
            var5 = var2[var4];
            System.out.println(var5);
         }
      }

      var2 = var0;
      var3 = var0.length;
      var4 = 0;

      while(var4 < var3) {
         var5 = var2[var4];

         try {
            if (LWJGLUtil.DEBUG) {
               System.out.println("Attempting to set displaymode: " + var5);
            }

            org.lwjgl.opengl.Display.setDisplayMode(var5);
            return var5;
         } catch (Exception var7) {
            if (LWJGLUtil.DEBUG) {
               System.out.println("Failed to set display mode to " + var5);
               var7.printStackTrace();
            }

            ++var4;
         }
      }

      throw new Exception("Failed to set display mode.");
   }
}
