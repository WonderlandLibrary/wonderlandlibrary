package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.base.Ascii;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.hash.Funnels;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Iterator;

public abstract class ByteSource implements InputSupplier {
   private static final int BUF_SIZE = 4096;
   private static final byte[] countBuffer = new byte[4096];

   protected ByteSource() {
   }

   public CharSource asCharSource(Charset var1) {
      return new ByteSource.AsCharSource(this, var1);
   }

   public abstract InputStream openStream() throws IOException;

   /** @deprecated */
   @Deprecated
   public final InputStream getInput() throws IOException {
      return this.openStream();
   }

   public InputStream openBufferedStream() throws IOException {
      InputStream var1 = this.openStream();
      return var1 instanceof BufferedInputStream ? (BufferedInputStream)var1 : new BufferedInputStream(var1);
   }

   public ByteSource slice(long var1, long var3) {
      return new ByteSource.SlicedByteSource(this, var1, var3);
   }

   public boolean isEmpty() throws IOException {
      Closer var1 = Closer.create();

      boolean var3;
      try {
         InputStream var2 = (InputStream)var1.register(this.openStream());
         var3 = var2.read() == -1;
      } catch (Throwable var5) {
         throw var1.rethrow(var5);
      }

      var1.close();
      return var3;
   }

   public long size() throws IOException {
      Closer var1 = Closer.create();

      InputStream var2;
      long var3;
      try {
         var2 = (InputStream)var1.register(this.openStream());
         var3 = this.countBySkipping(var2);
      } catch (IOException var8) {
         var1.close();
         var1 = Closer.create();

         try {
            var2 = (InputStream)var1.register(this.openStream());
            var3 = this.countByReading(var2);
         } catch (Throwable var7) {
            throw var1.rethrow(var7);
         }

         var1.close();
         return var3;
      }

      var1.close();
      return var3;
   }

   private long countBySkipping(InputStream var1) throws IOException {
      long var2 = 0L;

      while(true) {
         while(true) {
            long var4 = var1.skip((long)Math.min(var1.available(), Integer.MAX_VALUE));
            if (var4 <= 0L) {
               if (var1.read() == -1) {
                  return var2;
               }

               if (var2 == 0L && var1.available() == 0) {
                  throw new IOException();
               }

               ++var2;
            } else {
               var2 += var4;
            }
         }
      }
   }

   private long countByReading(InputStream var1) throws IOException {
      long var2;
      long var4;
      for(var2 = 0L; (var4 = (long)var1.read(countBuffer)) != -1L; var2 += var4) {
      }

      return var2;
   }

   public long copyTo(OutputStream var1) throws IOException {
      Preconditions.checkNotNull(var1);
      Closer var2 = Closer.create();

      long var4;
      try {
         InputStream var3 = (InputStream)var2.register(this.openStream());
         var4 = ByteStreams.copy(var3, var1);
      } catch (Throwable var7) {
         throw var2.rethrow(var7);
      }

      var2.close();
      return var4;
   }

   public long copyTo(ByteSink var1) throws IOException {
      Preconditions.checkNotNull(var1);
      Closer var2 = Closer.create();

      long var5;
      try {
         InputStream var3 = (InputStream)var2.register(this.openStream());
         OutputStream var4 = (OutputStream)var2.register(var1.openStream());
         var5 = ByteStreams.copy(var3, var4);
      } catch (Throwable var8) {
         throw var2.rethrow(var8);
      }

      var2.close();
      return var5;
   }

   public byte[] read() throws IOException {
      Closer var1 = Closer.create();

      byte[] var3;
      try {
         InputStream var2 = (InputStream)var1.register(this.openStream());
         var3 = ByteStreams.toByteArray(var2);
      } catch (Throwable var5) {
         throw var1.rethrow(var5);
      }

      var1.close();
      return var3;
   }

   @Beta
   public Object read(ByteProcessor var1) throws IOException {
      Preconditions.checkNotNull(var1);
      Closer var2 = Closer.create();

      Object var4;
      try {
         InputStream var3 = (InputStream)var2.register(this.openStream());
         var4 = ByteStreams.readBytes(var3, var1);
      } catch (Throwable var6) {
         throw var2.rethrow(var6);
      }

      var2.close();
      return var4;
   }

   public HashCode hash(HashFunction var1) throws IOException {
      Hasher var2 = var1.newHasher();
      this.copyTo(Funnels.asOutputStream(var2));
      return var2.hash();
   }

   public boolean contentEquals(ByteSource var1) throws IOException {
      Preconditions.checkNotNull(var1);
      byte[] var2 = new byte[4096];
      byte[] var3 = new byte[4096];
      Closer var4 = Closer.create();

      Throwable var10000;
      label47: {
         InputStream var5;
         InputStream var6;
         boolean var10001;
         try {
            var5 = (InputStream)var4.register(this.openStream());
            var6 = (InputStream)var4.register(var1.openStream());
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label47;
         }

         boolean var9;
         while(true) {
            int var7;
            try {
               var7 = ByteStreams.read(var5, var2, 0, 4096);
               int var8 = ByteStreams.read(var6, var3, 0, 4096);
               if (var7 != var8 || !Arrays.equals(var2, var3)) {
                  var9 = false;
                  break;
               }
            } catch (Throwable var12) {
               var10000 = var12;
               var10001 = false;
               break label47;
            }

            label29:
            try {
               if (var7 != 4096) {
                  var9 = true;
                  break label29;
               }
               continue;
            } catch (Throwable var11) {
               var10000 = var11;
               var10001 = false;
               break label47;
            }

            var4.close();
            return var9;
         }

         var4.close();
         return var9;
      }

      Throwable var14 = var10000;
      throw var4.rethrow(var14);
   }

   public static ByteSource concat(Iterable var0) {
      return new ByteSource.ConcatenatedByteSource(var0);
   }

   public static ByteSource concat(Iterator var0) {
      return concat((Iterable)ImmutableList.copyOf(var0));
   }

   public static ByteSource concat(ByteSource... var0) {
      return concat((Iterable)ImmutableList.copyOf((Object[])var0));
   }

   public static ByteSource wrap(byte[] var0) {
      return new ByteSource.ByteArrayByteSource(var0);
   }

   public static ByteSource empty() {
      return ByteSource.EmptyByteSource.access$200();
   }

   public Object getInput() throws IOException {
      return this.getInput();
   }

   private static final class ConcatenatedByteSource extends ByteSource {
      private final Iterable sources;

      ConcatenatedByteSource(Iterable var1) {
         this.sources = (Iterable)Preconditions.checkNotNull(var1);
      }

      public InputStream openStream() throws IOException {
         return new MultiInputStream(this.sources.iterator());
      }

      public boolean isEmpty() throws IOException {
         Iterator var1 = this.sources.iterator();

         ByteSource var2;
         do {
            if (!var1.hasNext()) {
               return true;
            }

            var2 = (ByteSource)var1.next();
         } while(var2.isEmpty());

         return false;
      }

      public long size() throws IOException {
         long var1 = 0L;

         ByteSource var4;
         for(Iterator var3 = this.sources.iterator(); var3.hasNext(); var1 += var4.size()) {
            var4 = (ByteSource)var3.next();
         }

         return var1;
      }

      public String toString() {
         return "ByteSource.concat(" + this.sources + ")";
      }

      public Object getInput() throws IOException {
         return super.getInput();
      }
   }

   private static final class EmptyByteSource extends ByteSource.ByteArrayByteSource {
      private static final ByteSource.EmptyByteSource INSTANCE = new ByteSource.EmptyByteSource();

      private EmptyByteSource() {
         super(new byte[0]);
      }

      public CharSource asCharSource(Charset var1) {
         Preconditions.checkNotNull(var1);
         return CharSource.empty();
      }

      public byte[] read() {
         return this.bytes;
      }

      public String toString() {
         return "ByteSource.empty()";
      }

      static ByteSource.EmptyByteSource access$200() {
         return INSTANCE;
      }
   }

   private static class ByteArrayByteSource extends ByteSource {
      protected final byte[] bytes;

      protected ByteArrayByteSource(byte[] var1) {
         this.bytes = (byte[])Preconditions.checkNotNull(var1);
      }

      public InputStream openStream() {
         return new ByteArrayInputStream(this.bytes);
      }

      public InputStream openBufferedStream() throws IOException {
         return this.openStream();
      }

      public boolean isEmpty() {
         return this.bytes.length == 0;
      }

      public long size() {
         return (long)this.bytes.length;
      }

      public byte[] read() {
         return (byte[])this.bytes.clone();
      }

      public long copyTo(OutputStream var1) throws IOException {
         var1.write(this.bytes);
         return (long)this.bytes.length;
      }

      public Object read(ByteProcessor var1) throws IOException {
         var1.processBytes(this.bytes, 0, this.bytes.length);
         return var1.getResult();
      }

      public HashCode hash(HashFunction var1) throws IOException {
         return var1.hashBytes(this.bytes);
      }

      public String toString() {
         return "ByteSource.wrap(" + Ascii.truncate(BaseEncoding.base16().encode(this.bytes), 30, "...") + ")";
      }

      public Object getInput() throws IOException {
         return super.getInput();
      }
   }

   private final class SlicedByteSource extends ByteSource {
      private final long offset;
      private final long length;
      final ByteSource this$0;

      private SlicedByteSource(ByteSource var1, long var2, long var4) {
         this.this$0 = var1;
         Preconditions.checkArgument(var2 >= 0L, "offset (%s) may not be negative", var2);
         Preconditions.checkArgument(var4 >= 0L, "length (%s) may not be negative", var4);
         this.offset = var2;
         this.length = var4;
      }

      public InputStream openStream() throws IOException {
         return this.sliceStream(this.this$0.openStream());
      }

      public InputStream openBufferedStream() throws IOException {
         return this.sliceStream(this.this$0.openBufferedStream());
      }

      private InputStream sliceStream(InputStream var1) throws IOException {
         if (this.offset > 0L) {
            try {
               ByteStreams.skipFully(var1, this.offset);
            } catch (Throwable var5) {
               Closer var3 = Closer.create();
               var3.register(var1);
               throw var3.rethrow(var5);
            }
         }

         return ByteStreams.limit(var1, this.length);
      }

      public ByteSource slice(long var1, long var3) {
         Preconditions.checkArgument(var1 >= 0L, "offset (%s) may not be negative", var1);
         Preconditions.checkArgument(var3 >= 0L, "length (%s) may not be negative", var3);
         long var5 = this.length - var1;
         return this.this$0.slice(this.offset + var1, Math.min(var3, var5));
      }

      public boolean isEmpty() throws IOException {
         return this.length == 0L || super.isEmpty();
      }

      public String toString() {
         return this.this$0.toString() + ".slice(" + this.offset + ", " + this.length + ")";
      }

      public Object getInput() throws IOException {
         return super.getInput();
      }

      SlicedByteSource(ByteSource var1, long var2, long var4, Object var6) {
         this(var1, var2, var4);
      }
   }

   private final class AsCharSource extends CharSource {
      private final Charset charset;
      final ByteSource this$0;

      private AsCharSource(ByteSource var1, Charset var2) {
         this.this$0 = var1;
         this.charset = (Charset)Preconditions.checkNotNull(var2);
      }

      public Reader openStream() throws IOException {
         return new InputStreamReader(this.this$0.openStream(), this.charset);
      }

      public String toString() {
         return this.this$0.toString() + ".asCharSource(" + this.charset + ")";
      }

      AsCharSource(ByteSource var1, Charset var2, Object var3) {
         this(var1, var2);
      }
   }
}
