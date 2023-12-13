package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Longs;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Beta
public class LittleEndianDataOutputStream extends FilterOutputStream implements DataOutput {
   public LittleEndianDataOutputStream(OutputStream var1) {
      super(new DataOutputStream((OutputStream)Preconditions.checkNotNull(var1)));
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      this.out.write(var1, var2, var3);
   }

   public void writeBoolean(boolean var1) throws IOException {
      ((DataOutputStream)this.out).writeBoolean(var1);
   }

   public void writeByte(int var1) throws IOException {
      ((DataOutputStream)this.out).writeByte(var1);
   }

   /** @deprecated */
   @Deprecated
   public void writeBytes(String var1) throws IOException {
      ((DataOutputStream)this.out).writeBytes(var1);
   }

   public void writeChar(int var1) throws IOException {
      this.writeShort(var1);
   }

   public void writeChars(String var1) throws IOException {
      for(int var2 = 0; var2 < var1.length(); ++var2) {
         this.writeChar(var1.charAt(var2));
      }

   }

   public void writeDouble(double var1) throws IOException {
      this.writeLong(Double.doubleToLongBits(var1));
   }

   public void writeFloat(float var1) throws IOException {
      this.writeInt(Float.floatToIntBits(var1));
   }

   public void writeInt(int var1) throws IOException {
      this.out.write(255 & var1);
      this.out.write(255 & var1 >> 8);
      this.out.write(255 & var1 >> 16);
      this.out.write(255 & var1 >> 24);
   }

   public void writeLong(long var1) throws IOException {
      byte[] var3 = Longs.toByteArray(Long.reverseBytes(var1));
      this.write(var3, 0, var3.length);
   }

   public void writeShort(int var1) throws IOException {
      this.out.write(255 & var1);
      this.out.write(255 & var1 >> 8);
   }

   public void writeUTF(String var1) throws IOException {
      ((DataOutputStream)this.out).writeUTF(var1);
   }

   public void close() throws IOException {
      this.out.close();
   }
}
