package com.sun.javafx.font;

public abstract class CharToGlyphMapper {
   public static final int HI_SURROGATE_SHIFT = 10;
   public static final int HI_SURROGATE_START = 55296;
   public static final int HI_SURROGATE_END = 56319;
   public static final int LO_SURROGATE_START = 56320;
   public static final int LO_SURROGATE_END = 57343;
   public static final int SURROGATES_START = 65536;
   public static final int MISSING_GLYPH = 0;
   public static final int INVISIBLE_GLYPH_ID = 65535;
   protected int missingGlyph = 0;

   public boolean canDisplay(char var1) {
      int var2 = this.charToGlyph(var1);
      return var2 != this.missingGlyph;
   }

   public int getMissingGlyphCode() {
      return this.missingGlyph;
   }

   public abstract int getGlyphCode(int var1);

   public int charToGlyph(char var1) {
      return this.getGlyphCode(var1);
   }

   public int charToGlyph(int var1) {
      return this.getGlyphCode(var1);
   }

   public void charsToGlyphs(int var1, int var2, char[] var3, int[] var4, int var5) {
      for(int var6 = 0; var6 < var2; ++var6) {
         int var7 = var3[var1 + var6];
         if (var7 >= 55296 && var7 <= 56319 && var6 + 1 < var2) {
            char var8 = var3[var1 + var6 + 1];
            if (var8 >= '\udc00' && var8 <= '\udfff') {
               var7 = (var7 - '\ud800' << 10) + var8 - '\udc00' + 65536;
               var4[var5 + var6] = this.getGlyphCode(var7);
               ++var6;
               var4[var5 + var6] = 65535;
               continue;
            }
         }

         var4[var5 + var6] = this.getGlyphCode(var7);
      }

   }

   public void charsToGlyphs(int var1, int var2, char[] var3, int[] var4) {
      this.charsToGlyphs(var1, var2, var3, var4, 0);
   }

   public void charsToGlyphs(int var1, char[] var2, int[] var3) {
      this.charsToGlyphs(0, var1, var2, var3, 0);
   }
}
