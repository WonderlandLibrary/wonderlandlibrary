package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagInt extends NBTBase.NBTPrimitive {
   private int data;

   public int hashCode() {
      return super.hashCode() ^ this.data;
   }

   public byte getByte() {
      return (byte)(this.data & 255);
   }

   public int getInt() {
      return this.data;
   }

   void read(DataInput var1, int var2, NBTSizeTracker var3) throws IOException {
      var3.read(96L);
      this.data = var1.readInt();
   }

   void write(DataOutput var1) throws IOException {
      var1.writeInt(this.data);
   }

   public NBTBase copy() {
      return new NBTTagInt(this.data);
   }

   public String toString() {
      return "" + this.data;
   }

   NBTTagInt() {
   }

   public long getLong() {
      return (long)this.data;
   }

   public boolean equals(Object var1) {
      if (super.equals(var1)) {
         NBTTagInt var2 = (NBTTagInt)var1;
         return this.data == var2.data;
      } else {
         return false;
      }
   }

   public float getFloat() {
      return (float)this.data;
   }

   public NBTTagInt(int var1) {
      this.data = var1;
   }

   public double getDouble() {
      return (double)this.data;
   }

   public short getShort() {
      return (short)(this.data & '\uffff');
   }

   public byte getId() {
      return 3;
   }
}
