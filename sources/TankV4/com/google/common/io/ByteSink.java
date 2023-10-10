package com.google.common.io;

import com.google.common.base.Preconditions;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

public abstract class ByteSink implements OutputSupplier {
   protected ByteSink() {
   }

   public CharSink asCharSink(Charset var1) {
      return new ByteSink.AsCharSink(this, var1);
   }

   public abstract OutputStream openStream() throws IOException;

   /** @deprecated */
   @Deprecated
   public final OutputStream getOutput() throws IOException {
      return this.openStream();
   }

   public OutputStream openBufferedStream() throws IOException {
      OutputStream var1 = this.openStream();
      return var1 instanceof BufferedOutputStream ? (BufferedOutputStream)var1 : new BufferedOutputStream(var1);
   }

   public void write(byte[] var1) throws IOException {
      Preconditions.checkNotNull(var1);
      Closer var2 = Closer.create();

      try {
         OutputStream var3 = (OutputStream)var2.register(this.openStream());
         var3.write(var1);
         var3.flush();
      } catch (Throwable var5) {
         throw var2.rethrow(var5);
      }

      var2.close();
   }

   public long writeFrom(InputStream var1) throws IOException {
      Preconditions.checkNotNull(var1);
      Closer var2 = Closer.create();

      long var6;
      try {
         OutputStream var3 = (OutputStream)var2.register(this.openStream());
         long var4 = ByteStreams.copy(var1, var3);
         var3.flush();
         var6 = var4;
      } catch (Throwable var9) {
         throw var2.rethrow(var9);
      }

      var2.close();
      return var6;
   }

   public Object getOutput() throws IOException {
      return this.getOutput();
   }

   private final class AsCharSink extends CharSink {
      private final Charset charset;
      final ByteSink this$0;

      private AsCharSink(ByteSink var1, Charset var2) {
         this.this$0 = var1;
         this.charset = (Charset)Preconditions.checkNotNull(var2);
      }

      public Writer openStream() throws IOException {
         return new OutputStreamWriter(this.this$0.openStream(), this.charset);
      }

      public String toString() {
         return this.this$0.toString() + ".asCharSink(" + this.charset + ")";
      }

      AsCharSink(ByteSink var1, Charset var2, Object var3) {
         this(var1, var2);
      }
   }
}
