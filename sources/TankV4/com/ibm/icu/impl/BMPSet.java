package com.ibm.icu.impl;

import com.ibm.icu.text.UnicodeSet;

public final class BMPSet {
   public static int U16_SURROGATE_OFFSET = 56613888;
   private boolean[] latin1Contains;
   private int[] table7FF;
   private int[] bmpBlockBits;
   private int[] list4kStarts;
   private final int[] list;
   private final int listLength;
   static final boolean $assertionsDisabled = !BMPSet.class.desiredAssertionStatus();

   public BMPSet(int[] var1, int var2) {
      this.list = var1;
      this.listLength = var2;
      this.latin1Contains = new boolean[256];
      this.table7FF = new int[64];
      this.bmpBlockBits = new int[64];
      this.list4kStarts = new int[18];
      this.list4kStarts[0] = this.findCodePoint(2048, 0, this.listLength - 1);

      for(int var3 = 1; var3 <= 16; ++var3) {
         this.list4kStarts[var3] = this.findCodePoint(var3 << 12, this.list4kStarts[var3 - 1], this.listLength - 1);
      }

      this.list4kStarts[17] = this.listLength - 1;
      this.initBits();
   }

   public BMPSet(BMPSet var1, int[] var2, int var3) {
      this.list = var2;
      this.listLength = var3;
      this.latin1Contains = (boolean[])var1.latin1Contains.clone();
      this.table7FF = (int[])var1.table7FF.clone();
      this.bmpBlockBits = (int[])var1.bmpBlockBits.clone();
      this.list4kStarts = (int[])var1.list4kStarts.clone();
   }

   public boolean contains(int var1) {
      if (var1 <= 255) {
         return this.latin1Contains[var1];
      } else if (var1 <= 2047) {
         return (this.table7FF[var1 & 63] & 1 << (var1 >> 6)) != 0;
      } else if (var1 < 55296 || var1 >= 57344 && var1 <= 65535) {
         int var2 = var1 >> 12;
         int var3 = this.bmpBlockBits[var1 >> 6 & 63] >> var2 & 65537;
         if (var3 <= 1) {
            return 0 != var3;
         } else {
            return this.containsSlow(var1, this.list4kStarts[var2], this.list4kStarts[var2 + 1]);
         }
      } else {
         return var1 <= 1114111 ? this.containsSlow(var1, this.list4kStarts[13], this.list4kStarts[17]) : false;
      }
   }

   public final int span(CharSequence var1, int var2, int var3, UnicodeSet.SpanCondition var4) {
      int var7 = var2;
      int var8 = Math.min(var1.length(), var3);
      char var5;
      char var6;
      int var9;
      int var10;
      if (UnicodeSet.SpanCondition.NOT_CONTAINED != var4) {
         for(; var7 < var8; ++var7) {
            var5 = var1.charAt(var7);
            if (var5 <= 255) {
               if (!this.latin1Contains[var5]) {
                  break;
               }
            } else if (var5 <= 2047) {
               if ((this.table7FF[var5 & 63] & 1 << (var5 >> 6)) == 0) {
                  break;
               }
            } else if (var5 >= '\ud800' && var5 < '\udc00' && var7 + 1 != var8 && (var6 = var1.charAt(var7 + 1)) >= '\udc00' && var6 < '\ue000') {
               var9 = UCharacterProperty.getRawSupplementary(var5, var6);
               if (this.list4kStarts[16] != this.list4kStarts[17]) {
                  break;
               }

               ++var7;
            } else {
               var9 = var5 >> 12;
               var10 = this.bmpBlockBits[var5 >> 6 & 63] >> var9 & 65537;
               if (var10 <= 1) {
                  if (var10 == 0) {
                     break;
                  }
               } else if (this.list4kStarts[var9] != this.list4kStarts[var9 + 1]) {
                  break;
               }
            }
         }
      } else {
         for(; var7 < var8; ++var7) {
            var5 = var1.charAt(var7);
            if (var5 <= 255) {
               if (this.latin1Contains[var5]) {
                  break;
               }
            } else if (var5 <= 2047) {
               if ((this.table7FF[var5 & 63] & 1 << (var5 >> 6)) != 0) {
                  break;
               }
            } else if (var5 >= '\ud800' && var5 < '\udc00' && var7 + 1 != var8 && (var6 = var1.charAt(var7 + 1)) >= '\udc00' && var6 < '\ue000') {
               var9 = UCharacterProperty.getRawSupplementary(var5, var6);
               if (this.list4kStarts[16] != this.list4kStarts[17]) {
                  break;
               }

               ++var7;
            } else {
               var9 = var5 >> 12;
               var10 = this.bmpBlockBits[var5 >> 6 & 63] >> var9 & 65537;
               if (var10 <= 1) {
                  if (var10 != 0) {
                     break;
                  }
               } else if (this.list4kStarts[var9] != this.list4kStarts[var9 + 1]) {
                  break;
               }
            }
         }
      }

      return var7 - var2;
   }

   public final int spanBack(CharSequence var1, int var2, UnicodeSet.SpanCondition var3) {
      var2 = Math.min(var1.length(), var2);
      char var4;
      char var5;
      int var6;
      int var7;
      if (UnicodeSet.SpanCondition.NOT_CONTAINED != var3) {
         while(true) {
            --var2;
            var4 = var1.charAt(var2);
            if (var4 <= 255) {
               if (!this.latin1Contains[var4]) {
                  break;
               }
            } else if (var4 <= 2047) {
               if ((this.table7FF[var4 & 63] & 1 << (var4 >> 6)) == 0) {
                  break;
               }
            } else if (var4 >= '\ud800' && var4 >= '\udc00' && 0 != var2 && (var5 = var1.charAt(var2 - 1)) >= '\ud800' && var5 < '\udc00') {
               var6 = UCharacterProperty.getRawSupplementary(var5, var4);
               if (this.list4kStarts[16] != this.list4kStarts[17]) {
                  break;
               }

               --var2;
            } else {
               var6 = var4 >> 12;
               var7 = this.bmpBlockBits[var4 >> 6 & 63] >> var6 & 65537;
               if (var7 <= 1) {
                  if (var7 == 0) {
                     break;
                  }
               } else if (this.list4kStarts[var6] != this.list4kStarts[var6 + 1]) {
                  break;
               }
            }

            if (0 == var2) {
               return 0;
            }
         }
      } else {
         while(true) {
            --var2;
            var4 = var1.charAt(var2);
            if (var4 <= 255) {
               if (this.latin1Contains[var4]) {
                  break;
               }
            } else if (var4 <= 2047) {
               if ((this.table7FF[var4 & 63] & 1 << (var4 >> 6)) != 0) {
                  break;
               }
            } else if (var4 >= '\ud800' && var4 >= '\udc00' && 0 != var2 && (var5 = var1.charAt(var2 - 1)) >= '\ud800' && var5 < '\udc00') {
               var6 = UCharacterProperty.getRawSupplementary(var5, var4);
               if (this.list4kStarts[16] != this.list4kStarts[17]) {
                  break;
               }

               --var2;
            } else {
               var6 = var4 >> 12;
               var7 = this.bmpBlockBits[var4 >> 6 & 63] >> var6 & 65537;
               if (var7 <= 1) {
                  if (var7 != 0) {
                     break;
                  }
               } else if (this.list4kStarts[var6] != this.list4kStarts[var6 + 1]) {
                  break;
               }
            }

            if (0 == var2) {
               return 0;
            }
         }
      }

      return var2 + 1;
   }

   private static void set32x64Bits(int[] var0, int var1, int var2) {
      if (!$assertionsDisabled && 64 != var0.length) {
         throw new AssertionError();
      } else {
         int var3 = var1 >> 6;
         int var4 = var1 & 63;
         int var5 = 1 << var3;
         if (var1 + 1 == var2) {
            var0[var4] |= var5;
         } else {
            int var6 = var2 >> 6;
            int var7 = var2 & 63;
            int var10001;
            if (var3 == var6) {
               while(var4 < var7) {
                  var10001 = var4++;
                  var0[var10001] |= var5;
               }
            } else {
               if (var4 > 0) {
                  do {
                     var10001 = var4++;
                     var0[var10001] |= var5;
                  } while(var4 < 64);

                  ++var3;
               }

               if (var3 < var6) {
                  var5 = ~((1 << var3) - 1);
                  if (var6 < 32) {
                     var5 &= (1 << var6) - 1;
                  }

                  for(var4 = 0; var4 < 64; ++var4) {
                     var0[var4] |= var5;
                  }
               }

               var5 = 1 << var6;

               for(var4 = 0; var4 < var7; ++var4) {
                  var0[var4] |= var5;
               }
            }

         }
      }
   }

   private void initBits() {
      int var3 = 0;

      int var1;
      int var2;
      do {
         var1 = this.list[var3++];
         if (var3 < this.listLength) {
            var2 = this.list[var3++];
         } else {
            var2 = 1114112;
         }

         if (var1 >= 256) {
            break;
         }

         do {
            this.latin1Contains[var1++] = true;
         } while(var1 < var2 && var1 < 256);
      } while(var2 <= 256);

      while(var1 < 2048) {
         set32x64Bits(this.table7FF, var1, var2 <= 2048 ? var2 : 2048);
         if (var2 > 2048) {
            var1 = 2048;
            break;
         }

         var1 = this.list[var3++];
         if (var3 < this.listLength) {
            var2 = this.list[var3++];
         } else {
            var2 = 1114112;
         }
      }

      int var4 = 2048;

      while(var1 < 65536) {
         if (var2 > 65536) {
            var2 = 65536;
         }

         if (var1 < var4) {
            var1 = var4;
         }

         if (var1 < var2) {
            int[] var10000;
            if (0 != (var1 & 63)) {
               var1 >>= 6;
               var10000 = this.bmpBlockBits;
               var10000[var1 & 63] |= 65537 << (var1 >> 6);
               var1 = var1 + 1 << 6;
               var4 = var1;
            }

            if (var1 < var2) {
               if (var1 < (var2 & -64)) {
                  set32x64Bits(this.bmpBlockBits, var1 >> 6, var2 >> 6);
               }

               if (0 != (var2 & 63)) {
                  var2 >>= 6;
                  var10000 = this.bmpBlockBits;
                  var10000[var2 & 63] |= 65537 << (var2 >> 6);
                  var2 = var2 + 1 << 6;
                  var4 = var2;
               }
            }
         }

         if (var2 == 65536) {
            break;
         }

         var1 = this.list[var3++];
         if (var3 < this.listLength) {
            var2 = this.list[var3++];
         } else {
            var2 = 1114112;
         }
      }

   }

   private int findCodePoint(int var1, int var2, int var3) {
      if (var1 < this.list[var2]) {
         return var2;
      } else if (var2 < var3 && var1 < this.list[var3 - 1]) {
         while(true) {
            int var4 = var2 + var3 >>> 1;
            if (var4 == var2) {
               return var3;
            }

            if (var1 < this.list[var4]) {
               var3 = var4;
            } else {
               var2 = var4;
            }
         }
      } else {
         return var3;
      }
   }
}
