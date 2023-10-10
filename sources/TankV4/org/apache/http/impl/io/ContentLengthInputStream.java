package org.apache.http.impl.io;

import java.io.IOException;
import java.io.InputStream;
import org.apache.http.ConnectionClosedException;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.io.BufferInfo;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.util.Args;

@NotThreadSafe
public class ContentLengthInputStream extends InputStream {
   private static final int BUFFER_SIZE = 2048;
   private final long contentLength;
   private long pos = 0L;
   private boolean closed = false;
   private SessionInputBuffer in = null;

   public ContentLengthInputStream(SessionInputBuffer var1, long var2) {
      this.in = (SessionInputBuffer)Args.notNull(var1, "Session input buffer");
      this.contentLength = Args.notNegative(var2, "Content length");
   }

   public void close() throws IOException {
      if (!this.closed) {
         if (this.pos < this.contentLength) {
            byte[] var1 = new byte[2048];

            while(this.read(var1) >= 0) {
            }
         }

         this.closed = true;
      }

   }

   public int available() throws IOException {
      if (this.in instanceof BufferInfo) {
         int var1 = ((BufferInfo)this.in).length();
         return Math.min(var1, (int)(this.contentLength - this.pos));
      } else {
         return 0;
      }
   }

   public int read() throws IOException {
      if (this.closed) {
         throw new IOException("Attempted read from closed stream.");
      } else if (this.pos >= this.contentLength) {
         return -1;
      } else {
         int var1 = this.in.read();
         if (var1 == -1) {
            if (this.pos < this.contentLength) {
               throw new ConnectionClosedException("Premature end of Content-Length delimited message body (expected: " + this.contentLength + "; received: " + this.pos);
            }
         } else {
            ++this.pos;
         }

         return var1;
      }
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      if (this.closed) {
         throw new IOException("Attempted read from closed stream.");
      } else if (this.pos >= this.contentLength) {
         return -1;
      } else {
         int var4 = var3;
         if (this.pos + (long)var3 > this.contentLength) {
            var4 = (int)(this.contentLength - this.pos);
         }

         int var5 = this.in.read(var1, var2, var4);
         if (var5 == -1 && this.pos < this.contentLength) {
            throw new ConnectionClosedException("Premature end of Content-Length delimited message body (expected: " + this.contentLength + "; received: " + this.pos);
         } else {
            if (var5 > 0) {
               this.pos += (long)var5;
            }

            return var5;
         }
      }
   }

   public int read(byte[] var1) throws IOException {
      return this.read(var1, 0, var1.length);
   }

   public long skip(long var1) throws IOException {
      if (var1 <= 0L) {
         return 0L;
      } else {
         byte[] var3 = new byte[2048];
         long var4 = Math.min(var1, this.contentLength - this.pos);

         long var6;
         int var8;
         for(var6 = 0L; var4 > 0L; var4 -= (long)var8) {
            var8 = this.read(var3, 0, (int)Math.min(2048L, var4));
            if (var8 == -1) {
               break;
            }

            var6 += (long)var8;
         }

         return var6;
      }
   }
}
