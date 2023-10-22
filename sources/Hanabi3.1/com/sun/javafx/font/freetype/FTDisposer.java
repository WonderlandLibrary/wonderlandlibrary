package com.sun.javafx.font.freetype;

import com.sun.javafx.font.DisposerRecord;
import com.sun.javafx.font.PrismFontFactory;

class FTDisposer implements DisposerRecord {
   long library;
   long face;

   FTDisposer(long var1, long var3) {
      this.library = var1;
      this.face = var3;
   }

   public synchronized void dispose() {
      if (this.face != 0L) {
         OSFreetype.FT_Done_Face(this.face);
         if (PrismFontFactory.debugFonts) {
            System.err.println("Done Face=" + this.face);
         }

         this.face = 0L;
      }

      if (this.library != 0L) {
         OSFreetype.FT_Done_FreeType(this.library);
         if (PrismFontFactory.debugFonts) {
            System.err.println("Done Library=" + this.library);
         }

         this.library = 0L;
      }

   }
}
