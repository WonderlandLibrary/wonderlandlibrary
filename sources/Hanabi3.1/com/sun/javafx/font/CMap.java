package com.sun.javafx.font;

abstract class CMap {
   static final char noSuchChar = '�';
   static final int SHORTMASK = 65535;
   static final int INTMASK = -1;
   private static final int MAX_CODE_POINTS = 1114111;
   public static final NullCMapClass theNullCmap = new NullCMapClass();

   static CMap initialize(PrismFontFile var0) {
      CMap var1 = null;
      boolean var4 = true;
      int var5 = 0;
      int var6 = 0;
      int var7 = 0;
      int var8 = 0;
      boolean var9 = false;
      boolean var10 = false;
      FontFileReader.Buffer var11 = var0.readTable(1668112752);
      short var12 = var11.getShort(2);

      for(int var13 = 0; var13 < var12; ++var13) {
         var11.position(var13 * 8 + 4);
         short var3 = var11.getShort();
         short var14;
         if (var3 == 0) {
            var9 = true;
            var14 = var11.getShort();
            var8 = var11.getInt();
         } else if (var3 == 3) {
            var10 = true;
            var14 = var11.getShort();
            int var2 = var11.getInt();
            switch (var14) {
               case 0:
                  var5 = var2;
                  break;
               case 1:
                  var6 = var2;
                  break;
               case 10:
                  var7 = var2;
            }
         }
      }

      if (var10) {
         if (var7 != 0) {
            var1 = createCMap(var11, var7);
         } else if (var5 != 0) {
            var1 = createCMap(var11, var5);
         } else if (var6 != 0) {
            var1 = createCMap(var11, var6);
         }
      } else if (var9 && var8 != 0) {
         var1 = createCMap(var11, var8);
      } else {
         var1 = createCMap(var11, var11.getInt(8));
      }

      return var1;
   }

   static CMap createCMap(FontFileReader.Buffer var0, int var1) {
      char var2 = var0.getChar(var1);
      switch (var2) {
         case '\u0000':
            return new CMapFormat0(var0, var1);
         case '\u0001':
         case '\u0003':
         case '\u0005':
         case '\u0007':
         case '\t':
         case '\u000b':
         default:
            throw new RuntimeException("Cmap format unimplemented: " + var0.getChar(var1));
         case '\u0002':
            return new CMapFormat2(var0, var1);
         case '\u0004':
            return new CMapFormat4(var0, var1);
         case '\u0006':
            return new CMapFormat6(var0, var1);
         case '\b':
            return new CMapFormat8(var0, var1);
         case '\n':
            return new CMapFormat10(var0, var1);
         case '\f':
            return new CMapFormat12(var0, var1);
      }
   }

   abstract char getGlyph(int var1);

   final int getControlCodeGlyph(int var1, boolean var2) {
      if (var1 < 16) {
         switch (var1) {
            case 9:
            case 10:
            case 13:
               return 65535;
            case 11:
            case 12:
         }
      } else if (var1 >= 8204) {
         if (var1 <= 8207 || var1 >= 8232 && var1 <= 8238 || var1 >= 8298 && var1 <= 8303) {
            return 65535;
         }

         if (var2 && var1 >= 65535) {
            return 0;
         }
      }

      return -1;
   }

   static class NullCMapClass extends CMap {
      char getGlyph(int var1) {
         return '\u0000';
      }
   }

   static class CMapFormat12 extends CMap {
      int numGroups;
      int highBit = 0;
      int power;
      int extra;
      long[] startCharCode;
      long[] endCharCode;
      int[] startGlyphID;

      CMapFormat12(FontFileReader.Buffer var1, int var2) {
         this.numGroups = var1.getInt(var2 + 12);
         if (this.numGroups > 0 && this.numGroups <= 1114111 && var2 <= var1.capacity() - this.numGroups * 12 - 12 - 4) {
            this.startCharCode = new long[this.numGroups];
            this.endCharCode = new long[this.numGroups];
            this.startGlyphID = new int[this.numGroups];
            var1.position(var2 + 16);

            int var3;
            for(var3 = 0; var3 < this.numGroups; ++var3) {
               this.startCharCode[var3] = (long)(var1.getInt() & -1);
               this.endCharCode[var3] = (long)(var1.getInt() & -1);
               this.startGlyphID[var3] = var1.getInt() & -1;
            }

            var3 = this.numGroups;
            if (var3 >= 65536) {
               var3 >>= 16;
               this.highBit += 16;
            }

            if (var3 >= 256) {
               var3 >>= 8;
               this.highBit += 8;
            }

            if (var3 >= 16) {
               var3 >>= 4;
               this.highBit += 4;
            }

            if (var3 >= 4) {
               var3 >>= 2;
               this.highBit += 2;
            }

            if (var3 >= 2) {
               var3 >>= 1;
               ++this.highBit;
            }

            this.power = 1 << this.highBit;
            this.extra = this.numGroups - this.power;
         } else {
            throw new RuntimeException("Invalid cmap subtable");
         }
      }

      char getGlyph(int var1) {
         int var2 = this.getControlCodeGlyph(var1, false);
         if (var2 >= 0) {
            return (char)var2;
         } else {
            int var3 = this.power;
            int var4 = 0;
            if (this.startCharCode[this.extra] <= (long)var1) {
               var4 = this.extra;
            }

            while(var3 > 1) {
               var3 >>= 1;
               if (this.startCharCode[var4 + var3] <= (long)var1) {
                  var4 += var3;
               }
            }

            if (this.startCharCode[var4] <= (long)var1 && this.endCharCode[var4] >= (long)var1) {
               return (char)((int)((long)this.startGlyphID[var4] + ((long)var1 - this.startCharCode[var4])));
            } else {
               return '\u0000';
            }
         }
      }
   }

   static class CMapFormat10 extends CMap {
      long startCharCode;
      int numChars;
      char[] glyphIdArray;

      CMapFormat10(FontFileReader.Buffer var1, int var2) {
         var1.position(var2 + 12);
         this.startCharCode = (long)(var1.getInt() & -1);
         this.numChars = var1.getInt() & -1;
         if (this.numChars > 0 && this.numChars <= 1114111 && var2 <= var1.capacity() - this.numChars * 2 - 12 - 8) {
            this.glyphIdArray = new char[this.numChars];

            for(int var3 = 0; var3 < this.numChars; ++var3) {
               this.glyphIdArray[var3] = var1.getChar();
            }

         } else {
            throw new RuntimeException("Invalid cmap subtable");
         }
      }

      char getGlyph(int var1) {
         int var2 = (int)((long)var1 - this.startCharCode);
         return var2 >= 0 && var2 < this.numChars ? this.glyphIdArray[var2] : '\u0000';
      }
   }

   static class CMapFormat8 extends CMap {
      CMapFormat8(FontFileReader.Buffer var1, int var2) {
      }

      char getGlyph(int var1) {
         return '\u0000';
      }
   }

   static class CMapFormat6 extends CMap {
      char firstCode;
      char entryCount;
      char[] glyphIdArray;

      CMapFormat6(FontFileReader.Buffer var1, int var2) {
         var1.position(var2 + 6);
         this.firstCode = var1.getChar();
         this.entryCount = var1.getChar();
         this.glyphIdArray = new char[this.entryCount];

         for(int var3 = 0; var3 < this.entryCount; ++var3) {
            this.glyphIdArray[var3] = var1.getChar();
         }

      }

      char getGlyph(int var1) {
         int var2 = this.getControlCodeGlyph(var1, true);
         if (var2 >= 0) {
            return (char)var2;
         } else {
            var1 -= this.firstCode;
            return var1 >= 0 && var1 < this.entryCount ? this.glyphIdArray[var1] : '\u0000';
         }
      }
   }

   static class CMapFormat2 extends CMap {
      char[] subHeaderKey = new char[256];
      char[] firstCodeArray;
      char[] entryCountArray;
      short[] idDeltaArray;
      char[] idRangeOffSetArray;
      char[] glyphIndexArray;

      CMapFormat2(FontFileReader.Buffer var1, int var2) {
         char var3 = var1.getChar(var2 + 2);
         var1.position(var2 + 6);
         char var4 = 0;

         int var5;
         for(var5 = 0; var5 < 256; ++var5) {
            this.subHeaderKey[var5] = var1.getChar();
            if (this.subHeaderKey[var5] > var4) {
               var4 = this.subHeaderKey[var5];
            }
         }

         var5 = (var4 >> 3) + 1;
         this.firstCodeArray = new char[var5];
         this.entryCountArray = new char[var5];
         this.idDeltaArray = new short[var5];
         this.idRangeOffSetArray = new char[var5];

         int var6;
         for(var6 = 0; var6 < var5; ++var6) {
            this.firstCodeArray[var6] = var1.getChar();
            this.entryCountArray[var6] = var1.getChar();
            this.idDeltaArray[var6] = (short)var1.getChar();
            this.idRangeOffSetArray[var6] = var1.getChar();
         }

         var6 = (var3 - 518 - var5 * 8) / 2;
         this.glyphIndexArray = new char[var6];

         for(int var7 = 0; var7 < var6; ++var7) {
            this.glyphIndexArray[var7] = var1.getChar();
         }

      }

      char getGlyph(int var1) {
         int var2 = this.getControlCodeGlyph(var1, true);
         if (var2 >= 0) {
            return (char)var2;
         } else {
            char var3 = (char)(var1 >> 8);
            char var4 = (char)(var1 & 255);
            int var5 = this.subHeaderKey[var3] >> 3;
            char var6;
            if (var5 != 0) {
               var6 = var4;
            } else {
               var6 = var3;
               if (var3 == 0) {
                  var6 = var4;
               }
            }

            char var7 = this.firstCodeArray[var5];
            if (var6 < var7) {
               return '\u0000';
            } else {
               var6 -= var7;
               if (var6 < this.entryCountArray[var5]) {
                  int var8 = (this.idRangeOffSetArray.length - var5) * 8 - 6;
                  int var9 = (this.idRangeOffSetArray[var5] - var8) / 2;
                  char var10 = this.glyphIndexArray[var9 + var6];
                  if (var10 != 0) {
                     var10 = (char)(var10 + this.idDeltaArray[var5]);
                     return var10;
                  }
               }

               return '\u0000';
            }
         }
      }
   }

   static class CMapFormat0 extends CMap {
      byte[] cmap;

      CMapFormat0(FontFileReader.Buffer var1, int var2) {
         char var3 = var1.getChar(var2 + 2);
         this.cmap = new byte[var3 - 6];
         var1.get(var2 + 6, this.cmap, 0, var3 - 6);
      }

      char getGlyph(int var1) {
         if (var1 < 256) {
            if (var1 < 16) {
               switch (var1) {
                  case 9:
                  case 10:
                  case 13:
                     return '\uffff';
                  case 11:
                  case 12:
               }
            }

            return (char)(255 & this.cmap[var1]);
         } else {
            return '\u0000';
         }
      }
   }

   static class CMapFormat4 extends CMap {
      int segCount;
      int entrySelector;
      int rangeShift;
      char[] endCount;
      char[] startCount;
      short[] idDelta;
      char[] idRangeOffset;
      char[] glyphIds;

      CMapFormat4(FontFileReader.Buffer var1, int var2) {
         var1.position(var2);
         var1.getChar();
         int var3 = var1.getChar();
         if (var2 + var3 > var1.capacity()) {
            var3 = var1.capacity() - var2;
         }

         var1.getChar();
         this.segCount = var1.getChar() / 2;
         var1.getChar();
         this.entrySelector = var1.getChar();
         this.rangeShift = var1.getChar() / 2;
         this.startCount = new char[this.segCount];
         this.endCount = new char[this.segCount];
         this.idDelta = new short[this.segCount];
         this.idRangeOffset = new char[this.segCount];

         int var4;
         for(var4 = 0; var4 < this.segCount; ++var4) {
            this.endCount[var4] = var1.getChar();
         }

         var1.getChar();

         for(var4 = 0; var4 < this.segCount; ++var4) {
            this.startCount[var4] = var1.getChar();
         }

         for(var4 = 0; var4 < this.segCount; ++var4) {
            this.idDelta[var4] = (short)var1.getChar();
         }

         int var5;
         for(var4 = 0; var4 < this.segCount; ++var4) {
            var5 = var1.getChar();
            this.idRangeOffset[var4] = (char)(var5 >> 1 & '\uffff');
         }

         var4 = (this.segCount * 8 + 16) / 2;
         var1.position(var4 * 2 + var2);
         var5 = var3 / 2 - var4;
         this.glyphIds = new char[var5];

         for(int var6 = 0; var6 < var5; ++var6) {
            this.glyphIds[var6] = var1.getChar();
         }

      }

      char getGlyph(int var1) {
         boolean var2 = false;
         char var3 = 0;
         int var4 = this.getControlCodeGlyph(var1, true);
         if (var4 >= 0) {
            return (char)var4;
         } else {
            int var5 = 0;
            int var6 = this.startCount.length;

            int var9;
            for(var9 = this.startCount.length >> 1; var5 < var6; var9 = var5 + var6 >> 1) {
               if (this.endCount[var9] < var1) {
                  var5 = var9 + 1;
               } else {
                  var6 = var9;
               }
            }

            if (var1 >= this.startCount[var9] && var1 <= this.endCount[var9]) {
               char var7 = this.idRangeOffset[var9];
               if (var7 == 0) {
                  var3 = (char)(var1 + this.idDelta[var9]);
               } else {
                  int var8 = var7 - this.segCount + var9 + (var1 - this.startCount[var9]);
                  var3 = this.glyphIds[var8];
                  if (var3 != 0) {
                     var3 = (char)(var3 + this.idDelta[var9]);
                  }
               }
            }

            return var3;
         }
      }
   }
}
