package io.netty.handler.codec.marshalling;

import java.io.IOException;
import org.jboss.marshalling.ByteInput;

class LimitingByteInput implements ByteInput {
   private static final LimitingByteInput.TooBigObjectException EXCEPTION = new LimitingByteInput.TooBigObjectException();
   private final ByteInput input;
   private final long limit;
   private long read;

   public LimitingByteInput(ByteInput var1, long var2) {
      if (var2 <= 0L) {
         throw new IllegalArgumentException("The limit MUST be > 0");
      } else {
         this.input = var1;
         this.limit = var2;
      }
   }

   public void close() throws IOException {
   }

   public int available() throws IOException {
      return this.readable(this.input.available());
   }

   public int read() throws IOException {
      int var1 = this.readable(1);
      if (var1 > 0) {
         int var2 = this.input.read();
         ++this.read;
         return var2;
      } else {
         throw EXCEPTION;
      }
   }

   public int read(byte[] var1) throws IOException {
      return this.read(var1, 0, var1.length);
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      int var4 = this.readable(var3);
      if (var4 > 0) {
         int var5 = this.input.read(var1, var2, var4);
         this.read += (long)var5;
         return var5;
      } else {
         throw EXCEPTION;
      }
   }

   public long skip(long var1) throws IOException {
      int var3 = this.readable((int)var1);
      if (var3 > 0) {
         long var4 = this.input.skip((long)var3);
         this.read += var4;
         return var4;
      } else {
         throw EXCEPTION;
      }
   }

   private int readable(int var1) {
      return (int)Math.min((long)var1, this.limit - this.read);
   }

   static final class TooBigObjectException extends IOException {
      private static final long serialVersionUID = 1L;
   }
}
