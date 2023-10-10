package javassist.bytecode;

import java.io.IOException;
import java.io.OutputStream;

final class ByteStream extends OutputStream {
   private byte[] buf;
   private int count;

   public ByteStream() {
      this(32);
   }

   public ByteStream(int var1) {
      this.buf = new byte[var1];
      this.count = 0;
   }

   public int getPos() {
      return this.count;
   }

   public int size() {
      return this.count;
   }

   public void writeBlank(int var1) {
      this.enlarge(var1);
      this.count += var1;
   }

   public void write(byte[] var1) {
      this.write(var1, 0, var1.length);
   }

   public void write(byte[] var1, int var2, int var3) {
      this.enlarge(var3);
      System.arraycopy(var1, var2, this.buf, this.count, var3);
      this.count += var3;
   }

   public void write(int var1) {
      this.enlarge(1);
      int var2 = this.count;
      this.buf[var2] = (byte)var1;
      this.count = var2 + 1;
   }

   public void writeShort(int var1) {
      this.enlarge(2);
      int var2 = this.count;
      this.buf[var2] = (byte)(var1 >>> 8);
      this.buf[var2 + 1] = (byte)var1;
      this.count = var2 + 2;
   }

   public void writeInt(int var1) {
      this.enlarge(4);
      int var2 = this.count;
      this.buf[var2] = (byte)(var1 >>> 24);
      this.buf[var2 + 1] = (byte)(var1 >>> 16);
      this.buf[var2 + 2] = (byte)(var1 >>> 8);
      this.buf[var2 + 3] = (byte)var1;
      this.count = var2 + 4;
   }

   public void writeLong(long var1) {
      this.enlarge(8);
      int var3 = this.count;
      this.buf[var3] = (byte)((int)(var1 >>> 56));
      this.buf[var3 + 1] = (byte)((int)(var1 >>> 48));
      this.buf[var3 + 2] = (byte)((int)(var1 >>> 40));
      this.buf[var3 + 3] = (byte)((int)(var1 >>> 32));
      this.buf[var3 + 4] = (byte)((int)(var1 >>> 24));
      this.buf[var3 + 5] = (byte)((int)(var1 >>> 16));
      this.buf[var3 + 6] = (byte)((int)(var1 >>> 8));
      this.buf[var3 + 7] = (byte)((int)var1);
      this.count = var3 + 8;
   }

   public void writeFloat(float var1) {
      this.writeInt(Float.floatToIntBits(var1));
   }

   public void writeDouble(double var1) {
      this.writeLong(Double.doubleToLongBits(var1));
   }

   public void writeUTF(String var1) {
      int var2 = var1.length();
      int var3 = this.count;
      this.enlarge(var2 + 2);
      byte[] var4 = this.buf;
      var4[var3++] = (byte)(var2 >>> 8);
      var4[var3++] = (byte)var2;

      for(int var5 = 0; var5 < var2; ++var5) {
         char var6 = var1.charAt(var5);
         if (1 > var6 || var6 > 127) {
            this.writeUTF2(var1, var2, var5);
            return;
         }

         var4[var3++] = (byte)var6;
      }

      this.count = var3;
   }

   private void writeUTF2(String var1, int var2, int var3) {
      int var4 = var2;

      int var5;
      for(var5 = var3; var5 < var2; ++var5) {
         char var6 = var1.charAt(var5);
         if (var6 > 2047) {
            var4 += 2;
         } else if (var6 == 0 || var6 > 127) {
            ++var4;
         }
      }

      if (var4 > 65535) {
         throw new RuntimeException("encoded string too long: " + var2 + var4 + " bytes");
      } else {
         this.enlarge(var4 + 2);
         var5 = this.count;
         byte[] var9 = this.buf;
         var9[var5] = (byte)(var4 >>> 8);
         var9[var5 + 1] = (byte)var4;
         var5 += 2 + var3;

         for(int var7 = var3; var7 < var2; ++var7) {
            char var8 = var1.charAt(var7);
            if (1 <= var8 && var8 <= 127) {
               var9[var5++] = (byte)var8;
            } else if (var8 > 2047) {
               var9[var5] = (byte)(224 | var8 >> 12 & 15);
               var9[var5 + 1] = (byte)(128 | var8 >> 6 & 63);
               var9[var5 + 2] = (byte)(128 | var8 & 63);
               var5 += 3;
            } else {
               var9[var5] = (byte)(192 | var8 >> 6 & 31);
               var9[var5 + 1] = (byte)(128 | var8 & 63);
               var5 += 2;
            }
         }

         this.count = var5;
      }
   }

   public void write(int var1, int var2) {
      this.buf[var1] = (byte)var2;
   }

   public void writeShort(int var1, int var2) {
      this.buf[var1] = (byte)(var2 >>> 8);
      this.buf[var1 + 1] = (byte)var2;
   }

   public void writeInt(int var1, int var2) {
      this.buf[var1] = (byte)(var2 >>> 24);
      this.buf[var1 + 1] = (byte)(var2 >>> 16);
      this.buf[var1 + 2] = (byte)(var2 >>> 8);
      this.buf[var1 + 3] = (byte)var2;
   }

   public byte[] toByteArray() {
      byte[] var1 = new byte[this.count];
      System.arraycopy(this.buf, 0, var1, 0, this.count);
      return var1;
   }

   public void writeTo(OutputStream var1) throws IOException {
      var1.write(this.buf, 0, this.count);
   }

   public void enlarge(int var1) {
      int var2 = this.count + var1;
      if (var2 > this.buf.length) {
         int var3 = this.buf.length << 1;
         byte[] var4 = new byte[var3 > var2 ? var3 : var2];
         System.arraycopy(this.buf, 0, var4, 0, this.count);
         this.buf = var4;
      }

   }
}
