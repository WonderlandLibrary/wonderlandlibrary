package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;

public class BoundedInputStream extends InputStream {
   private final InputStream in;
   private final long max;
   private long pos;
   private long mark;
   private boolean propagateClose;

   public BoundedInputStream(InputStream var1, long var2) {
      this.pos = 0L;
      this.mark = -1L;
      this.propagateClose = true;
      this.max = var2;
      this.in = var1;
   }

   public BoundedInputStream(InputStream var1) {
      this(var1, -1L);
   }

   public int read() throws IOException {
      if (this.max >= 0L && this.pos >= this.max) {
         return -1;
      } else {
         int var1 = this.in.read();
         ++this.pos;
         return var1;
      }
   }

   public int read(byte[] var1) throws IOException {
      return this.read(var1, 0, var1.length);
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      if (this.max >= 0L && this.pos >= this.max) {
         return -1;
      } else {
         long var4 = this.max >= 0L ? Math.min((long)var3, this.max - this.pos) : (long)var3;
         int var6 = this.in.read(var1, var2, (int)var4);
         if (var6 == -1) {
            return -1;
         } else {
            this.pos += (long)var6;
            return var6;
         }
      }
   }

   public long skip(long var1) throws IOException {
      long var3 = this.max >= 0L ? Math.min(var1, this.max - this.pos) : var1;
      long var5 = this.in.skip(var3);
      this.pos += var5;
      return var5;
   }

   public int available() throws IOException {
      return this.max >= 0L && this.pos >= this.max ? 0 : this.in.available();
   }

   public String toString() {
      return this.in.toString();
   }

   public void close() throws IOException {
      if (this.propagateClose) {
         this.in.close();
      }

   }

   public synchronized void reset() throws IOException {
      this.in.reset();
      this.pos = this.mark;
   }

   public synchronized void mark(int var1) {
      this.in.mark(var1);
      this.mark = this.pos;
   }

   public boolean markSupported() {
      return this.in.markSupported();
   }

   public boolean isPropagateClose() {
      return this.propagateClose;
   }

   public void setPropagateClose(boolean var1) {
      this.propagateClose = var1;
   }
}
