package com.sun.javafx.font.directwrite;

class IDWriteTextAnalyzer extends IUnknown {
   IDWriteTextAnalyzer(long var1) {
      super(var1);
   }

   int AnalyzeScript(JFXTextAnalysisSink var1, int var2, int var3, JFXTextAnalysisSink var4) {
      return OS.AnalyzeScript(this.ptr, var1.ptr, var2, var3, var4.ptr);
   }

   int GetGlyphs(char[] var1, int var2, int var3, IDWriteFontFace var4, boolean var5, boolean var6, DWRITE_SCRIPT_ANALYSIS var7, String var8, long var9, long[] var11, int[] var12, int var13, int var14, short[] var15, short[] var16, short[] var17, short[] var18, int[] var19) {
      return OS.GetGlyphs(this.ptr, var1, var2, var3, var4.ptr, var5, var6, var7, var8 != null ? (var8 + '\u0000').toCharArray() : (char[])null, var9, var11, var12, var13, var14, var15, var16, var17, var18, var19);
   }

   int GetGlyphPlacements(char[] var1, short[] var2, short[] var3, int var4, int var5, short[] var6, short[] var7, int var8, IDWriteFontFace var9, float var10, boolean var11, boolean var12, DWRITE_SCRIPT_ANALYSIS var13, String var14, long[] var15, int[] var16, int var17, float[] var18, float[] var19) {
      return OS.GetGlyphPlacements(this.ptr, var1, var2, var3, var4, var5, var6, var7, var8, var9.ptr, var10, var11, var12, var13, var14 != null ? (var14 + '\u0000').toCharArray() : (char[])null, var15, var16, var17, var18, var19);
   }
}
