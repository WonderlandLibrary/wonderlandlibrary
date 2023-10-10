package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Beta
public final class CharStreams {
   private static final int BUF_SIZE = 2048;

   private CharStreams() {
   }

   /** @deprecated */
   @Deprecated
   public static InputSupplier newReaderSupplier(String var0) {
      return asInputSupplier(CharSource.wrap(var0));
   }

   /** @deprecated */
   @Deprecated
   public static InputSupplier newReaderSupplier(InputSupplier var0, Charset var1) {
      return asInputSupplier(ByteStreams.asByteSource(var0).asCharSource(var1));
   }

   /** @deprecated */
   @Deprecated
   public static OutputSupplier newWriterSupplier(OutputSupplier var0, Charset var1) {
      return asOutputSupplier(ByteStreams.asByteSink(var0).asCharSink(var1));
   }

   /** @deprecated */
   @Deprecated
   public static void write(CharSequence var0, OutputSupplier var1) throws IOException {
      asCharSink(var1).write(var0);
   }

   /** @deprecated */
   @Deprecated
   public static long copy(InputSupplier var0, OutputSupplier var1) throws IOException {
      return asCharSource(var0).copyTo(asCharSink(var1));
   }

   /** @deprecated */
   @Deprecated
   public static long copy(InputSupplier var0, Appendable var1) throws IOException {
      return asCharSource(var0).copyTo(var1);
   }

   public static long copy(Readable var0, Appendable var1) throws IOException {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);
      CharBuffer var2 = CharBuffer.allocate(2048);
      long var3 = 0L;

      while(var0.read(var2) != -1) {
         var2.flip();
         var1.append(var2);
         var3 += (long)var2.remaining();
         var2.clear();
      }

      return var3;
   }

   public static String toString(Readable var0) throws IOException {
      return toStringBuilder(var0).toString();
   }

   /** @deprecated */
   @Deprecated
   public static String toString(InputSupplier var0) throws IOException {
      return asCharSource(var0).read();
   }

   private static StringBuilder toStringBuilder(Readable var0) throws IOException {
      StringBuilder var1 = new StringBuilder();
      copy((Readable)var0, (Appendable)var1);
      return var1;
   }

   /** @deprecated */
   @Deprecated
   public static String readFirstLine(InputSupplier var0) throws IOException {
      return asCharSource(var0).readFirstLine();
   }

   /** @deprecated */
   @Deprecated
   public static List readLines(InputSupplier var0) throws IOException {
      Closer var1 = Closer.create();

      List var3;
      try {
         Readable var2 = (Readable)var1.register((Closeable)var0.getInput());
         var3 = readLines(var2);
      } catch (Throwable var5) {
         throw var1.rethrow(var5);
      }

      var1.close();
      return var3;
   }

   public static List readLines(Readable var0) throws IOException {
      ArrayList var1 = new ArrayList();
      LineReader var2 = new LineReader(var0);

      String var3;
      while((var3 = var2.readLine()) != null) {
         var1.add(var3);
      }

      return var1;
   }

   public static Object readLines(Readable var0, LineProcessor var1) throws IOException {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);
      LineReader var2 = new LineReader(var0);

      String var3;
      while((var3 = var2.readLine()) != null && var1.processLine(var3)) {
      }

      return var1.getResult();
   }

   /** @deprecated */
   @Deprecated
   public static Object readLines(InputSupplier var0, LineProcessor var1) throws IOException {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);
      Closer var2 = Closer.create();

      Object var4;
      try {
         Readable var3 = (Readable)var2.register((Closeable)var0.getInput());
         var4 = readLines(var3, var1);
      } catch (Throwable var6) {
         throw var2.rethrow(var6);
      }

      var2.close();
      return var4;
   }

   /** @deprecated */
   @Deprecated
   public static InputSupplier join(Iterable var0) {
      Preconditions.checkNotNull(var0);
      Iterable var1 = Iterables.transform(var0, new Function() {
         public CharSource apply(InputSupplier var1) {
            return CharStreams.asCharSource(var1);
         }

         public Object apply(Object var1) {
            return this.apply((InputSupplier)var1);
         }
      });
      return asInputSupplier(CharSource.concat(var1));
   }

   /** @deprecated */
   @Deprecated
   public static InputSupplier join(InputSupplier... var0) {
      return join((Iterable)Arrays.asList(var0));
   }

   public static void skipFully(Reader var0, long var1) throws IOException {
      Preconditions.checkNotNull(var0);

      while(var1 > 0L) {
         long var3 = var0.skip(var1);
         if (var3 == 0L) {
            if (var0.read() == -1) {
               throw new EOFException();
            }

            --var1;
         } else {
            var1 -= var3;
         }
      }

   }

   public static Writer nullWriter() {
      return CharStreams.NullWriter.access$000();
   }

   public static Writer asWriter(Appendable var0) {
      return (Writer)(var0 instanceof Writer ? (Writer)var0 : new AppendableWriter(var0));
   }

   static Reader asReader(Readable var0) {
      Preconditions.checkNotNull(var0);
      return var0 instanceof Reader ? (Reader)var0 : new Reader(var0) {
         final Readable val$readable;

         {
            this.val$readable = var1;
         }

         public int read(char[] var1, int var2, int var3) throws IOException {
            return this.read(CharBuffer.wrap(var1, var2, var3));
         }

         public int read(CharBuffer var1) throws IOException {
            return this.val$readable.read(var1);
         }

         public void close() throws IOException {
            if (this.val$readable instanceof Closeable) {
               ((Closeable)this.val$readable).close();
            }

         }
      };
   }

   /** @deprecated */
   @Deprecated
   public static CharSource asCharSource(InputSupplier var0) {
      Preconditions.checkNotNull(var0);
      return new CharSource(var0) {
         final InputSupplier val$supplier;

         {
            this.val$supplier = var1;
         }

         public Reader openStream() throws IOException {
            return CharStreams.asReader((Readable)this.val$supplier.getInput());
         }

         public String toString() {
            return "CharStreams.asCharSource(" + this.val$supplier + ")";
         }
      };
   }

   /** @deprecated */
   @Deprecated
   public static CharSink asCharSink(OutputSupplier var0) {
      Preconditions.checkNotNull(var0);
      return new CharSink(var0) {
         final OutputSupplier val$supplier;

         {
            this.val$supplier = var1;
         }

         public Writer openStream() throws IOException {
            return CharStreams.asWriter((Appendable)this.val$supplier.getOutput());
         }

         public String toString() {
            return "CharStreams.asCharSink(" + this.val$supplier + ")";
         }
      };
   }

   static InputSupplier asInputSupplier(CharSource var0) {
      return (InputSupplier)Preconditions.checkNotNull(var0);
   }

   static OutputSupplier asOutputSupplier(CharSink var0) {
      return (OutputSupplier)Preconditions.checkNotNull(var0);
   }

   private static final class NullWriter extends Writer {
      private static final CharStreams.NullWriter INSTANCE = new CharStreams.NullWriter();

      public void write(int var1) {
      }

      public void write(char[] var1) {
         Preconditions.checkNotNull(var1);
      }

      public void write(char[] var1, int var2, int var3) {
         Preconditions.checkPositionIndexes(var2, var2 + var3, var1.length);
      }

      public void write(String var1) {
         Preconditions.checkNotNull(var1);
      }

      public void write(String var1, int var2, int var3) {
         Preconditions.checkPositionIndexes(var2, var2 + var3, var1.length());
      }

      public Writer append(CharSequence var1) {
         Preconditions.checkNotNull(var1);
         return this;
      }

      public Writer append(CharSequence var1, int var2, int var3) {
         Preconditions.checkPositionIndexes(var2, var3, var1.length());
         return this;
      }

      public Writer append(char var1) {
         return this;
      }

      public void flush() {
      }

      public void close() {
      }

      public String toString() {
         return "CharStreams.nullWriter()";
      }

      public Appendable append(char var1) throws IOException {
         return this.append(var1);
      }

      public Appendable append(CharSequence var1, int var2, int var3) throws IOException {
         return this.append(var1, var2, var3);
      }

      public Appendable append(CharSequence var1) throws IOException {
         return this.append(var1);
      }

      static CharStreams.NullWriter access$000() {
         return INSTANCE;
      }
   }
}
