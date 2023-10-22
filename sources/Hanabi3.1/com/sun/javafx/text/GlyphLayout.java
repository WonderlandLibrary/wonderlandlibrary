package com.sun.javafx.text;

import com.sun.javafx.font.FontResource;
import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.PGFont;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.scene.text.TextSpan;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.Bidi;

public abstract class GlyphLayout {
   public static final int CANONICAL_SUBSTITUTION = 1073741824;
   public static final int LAYOUT_LEFT_TO_RIGHT = 1;
   public static final int LAYOUT_RIGHT_TO_LEFT = 2;
   public static final int LAYOUT_NO_START_CONTEXT = 4;
   public static final int LAYOUT_NO_LIMIT_CONTEXT = 8;
   public static final int HINTING = 16;
   private static Method isIdeographicMethod = null;
   private static GlyphLayout reusableGL;
   private static boolean inUse;

   protected TextRun addTextRun(PrismTextLayout var1, char[] var2, int var3, int var4, PGFont var5, TextSpan var6, byte var7) {
      TextRun var8 = new TextRun(var3, var4, var7, true, 0, var6, 0, false);
      var1.addTextRun(var8);
      return var8;
   }

   private TextRun addTextRun(PrismTextLayout var1, char[] var2, int var3, int var4, PGFont var5, TextSpan var6, byte var7, boolean var8) {
      if (!var8 && (var7 & 1) == 0) {
         TextRun var9 = new TextRun(var3, var4, var7, false, 0, var6, 0, false);
         var1.addTextRun(var9);
         return var9;
      } else {
         return this.addTextRun(var1, var2, var3, var4, var5, var6, var7);
      }
   }

   public int breakRuns(PrismTextLayout var1, char[] var2, int var3) {
      int var4 = var2.length;
      boolean var5 = false;
      boolean var6 = false;
      int var7 = 0;
      int var8 = 0;
      boolean var9 = true;
      boolean var10 = true;
      if ((var3 & 2) != 0) {
         var9 = (var3 & 16) != 0;
         var10 = (var3 & 8) != 0;
      }

      TextRun var11 = null;
      Bidi var12 = null;
      byte var13 = 0;
      int var14 = var4;
      int var15 = 0;
      int var16 = 0;
      TextSpan var17 = null;
      int var18 = var4;
      PGFont var19 = null;
      TextSpan[] var20 = var1.getTextSpans();
      if (var20 != null) {
         if (var20.length > 0) {
            var17 = var20[var16];
            var18 = var17.getText().length();
            var19 = (PGFont)var17.getFont();
            if (var19 == null) {
               var3 |= 32;
            }
         }
      } else {
         var19 = var1.getFont();
      }

      int var22;
      if (var19 != null) {
         FontResource var21 = var19.getFontResource();
         var22 = var19.getFeatures();
         int var23 = var21.getFeatures();
         var6 = (var22 & var23) != 0;
      }

      int var32;
      if (var10 && var4 > 0) {
         var32 = var1.getDirection();
         var12 = new Bidi(var2, 0, (byte[])null, 0, var4, var32);
         var13 = (byte)var12.getLevelAt(var12.getRunStart(var15));
         var14 = var12.getRunLimit(var15);
         if ((var13 & 1) != 0) {
            var3 |= 24;
         }
      }

      var32 = 0;
      var22 = 0;

      while(var22 < var4) {
         char var33 = var2[var22];
         int var24 = var33;
         boolean var25 = var33 == '\t' || var33 == '\n' || var33 == '\r';
         if (var25 && var22 != var32) {
            var11 = this.addTextRun(var1, var2, var32, var22 - var32, var19, var17, var13, var5);
            if (var5) {
               var3 |= 16;
               var5 = false;
            }

            var32 = var22;
         }

         boolean var26 = var22 >= var18 && var22 < var4;
         boolean var27 = var22 >= var14 && var22 < var4;
         boolean var28 = false;
         if (!var25) {
            if (var9) {
               if (Character.isHighSurrogate(var33) && var22 + 1 < var18 && Character.isLowSurrogate(var2[var22 + 1])) {
                  ++var22;
                  var24 = Character.toCodePoint(var33, var2[var22]);
               }

               if (isIdeographic(var24)) {
                  var3 |= 64;
               }

               var8 = ScriptMapper.getScript(var24);
               if (var7 > 1 && var8 > 1 && var8 != var7) {
                  var28 = true;
               }

               if (!var5) {
                  var5 = var6 || ScriptMapper.isComplexCharCode(var24);
               }
            }

            if ((var26 || var27 || var28) && var32 != var22) {
               var11 = this.addTextRun(var1, var2, var32, var22 - var32, var19, var17, var13, var5);
               if (var5) {
                  var3 |= 16;
                  var5 = false;
               }

               var32 = var22;
            }

            ++var22;
         }

         if (var26) {
            ++var16;
            var17 = var20[var16];
            var18 += var17.getText().length();
            var19 = (PGFont)var17.getFont();
            if (var19 == null) {
               var3 |= 32;
            } else {
               FontResource var29 = var19.getFontResource();
               int var30 = var19.getFeatures();
               int var31 = var29.getFeatures();
               var6 = (var30 & var31) != 0;
            }
         }

         if (var27) {
            ++var15;
            var13 = (byte)var12.getLevelAt(var12.getRunStart(var15));
            var14 = var12.getRunLimit(var15);
            if ((var13 & 1) != 0) {
               var3 |= 24;
            }
         }

         if (var28) {
            var7 = var8;
         }

         if (var25) {
            ++var22;
            if (var33 == '\r' && var22 < var18 && var2[var22] == '\n') {
               ++var22;
            }

            var11 = new TextRun(var32, var22 - var32, var13, false, 0, var17, 0, false);
            if (var33 == '\t') {
               var11.setTab();
               var3 |= 4;
            } else {
               var11.setLinebreak();
            }

            var1.addTextRun(var11);
            var32 = var22;
         }
      }

      if (var32 < var4) {
         this.addTextRun(var1, var2, var32, var4 - var32, var19, var17, var13, var5);
         if (var5) {
            var3 |= 16;
         }
      } else if (var11 == null || var11.isLinebreak()) {
         var11 = new TextRun(var32, 0, (byte)0, false, 0, var17, 0, false);
         var1.addTextRun(var11);
      }

      if (var12 != null && !var12.baseIsLeftToRight()) {
         var3 |= 256;
      }

      var3 |= 2;
      return var3;
   }

   public abstract void layout(TextRun var1, PGFont var2, FontStrike var3, char[] var4);

   protected int getInitialSlot(FontResource var1) {
      if (PrismFontFactory.isJreFont(var1)) {
         if (PrismFontFactory.debugFonts) {
            System.err.println("Avoiding JRE Font: " + var1.getFullName());
         }

         return 1;
      } else {
         return 0;
      }
   }

   private static GlyphLayout newInstance() {
      PrismFontFactory var0 = PrismFontFactory.getFontFactory();
      return var0.createGlyphLayout();
   }

   public static GlyphLayout getInstance() {
      if (inUse) {
         return newInstance();
      } else {
         Class var0 = GlyphLayout.class;
         synchronized(GlyphLayout.class) {
            if (inUse) {
               return newInstance();
            } else {
               inUse = true;
               return reusableGL;
            }
         }
      }
   }

   public void dispose() {
      if (this == reusableGL) {
         inUse = false;
      }

   }

   private static boolean isIdeographic(int var0) {
      if (isIdeographicMethod != null) {
         try {
            return (Boolean)isIdeographicMethod.invoke((Object)null, var0);
         } catch (InvocationTargetException | IllegalAccessException var2) {
            return false;
         }
      } else {
         return false;
      }
   }

   static {
      try {
         isIdeographicMethod = Character.class.getMethod("isIdeographic", Integer.TYPE);
      } catch (SecurityException | NoSuchMethodException var1) {
         isIdeographicMethod = null;
      }

      reusableGL = newInstance();
   }
}
