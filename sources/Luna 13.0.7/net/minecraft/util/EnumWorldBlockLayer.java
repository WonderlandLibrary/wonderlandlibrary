package net.minecraft.util;

public enum EnumWorldBlockLayer
{
  private final String field_180338_e;
  private static final EnumWorldBlockLayer[] $VALUES = { SOLID, CUTOUT_MIPPED, CUTOUT, TRANSLUCENT };
  private static final String __OBFID = "CL_00002152";
  
  private EnumWorldBlockLayer(String p_i45755_1_, int p_i45755_2_, String p_i45755_3_)
  {
    this.field_180338_e = p_i45755_3_;
  }
  
  public String toString()
  {
    return this.field_180338_e;
  }
}
