package com.sun.javafx.font.coretext;

import com.sun.glass.utils.NativeLibLoader;
import com.sun.javafx.geom.Path2D;
import java.nio.ByteOrder;
import java.security.AccessController;

class OS {
   static final int kCFURLPOSIXPathStyle = 0;
   static final int kCTFontOrientationDefault = 0;
   static final int kCTFontManagerScopeProcess = 1;
   static final int kCGBitmapByteOrder32Big = 16384;
   static final int kCGBitmapByteOrder32Little = 8192;
   static final int kCGBitmapByteOrder32Host;
   static final int kCGImageAlphaPremultipliedFirst = 2;
   static final int kCGImageAlphaNone = 0;
   static final int kCTWritingDirectionRightToLeft = 1;

   static final long CFStringCreate(String var0) {
      char[] var1 = var0.toCharArray();
      long var2 = kCFAllocatorDefault();
      return CFStringCreateWithCharacters(var2, var1, (long)var1.length);
   }

   static final native byte[] CGBitmapContextGetData(long var0, int var2, int var3, int var4);

   static final native void CGRectApplyAffineTransform(CGRect var0, CGAffineTransform var1);

   static final native Path2D CGPathApply(long var0);

   static final native CGRect CGPathGetPathBoundingBox(long var0);

   static final native long CFStringCreateWithCharacters(long var0, char[] var2, long var3, long var5);

   static final native String CTFontCopyAttributeDisplayName(long var0);

   static final native void CTFontDrawGlyphs(long var0, short var2, double var3, double var5, long var7);

   static final native double CTFontGetAdvancesForGlyphs(long var0, int var2, short var3, CGSize var4);

   static final native boolean CTFontGetBoundingRectForGlyphUsingTables(long var0, short var2, short var3, int[] var4);

   static final native int CTRunGetGlyphs(long var0, int var2, int var3, int[] var4);

   static final native int CTRunGetStringIndices(long var0, int var2, int[] var3);

   static final native int CTRunGetPositions(long var0, int var2, float[] var3);

   static final native long kCFAllocatorDefault();

   static final native long kCFTypeDictionaryKeyCallBacks();

   static final native long kCFTypeDictionaryValueCallBacks();

   static final native long kCTFontAttributeName();

   static final native long kCTParagraphStyleAttributeName();

   static final native long CFArrayGetCount(long var0);

   static final native long CFArrayGetValueAtIndex(long var0, long var2);

   static final native long CFAttributedStringCreate(long var0, long var2, long var4);

   static final native void CFDictionaryAddValue(long var0, long var2, long var4);

   static final native long CFDictionaryCreateMutable(long var0, long var2, long var4, long var6);

   static final native long CFDictionaryGetValue(long var0, long var2);

   static final native void CFRelease(long var0);

   static final native long CFStringCreateWithCharacters(long var0, char[] var2, long var3);

   static final native long CFURLCreateWithFileSystemPath(long var0, long var2, long var4, boolean var6);

   static final native long CGBitmapContextCreate(long var0, long var2, long var4, long var6, long var8, long var10, int var12);

   static final native void CGContextFillRect(long var0, CGRect var2);

   static final native void CGContextRelease(long var0);

   static final native void CGContextSetAllowsFontSmoothing(long var0, boolean var2);

   static final native void CGContextSetAllowsAntialiasing(long var0, boolean var2);

   static final native void CGContextSetAllowsFontSubpixelPositioning(long var0, boolean var2);

   static final native void CGContextSetAllowsFontSubpixelQuantization(long var0, boolean var2);

   static final native void CGContextSetRGBFillColor(long var0, double var2, double var4, double var6, double var8);

   static final native void CGContextTranslateCTM(long var0, double var2, double var4);

   static final native long CGColorSpaceCreateDeviceGray();

   static final native long CGColorSpaceCreateDeviceRGB();

   static final native void CGColorSpaceRelease(long var0);

   static final native long CGDataProviderCreateWithURL(long var0);

   static final native long CGFontCreateWithDataProvider(long var0);

   static final native void CGPathRelease(long var0);

   static final native long CTFontCreatePathForGlyph(long var0, short var2, CGAffineTransform var3);

   static final native long CTFontCreateWithGraphicsFont(long var0, double var2, CGAffineTransform var4, long var5);

   static final native long CTFontCreateWithName(long var0, double var2, CGAffineTransform var4);

   static final native boolean CTFontManagerRegisterFontsForURL(long var0, int var2, long var3);

   static final native long CTLineCreateWithAttributedString(long var0);

   static final native long CTLineGetGlyphRuns(long var0);

   static final native long CTLineGetGlyphCount(long var0);

   static final native double CTLineGetTypographicBounds(long var0);

   static final native long CTRunGetGlyphCount(long var0);

   static final native long CTRunGetAttributes(long var0);

   static final native long CTParagraphStyleCreate(int var0);

   static {
      AccessController.doPrivileged(() -> {
         NativeLibLoader.loadLibrary("javafx_font");
         return null;
      });
      kCGBitmapByteOrder32Host = ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN ? 8192 : 16384;
   }
}
