package org.apache.commons.codec.binary;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BaseNCodecOutputStream extends FilterOutputStream {
   private final boolean doEncode;
   private final BaseNCodec baseNCodec;
   private final byte[] singleByte = new byte[1];
   private final BaseNCodec.Context context = new BaseNCodec.Context();

   public BaseNCodecOutputStream(OutputStream var1, BaseNCodec var2, boolean var3) {
      super(var1);
      this.baseNCodec = var2;
      this.doEncode = var3;
   }

   public void write(int var1) throws IOException {
      this.singleByte[0] = (byte)var1;
      this.write(this.singleByte, 0, 1);
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      if (var1 == null) {
         throw new NullPointerException();
      } else if (var2 >= 0 && var3 >= 0) {
         if (var2 <= var1.length && var2 + var3 <= var1.length) {
            if (var3 > 0) {
               if (this.doEncode) {
                  this.baseNCodec.encode(var1, var2, var3, this.context);
               } else {
                  this.baseNCodec.decode(var1, var2, var3, this.context);
               }

               this.flush(false);
            }

         } else {
            throw new IndexOutOfBoundsException();
         }
      } else {
         throw new IndexOutOfBoundsException();
      }
   }

   private void flush(boolean var1) throws IOException {
      int var2 = this.baseNCodec.available(this.context);
      if (var2 > 0) {
         byte[] var3 = new byte[var2];
         int var4 = this.baseNCodec.readResults(var3, 0, var2, this.context);
         if (var4 > 0) {
            this.out.write(var3, 0, var4);
         }
      }

      if (var1) {
         this.out.flush();
      }

   }

   public void flush() throws IOException {
      this.flush(true);
   }

   public void close() throws IOException {
      if (this.doEncode) {
         this.baseNCodec.encode(this.singleByte, 0, -1, this.context);
      } else {
         this.baseNCodec.decode(this.singleByte, 0, -1, this.context);
      }

      this.flush();
      this.out.close();
   }
}
