package io.netty.handler.codec.serialization;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.OutputStream;

public class ObjectEncoderOutputStream extends OutputStream implements ObjectOutput {
   private final DataOutputStream out;
   private final int estimatedLength;

   public ObjectEncoderOutputStream(OutputStream var1) {
      this(var1, 512);
   }

   public ObjectEncoderOutputStream(OutputStream var1, int var2) {
      if (var1 == null) {
         throw new NullPointerException("out");
      } else if (var2 < 0) {
         throw new IllegalArgumentException("estimatedLength: " + var2);
      } else {
         if (var1 instanceof DataOutputStream) {
            this.out = (DataOutputStream)var1;
         } else {
            this.out = new DataOutputStream(var1);
         }

         this.estimatedLength = var2;
      }
   }

   public void writeObject(Object var1) throws IOException {
      ByteBufOutputStream var2 = new ByteBufOutputStream(Unpooled.buffer(this.estimatedLength));
      CompactObjectOutputStream var3 = new CompactObjectOutputStream(var2);
      var3.writeObject(var1);
      var3.flush();
      var3.close();
      ByteBuf var4 = var2.buffer();
      int var5 = var4.readableBytes();
      this.writeInt(var5);
      var4.getBytes(0, (OutputStream)this, var5);
   }

   public void write(int var1) throws IOException {
      this.out.write(var1);
   }

   public void close() throws IOException {
      this.out.close();
   }

   public void flush() throws IOException {
      this.out.flush();
   }

   public final int size() {
      return this.out.size();
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      this.out.write(var1, var2, var3);
   }

   public void write(byte[] var1) throws IOException {
      this.out.write(var1);
   }

   public final void writeBoolean(boolean var1) throws IOException {
      this.out.writeBoolean(var1);
   }

   public final void writeByte(int var1) throws IOException {
      this.out.writeByte(var1);
   }

   public final void writeBytes(String var1) throws IOException {
      this.out.writeBytes(var1);
   }

   public final void writeChar(int var1) throws IOException {
      this.out.writeChar(var1);
   }

   public final void writeChars(String var1) throws IOException {
      this.out.writeChars(var1);
   }

   public final void writeDouble(double var1) throws IOException {
      this.out.writeDouble(var1);
   }

   public final void writeFloat(float var1) throws IOException {
      this.out.writeFloat(var1);
   }

   public final void writeInt(int var1) throws IOException {
      this.out.writeInt(var1);
   }

   public final void writeLong(long var1) throws IOException {
      this.out.writeLong(var1);
   }

   public final void writeShort(int var1) throws IOException {
      this.out.writeShort(var1);
   }

   public final void writeUTF(String var1) throws IOException {
      this.out.writeUTF(var1);
   }
}
