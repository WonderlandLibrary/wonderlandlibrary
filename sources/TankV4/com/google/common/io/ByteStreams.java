package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Arrays;

@Beta
public final class ByteStreams {
   private static final int BUF_SIZE = 4096;
   private static final OutputStream NULL_OUTPUT_STREAM = new OutputStream() {
      public void write(int var1) {
      }

      public void write(byte[] var1) {
         Preconditions.checkNotNull(var1);
      }

      public void write(byte[] var1, int var2, int var3) {
         Preconditions.checkNotNull(var1);
      }

      public String toString() {
         return "ByteStreams.nullOutputStream()";
      }
   };

   private ByteStreams() {
   }

   /** @deprecated */
   @Deprecated
   public static InputSupplier newInputStreamSupplier(byte[] var0) {
      return asInputSupplier(ByteSource.wrap(var0));
   }

   /** @deprecated */
   @Deprecated
   public static InputSupplier newInputStreamSupplier(byte[] var0, int var1, int var2) {
      return asInputSupplier(ByteSource.wrap(var0).slice((long)var1, (long)var2));
   }

   /** @deprecated */
   @Deprecated
   public static void write(byte[] var0, OutputSupplier var1) throws IOException {
      asByteSink(var1).write(var0);
   }

   /** @deprecated */
   @Deprecated
   public static long copy(InputSupplier var0, OutputSupplier var1) throws IOException {
      return asByteSource(var0).copyTo(asByteSink(var1));
   }

   /** @deprecated */
   @Deprecated
   public static long copy(InputSupplier var0, OutputStream var1) throws IOException {
      return asByteSource(var0).copyTo(var1);
   }

   /** @deprecated */
   @Deprecated
   public static long copy(InputStream var0, OutputSupplier var1) throws IOException {
      return asByteSink(var1).writeFrom(var0);
   }

   public static long copy(InputStream var0, OutputStream var1) throws IOException {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);
      byte[] var2 = new byte[4096];
      long var3 = 0L;

      while(true) {
         int var5 = var0.read(var2);
         if (var5 == -1) {
            return var3;
         }

         var1.write(var2, 0, var5);
         var3 += (long)var5;
      }
   }

   public static long copy(ReadableByteChannel var0, WritableByteChannel var1) throws IOException {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);
      ByteBuffer var2 = ByteBuffer.allocate(4096);
      long var3 = 0L;

      while(var0.read(var2) != -1) {
         var2.flip();

         while(var2.hasRemaining()) {
            var3 += (long)var1.write(var2);
         }

         var2.clear();
      }

      return var3;
   }

   public static byte[] toByteArray(InputStream var0) throws IOException {
      ByteArrayOutputStream var1 = new ByteArrayOutputStream();
      copy((InputStream)var0, (OutputStream)var1);
      return var1.toByteArray();
   }

   static byte[] toByteArray(InputStream var0, int var1) throws IOException {
      byte[] var2 = new byte[var1];

      int var4;
      int var5;
      for(int var3 = var1; var3 > 0; var3 -= var5) {
         var4 = var1 - var3;
         var5 = var0.read(var2, var4, var3);
         if (var5 == -1) {
            return Arrays.copyOf(var2, var4);
         }
      }

      var4 = var0.read();
      if (var4 == -1) {
         return var2;
      } else {
         ByteStreams.FastByteArrayOutputStream var7 = new ByteStreams.FastByteArrayOutputStream();
         var7.write(var4);
         copy((InputStream)var0, (OutputStream)var7);
         byte[] var6 = new byte[var2.length + var7.size()];
         System.arraycopy(var2, 0, var6, 0, var2.length);
         var7.writeTo(var6, var2.length);
         return var6;
      }
   }

   /** @deprecated */
   @Deprecated
   public static byte[] toByteArray(InputSupplier var0) throws IOException {
      return asByteSource(var0).read();
   }

   public static ByteArrayDataInput newDataInput(byte[] var0) {
      return newDataInput(new ByteArrayInputStream(var0));
   }

   public static ByteArrayDataInput newDataInput(byte[] var0, int var1) {
      Preconditions.checkPositionIndex(var1, var0.length);
      return newDataInput(new ByteArrayInputStream(var0, var1, var0.length - var1));
   }

   public static ByteArrayDataInput newDataInput(ByteArrayInputStream var0) {
      return new ByteStreams.ByteArrayDataInputStream((ByteArrayInputStream)Preconditions.checkNotNull(var0));
   }

   public static ByteArrayDataOutput newDataOutput() {
      return newDataOutput(new ByteArrayOutputStream());
   }

   public static ByteArrayDataOutput newDataOutput(int var0) {
      Preconditions.checkArgument(var0 >= 0, "Invalid size: %s", var0);
      return newDataOutput(new ByteArrayOutputStream(var0));
   }

   public static ByteArrayDataOutput newDataOutput(ByteArrayOutputStream var0) {
      return new ByteStreams.ByteArrayDataOutputStream((ByteArrayOutputStream)Preconditions.checkNotNull(var0));
   }

   public static OutputStream nullOutputStream() {
      return NULL_OUTPUT_STREAM;
   }

   public static InputStream limit(InputStream var0, long var1) {
      return new ByteStreams.LimitedInputStream(var0, var1);
   }

   /** @deprecated */
   @Deprecated
   public static long length(InputSupplier var0) throws IOException {
      return asByteSource(var0).size();
   }

   /** @deprecated */
   @Deprecated
   public static boolean equal(InputSupplier var0, InputSupplier var1) throws IOException {
      return asByteSource(var0).contentEquals(asByteSource(var1));
   }

   public static void readFully(InputStream var0, byte[] var1) throws IOException {
      readFully(var0, var1, 0, var1.length);
   }

   public static void readFully(InputStream var0, byte[] var1, int var2, int var3) throws IOException {
      int var4 = read(var0, var1, var2, var3);
      if (var4 != var3) {
         throw new EOFException("reached end of stream after reading " + var4 + " bytes; " + var3 + " bytes expected");
      }
   }

   public static void skipFully(InputStream var0, long var1) throws IOException {
      long var3 = var1;

      while(var1 > 0L) {
         long var5 = var0.skip(var1);
         if (var5 == 0L) {
            if (var0.read() == -1) {
               long var7 = var3 - var1;
               throw new EOFException("reached end of stream after skipping " + var7 + " bytes; " + var3 + " bytes expected");
            }

            --var1;
         } else {
            var1 -= var5;
         }
      }

   }

   /** @deprecated */
   @Deprecated
   public static Object readBytes(InputSupplier var0, ByteProcessor var1) throws IOException {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);
      Closer var2 = Closer.create();

      Object var4;
      try {
         InputStream var3 = (InputStream)var2.register((Closeable)var0.getInput());
         var4 = readBytes(var3, var1);
      } catch (Throwable var6) {
         throw var2.rethrow(var6);
      }

      var2.close();
      return var4;
   }

   public static Object readBytes(InputStream var0, ByteProcessor var1) throws IOException {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);
      byte[] var2 = new byte[4096];

      int var3;
      do {
         var3 = var0.read(var2);
      } while(var3 != -1 && var1.processBytes(var2, 0, var3));

      return var1.getResult();
   }

   /** @deprecated */
   @Deprecated
   public static HashCode hash(InputSupplier var0, HashFunction var1) throws IOException {
      return asByteSource(var0).hash(var1);
   }

   public static int read(InputStream var0, byte[] var1, int var2, int var3) throws IOException {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);
      if (var3 < 0) {
         throw new IndexOutOfBoundsException("len is negative");
      } else {
         int var4;
         int var5;
         for(var4 = 0; var4 < var3; var4 += var5) {
            var5 = var0.read(var1, var2 + var4, var3 - var4);
            if (var5 == -1) {
               break;
            }
         }

         return var4;
      }
   }

   /** @deprecated */
   @Deprecated
   public static InputSupplier slice(InputSupplier var0, long var1, long var3) {
      return asInputSupplier(asByteSource(var0).slice(var1, var3));
   }

   /** @deprecated */
   @Deprecated
   public static InputSupplier join(Iterable var0) {
      Preconditions.checkNotNull(var0);
      Iterable var1 = Iterables.transform(var0, new Function() {
         public ByteSource apply(InputSupplier var1) {
            return ByteStreams.asByteSource(var1);
         }

         public Object apply(Object var1) {
            return this.apply((InputSupplier)var1);
         }
      });
      return asInputSupplier(ByteSource.concat(var1));
   }

   /** @deprecated */
   @Deprecated
   public static InputSupplier join(InputSupplier... var0) {
      return join((Iterable)Arrays.asList(var0));
   }

   /** @deprecated */
   @Deprecated
   public static ByteSource asByteSource(InputSupplier var0) {
      Preconditions.checkNotNull(var0);
      return new ByteSource(var0) {
         final InputSupplier val$supplier;

         {
            this.val$supplier = var1;
         }

         public InputStream openStream() throws IOException {
            return (InputStream)this.val$supplier.getInput();
         }

         public String toString() {
            return "ByteStreams.asByteSource(" + this.val$supplier + ")";
         }
      };
   }

   /** @deprecated */
   @Deprecated
   public static ByteSink asByteSink(OutputSupplier var0) {
      Preconditions.checkNotNull(var0);
      return new ByteSink(var0) {
         final OutputSupplier val$supplier;

         {
            this.val$supplier = var1;
         }

         public OutputStream openStream() throws IOException {
            return (OutputStream)this.val$supplier.getOutput();
         }

         public String toString() {
            return "ByteStreams.asByteSink(" + this.val$supplier + ")";
         }
      };
   }

   static InputSupplier asInputSupplier(ByteSource var0) {
      return (InputSupplier)Preconditions.checkNotNull(var0);
   }

   static OutputSupplier asOutputSupplier(ByteSink var0) {
      return (OutputSupplier)Preconditions.checkNotNull(var0);
   }

   private static final class LimitedInputStream extends FilterInputStream {
      private long left;
      private long mark = -1L;

      LimitedInputStream(InputStream var1, long var2) {
         super(var1);
         Preconditions.checkNotNull(var1);
         Preconditions.checkArgument(var2 >= 0L, "limit must be non-negative");
         this.left = var2;
      }

      public int available() throws IOException {
         return (int)Math.min((long)this.in.available(), this.left);
      }

      public synchronized void mark(int var1) {
         this.in.mark(var1);
         this.mark = this.left;
      }

      public int read() throws IOException {
         if (this.left == 0L) {
            return -1;
         } else {
            int var1 = this.in.read();
            if (var1 != -1) {
               --this.left;
            }

            return var1;
         }
      }

      public int read(byte[] var1, int var2, int var3) throws IOException {
         if (this.left == 0L) {
            return -1;
         } else {
            var3 = (int)Math.min((long)var3, this.left);
            int var4 = this.in.read(var1, var2, var3);
            if (var4 != -1) {
               this.left -= (long)var4;
            }

            return var4;
         }
      }

      public synchronized void reset() throws IOException {
         if (!this.in.markSupported()) {
            throw new IOException("Mark not supported");
         } else if (this.mark == -1L) {
            throw new IOException("Mark not set");
         } else {
            this.in.reset();
            this.left = this.mark;
         }
      }

      public long skip(long var1) throws IOException {
         var1 = Math.min(var1, this.left);
         long var3 = this.in.skip(var1);
         this.left -= var3;
         return var3;
      }
   }

   private static class ByteArrayDataOutputStream implements ByteArrayDataOutput {
      final DataOutput output;
      final ByteArrayOutputStream byteArrayOutputSteam;

      ByteArrayDataOutputStream(ByteArrayOutputStream var1) {
         this.byteArrayOutputSteam = var1;
         this.output = new DataOutputStream(var1);
      }

      public void write(int var1) {
         try {
            this.output.write(var1);
         } catch (IOException var3) {
            throw new AssertionError(var3);
         }
      }

      public void write(byte[] var1) {
         try {
            this.output.write(var1);
         } catch (IOException var3) {
            throw new AssertionError(var3);
         }
      }

      public void write(byte[] var1, int var2, int var3) {
         try {
            this.output.write(var1, var2, var3);
         } catch (IOException var5) {
            throw new AssertionError(var5);
         }
      }

      public void writeBoolean(boolean var1) {
         try {
            this.output.writeBoolean(var1);
         } catch (IOException var3) {
            throw new AssertionError(var3);
         }
      }

      public void writeByte(int var1) {
         try {
            this.output.writeByte(var1);
         } catch (IOException var3) {
            throw new AssertionError(var3);
         }
      }

      public void writeBytes(String var1) {
         try {
            this.output.writeBytes(var1);
         } catch (IOException var3) {
            throw new AssertionError(var3);
         }
      }

      public void writeChar(int var1) {
         try {
            this.output.writeChar(var1);
         } catch (IOException var3) {
            throw new AssertionError(var3);
         }
      }

      public void writeChars(String var1) {
         try {
            this.output.writeChars(var1);
         } catch (IOException var3) {
            throw new AssertionError(var3);
         }
      }

      public void writeDouble(double var1) {
         try {
            this.output.writeDouble(var1);
         } catch (IOException var4) {
            throw new AssertionError(var4);
         }
      }

      public void writeFloat(float var1) {
         try {
            this.output.writeFloat(var1);
         } catch (IOException var3) {
            throw new AssertionError(var3);
         }
      }

      public void writeInt(int var1) {
         try {
            this.output.writeInt(var1);
         } catch (IOException var3) {
            throw new AssertionError(var3);
         }
      }

      public void writeLong(long var1) {
         try {
            this.output.writeLong(var1);
         } catch (IOException var4) {
            throw new AssertionError(var4);
         }
      }

      public void writeShort(int var1) {
         try {
            this.output.writeShort(var1);
         } catch (IOException var3) {
            throw new AssertionError(var3);
         }
      }

      public void writeUTF(String var1) {
         try {
            this.output.writeUTF(var1);
         } catch (IOException var3) {
            throw new AssertionError(var3);
         }
      }

      public byte[] toByteArray() {
         return this.byteArrayOutputSteam.toByteArray();
      }
   }

   private static class ByteArrayDataInputStream implements ByteArrayDataInput {
      final DataInput input;

      ByteArrayDataInputStream(ByteArrayInputStream var1) {
         this.input = new DataInputStream(var1);
      }

      public void readFully(byte[] var1) {
         try {
            this.input.readFully(var1);
         } catch (IOException var3) {
            throw new IllegalStateException(var3);
         }
      }

      public void readFully(byte[] var1, int var2, int var3) {
         try {
            this.input.readFully(var1, var2, var3);
         } catch (IOException var5) {
            throw new IllegalStateException(var5);
         }
      }

      public int skipBytes(int var1) {
         try {
            return this.input.skipBytes(var1);
         } catch (IOException var3) {
            throw new IllegalStateException(var3);
         }
      }

      public boolean readBoolean() {
         try {
            return this.input.readBoolean();
         } catch (IOException var2) {
            throw new IllegalStateException(var2);
         }
      }

      public byte readByte() {
         try {
            return this.input.readByte();
         } catch (EOFException var2) {
            throw new IllegalStateException(var2);
         } catch (IOException var3) {
            throw new AssertionError(var3);
         }
      }

      public int readUnsignedByte() {
         try {
            return this.input.readUnsignedByte();
         } catch (IOException var2) {
            throw new IllegalStateException(var2);
         }
      }

      public short readShort() {
         try {
            return this.input.readShort();
         } catch (IOException var2) {
            throw new IllegalStateException(var2);
         }
      }

      public int readUnsignedShort() {
         try {
            return this.input.readUnsignedShort();
         } catch (IOException var2) {
            throw new IllegalStateException(var2);
         }
      }

      public char readChar() {
         try {
            return this.input.readChar();
         } catch (IOException var2) {
            throw new IllegalStateException(var2);
         }
      }

      public int readInt() {
         try {
            return this.input.readInt();
         } catch (IOException var2) {
            throw new IllegalStateException(var2);
         }
      }

      public long readLong() {
         try {
            return this.input.readLong();
         } catch (IOException var2) {
            throw new IllegalStateException(var2);
         }
      }

      public float readFloat() {
         try {
            return this.input.readFloat();
         } catch (IOException var2) {
            throw new IllegalStateException(var2);
         }
      }

      public double readDouble() {
         try {
            return this.input.readDouble();
         } catch (IOException var2) {
            throw new IllegalStateException(var2);
         }
      }

      public String readLine() {
         try {
            return this.input.readLine();
         } catch (IOException var2) {
            throw new IllegalStateException(var2);
         }
      }

      public String readUTF() {
         try {
            return this.input.readUTF();
         } catch (IOException var2) {
            throw new IllegalStateException(var2);
         }
      }
   }

   private static final class FastByteArrayOutputStream extends ByteArrayOutputStream {
      private FastByteArrayOutputStream() {
      }

      void writeTo(byte[] var1, int var2) {
         System.arraycopy(this.buf, 0, var1, var2, this.count);
      }

      FastByteArrayOutputStream(Object var1) {
         this();
      }
   }
}
