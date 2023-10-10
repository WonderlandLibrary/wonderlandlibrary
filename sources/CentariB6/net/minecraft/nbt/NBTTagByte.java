package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTSizeTracker;

public class NBTTagByte extends NBTBase.NBTPrimitive {
   private byte data;

   NBTTagByte() {
   }

   public NBTTagByte(byte data) {
      this.data = data;
   }

   public boolean equals(Object p_equals_1_) {
      if(super.equals(p_equals_1_)) {
         NBTTagByte nbttagbyte = (NBTTagByte)p_equals_1_;
         return this.data == nbttagbyte.data;
      } else {
         return false;
      }
   }

   public String toString() {
      return "" + this.data + "b";
   }

   public int hashCode() {
      return super.hashCode() ^ this.data;
   }

   public byte getByte() {
      return this.data;
   }

   public short getShort() {
      return (short)this.data;
   }

   public int getInt() {
      return this.data;
   }

   public long getLong() {
      return (long)this.data;
   }

   public float getFloat() {
      return (float)this.data;
   }

   public double getDouble() {
      return (double)this.data;
   }

   void write(DataOutput output) throws IOException {
      output.writeByte(this.data);
   }

   void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
      sizeTracker.read(72L);
      this.data = input.readByte();
   }

   public byte getId() {
      return (byte)1;
   }

   public NBTBase copy() {
      return new NBTTagByte(this.data);
   }
}
