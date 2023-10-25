package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagEnd extends NBTBase {
  void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
    sizeTracker.read(64L);
  }
  
  void write(DataOutput output) throws IOException {}
  
  public byte getId() {
    return 0;
  }
  
  public String toString() {
    return "END";
  }
  
  public NBTBase copy() {
    return new NBTTagEnd();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\nbt\NBTTagEnd.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */