package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagInt extends NBTBase.NBTPrimitive {
  private int data;
  
  NBTTagInt() {}
  
  public NBTTagInt(int data) {
    this.data = data;
  }
  
  void write(DataOutput output) throws IOException {
    output.writeInt(this.data);
  }
  
  void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
    sizeTracker.read(96L);
    this.data = input.readInt();
  }
  
  public byte getId() {
    return 3;
  }
  
  public String toString() {
    return "" + this.data;
  }
  
  public NBTBase copy() {
    return new NBTTagInt(this.data);
  }
  
  public boolean equals(Object p_equals_1_) {
    if (super.equals(p_equals_1_)) {
      NBTTagInt nbttagint = (NBTTagInt)p_equals_1_;
      return (this.data == nbttagint.data);
    } 
    return false;
  }
  
  public int hashCode() {
    return super.hashCode() ^ this.data;
  }
  
  public long getLong() {
    return this.data;
  }
  
  public int getInt() {
    return this.data;
  }
  
  public short getShort() {
    return (short)(this.data & 0xFFFF);
  }
  
  public byte getByte() {
    return (byte)(this.data & 0xFF);
  }
  
  public double getDouble() {
    return this.data;
  }
  
  public float getFloat() {
    return this.data;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\nbt\NBTTagInt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */