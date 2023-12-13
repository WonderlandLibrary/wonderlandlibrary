package com.sun.javafx.font.coretext;

import com.sun.javafx.font.Disposer;
import com.sun.javafx.font.DisposerRecord;
import com.sun.javafx.font.FontStrikeDesc;
import com.sun.javafx.font.PrismFontFile;
import com.sun.javafx.font.PrismFontStrike;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.transform.BaseTransform;

class CTFontFile extends PrismFontFile {
   private final long cgFontRef;
   private static final CGAffineTransform tx = new CGAffineTransform();

   CTFontFile(String var1, String var2, int var3, boolean var4, boolean var5, boolean var6, boolean var7) throws Exception {
      super(var1, var2, var3, var4, var5, var6, var7);
      if (var5) {
         this.cgFontRef = this.createCGFontForEmbeddedFont();
         Disposer.addRecord(this, new SelfDisposerRecord(this.cgFontRef));
      } else {
         this.cgFontRef = 0L;
      }

   }

   public static boolean registerFont(String var0) {
      if (var0 == null) {
         return false;
      } else {
         long var1 = OS.kCFAllocatorDefault();
         boolean var3 = false;
         long var4 = OS.CFStringCreate(var0);
         if (var4 != 0L) {
            byte var6 = 0;
            long var7 = OS.CFURLCreateWithFileSystemPath(var1, var4, (long)var6, false);
            if (var7 != 0L) {
               byte var9 = 1;
               var3 = OS.CTFontManagerRegisterFontsForURL(var7, var9, 0L);
               OS.CFRelease(var7);
            }

            OS.CFRelease(var4);
         }

         return var3;
      }
   }

   private long createCGFontForEmbeddedFont() {
      long var1 = 0L;
      long var3 = OS.CFStringCreate(this.getFileName());
      if (var3 != 0L) {
         long var5 = OS.CFURLCreateWithFileSystemPath(OS.kCFAllocatorDefault(), var3, 0L, false);
         if (var5 != 0L) {
            long var7 = OS.CGDataProviderCreateWithURL(var5);
            if (var7 != 0L) {
               var1 = OS.CGFontCreateWithDataProvider(var7);
               OS.CFRelease(var7);
            }

            OS.CFRelease(var5);
         }

         OS.CFRelease(var3);
      }

      return var1;
   }

   long getCGFontRef() {
      return this.cgFontRef;
   }

   CGRect getBBox(int var1, float var2) {
      CTFontStrike var3 = (CTFontStrike)this.getStrike(var2, BaseTransform.IDENTITY_TRANSFORM);
      long var4 = var3.getFontRef();
      if (var4 == 0L) {
         return null;
      } else {
         long var6 = OS.CTFontCreatePathForGlyph(var4, (short)var1, tx);
         if (var6 == 0L) {
            return null;
         } else {
            CGRect var8 = OS.CGPathGetPathBoundingBox(var6);
            OS.CGPathRelease(var6);
            return var8;
         }
      }
   }

   Path2D getGlyphOutline(int var1, float var2) {
      CTFontStrike var3 = (CTFontStrike)this.getStrike(var2, BaseTransform.IDENTITY_TRANSFORM);
      long var4 = var3.getFontRef();
      if (var4 == 0L) {
         return null;
      } else {
         long var6 = OS.CTFontCreatePathForGlyph(var4, (short)var1, tx);
         if (var6 == 0L) {
            return null;
         } else {
            Path2D var8 = OS.CGPathApply(var6);
            OS.CGPathRelease(var6);
            return var8;
         }
      }
   }

   protected int[] createGlyphBoundingBox(int var1) {
      float var2 = 12.0F;
      CTFontStrike var3 = (CTFontStrike)this.getStrike(var2, BaseTransform.IDENTITY_TRANSFORM);
      long var4 = var3.getFontRef();
      if (var4 == 0L) {
         return null;
      } else {
         int[] var6 = new int[4];
         if (!this.isCFF()) {
            short var7 = this.getIndexToLocFormat();
            if (OS.CTFontGetBoundingRectForGlyphUsingTables(var4, (short)var1, var7, var6)) {
               return var6;
            }
         }

         long var11 = OS.CTFontCreatePathForGlyph(var4, (short)var1, (CGAffineTransform)null);
         if (var11 == 0L) {
            return null;
         } else {
            CGRect var9 = OS.CGPathGetPathBoundingBox(var11);
            OS.CGPathRelease(var11);
            float var10 = (float)this.getUnitsPerEm() / var2;
            var6[0] = (int)Math.round(var9.origin.x * (double)var10);
            var6[1] = (int)Math.round(var9.origin.y * (double)var10);
            var6[2] = (int)Math.round((var9.origin.x + var9.size.width) * (double)var10);
            var6[3] = (int)Math.round((var9.origin.y + var9.size.height) * (double)var10);
            return var6;
         }
      }
   }

   protected PrismFontStrike createStrike(float var1, BaseTransform var2, int var3, FontStrikeDesc var4) {
      return new CTFontStrike(this, var1, var2, var3, var4);
   }

   static {
      tx.a = 1.0;
      tx.d = -1.0;
   }

   private static class SelfDisposerRecord implements DisposerRecord {
      private long cgFontRef;

      SelfDisposerRecord(long var1) {
         this.cgFontRef = var1;
      }

      public synchronized void dispose() {
         if (this.cgFontRef != 0L) {
            OS.CFRelease(this.cgFontRef);
            this.cgFontRef = 0L;
         }

      }
   }
}
