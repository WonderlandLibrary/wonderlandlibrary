package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;

public class ReaderInputStream extends InputStream {
   private static final int DEFAULT_BUFFER_SIZE = 1024;
   private final Reader reader;
   private final CharsetEncoder encoder;
   private final CharBuffer encoderIn;
   private final ByteBuffer encoderOut;
   private CoderResult lastCoderResult;
   private boolean endOfInput;

   public ReaderInputStream(Reader var1, CharsetEncoder var2) {
      this(var1, (CharsetEncoder)var2, 1024);
   }

   public ReaderInputStream(Reader var1, CharsetEncoder var2, int var3) {
      this.reader = var1;
      this.encoder = var2;
      this.encoderIn = CharBuffer.allocate(var3);
      this.encoderIn.flip();
      this.encoderOut = ByteBuffer.allocate(128);
      this.encoderOut.flip();
   }

   public ReaderInputStream(Reader var1, Charset var2, int var3) {
      this(var1, var2.newEncoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE), var3);
   }

   public ReaderInputStream(Reader var1, Charset var2) {
      this(var1, (Charset)var2, 1024);
   }

   public ReaderInputStream(Reader var1, String var2, int var3) {
      this(var1, Charset.forName(var2), var3);
   }

   public ReaderInputStream(Reader var1, String var2) {
      this(var1, (String)var2, 1024);
   }

   public ReaderInputStream(Reader var1) {
      this(var1, Charset.defaultCharset());
   }

   private void fillBuffer() throws IOException {
      if (!this.endOfInput && (this.lastCoderResult == null || this.lastCoderResult.isUnderflow())) {
         this.encoderIn.compact();
         int var1 = this.encoderIn.position();
         int var2 = this.reader.read(this.encoderIn.array(), var1, this.encoderIn.remaining());
         if (var2 == -1) {
            this.endOfInput = true;
         } else {
            this.encoderIn.position(var1 + var2);
         }

         this.encoderIn.flip();
      }

      this.encoderOut.compact();
      this.lastCoderResult = this.encoder.encode(this.encoderIn, this.encoderOut, this.endOfInput);
      this.encoderOut.flip();
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      if (var1 == null) {
         throw new NullPointerException("Byte array must not be null");
      } else if (var3 >= 0 && var2 >= 0 && var2 + var3 <= var1.length) {
         int var4 = 0;
         if (var3 == 0) {
            return 0;
         } else {
            while(var3 > 0) {
               if (this.encoderOut.hasRemaining()) {
                  int var5 = Math.min(this.encoderOut.remaining(), var3);
                  this.encoderOut.get(var1, var2, var5);
                  var2 += var5;
                  var3 -= var5;
                  var4 += var5;
               } else {
                  this.fillBuffer();
                  if (this.endOfInput && !this.encoderOut.hasRemaining()) {
                     break;
                  }
               }
            }

            return var4 == 0 && this.endOfInput ? -1 : var4;
         }
      } else {
         throw new IndexOutOfBoundsException("Array Size=" + var1.length + ", offset=" + var2 + ", length=" + var3);
      }
   }

   public int read(byte[] var1) throws IOException {
      return this.read(var1, 0, var1.length);
   }

   public int read() throws IOException {
      do {
         if (this.encoderOut.hasRemaining()) {
            return this.encoderOut.get() & 255;
         }

         this.fillBuffer();
      } while(!this.endOfInput || this.encoderOut.hasRemaining());

      return -1;
   }

   public void close() throws IOException {
      this.reader.close();
   }
}
