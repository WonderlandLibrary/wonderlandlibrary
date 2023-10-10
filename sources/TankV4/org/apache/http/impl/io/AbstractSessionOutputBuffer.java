package org.apache.http.impl.io;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import org.apache.http.Consts;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.io.BufferInfo;
import org.apache.http.io.HttpTransportMetrics;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.CharArrayBuffer;

/** @deprecated */
@Deprecated
@NotThreadSafe
public abstract class AbstractSessionOutputBuffer implements SessionOutputBuffer, BufferInfo {
   private static final byte[] CRLF = new byte[]{13, 10};
   private OutputStream outstream;
   private ByteArrayBuffer buffer;
   private Charset charset;
   private boolean ascii;
   private int minChunkLimit;
   private HttpTransportMetricsImpl metrics;
   private CodingErrorAction onMalformedCharAction;
   private CodingErrorAction onUnmappableCharAction;
   private CharsetEncoder encoder;
   private ByteBuffer bbuf;

   protected AbstractSessionOutputBuffer(OutputStream var1, int var2, Charset var3, int var4, CodingErrorAction var5, CodingErrorAction var6) {
      Args.notNull(var1, "Input stream");
      Args.notNegative(var2, "Buffer size");
      this.outstream = var1;
      this.buffer = new ByteArrayBuffer(var2);
      this.charset = var3 != null ? var3 : Consts.ASCII;
      this.ascii = this.charset.equals(Consts.ASCII);
      this.encoder = null;
      this.minChunkLimit = var4 >= 0 ? var4 : 512;
      this.metrics = this.createTransportMetrics();
      this.onMalformedCharAction = var5 != null ? var5 : CodingErrorAction.REPORT;
      this.onUnmappableCharAction = var6 != null ? var6 : CodingErrorAction.REPORT;
   }

   public AbstractSessionOutputBuffer() {
   }

   protected void init(OutputStream var1, int var2, HttpParams var3) {
      Args.notNull(var1, "Input stream");
      Args.notNegative(var2, "Buffer size");
      Args.notNull(var3, "HTTP parameters");
      this.outstream = var1;
      this.buffer = new ByteArrayBuffer(var2);
      String var4 = (String)var3.getParameter("http.protocol.element-charset");
      this.charset = var4 != null ? Charset.forName(var4) : Consts.ASCII;
      this.ascii = this.charset.equals(Consts.ASCII);
      this.encoder = null;
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
      return this.buffer.capacity();
   }

   public int length() {
      return this.buffer.length();
   }

   public int available() {
      return this.capacity() - this.length();
   }

   protected void flushBuffer() throws IOException {
      int var1 = this.buffer.length();
      if (var1 > 0) {
         this.outstream.write(this.buffer.buffer(), 0, var1);
         this.buffer.clear();
         this.metrics.incrementBytesTransferred((long)var1);
      }

   }

   public void flush() throws IOException {
      this.flushBuffer();
      this.outstream.flush();
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      if (var1 != null) {
         if (var3 <= this.minChunkLimit && var3 <= this.buffer.capacity()) {
            int var4 = this.buffer.capacity() - this.buffer.length();
            if (var3 > var4) {
               this.flushBuffer();
            }

            this.buffer.append(var1, var2, var3);
         } else {
            this.flushBuffer();
            this.outstream.write(var1, var2, var3);
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
      if (this.buffer.isFull()) {
         this.flushBuffer();
      }

      this.buffer.append(var1);
   }

   public void writeLine(String var1) throws IOException {
      if (var1 != null) {
         if (var1.length() > 0) {
            if (this.ascii) {
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
         if (this.ascii) {
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
         if (this.encoder == null) {
            this.encoder = this.charset.newEncoder();
            this.encoder.onMalformedInput(this.onMalformedCharAction);
            this.encoder.onUnmappableCharacter(this.onUnmappableCharAction);
         }

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
