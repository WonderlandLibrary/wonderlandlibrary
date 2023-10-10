package com.ibm.icu.impl;

public final class USerializedSet {
   private char[] array = new char[8];
   private int arrayOffset;
   private int bmpLength;
   private int length;

   public final boolean getSet(char[] var1, int var2) {
      this.array = null;
      this.arrayOffset = this.bmpLength = this.length = 0;
      this.length = var1[var2++];
      if ((this.length & '耀') > 0) {
         this.length &= 32767;
         if (var1.length < var2 + 1 + this.length) {
            this.length = 0;
            throw new IndexOutOfBoundsException();
         }

         this.bmpLength = var1[var2++];
      } else {
         if (var1.length < var2 + this.length) {
            this.length = 0;
            throw new IndexOutOfBoundsException();
         }

         this.bmpLength = this.length;
      }

      this.array = new char[this.length];
      System.arraycopy(var1, var2, this.array, 0, this.length);
      return true;
   }

   public final void setToOne(int var1) {
      if (1114111 >= var1) {
         if (var1 < 65535) {
            this.bmpLength = this.length = 2;
            this.array[0] = (char)var1;
            this.array[1] = (char)(var1 + 1);
         } else if (var1 == 65535) {
            this.bmpLength = 1;
            this.length = 3;
            this.array[0] = '\uffff';
            this.array[1] = 1;
            this.array[2] = 0;
         } else if (var1 < 1114111) {
            this.bmpLength = 0;
            this.length = 4;
            this.array[0] = (char)(var1 >> 16);
            this.array[1] = (char)var1;
            ++var1;
            this.array[2] = (char)(var1 >> 16);
            this.array[3] = (char)var1;
         } else {
            this.bmpLength = 0;
            this.length = 2;
            this.array[0] = 16;
            this.array[1] = '\uffff';
         }

      }
   }

   public final boolean getRange(int var1, int[] var2) {
      if (var1 < 0) {
         return false;
      } else {
         if (this.array == null) {
            this.array = new char[8];
         }

         if (var2 != null && var2.length >= 2) {
            var1 *= 2;
            if (var1 < this.bmpLength) {
               var2[0] = this.array[var1++];
               if (var1 < this.bmpLength) {
                  var2[1] = this.array[var1] - 1;
               } else if (var1 < this.length) {
                  var2[1] = (this.array[var1] << 16 | this.array[var1 + 1]) - 1;
               } else {
                  var2[1] = 1114111;
               }

               return true;
            } else {
               var1 -= this.bmpLength;
               var1 *= 2;
               int var3 = this.length - this.bmpLength;
               if (var1 < var3) {
                  int var4 = this.arrayOffset + this.bmpLength;
                  var2[0] = this.array[var4 + var1] << 16 | this.array[var4 + var1 + 1];
                  var1 += 2;
                  if (var1 < var3) {
                     var2[1] = (this.array[var4 + var1] << 16 | this.array[var4 + var1 + 1]) - 1;
                  } else {
                     var2[1] = 1114111;
                  }

                  return true;
               } else {
                  return false;
               }
            }
         } else {
            throw new IllegalArgumentException();
         }
      }
   }

   public final boolean contains(int var1) {
      if (var1 > 1114111) {
         return false;
      } else {
         int var2;
         if (var1 <= 65535) {
            for(var2 = 0; var2 < this.bmpLength && (char)var1 >= this.array[var2]; ++var2) {
            }

            return (var2 & 1) != 0;
         } else {
            char var3 = (char)(var1 >> 16);
            char var4 = (char)var1;

            for(var2 = this.bmpLength; var2 < this.length && (var3 > this.array[var2] || var3 == this.array[var2] && var4 >= this.array[var2 + 1]); var2 += 2) {
            }

            return (var2 + this.bmpLength & 2) != 0;
         }
      }
   }

   public final int countRanges() {
      return (this.bmpLength + (this.length - this.bmpLength) / 2 + 1) / 2;
   }
}
