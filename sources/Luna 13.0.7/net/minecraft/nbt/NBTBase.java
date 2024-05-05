package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public abstract class NBTBase
{
  public static final String[] NBT_TYPES = { "END", "BYTE", "SHORT", "INT", "LONG", "FLOAT", "DOUBLE", "BYTE[]", "STRING", "LIST", "COMPOUND", "INT[]" };
  private static final String __OBFID = "CL_00001229";
  
  public NBTBase() {}
  
  abstract void write(DataOutput paramDataOutput)
    throws IOException;
  
  abstract void read(DataInput paramDataInput, int paramInt, NBTSizeTracker paramNBTSizeTracker)
    throws IOException;
  
  public abstract String toString();
  
  public abstract byte getId();
  
  protected static NBTBase createNewByType(byte id)
  {
    switch (id)
    {
    case 0: 
      return new NBTTagEnd();
    case 1: 
      return new NBTTagByte();
    case 2: 
      return new NBTTagShort();
    case 3: 
      return new NBTTagInt();
    case 4: 
      return new NBTTagLong();
    case 5: 
      return new NBTTagFloat();
    case 6: 
      return new NBTTagDouble();
    case 7: 
      return new NBTTagByteArray();
    case 8: 
      return new NBTTagString();
    case 9: 
      return new NBTTagList();
    case 10: 
      return new NBTTagCompound();
    case 11: 
      return new NBTTagIntArray();
    }
    return null;
  }
  
  public abstract NBTBase copy();
  
  public boolean hasNoTags()
  {
    return false;
  }
  
  public boolean equals(Object p_equals_1_)
  {
    if (!(p_equals_1_ instanceof NBTBase)) {
      return false;
    }
    NBTBase var2 = (NBTBase)p_equals_1_;
    return getId() == var2.getId();
  }
  
  public int hashCode()
  {
    return getId();
  }
  
  protected String getString()
  {
    return toString();
  }
  
  public static abstract class NBTPrimitive
    extends NBTBase
  {
    private static final String __OBFID = "CL_00001230";
    
    public NBTPrimitive() {}
    
    public abstract long getLong();
    
    public abstract int getInt();
    
    public abstract short getShort();
    
    public abstract byte getByte();
    
    public abstract double getDouble();
    
    public abstract float getFloat();
  }
}
