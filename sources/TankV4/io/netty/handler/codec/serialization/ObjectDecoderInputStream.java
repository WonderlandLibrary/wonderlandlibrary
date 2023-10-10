package io.netty.handler.codec.serialization;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.StreamCorruptedException;

public class ObjectDecoderInputStream extends InputStream implements ObjectInput {
   private final DataInputStream in;
   private final int maxObjectSize;
   private final ClassResolver classResolver;

   public ObjectDecoderInputStream(InputStream var1) {
      this(var1, (ClassLoader)null);
   }

   public ObjectDecoderInputStream(InputStream var1, ClassLoader var2) {
      this(var1, var2, 1048576);
   }

   public ObjectDecoderInputStream(InputStream var1, int var2) {
      this(var1, (ClassLoader)null, var2);
   }

   public ObjectDecoderInputStream(InputStream var1, ClassLoader var2, int var3) {
      if (var1 == null) {
         throw new NullPointerException("in");
      } else if (var3 <= 0) {
         throw new IllegalArgumentException("maxObjectSize: " + var3);
      } else {
         if (var1 instanceof DataInputStream) {
            this.in = (DataInputStream)var1;
         } else {
            this.in = new DataInputStream(var1);
         }

         this.classResolver = ClassResolvers.weakCachingResolver(var2);
         this.maxObjectSize = var3;
      }
   }

   public Object readObject() throws ClassNotFoundException, IOException {
      int var1 = this.readInt();
      if (var1 <= 0) {
         throw new StreamCorruptedException("invalid data length: " + var1);
      } else if (var1 > this.maxObjectSize) {
         throw new StreamCorruptedException("data length too big: " + var1 + " (max: " + this.maxObjectSize + ')');
      } else {
         return (new CompactObjectInputStream(this.in, this.classResolver)).readObject();
      }
   }

   public int available() throws IOException {
      return this.in.available();
   }

   public void close() throws IOException {
      this.in.close();
   }

   public void mark(int var1) {
      this.in.mark(var1);
   }

   public boolean markSupported() {
      return this.in.markSupported();
   }

   public int read() throws IOException {
      return this.in.read();
   }

   public final int read(byte[] var1, int var2, int var3) throws IOException {
      return this.in.read(var1, var2, var3);
   }

   public final int read(byte[] var1) throws IOException {
      return this.in.read(var1);
   }

   public final boolean readBoolean() throws IOException {
      return this.in.readBoolean();
   }

   public final byte readByte() throws IOException {
      return this.in.readByte();
   }

   public final char readChar() throws IOException {
      return this.in.readChar();
   }

   public final double readDouble() throws IOException {
      return this.in.readDouble();
   }

   public final float readFloat() throws IOException {
      return this.in.readFloat();
   }

   public final void readFully(byte[] var1, int var2, int var3) throws IOException {
      this.in.readFully(var1, var2, var3);
   }

   public final void readFully(byte[] var1) throws IOException {
      this.in.readFully(var1);
   }

   public final int readInt() throws IOException {
      return this.in.readInt();
   }

   /** @deprecated */
   @Deprecated
   public final String readLine() throws IOException {
      return this.in.readLine();
   }

   public final long readLong() throws IOException {
      return this.in.readLong();
   }

   public final short readShort() throws IOException {
      return this.in.readShort();
   }

   public final int readUnsignedByte() throws IOException {
      return this.in.readUnsignedByte();
   }

   public final int readUnsignedShort() throws IOException {
      return this.in.readUnsignedShort();
   }

   public final String readUTF() throws IOException {
      return this.in.readUTF();
   }

   public void reset() throws IOException {
      this.in.reset();
   }

   public long skip(long var1) throws IOException {
      return this.in.skip(var1);
   }

   public final int skipBytes(int var1) throws IOException {
      return this.in.skipBytes(var1);
   }
}
