package com.jcraft.jogg;

public class Buffer {
   private static final int BUFFER_INCREMENT = 256;
   private static final int[] mask = new int[]{0, 1, 3, 7, 15, 31, 63, 127, 255, 511, 1023, 2047, 4095, 8191, 16383, 32767, 65535, 131071, 262143, 524287, 1048575, 2097151, 4194303, 8388607, 16777215, 33554431, 67108863, 134217727, 268435455, 536870911, 1073741823, Integer.MAX_VALUE, -1};
   int ptr = 0;
   byte[] buffer = null;
   int endbit = 0;
   int endbyte = 0;
   int storage = 0;

   public void writeinit() {
      this.buffer = new byte[256];
      this.ptr = 0;
      this.buffer[0] = 0;
      this.storage = 256;
   }

   public void write(byte[] var1) {
      for(int var2 = 0; var2 < var1.length && var1[var2] != 0; ++var2) {
         this.write(var1[var2], 8);
      }

   }

   public void read(byte[] var1, int var2) {
      for(int var3 = 0; var2-- != 0; var1[var3++] = (byte)this.read(8)) {
      }

   }

   void reset() {
      this.ptr = 0;
      this.buffer[0] = 0;
      this.endbit = this.endbyte = 0;
   }

   public void writeclear() {
      this.buffer = null;
   }

   public void readinit(byte[] var1, int var2) {
      this.readinit(var1, 0, var2);
   }

   public void readinit(byte[] var1, int var2, int var3) {
      this.ptr = var2;
      this.buffer = var1;
      this.endbit = this.endbyte = 0;
      this.storage = var3;
   }

   public void write(int var1, int var2) {
      if (this.endbyte + 4 >= this.storage) {
         byte[] var3 = new byte[this.storage + 256];
         System.arraycopy(this.buffer, 0, var3, 0, this.storage);
         this.buffer = var3;
         this.storage += 256;
      }

      var1 &= mask[var2];
      var2 += this.endbit;
      byte[] var10000 = this.buffer;
      int var10001 = this.ptr;
      var10000[var10001] |= (byte)(var1 << this.endbit);
      if (var2 >= 8) {
         this.buffer[this.ptr + 1] = (byte)(var1 >>> 8 - this.endbit);
         if (var2 >= 16) {
            this.buffer[this.ptr + 2] = (byte)(var1 >>> 16 - this.endbit);
            if (var2 >= 24) {
               this.buffer[this.ptr + 3] = (byte)(var1 >>> 24 - this.endbit);
               if (var2 >= 32) {
                  if (this.endbit > 0) {
                     this.buffer[this.ptr + 4] = (byte)(var1 >>> 32 - this.endbit);
                  } else {
                     this.buffer[this.ptr + 4] = 0;
                  }
               }
            }
         }
      }

      this.endbyte += var2 / 8;
      this.ptr += var2 / 8;
      this.endbit = var2 & 7;
   }

   public int look(int var1) {
      int var3 = mask[var1];
      var1 += this.endbit;
      if (this.endbyte + 4 >= this.storage && this.endbyte + (var1 - 1) / 8 >= this.storage) {
         return -1;
      } else {
         int var2 = (this.buffer[this.ptr] & 255) >>> this.endbit;
         if (var1 > 8) {
            var2 |= (this.buffer[this.ptr + 1] & 255) << 8 - this.endbit;
            if (var1 > 16) {
               var2 |= (this.buffer[this.ptr + 2] & 255) << 16 - this.endbit;
               if (var1 > 24) {
                  var2 |= (this.buffer[this.ptr + 3] & 255) << 24 - this.endbit;
                  if (var1 > 32 && this.endbit != 0) {
                     var2 |= (this.buffer[this.ptr + 4] & 255) << 32 - this.endbit;
                  }
               }
            }
         }

         return var3 & var2;
      }
   }

   public int look1() {
      return this.endbyte >= this.storage ? -1 : this.buffer[this.ptr] >> this.endbit & 1;
   }

   public void adv(int var1) {
      var1 += this.endbit;
      this.ptr += var1 / 8;
      this.endbyte += var1 / 8;
      this.endbit = var1 & 7;
   }

   public void adv1() {
      ++this.endbit;
      if (this.endbit > 7) {
         this.endbit = 0;
         ++this.ptr;
         ++this.endbyte;
      }

   }

   public int read(int var1) {
      int var3 = mask[var1];
      var1 += this.endbit;
      if (this.endbyte + 4 >= this.storage) {
         byte var2 = -1;
         if (this.endbyte + (var1 - 1) / 8 >= this.storage) {
            this.ptr += var1 / 8;
            this.endbyte += var1 / 8;
            this.endbit = var1 & 7;
            return var2;
         }
      }

      int var4 = (this.buffer[this.ptr] & 255) >>> this.endbit;
      if (var1 > 8) {
         var4 |= (this.buffer[this.ptr + 1] & 255) << 8 - this.endbit;
         if (var1 > 16) {
            var4 |= (this.buffer[this.ptr + 2] & 255) << 16 - this.endbit;
            if (var1 > 24) {
               var4 |= (this.buffer[this.ptr + 3] & 255) << 24 - this.endbit;
               if (var1 > 32 && this.endbit != 0) {
                  var4 |= (this.buffer[this.ptr + 4] & 255) << 32 - this.endbit;
               }
            }
         }
      }

      var4 &= var3;
      this.ptr += var1 / 8;
      this.endbyte += var1 / 8;
      this.endbit = var1 & 7;
      return var4;
   }

   public int readB(int var1) {
      int var3 = 32 - var1;
      var1 += this.endbit;
      if (this.endbyte + 4 >= this.storage) {
         byte var2 = -1;
         if (this.endbyte * 8 + var1 > this.storage * 8) {
            this.ptr += var1 / 8;
            this.endbyte += var1 / 8;
            this.endbit = var1 & 7;
            return var2;
         }
      }

      int var4 = (this.buffer[this.ptr] & 255) << 24 + this.endbit;
      if (var1 > 8) {
         var4 |= (this.buffer[this.ptr + 1] & 255) << 16 + this.endbit;
         if (var1 > 16) {
            var4 |= (this.buffer[this.ptr + 2] & 255) << 8 + this.endbit;
            if (var1 > 24) {
               var4 |= (this.buffer[this.ptr + 3] & 255) << this.endbit;
               if (var1 > 32 && this.endbit != 0) {
                  var4 |= (this.buffer[this.ptr + 4] & 255) >> 8 - this.endbit;
               }
            }
         }
      }

      var4 = var4 >>> (var3 >> 1) >>> (var3 + 1 >> 1);
      this.ptr += var1 / 8;
      this.endbyte += var1 / 8;
      this.endbit = var1 & 7;
      return var4;
   }

   public int read1() {
      if (this.endbyte >= this.storage) {
         byte var2 = -1;
         ++this.endbit;
         if (this.endbit > 7) {
            this.endbit = 0;
            ++this.ptr;
            ++this.endbyte;
         }

         return var2;
      } else {
         int var1 = this.buffer[this.ptr] >> this.endbit & 1;
         ++this.endbit;
         if (this.endbit > 7) {
            this.endbit = 0;
            ++this.ptr;
            ++this.endbyte;
         }

         return var1;
      }
   }

   public int bytes() {
      return this.endbyte + (this.endbit + 7) / 8;
   }

   public int bits() {
      return this.endbyte * 8 + this.endbit;
   }

   public byte[] buffer() {
      return this.buffer;
   }

   public static int ilog(int var0) {
      int var1;
      for(var1 = 0; var0 > 0; var0 >>>= 1) {
         ++var1;
      }

      return var1;
   }

   public static void report(String var0) {
      System.err.println(var0);
      System.exit(1);
   }
}
