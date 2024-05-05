package net.minecraft.world.border;

public enum EnumBorderStatus
{
  private final int id;
  private static final EnumBorderStatus[] $VALUES = { GROWING, SHRINKING, STATIONARY };
  private static final String __OBFID = "CL_00002013";
  
  private EnumBorderStatus(String p_i45647_1_, int p_i45647_2_, int id)
  {
    this.id = id;
  }
  
  public int func_177766_a()
  {
    return this.id;
  }
}
