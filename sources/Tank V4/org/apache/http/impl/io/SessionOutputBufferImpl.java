package org.apache.http.impl.io;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.io.BufferInfo;
import org.apache.http.io.HttpTransportMetrics;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.CharArrayBuffer;

@NotThreadSafe
public class SessionOutputBufferImpl implements SessionOutputBuffer, BufferInfo {
   private static final byte[] CRLF = new byte[]{13, 10};
   private final HttpTransportMetricsImpl metrics;
   private final ByteArrayBuffer buffer;
   private final int fragementSizeHint;
   private final CharsetEncoder encoder;
   private OutputStream outstream;
   private ByteBuffer bbuf;

   public SessionOutputBufferImpl(HttpTransportMetricsImpl var1, int var2, int var3, CharsetEncoder var4) {
      Args.positive(var2, "Buffer size");
      Args.notNull(var1, "HTTP transport metrcis");
      this.metrics = var1;
      this.buffer = new ByteArrayBuffer(var2);
      this.fragementSizeHint = var3 >= 0 ? var3 : 0;
      this.encoder = var4;
   }

   public SessionOutputBufferImpl(HttpTransportMetricsImpl var1, int var2) {
      this(var1, var2, var2, (CharsetEncoder)null);
   }

   public void bind(OutputStream var1) {
      this.outstream = var1;
   }

   public boolean isBound() {
      return this.outstream != null;
   }

   public int capacity() {
      return this.buffer.capacity();
   }

   public int length() {
      return this.buffer.length();
   }

   public int available() {
      return this.capacity() - this.length();
   }

   private void streamWrite(byte[] var1, int var2, int var3) throws IOException {
      Asserts.notNull(this.outstream, "Output stream");
      this.outstream.write(var1, var2, var3);
   }

   private void flushStream() throws IOException {
      if (this.outstream != null) {
         this.outstream.flush();
      }

   }

   private void flushBuffer() throws IOException {
      int var1 = this.buffer.length();
      if (var1 > 0) {
         this.streamWrite(this.buffer.buffer(), 0, var1);
         this.buffer.clear();
         this.metrics.incrementBytesTransferred((long)var1);
      }

   }

   public void flush() throws IOException {
      this.flushBuffer();
      this.flushStream();
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      if (var1 != null) {
         if (var3 <= this.fragementSizeHint && var3 <= this.buffer.capacity()) {
            int var4 = this.buffer.capacity() - this.buffer.length();
            if (var3 > var4) {
               this.flushBuffer();
            }

            this.buffer.append(var1, var2, var3);
         } else {
            this.flushBuffer();
            this.streamWrite(var1, var2, var3);
            this.metrics.incrementBytesTransferred((long)var3);
         }

      }
   }

   public void write(byte[] var1) throws IOException {
      if (var1 != null) {
         this.write(var1, 0, var1.length);
      }
   }

   public void write(int var1) throws IOException {
      if (this.fragementSizeHint > 0) {
         if (this.buffer.isFull()) {
            this.flushBuffer();
         }

         this.buffer.append(var1);
      } else {
         this.flushBuffer();
         this.outstream.write(var1);
      }

   }

   public void writeLine(String var1) throws IOException {
      if (var1 != null) {
         if (var1.length() > 0) {
            if (this.encoder == null) {
               for(int var2 = 0; var2 < var1.length(); ++var2) {
                  this.write(var1.charAt(var2));
               }
            } else {
               CharBuffer var3 = CharBuffer.wrap(var1);
               this.writeEncoded(var3);
            }
         }

         this.write(CRLF);
      }
   }

   public void writeLine(CharArrayBuffer var1) throws IOException {
      if (var1 != null) {
         if (this.encoder == null) {
            int var2 = 0;

            int var4;
            for(int var3 = var1.length(); var3 > 0; var3 -= var4) {
               var4 = this.buffer.capacity() - this.buffer.length();
               var4 = Math.min(var4, var3);
               if (var4 > 0) {
                  this.buffer.append(var1, var2, var4);
               }

               if (this.buffer.isFull()) {
                  this.flushBuffer();
               }

               var2 += var4;
            }
         } else {
            CharBuffer var5 = CharBuffer.wrap(var1.buffer(), 0, var1.length());
            this.writeEncoded(var5);
         }

         this.write(CRLF);
      }
   }

   private void writeEncoded(CharBuffer var1) throws IOException {
      if (var1.hasRemaining()) {
         if (this.bbuf == null) {
            this.bbuf = ByteBuffer.allocate(1024);
         }

         this.encoder.reset();

         CoderResult var2;
         while(var1.hasRemaining()) {
            var2 = this.encoder.encode(var1, this.bbuf, true);
            this.handleEncodingResult(var2);
         }

         var2 = this.encoder.flush(this.bbuf);
         this.handleEncodingResult(var2);
         this.bbuf.clear();
      }
   }

   private void handleEncodingResult(CoderResult var1) throws IOException {
      if (var1.isError()) {
         var1.throwException();
      }

      this.bbuf.flip();

      while(this.bbuf.hasRemaining()) {
         this.write(this.bbuf.get());
      }

      this.bbuf.compact();
   }

   public HttpTransportMetrics getMetrics() {
      return this.metrics;
   }
}
