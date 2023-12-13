package org.apache.http.impl.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.config.MessageConstraints;
import org.apache.http.io.BufferInfo;
import org.apache.http.io.HttpTransportMetrics;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.CharArrayBuffer;

@NotThreadSafe
public class SessionInputBufferImpl implements SessionInputBuffer, BufferInfo {
   private final HttpTransportMetricsImpl metrics;
   private final byte[] buffer;
   private final ByteArrayBuffer linebuffer;
   private final int minChunkLimit;
   private final MessageConstraints constraints;
   private final CharsetDecoder decoder;
   private InputStream instream;
   private int bufferpos;
   private int bufferlen;
   private CharBuffer cbuf;

   public SessionInputBufferImpl(HttpTransportMetricsImpl var1, int var2, int var3, MessageConstraints var4, CharsetDecoder var5) {
      Args.notNull(var1, "HTTP transport metrcis");
      Args.positive(var2, "Buffer size");
      this.metrics = var1;
      this.buffer = new byte[var2];
      this.bufferpos = 0;
      this.bufferlen = 0;
      this.minChunkLimit = var3 >= 0 ? var3 : 512;
      this.constraints = var4 != null ? var4 : MessageConstraints.DEFAULT;
      this.linebuffer = new ByteArrayBuffer(var2);
      this.decoder = var5;
   }

   public SessionInputBufferImpl(HttpTransportMetricsImpl var1, int var2) {
      this(var1, var2, var2, (MessageConstraints)null, (CharsetDecoder)null);
   }

   public void bind(InputStream var1) {
      this.instream = var1;
   }

   public boolean isBound() {
      return this.instream != null;
   }

   public int capacity() {
      return this.buffer.length;
   }

   public int length() {
      return this.bufferlen - this.bufferpos;
   }

   public int available() {
      return this.capacity() - this.length();
   }

   private int streamRead(byte[] var1, int var2, int var3) throws IOException {
      Asserts.notNull(this.instream, "Input stream");
      return this.instream.read(var1, var2, var3);
   }

   public int fillBuffer() throws IOException {
      int var1;
      if (this.bufferpos > 0) {
         var1 = this.bufferlen - this.bufferpos;
         if (var1 > 0) {
            System.arraycopy(this.buffer, this.bufferpos, this.buffer, 0, var1);
         }

         this.bufferpos = 0;
         this.bufferlen = var1;
      }

      int var2 = this.bufferlen;
      int var3 = this.buffer.length - var2;
      var1 = this.streamRead(this.buffer, var2, var3);
      if (var1 == -1) {
         return -1;
      } else {
         this.bufferlen = var2 + var1;
         this.metrics.incrementBytesTransferred((long)var1);
         return var1;
      }
   }

   public void clear() {
      this.bufferpos = 0;
      this.bufferlen = 0;
   }

   public int read() throws IOException {
      // $FF: Couldn't be decompiled
   }

   public int read(byte[] param1, int param2, int param3) throws IOException {
      // $FF: Couldn't be decompiled
   }

   public int read(byte[] var1) throws IOException {
      return var1 == null ? 0 : this.read(var1, 0, var1.length);
   }

   private int locateLF() {
      for(int var1 = this.bufferpos; var1 < this.bufferlen; ++var1) {
         if (this.buffer[var1] == 10) {
            return var1;
         }
      }

      return -1;
   }

   public int readLine(CharArrayBuffer param1) throws IOException {
      // $FF: Couldn't be decompiled
   }

   private int lineFromLineBuffer(CharArrayBuffer var1) throws IOException {
      int var2 = this.linebuffer.length();
      if (var2 > 0) {
         if (this.linebuffer.byteAt(var2 - 1) == 10) {
            --var2;
         }

         if (var2 > 0 && this.linebuffer.byteAt(var2 - 1) == 13) {
            --var2;
         }
      }

      if (this.decoder == null) {
         var1.append((ByteArrayBuffer)this.linebuffer, 0, var2);
      } else {
         ByteBuffer var3 = ByteBuffer.wrap(this.linebuffer.buffer(), 0, var2);
         var2 = this.appendDecoded(var1, var3);
      }

      this.linebuffer.clear();
      return var2;
   }

   private int lineFromReadBuffer(CharArrayBuffer var1, int var2) throws IOException {
      int var3 = var2;
      int var4 = this.bufferpos;
      this.bufferpos = var2 + 1;
      if (var2 > var4 && this.buffer[var2 - 1] == 13) {
         var3 = var2 - 1;
      }

      int var5 = var3 - var4;
      if (this.decoder == null) {
         var1.append(this.buffer, var4, var5);
      } else {
         ByteBuffer var6 = ByteBuffer.wrap(this.buffer, var4, var5);
         var5 = this.appendDecoded(var1, var6);
      }

      return var5;
   }

   private int appendDecoded(CharArrayBuffer var1, ByteBuffer var2) throws IOException {
      if (!var2.hasRemaining()) {
         return 0;
      } else {
         if (this.cbuf == null) {
            this.cbuf = CharBuffer.allocate(1024);
         }

         this.decoder.reset();

         int var3;
         CoderResult var4;
         for(var3 = 0; var2.hasRemaining(); var3 += this.handleDecodingResult(var4, var1, var2)) {
            var4 = this.decoder.decode(var2, this.cbuf, true);
         }

         var4 = this.decoder.flush(this.cbuf);
         var3 += this.handleDecodingResult(var4, var1, var2);
         this.cbuf.clear();
         return var3;
      }
   }

   private int handleDecodingResult(CoderResult var1, CharArrayBuffer var2, ByteBuffer var3) throws IOException {
      if (var1.isError()) {
         var1.throwException();
      }

      this.cbuf.flip();
      int var4 = this.cbuf.remaining();

      while(this.cbuf.hasRemaining()) {
         var2.append(this.cbuf.get());
      }

      this.cbuf.compact();
      return var4;
   }

   public String readLine() throws IOException {
      CharArrayBuffer var1 = new CharArrayBuffer(64);
      int var2 = this.readLine(var1);
      return var2 != -1 ? var1.toString() : null;
   }

   public boolean isDataAvailable(int var1) throws IOException {
      return this.hasBufferedData();
   }

   public HttpTransportMetrics getMetrics() {
      return this.metrics;
   }
}
