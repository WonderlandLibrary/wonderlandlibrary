package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;

public class CountingInputStream extends ProxyInputStream {
   private long count;

   public CountingInputStream(InputStream var1) {
      super(var1);
   }

   public synchronized long skip(long var1) throws IOException {
      long var3 = super.skip(var1);
      this.count += var3;
      return var3;
   }

   protected synchronized void afterRead(int var1) {
      if (var1 != -1) {
         this.count += (long)var1;
      }

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
