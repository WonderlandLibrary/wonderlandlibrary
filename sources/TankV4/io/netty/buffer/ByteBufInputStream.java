package io.netty.buffer;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class ByteBufInputStream extends InputStream implements DataInput {
   private final ByteBuf buffer;
   private final int startIndex;
   private final int endIndex;
   private final StringBuilder lineBuf;

   public ByteBufInputStream(ByteBuf var1) {
      this(var1, var1.readableBytes());
   }

   public ByteBufInputStream(ByteBuf var1, int var2) {
      this.lineBuf = new StringBuilder();
      if (var1 == null) {
         throw new NullPointerException("buffer");
      } else if (var2 < 0) {
         throw new IllegalArgumentException("length: " + var2);
      } else if (var2 > var1.readableBytes()) {
         throw new IndexOutOfBoundsException("Too many bytes to be read - Needs " + var2 + ", maximum is " + var1.readableBytes());
      } else {
         this.buffer = var1;
         this.startIndex = var1.readerIndex();
         this.endIndex = this.startIndex + var2;
         var1.markReaderIndex();
      }
   }

   public int readBytes() {
      return this.buffer.readerIndex() - this.startIndex;
   }

   public int available() throws IOException {
      return this.endIndex - this.buffer.readerIndex();
   }

   public void mark(int var1) {
      this.buffer.markReaderIndex();
   }

   public boolean markSupported() {
      return true;
   }

   public int read() throws IOException {
      return !this.buffer.isReadable() ? -1 : this.buffer.readByte() & 255;
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      int var4 = this.available();
      if (var4 == 0) {
         return -1;
      } else {
         var3 = Math.min(var4, var3);
         this.buffer.readBytes(var1, var2, var3);
         return var3;
      }
   }

   public void reset() throws IOException {
      this.buffer.resetReaderIndex();
   }

   public long skip(long var1) throws IOException {
      return var1 > 2147483647L ? (long)this.skipBytes(Integer.MAX_VALUE) : (long)this.skipBytes((int)var1);
   }

   public boolean readBoolean() throws IOException {
      this.checkAvailable(1);
      return this.read() != 0;
   }

   public byte readByte() throws IOException {
      if (!this.buffer.isReadable()) {
         throw new EOFException();
      } else {
         return this.buffer.readByte();
      }
   }

   public char readChar() throws IOException {
      return (char)this.readShort();
   }

   public double readDouble() throws IOException {
      return Double.longBitsToDouble(this.readLong());
   }

   public float readFloat() throws IOException {
      return Float.intBitsToFloat(this.readInt());
   }

   public void readFully(byte[] var1) throws IOException {
      this.readFully(var1, 0, var1.length);
   }

   public void readFully(byte[] var1, int var2, int var3) throws IOException {
      this.checkAvailable(var3);
      this.buffer.readBytes(var1, var2, var3);
   }

   public int readInt() throws IOException {
      this.checkAvailable(4);
      return this.buffer.readInt();
   }

   public String readLine() throws IOException {
      this.lineBuf.setLength(0);

      while(this.buffer.isReadable()) {
         short var1 = this.buffer.readUnsignedByte();
         switch(var1) {
         case 13:
            if (this.buffer.isReadable() && this.buffer.getUnsignedByte(this.buffer.readerIndex()) == 10) {
               this.buffer.skipBytes(1);
            }
         case 10:
            return this.lineBuf.toString();
         default:
            this.lineBuf.append((char)var1);
         }
      }

      return this.lineBuf.length() > 0 ? this.lineBuf.toString() : null;
   }

   public long readLong() throws IOException {
      this.checkAvailable(8);
      return this.buffer.readLong();
   }

   public short readShort() throws IOException {
      this.checkAvailable(2);
      return this.buffer.readShort();
   }

   public String readUTF() throws IOException {
      return DataInputStream.readUTF(this);
   }

   public int readUnsignedByte() throws IOException {
      return this.readByte() & 255;
   }

   public int readUnsignedShort() throws IOException {
      return this.readShort() & '\uffff';
   }

   public int skipBytes(int var1) throws IOException {
      int var2 = Math.min(this.available(), var1);
      this.buffer.skipBytes(var2);
      return var2;
   }

   private void checkAvailable(int var1) throws IOException {
      if (var1 < 0) {
         throw new IndexOutOfBoundsException("fieldSize cannot be a negative number");
      } else if (var1 > this.available()) {
         throw new EOFException("fieldSize is too long! Length is " + var1 + ", but maximum is " + this.available());
      }
   }
}
