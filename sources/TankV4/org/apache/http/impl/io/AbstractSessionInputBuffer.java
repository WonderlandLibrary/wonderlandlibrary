package org.apache.http.impl.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import org.apache.http.Consts;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.io.BufferInfo;
import org.apache.http.io.HttpTransportMetrics;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.CharArrayBuffer;

/** @deprecated */
@Deprecated
@NotThreadSafe
public abstract class AbstractSessionInputBuffer implements SessionInputBuffer, BufferInfo {
   private InputStream instream;
   private byte[] buffer;
   private ByteArrayBuffer linebuffer;
   private Charset charset;
   private boolean ascii;
   private int maxLineLen;
   private int minChunkLimit;
   private HttpTransportMetricsImpl metrics;
   private CodingErrorAction onMalformedCharAction;
   private CodingErrorAction onUnmappableCharAction;
   private int bufferpos;
   private int bufferlen;
   private CharsetDecoder decoder;
   private CharBuffer cbuf;

   protected void init(InputStream var1, int var2, HttpParams var3) {
      Args.notNull(var1, "Input stream");
      Args.notNegative(var2, "Buffer size");
      Args.notNull(var3, "HTTP parameters");
      this.instream = var1;
      this.buffer = new byte[var2];
      this.bufferpos = 0;
      this.bufferlen = 0;
      this.linebuffer = new ByteArrayBuffer(var2);
      String var4 = (String)var3.getParameter("http.protocol.element-charset");
      this.charset = var4 != null ? Charset.forName(var4) : Consts.ASCII;
      this.ascii = this.charset.equals(Consts.ASCII);
      this.decoder = null;
      this.maxLineLen = var3.getIntParameter("http.connection.max-line-length", -1);
      this.minChunkLimit = var3.getIntParameter("http.connection.min-chunk-limit", 512);
      this.metrics = this.createTransportMetrics();
      CodingErrorAction var5 = (CodingErrorAction)var3.getParameter("http.malformed.input.action");
      this.onMalformedCharAction = var5 != null ? var5 : CodingErrorAction.REPORT;
      CodingErrorAction var6 = (CodingErrorAction)var3.getParameter("http.unmappable.input.action");
      this.onUnmappableCharAction = var6 != null ? var6 : CodingErrorAction.REPORT;
   }

   protected HttpTransportMetricsImpl createTransportMetrics() {
      return new HttpTransportMetricsImpl();
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

   protected int fillBuffer() throws IOException {
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
      var1 = this.instream.read(this.buffer, var2, var3);
      if (var1 == -1) {
         return -1;
      } else {
         this.bufferlen = var2 + var1;
         this.metrics.incrementBytesTransferred((long)var1);
         return var1;
      }
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

      if (this.ascii) {
         var1.append((ByteArrayBuffer)this.linebuffer, 0, var2);
      } else {
         ByteBuffer var3 = ByteBuffer.wrap(this.linebuffer.buffer(), 0, var2);
         var2 = this.appendDecoded(var1, var3);
      }

      this.linebuffer.clear();
      return var2;
   }

   private int lineFromReadBuffer(CharArrayBuffer var1, int var2) throws IOException {
      int var3 = this.bufferpos;
      int var4 = var2;
      this.bufferpos = var2 + 1;
      if (var2 > var3 && this.buffer[var2 - 1] == 13) {
         var4 = var2 - 1;
      }

      int var5 = var4 - var3;
      if (this.ascii) {
         var1.append(this.buffer, var3, var5);
      } else {
         ByteBuffer var6 = ByteBuffer.wrap(this.buffer, var3, var5);
         var5 = this.appendDecoded(var1, var6);
      }

      return var5;
   }

   private int appendDecoded(CharArrayBuffer var1, ByteBuffer var2) throws IOException {
      if (!var2.hasRemaining()) {
         return 0;
      } else {
         if (this.decoder == null) {
            this.decoder = this.charset.newDecoder();
            this.decoder.onMalformedInput(this.onMalformedCharAction);
            this.decoder.onUnmappableCharacter(this.onUnmappableCharAction);
         }

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

   public HttpTransportMetrics getMetrics() {
      return this.metrics;
   }
}
