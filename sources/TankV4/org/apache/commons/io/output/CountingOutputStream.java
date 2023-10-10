package org.apache.commons.io.output;

import java.io.OutputStream;

public class CountingOutputStream extends ProxyOutputStream {
   private long count = 0L;

   public CountingOutputStream(OutputStream var1) {
      super(var1);
   }

   protected synchronized void beforeWrite(int var1) {
      this.count += (long)var1;
   }

   public int getCount() {
      long var1 = this.getByteCount();
      if (var1 > 2147483647L) {
         throw new ArithmeticException("The byte count " + var1 + " is too large to be converted to an int");
      } else {
         return (int)var1;
      }
   }

   public int resetCount() {
      long var1 = this.resetByteCount();
      if (var1 > 2147483647L) {
         throw new ArithmeticException("The byte count " + var1 + " is too large to be converted to an int");
      } else {
         return (int)var1;
      }
   }

   public synchronized long getByteCount() {
      return this.count;
   }

   public synchronized long resetByteCount() {
      long var1 = this.count;
      this.count = 0L;
      return var1;
   }
}
