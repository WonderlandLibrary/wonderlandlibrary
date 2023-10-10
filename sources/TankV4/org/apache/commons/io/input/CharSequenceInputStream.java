package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;

public class CharSequenceInputStream extends InputStream {
   private final CharsetEncoder encoder;
   private final CharBuffer cbuf;
   private final ByteBuffer bbuf;
   private int mark;

   public CharSequenceInputStream(CharSequence var1, Charset var2, int var3) {
      this.encoder = var2.newEncoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE);
      this.bbuf = ByteBuffer.allocate(var3);
      this.bbuf.flip();
      this.cbuf = CharBuffer.wrap(var1);
      this.mark = -1;
   }

   public CharSequenceInputStream(CharSequence var1, String var2, int var3) {
      this(var1, Charset.forName(var2), var3);
   }

   public CharSequenceInputStream(CharSequence var1, Charset var2) {
      this(var1, (Charset)var2, 2048);
   }

   public CharSequenceInputStream(CharSequence var1, String var2) {
      this(var1, (String)var2, 2048);
   }

   private void fillBuffer() throws CharacterCodingException {
      this.bbuf.compact();
      CoderResult var1 = this.encoder.encode(this.cbuf, this.bbuf, true);
      if (var1.isError()) {
         var1.throwException();
      }

      this.bbuf.flip();
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      if (var1 == null) {
         throw new NullPointerException("Byte array is null");
      } else if (var3 >= 0 && var2 + var3 <= var1.length) {
         if (var3 == 0) {
            return 0;
         } else if (!this.bbuf.hasRemaining() && !this.cbuf.hasRemaining()) {
            return -1;
         } else {
            int var4 = 0;

            while(var3 > 0) {
               if (this.bbuf.hasRemaining()) {
                  int var5 = Math.min(this.bbuf.remaining(), var3);
                  this.bbuf.get(var1, var2, var5);
                  var2 += var5;
                  var3 -= var5;
                  var4 += var5;
               } else {
                  this.fillBuffer();
                  if (!this.bbuf.hasRemaining() && !this.cbuf.hasRemaining()) {
                     break;
                  }
               }
            }

            return var4 == 0 && !this.cbuf.hasRemaining() ? -1 : var4;
         }
      } else {
         throw new IndexOutOfBoundsException("Array Size=" + var1.length + ", offset=" + var2 + ", length=" + var3);
      }
   }

   public int read() throws IOException {
      do {
         if (this.bbuf.hasRemaining()) {
            return this.bbuf.get() & 255;
         }

         this.fillBuffer();
      } while(this.bbuf.hasRemaining() || this.cbuf.hasRemaining());

      return -1;
   }

   public int read(byte[] var1) throws IOException {
      return this.read(var1, 0, var1.length);
   }

   public long skip(long var1) throws IOException {
      int var3;
      for(var3 = 0; var1 > 0L && this.cbuf.hasRemaining(); ++var3) {
         this.cbuf.get();
         --var1;
      }

      return (long)var3;
   }

   public int available() throws IOException {
      return this.cbuf.remaining();
   }

   public void close() throws IOException {
   }

   public synchronized void mark(int var1) {
      this.mark = this.cbuf.position();
   }

   public synchronized void reset() throws IOException {
      if (this.mark != -1) {
         this.cbuf.position(this.mark);
         this.mark = -1;
      }

   }

   public boolean markSupported() {
      return true;
   }
}
