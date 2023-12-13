package com.sun.javafx.font;

public class OpenTypeGlyphMapper extends CharToGlyphMapper {
   PrismFontFile font;
   CMap cmap;

   public OpenTypeGlyphMapper(PrismFontFile var1) {
      this.font = var1;

      try {
         this.cmap = CMap.initialize(var1);
      } catch (Exception var3) {
         this.cmap = null;
      }

      if (this.cmap == null) {
         this.handleBadCMAP();
      }

      this.missingGlyph = 0;
   }

   public int getGlyphCode(int var1) {
      try {
         return this.cmap.getGlyph(var1);
      } catch (Exception var3) {
         this.handleBadCMAP();
         return this.missingGlyph;
      }
   }

   private void handleBadCMAP() {
      this.cmap = CMap.theNullCmap;
   }

   boolean hasSupplementaryChars() {
      return this.cmap instanceof CMap.CMapFormat8 || this.cmap instanceof CMap.CMapFormat10 || this.cmap instanceof CMap.CMapFormat12;
   }
}
