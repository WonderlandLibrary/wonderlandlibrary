package com.sun.javafx.font.directwrite;

import com.sun.glass.utils.NativeLibLoader;
import com.sun.javafx.geom.Path2D;
import java.security.AccessController;

class OS {
   static final int S_OK = 0;
   static final int E_NOT_SUFFICIENT_BUFFER = -2147024774;
   static final int COINIT_APARTMENTTHREADED = 2;
   static final int COINIT_DISABLE_OLE1DDE = 4;
   static final int D2D1_FACTORY_TYPE_SINGLE_THREADED = 0;
   static final int D2D1_RENDER_TARGET_TYPE_DEFAULT = 0;
   static final int D2D1_RENDER_TARGET_TYPE_SOFTWARE = 1;
   static final int D2D1_RENDER_TARGET_TYPE_HARDWARE = 2;
   static final int D2D1_RENDER_TARGET_USAGE_NONE = 0;
   static final int D2D1_RENDER_TARGET_USAGE_FORCE_BITMAP_REMOTING = 1;
   static final int D2D1_RENDER_TARGET_USAGE_GDI_COMPATIBLE = 2;
   static final int D2D1_FEATURE_LEVEL_DEFAULT = 0;
   static final int D2D1_ALPHA_MODE_UNKNOWN = 0;
   static final int D2D1_ALPHA_MODE_PREMULTIPLIED = 1;
   static final int D2D1_ALPHA_MODE_STRAIGHT = 2;
   static final int D2D1_ALPHA_MODE_IGNORE = 3;
   static final int DXGI_FORMAT_UNKNOWN = 0;
   static final int DXGI_FORMAT_A8_UNORM = 65;
   static final int DXGI_FORMAT_B8G8R8A8_UNORM = 87;
   static final int D2D1_TEXT_ANTIALIAS_MODE_DEFAULT = 0;
   static final int D2D1_TEXT_ANTIALIAS_MODE_CLEARTYPE = 1;
   static final int D2D1_TEXT_ANTIALIAS_MODE_GRAYSCALE = 2;
   static final int D2D1_TEXT_ANTIALIAS_MODE_ALIASED = 3;
   static final int GUID_WICPixelFormat8bppGray = 1;
   static final int GUID_WICPixelFormat8bppAlpha = 2;
   static final int GUID_WICPixelFormat16bppGray = 3;
   static final int GUID_WICPixelFormat24bppRGB = 4;
   static final int GUID_WICPixelFormat24bppBGR = 5;
   static final int GUID_WICPixelFormat32bppBGR = 6;
   static final int GUID_WICPixelFormat32bppBGRA = 7;
   static final int GUID_WICPixelFormat32bppPBGRA = 8;
   static final int GUID_WICPixelFormat32bppGrayFloat = 9;
   static final int GUID_WICPixelFormat32bppRGBA = 10;
   static final int GUID_WICPixelFormat32bppPRGBA = 11;
   static final int WICBitmapNoCache = 0;
   static final int WICBitmapCacheOnDemand = 1;
   static final int WICBitmapCacheOnLoad = 2;
   static final int WICBitmapLockRead = 1;
   static final int WICBitmapLockWrite = 2;
   static final int DWRITE_FONT_WEIGHT_THIN = 100;
   static final int DWRITE_FONT_WEIGHT_EXTRA_LIGHT = 200;
   static final int DWRITE_FONT_WEIGHT_ULTRA_LIGHT = 200;
   static final int DWRITE_FONT_WEIGHT_LIGHT = 300;
   static final int DWRITE_FONT_WEIGHT_SEMI_LIGHT = 350;
   static final int DWRITE_FONT_WEIGHT_NORMAL = 400;
   static final int DWRITE_FONT_WEIGHT_REGULAR = 400;
   static final int DWRITE_FONT_WEIGHT_MEDIUM = 500;
   static final int DWRITE_FONT_WEIGHT_DEMI_BOLD = 600;
   static final int DWRITE_FONT_WEIGHT_SEMI_BOLD = 600;
   static final int DWRITE_FONT_WEIGHT_BOLD = 700;
   static final int DWRITE_FONT_WEIGHT_EXTRA_BOLD = 800;
   static final int DWRITE_FONT_WEIGHT_ULTRA_BOLD = 800;
   static final int DWRITE_FONT_WEIGHT_BLACK = 900;
   static final int DWRITE_FONT_WEIGHT_HEAVY = 900;
   static final int DWRITE_FONT_WEIGHT_EXTRA_BLACK = 950;
   static final int DWRITE_FONT_WEIGHT_ULTRA_BLACK = 950;
   static final int DWRITE_FONT_STRETCH_UNDEFINED = 0;
   static final int DWRITE_FONT_STRETCH_ULTRA_CONDENSED = 1;
   static final int DWRITE_FONT_STRETCH_EXTRA_CONDENSED = 2;
   static final int DWRITE_FONT_STRETCH_CONDENSED = 3;
   static final int DWRITE_FONT_STRETCH_SEMI_CONDENSED = 4;
   static final int DWRITE_FONT_STRETCH_NORMAL = 5;
   static final int DWRITE_FONT_STRETCH_MEDIUM = 5;
   static final int DWRITE_FONT_STRETCH_SEMI_EXPANDED = 6;
   static final int DWRITE_FONT_STRETCH_EXPANDED = 7;
   static final int DWRITE_FONT_STRETCH_EXTRA_EXPANDED = 8;
   static final int DWRITE_FONT_STRETCH_ULTRA_EXPANDED = 9;
   static final int DWRITE_FONT_STYLE_NORMAL = 0;
   static final int DWRITE_FONT_STYLE_OBLIQUE = 1;
   static final int DWRITE_FONT_STYLE_ITALIC = 2;
   static final int DWRITE_TEXTURE_ALIASED_1x1 = 0;
   static final int DWRITE_TEXTURE_CLEARTYPE_3x1 = 1;
   static final int DWRITE_RENDERING_MODE_DEFAULT = 0;
   static final int DWRITE_RENDERING_MODE_ALIASED = 1;
   static final int DWRITE_RENDERING_MODE_GDI_CLASSIC = 2;
   static final int DWRITE_RENDERING_MODE_GDI_NATURAL = 3;
   static final int DWRITE_RENDERING_MODE_NATURAL = 4;
   static final int DWRITE_RENDERING_MODE_NATURAL_SYMMETRIC = 5;
   static final int DWRITE_RENDERING_MODE_OUTLINE = 6;
   static final int DWRITE_RENDERING_MODE_CLEARTYPE_GDI_CLASSIC = 2;
   static final int DWRITE_RENDERING_MODE_CLEARTYPE_GDI_NATURAL = 3;
   static final int DWRITE_RENDERING_MODE_CLEARTYPE_NATURAL = 4;
   static final int DWRITE_RENDERING_MODE_CLEARTYPE_NATURAL_SYMMETRIC = 5;
   static final int DWRITE_MEASURING_MODE_NATURAL = 0;
   static final int DWRITE_MEASURING_MODE_GDI_CLASSIC = 1;
   static final int DWRITE_MEASURING_MODE_GDI_NATURAL = 2;
   static final int DWRITE_FACTORY_TYPE_SHARED = 0;
   static final int DWRITE_READING_DIRECTION_LEFT_TO_RIGHT = 0;
   static final int DWRITE_READING_DIRECTION_RIGHT_TO_LEFT = 1;
   static final int DWRITE_FONT_SIMULATIONS_NONE = 0;
   static final int DWRITE_FONT_SIMULATIONS_BOLD = 1;
   static final int DWRITE_FONT_SIMULATIONS_OBLIQUE = 2;
   static final int DWRITE_INFORMATIONAL_STRING_NONE = 0;
   static final int DWRITE_INFORMATIONAL_STRING_COPYRIGHT_NOTICE = 1;
   static final int DWRITE_INFORMATIONAL_STRING_VERSION_STRINGS = 2;
   static final int DWRITE_INFORMATIONAL_STRING_TRADEMARK = 3;
   static final int DWRITE_INFORMATIONAL_STRING_MANUFACTURER = 4;
   static final int DWRITE_INFORMATIONAL_STRING_DESIGNER = 5;
   static final int DWRITE_INFORMATIONAL_STRING_DESIGNER_URL = 6;
   static final int DWRITE_INFORMATIONAL_STRING_DESCRIPTION = 7;
   static final int DWRITE_INFORMATIONAL_STRING_FONT_VENDOR_URL = 8;
   static final int DWRITE_INFORMATIONAL_STRING_LICENSE_DESCRIPTION = 9;
   static final int DWRITE_INFORMATIONAL_STRING_LICENSE_INFO_URL = 10;
   static final int DWRITE_INFORMATIONAL_STRING_WIN32_FAMILY_NAMES = 11;
   static final int DWRITE_INFORMATIONAL_STRING_WIN32_SUBFAMILY_NAMES = 12;
   static final int DWRITE_INFORMATIONAL_STRING_PREFERRED_FAMILY_NAMES = 13;
   static final int DWRITE_INFORMATIONAL_STRING_PREFERRED_SUBFAMILY_NAMES = 14;
   static final int DWRITE_INFORMATIONAL_STRING_SAMPLE_TEXT = 15;
   static final int DWRITE_INFORMATIONAL_STRING_FULL_NAME = 16;
   static final int DWRITE_INFORMATIONAL_STRING_POSTSCRIPT_NAME = 17;
   static final int DWRITE_INFORMATIONAL_STRING_POSTSCRIPT_CID_NAME = 18;

   private static final native long _DWriteCreateFactory(int var0);

   static final IDWriteFactory DWriteCreateFactory(int var0) {
      long var1 = _DWriteCreateFactory(var0);
      return var1 != 0L ? new IDWriteFactory(var1) : null;
   }

   private static final native long _D2D1CreateFactory(int var0);

   static final ID2D1Factory D2D1CreateFactory(int var0) {
      long var1 = _D2D1CreateFactory(var0);
      return var1 != 0L ? new ID2D1Factory(var1) : null;
   }

   private static final native long _WICCreateImagingFactory();

   static final IWICImagingFactory WICCreateImagingFactory() {
      long var0 = _WICCreateImagingFactory();
      return var0 != 0L ? new IWICImagingFactory(var0) : null;
   }

   static final native boolean CoInitializeEx(int var0);

   static final native void CoUninitialize();

   private static final native long _NewJFXTextAnalysisSink(char[] var0, int var1, int var2, char[] var3, int var4, long var5);

   static final JFXTextAnalysisSink NewJFXTextAnalysisSink(char[] var0, int var1, int var2, String var3, int var4) {
      long var5 = _NewJFXTextAnalysisSink(var0, var1, var2, (var3 + '\u0000').toCharArray(), var4, 0L);
      return var5 != 0L ? new JFXTextAnalysisSink(var5) : null;
   }

   private static final native long _NewJFXTextRenderer();

   static final JFXTextRenderer NewJFXTextRenderer() {
      long var0 = _NewJFXTextRenderer();
      return var0 != 0L ? new JFXTextRenderer(var0) : null;
   }

   static final native boolean Next(long var0);

   static final native int GetStart(long var0);

   static final native int GetLength(long var0);

   static final native DWRITE_SCRIPT_ANALYSIS GetAnalysis(long var0);

   static final native boolean JFXTextRendererNext(long var0);

   static final native int JFXTextRendererGetStart(long var0);

   static final native int JFXTextRendererGetLength(long var0);

   static final native int JFXTextRendererGetGlyphCount(long var0);

   static final native int JFXTextRendererGetTotalGlyphCount(long var0);

   static final native long JFXTextRendererGetFontFace(long var0);

   static final native int JFXTextRendererGetGlyphIndices(long var0, int[] var2, int var3, int var4);

   static final native int JFXTextRendererGetGlyphAdvances(long var0, float[] var2, int var3);

   static final native int JFXTextRendererGetGlyphOffsets(long var0, float[] var2, int var3);

   static final native int JFXTextRendererGetClusterMap(long var0, short[] var2, int var3, int var4);

   static final native DWRITE_GLYPH_METRICS GetDesignGlyphMetrics(long var0, short var2, boolean var3);

   static final native Path2D GetGlyphRunOutline(long var0, float var2, short var3, boolean var4);

   static final native long CreateFontFace(long var0);

   static final native long GetFaceNames(long var0);

   static final native long GetFontFamily(long var0);

   static final native int GetStretch(long var0);

   static final native int GetStyle(long var0);

   static final native int GetWeight(long var0);

   static final native long GetInformationalStrings(long var0, int var2);

   static final native int GetSimulations(long var0);

   static final native int GetFontCount(long var0);

   static final native long GetFont(long var0, int var2);

   static final native int Analyze(long var0, boolean[] var2, int[] var3, int[] var4, int[] var5);

   static final native char[] GetString(long var0, int var2, int var3);

   static final native int GetStringLength(long var0, int var2);

   static final native int FindLocaleName(long var0, char[] var2);

   static final native long GetFamilyNames(long var0);

   static final native long GetFirstMatchingFont(long var0, int var2, int var3, int var4);

   static final native int GetFontFamilyCount(long var0);

   static final native long GetFontFamily(long var0, int var2);

   static final native int FindFamilyName(long var0, char[] var2);

   static final native long GetFontFromFontFace(long var0, long var2);

   static final native byte[] CreateAlphaTexture(long var0, int var2, RECT var3);

   static final native RECT GetAlphaTextureBounds(long var0, int var2);

   static final native long GetSystemFontCollection(long var0, boolean var2);

   static final native long CreateGlyphRunAnalysis(long var0, DWRITE_GLYPH_RUN var2, float var3, DWRITE_MATRIX var4, int var5, int var6, float var7, float var8);

   static final native long CreateTextAnalyzer(long var0);

   static final native long CreateTextFormat(long var0, char[] var2, long var3, int var5, int var6, int var7, float var8, char[] var9);

   static final native long CreateTextLayout(long var0, char[] var2, int var3, int var4, long var5, float var7, float var8);

   static final native long CreateFontFileReference(long var0, char[] var2);

   static final native long CreateFontFace(long var0, int var2, long var3, int var5, int var6);

   static final native int AddRef(long var0);

   static final native int Release(long var0);

   static final native int AnalyzeScript(long var0, long var2, int var4, int var5, long var6);

   static final native int GetGlyphs(long var0, char[] var2, int var3, int var4, long var5, boolean var7, boolean var8, DWRITE_SCRIPT_ANALYSIS var9, char[] var10, long var11, long[] var13, int[] var14, int var15, int var16, short[] var17, short[] var18, short[] var19, short[] var20, int[] var21);

   static final native int GetGlyphPlacements(long var0, char[] var2, short[] var3, short[] var4, int var5, int var6, short[] var7, short[] var8, int var9, long var10, float var12, boolean var13, boolean var14, DWRITE_SCRIPT_ANALYSIS var15, char[] var16, long[] var17, int[] var18, int var19, float[] var20, float[] var21);

   static final native int Draw(long var0, long var2, long var4, float var6, float var7);

   static final native long CreateBitmap(long var0, int var2, int var3, int var4, int var5);

   static final native long Lock(long var0, int var2, int var3, int var4, int var5, int var6);

   static final native byte[] GetDataPointer(long var0);

   static final native int GetStride(long var0);

   static final native long CreateWicBitmapRenderTarget(long var0, long var2, D2D1_RENDER_TARGET_PROPERTIES var4);

   static final native void BeginDraw(long var0);

   static final native int EndDraw(long var0);

   static final native void Clear(long var0, D2D1_COLOR_F var2);

   static final native void SetTextAntialiasMode(long var0, int var2);

   static final native void SetTransform(long var0, D2D1_MATRIX_3X2_F var2);

   static final native void DrawGlyphRun(long var0, D2D1_POINT_2F var2, DWRITE_GLYPH_RUN var3, long var4, int var6);

   static final native long CreateSolidColorBrush(long var0, D2D1_COLOR_F var2);

   static {
      AccessController.doPrivileged(() -> {
         NativeLibLoader.loadLibrary("javafx_font");
         return null;
      });
   }
}
