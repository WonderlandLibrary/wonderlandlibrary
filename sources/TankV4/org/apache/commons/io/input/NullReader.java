package org.apache.commons.io.input;

import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;

public class NullReader extends Reader {
   private final long size;
   private long position;
   private long mark;
   private long readlimit;
   private boolean eof;
   private final boolean throwEofException;
   private final boolean markSupported;

   public NullReader(long var1) {
      this(var1, true, false);
   }

   public NullReader(long var1, boolean var3, boolean var4) {
      this.mark = -1L;
      this.size = var1;
      this.markSupported = var3;
      this.throwEofException = var4;
   }

   public long getPosition() {
      return this.position;
   }

   public long getSize() {
      return this.size;
   }

   public void close() throws IOException {
      this.eof = false;
      this.position = 0L;
      this.mark = -1L;
   }

   public synchronized void mark(int var1) {
      if (!this.markSupported) {
         throw new UnsupportedOperationException("Mark not supported");
      } else {
         this.mark = this.position;
         this.readlimit = (long)var1;
      }
   }

   public boolean markSupported() {
      return this.markSupported;
   }

   public int read() throws IOException {
      if (this.eof) {
         throw new IOException("Read after end of file");
      } else if (this.position == this.size) {
         return this.doEndOfFile();
      } else {
         ++this.position;
         return this.processChar();
      }
   }

   public int read(char[] var1) throws IOException {
      return this.read(var1, 0, var1.length);
   }

   public int read(char[] var1, int var2, int var3) throws IOException {
      if (this.eof) {
         throw new IOException("Read after end of file");
      } else if (this.position == this.size) {
         return this.doEndOfFile();
      } else {
         this.position += (long)var3;
         int var4 = var3;
         if (this.position > this.size) {
            var4 = var3 - (int)(this.position - this.size);
            this.position = this.size;
         }

         this.processChars(var1, var2, var4);
         return var4;
      }
   }

   public synchronized void reset() throws IOException {
      if (!this.markSupported) {
         throw new UnsupportedOperationException("Mark not supported");
      } else if (this.mark < 0L) {
         throw new IOException("No position has been marked");
      } else if (this.position > this.mark + this.readlimit) {
         throw new IOException("Marked position [" + this.mark + "] is no longer valid - passed the read limit [" + this.readlimit + "]");
      } else {
         this.position = this.mark;
         this.eof = false;
      }
   }

   public long skip(long var1) throws IOException {
      if (this.eof) {
         throw new IOException("Skip after end of file");
      } else if (this.position == this.size) {
         return (long)this.doEndOfFile();
      } else {
         this.position += var1;
         long var3 = var1;
         if (this.position > this.size) {
            var3 = var1 - (this.position - this.size);
            this.position = this.size;
         }

         return var3;
      }
   }

   protected int processChar() {
      return 0;
   }

   protected void processChars(char[] var1, int var2, int var3) {
   }

   private int doEndOfFile() throws EOFException {
      this.eof = true;
      if (this.throwEofException) {
         throw new EOFException();
      } else {
         return -1;
      }
   }
}
