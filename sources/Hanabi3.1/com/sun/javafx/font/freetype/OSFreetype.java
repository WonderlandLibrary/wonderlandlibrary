package com.sun.javafx.font.freetype;

import com.sun.glass.utils.NativeLibLoader;
import com.sun.javafx.geom.Path2D;
import java.security.AccessController;

class OSFreetype {
   static final int FT_FACE_FLAG_SCALABLE = 1;
   static final int FT_FACE_FLAG_FIXED_SIZES = 2;
   static final int FT_FACE_FLAG_FIXED_WIDTH = 4;
   static final int FT_FACE_FLAG_SFNT = 8;
   static final int FT_FACE_FLAG_HORIZONTAL = 16;
   static final int FT_FACE_FLAG_VERTICAL = 32;
   static final int FT_FACE_FLAG_KERNING = 64;
   static final int FT_FACE_FLAG_FAST_GLYPHS = 128;
   static final int FT_FACE_FLAG_MULTIPLE_MASTERS = 256;
   static final int FT_FACE_FLAG_GLYPH_NAMES = 512;
   static final int FT_FACE_FLAG_EXTERNAL_STREAM = 1024;
   static final int FT_FACE_FLAG_HINTER = 2048;
   static final int FT_FACE_FLAG_CID_KEYED = 4096;
   static final int FT_FACE_FLAG_TRICKY = 8192;
   static final int FT_STYLE_FLAG_ITALIC = 1;
   static final int FT_STYLE_FLAG_BOLD = 2;
   static final int FT_RENDER_MODE_NORMAL = 0;
   static final int FT_RENDER_MODE_LIGHT = 1;
   static final int FT_RENDER_MODE_MONO = 2;
   static final int FT_RENDER_MODE_LCD = 3;
   static final int FT_RENDER_MODE_LCD_V = 4;
   static final int FT_PIXEL_MODE_NONE = 0;
   static final int FT_PIXEL_MODE_MONO = 1;
   static final int FT_PIXEL_MODE_GRAY = 2;
   static final int FT_PIXEL_MODE_GRAY2 = 3;
   static final int FT_PIXEL_MODE_GRAY4 = 4;
   static final int FT_PIXEL_MODE_LCD = 5;
   static final int FT_PIXEL_MODE_LCD_V = 6;
   static final int FT_LOAD_DEFAULT = 0;
   static final int FT_LOAD_NO_SCALE = 1;
   static final int FT_LOAD_NO_HINTING = 2;
   static final int FT_LOAD_RENDER = 4;
   static final int FT_LOAD_NO_BITMAP = 8;
   static final int FT_LOAD_VERTICAL_LAYOUT = 16;
   static final int FT_LOAD_FORCE_AUTOHINT = 32;
   static final int FT_LOAD_CROP_BITMAP = 64;
   static final int FT_LOAD_PEDANTIC = 128;
   static final int FT_LOAD_IGNORE_GLOBAL_ADVANCE_WIDTH = 512;
   static final int FT_LOAD_NO_RECURSE = 1024;
   static final int FT_LOAD_IGNORE_TRANSFORM = 2048;
   static final int FT_LOAD_MONOCHROME = 4096;
   static final int FT_LOAD_LINEAR_DESIGN = 8192;
   static final int FT_LOAD_NO_AUTOHINT = 32768;
   static final int FT_LOAD_TARGET_NORMAL = 0;
   static final int FT_LOAD_TARGET_LIGHT = 65536;
   static final int FT_LOAD_TARGET_MONO = 131072;
   static final int FT_LOAD_TARGET_LCD = 196608;
   static final int FT_LOAD_TARGET_LCD_V = 262144;
   static final int FT_LCD_FILTER_NONE = 0;
   static final int FT_LCD_FILTER_DEFAULT = 1;
   static final int FT_LCD_FILTER_LIGHT = 2;
   static final int FT_LCD_FILTER_LEGACY = 16;

   static final int FT_LOAD_TARGET_MODE(int var0) {
      return var0 >> 16 & 15;
   }

   static final native Path2D FT_Outline_Decompose(long var0);

   static final native int FT_Init_FreeType(long[] var0);

   static final native int FT_Done_FreeType(long var0);

   static final native void FT_Library_Version(long var0, int[] var2, int[] var3, int[] var4);

   static final native int FT_Library_SetLcdFilter(long var0, int var2);

   static final native int FT_New_Face(long var0, byte[] var2, long var3, long[] var5);

   static final native int FT_Done_Face(long var0);

   static final native int FT_Get_Char_Index(long var0, long var2);

   static final native int FT_Set_Char_Size(long var0, long var2, long var4, int var6, int var7);

   static final native int FT_Load_Glyph(long var0, int var2, int var3);

   static final native void FT_Set_Transform(long var0, FT_Matrix var2, long var3, long var5);

   static final native FT_GlyphSlotRec getGlyphSlot(long var0);

   static final native byte[] getBitmapData(long var0);

   static final native boolean isPangoEnabled();

   static final native boolean isHarfbuzzEnabled();

   static {
      AccessController.doPrivileged(() -> {
         NativeLibLoader.loadLibrary("javafx_font_freetype");
         return null;
      });
   }
}
