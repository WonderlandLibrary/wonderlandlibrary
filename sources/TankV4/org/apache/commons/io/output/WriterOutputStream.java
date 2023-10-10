package org.apache.commons.io.output;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;

public class WriterOutputStream extends OutputStream {
   private static final int DEFAULT_BUFFER_SIZE = 1024;
   private final Writer writer;
   private final CharsetDecoder decoder;
   private final boolean writeImmediately;
   private final ByteBuffer decoderIn;
   private final CharBuffer decoderOut;

   public WriterOutputStream(Writer var1, CharsetDecoder var2) {
      this(var1, (CharsetDecoder)var2, 1024, false);
   }

   public WriterOutputStream(Writer var1, CharsetDecoder var2, int var3, boolean var4) {
      this.decoderIn = ByteBuffer.allocate(128);
      this.writer = var1;
      this.decoder = var2;
      this.writeImmediately = var4;
      this.decoderOut = CharBuffer.allocate(var3);
   }

   public WriterOutputStream(Writer var1, Charset var2, int var3, boolean var4) {
      this(var1, var2.newDecoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE).replaceWith("?"), var3, var4);
   }

   public WriterOutputStream(Writer var1, Charset var2) {
      this(var1, (Charset)var2, 1024, false);
   }

   public WriterOutputStream(Writer var1, String var2, int var3, boolean var4) {
      this(var1, Charset.forName(var2), var3, var4);
   }

   public WriterOutputStream(Writer var1, String var2) {
      this(var1, (String)var2, 1024, false);
   }

   public WriterOutputStream(Writer var1) {
      this(var1, (Charset)Charset.defaultCharset(), 1024, false);
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      while(var3 > 0) {
         int var4 = Math.min(var3, this.decoderIn.remaining());
         this.decoderIn.put(var1, var2, var4);
         this.processInput(false);
         var3 -= var4;
         var2 += var4;
      }

      if (this.writeImmediately) {
         this.flushOutput();
      }

   }

   public void write(byte[] var1) throws IOException {
      this.write(var1, 0, var1.length);
   }

   public void write(int var1) throws IOException {
      this.write(new byte[]{(byte)var1}, 0, 1);
   }

   public void flush() throws IOException {
      this.flushOutput();
      this.writer.flush();
   }

   public void close() throws IOException {
      this.processInput(true);
      this.flushOutput();
      this.writer.close();
   }

   private void processInput(boolean var1) throws IOException {
      this.decoderIn.flip();

      while(true) {
         CoderResult var2 = this.decoder.decode(this.decoderIn, this.decoderOut, var1);
         if (!var2.isOverflow()) {
            if (var2.isUnderflow()) {
               this.decoderIn.compact();
               return;
            } else {
               throw new IOException("Unexpected coder result");
            }
         }

         this.flushOutput();
      }
   }

   private void flushOutput() throws IOException {
      if (this.decoderOut.position() > 0) {
         this.writer.write(this.decoderOut.array(), 0, this.decoderOut.position());
         this.decoderOut.rewind();
      }

   }
}
