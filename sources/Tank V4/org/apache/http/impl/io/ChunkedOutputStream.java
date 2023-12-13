package org.apache.http.impl.io;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.io.SessionOutputBuffer;

@NotThreadSafe
public class ChunkedOutputStream extends OutputStream {
   private final SessionOutputBuffer out;
   private final byte[] cache;
   private int cachePosition;
   private boolean wroteLastChunk;
   private boolean closed;

   /** @deprecated */
   @Deprecated
   public ChunkedOutputStream(SessionOutputBuffer var1, int var2) throws IOException {
      this(var2, var1);
   }

   /** @deprecated */
   @Deprecated
   public ChunkedOutputStream(SessionOutputBuffer var1) throws IOException {
      this(2048, var1);
   }

   public ChunkedOutputStream(int var1, SessionOutputBuffer var2) {
      this.cachePosition = 0;
      this.wroteLastChunk = false;
      this.closed = false;
      this.cache = new byte[var1];
      this.out = var2;
   }

   protected void flushCache() throws IOException {
      if (this.cachePosition > 0) {
         this.out.writeLine(Integer.toHexString(this.cachePosition));
         this.out.write(this.cache, 0, this.cachePosition);
         this.out.writeLine("");
         this.cachePosition = 0;
      }

   }

   protected void flushCacheWithAppend(byte[] var1, int var2, int var3) throws IOException {
      this.out.writeLine(Integer.toHexString(this.cachePosition + var3));
      this.out.write(this.cache, 0, this.cachePosition);
      this.out.write(var1, var2, var3);
      this.out.writeLine("");
      this.cachePosition = 0;
   }

   protected void writeClosingChunk() throws IOException {
      this.out.writeLine("0");
      this.out.writeLine("");
   }

   public void finish() throws IOException {
      if (!this.wroteLastChunk) {
         this.flushCache();
         this.writeClosingChunk();
         this.wroteLastChunk = true;
      }

   }

   public void write(int var1) throws IOException {
      if (this.closed) {
         throw new IOException("Attempted write to closed stream.");
      } else {
         this.cache[this.cachePosition] = (byte)var1;
         ++this.cachePosition;
         if (this.cachePosition == this.cache.length) {
            this.flushCache();
         }

      }
   }

   public void write(byte[] var1) throws IOException {
      this.write(var1, 0, var1.length);
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      if (this.closed) {
         throw new IOException("Attempted write to closed stream.");
      } else {
         if (var3 >= this.cache.length - this.cachePosition) {
            this.flushCacheWithAppend(var1, var2, var3);
         } else {
            System.arraycopy(var1, var2, this.cache, this.cachePosition, var3);
            this.cachePosition += var3;
         }

      }
   }

   public void flush() throws IOException {
      this.flushCache();
      this.out.flush();
   }

   public void close() throws IOException {
      if (!this.closed) {
         this.closed = true;
         this.finish();
         this.out.flush();
      }

   }
}
