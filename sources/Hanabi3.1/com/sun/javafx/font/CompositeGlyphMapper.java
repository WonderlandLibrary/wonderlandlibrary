package com.sun.javafx.font;

import java.util.HashMap;

public class CompositeGlyphMapper extends CharToGlyphMapper {
   public static final int SLOTMASK = -16777216;
   public static final int GLYPHMASK = 16777215;
   public static final int NBLOCKS = 216;
   public static final int BLOCKSZ = 256;
   public static final int MAXUNICODE = 55296;
   private static final int SIMPLE_ASCII_MASK_START = 32;
   private static final int SIMPLE_ASCII_MASK_END = 126;
   private static final int ASCII_COUNT = 95;
   private boolean asciiCacheOK;
   private char[] charToGlyph;
   CompositeFontResource font;
   CharToGlyphMapper[] slotMappers;
   HashMap glyphMap;

   public CompositeGlyphMapper(CompositeFontResource var1) {
      this.font = var1;
      this.missingGlyph = 0;
      this.glyphMap = new HashMap();
      this.slotMappers = new CharToGlyphMapper[var1.getNumSlots()];
      this.asciiCacheOK = true;
   }

   private final CharToGlyphMapper getSlotMapper(int var1) {
      if (var1 >= this.slotMappers.length) {
         CharToGlyphMapper[] var2 = new CharToGlyphMapper[this.font.getNumSlots()];
         System.arraycopy(this.slotMappers, 0, var2, 0, this.slotMappers.length);
         this.slotMappers = var2;
      }

      CharToGlyphMapper var3 = this.slotMappers[var1];
      if (var3 == null) {
         var3 = this.font.getSlotResource(var1).getGlyphMapper();
         this.slotMappers[var1] = var3;
      }

      return var3;
   }

   public int getMissingGlyphCode() {
      return this.missingGlyph;
   }

   public final int compositeGlyphCode(int var1, int var2) {
      return var1 << 24 | var2 & 16777215;
   }

   private final int convertToGlyph(int var1) {
      for(int var2 = 0; var2 < this.font.getNumSlots(); ++var2) {
         if (var2 >= 255) {
            return this.missingGlyph;
         }

         CharToGlyphMapper var3 = this.getSlotMapper(var2);
         int var4 = var3.charToGlyph(var1);
         if (var4 != var3.getMissingGlyphCode()) {
            var4 = this.compositeGlyphCode(var2, var4);
            this.glyphMap.put(var1, var4);
            return var4;
         }
      }

      return this.missingGlyph;
   }

   private int getAsciiGlyphCode(int var1) {
      if (this.asciiCacheOK && var1 <= 126 && var1 >= 32) {
         if (this.charToGlyph == null) {
            char[] var2 = new char[95];
            CharToGlyphMapper var3 = this.getSlotMapper(0);
            int var4 = var3.getMissingGlyphCode();

            for(int var5 = 0; var5 < 95; ++var5) {
               int var6 = var3.charToGlyph(32 + var5);
               if (var6 == var4) {
                  this.charToGlyph = null;
                  this.asciiCacheOK = false;
                  return -1;
               }

               var2[var5] = (char)var6;
            }

            this.charToGlyph = var2;
         }

         int var7 = var1 - 32;
         return this.charToGlyph[var7];
      } else {
         return -1;
      }
   }

   public int getGlyphCode(int var1) {
      int var2 = this.getAsciiGlyphCode(var1);
      if (var2 >= 0) {
         return var2;
      } else {
         Integer var3 = (Integer)this.glyphMap.get(var1);
         return var3 != null ? var3 : this.convertToGlyph(var1);
      }
   }
}
