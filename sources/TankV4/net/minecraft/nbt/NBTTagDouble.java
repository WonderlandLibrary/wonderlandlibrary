package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import net.minecraft.util.MathHelper;

public class NBTTagDouble extends NBTBase.NBTPrimitive {
   private double data;

   public byte getId() {
      return 6;
   }

   NBTTagDouble() {
   }

   void write(DataOutput var1) throws IOException {
      var1.writeDouble(this.data);
   }

   public float getFloat() {
      return (float)this.data;
   }

   public double getDouble() {
      return this.data;
   }

   public NBTBase copy() {
      return new NBTTagDouble(this.data);
   }

   void read(DataInput var1, int var2, NBTSizeTracker var3) throws IOException {
      var3.read(128L);
      this.data = var1.readDouble();
   }

   public String toString() {
      return this.data + "d";
   }

   public int getInt() {
      return MathHelper.floor_double(this.data);
   }

   public NBTTagDouble(double var1) {
      this.data = var1;
   }

   public long getLong() {
      return (long)Math.floor(this.data);
   }

   public byte getByte() {
      return (byte)(MathHelper.floor_double(this.data) & 255);
   }

   public int hashCode() {
      long var1 = Double.doubleToLongBits(this.data);
      return super.hashCode() ^ (int)(var1 ^ var1 >>> 32);
   }

   public short getShort() {
      return (short)(MathHelper.floor_double(this.data) & '\uffff');
   }

   public boolean equals(Object var1) {
      if (super.equals(var1)) {
         NBTTagDouble var2 = (NBTTagDouble)var1;
         return this.data == var2.data;
      } else {
         return false;
      }
   }
}
