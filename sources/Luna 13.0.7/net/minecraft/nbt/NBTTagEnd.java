package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;

public class NBTTagEnd
  extends NBTBase
{
  private static final String __OBFID = "CL_00001219";
  
  public NBTTagEnd() {}
  
  void read(DataInput input, int depth, NBTSizeTracker sizeTracker) {}
  
  void write(DataOutput output) {}
  
  public byte getId()
  {
    return 0;
  }
  
  public String toString()
  {
    return "END";
  }
  
  public NBTBase copy()
  {
    return new NBTTagEnd();
  }
}
