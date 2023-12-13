package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagShort extends NBTBase.NBTPrimitive {
   private short data;

   public int hashCode() {
      return super.hashCode() ^ this.data;
   }

   void write(DataOutput var1) throws IOException {
      var1.writeShort(this.data);
   }

   public float getFloat() {
      return (float)this.data;
   }

   public byte getByte() {
      return (byte)(this.data & 255);
   }

   public double getDouble() {
      return (double)this.data;
   }

   void read(DataInput var1, int var2, NBTSizeTracker var3) throws IOException {
      var3.read(80L);
      this.data = var1.readShort();
   }

   public long getLong() {
      return (long)this.data;
   }

   public int getInt() {
      return this.data;
   }

   public NBTTagShort() {
   }

   public NBTBase copy() {
      return new NBTTagShort(this.data);
   }

   public NBTTagShort(short var1) {
      this.data = var1;
   }

   public byte getId() {
      return 2;
   }

   public boolean equals(Object var1) {
      if (super.equals(var1)) {
         NBTTagShort var2 = (NBTTagShort)var1;
         return this.data == var2.data;
      } else {
         return false;
      }
   }

   public String toString() {
      return this.data + "s";
   }

   public short getShort() {
      return this.data;
   }
}
