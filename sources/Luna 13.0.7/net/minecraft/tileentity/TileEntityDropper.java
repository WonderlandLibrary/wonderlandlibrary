package net.minecraft.tileentity;

public class TileEntityDropper
  extends TileEntityDispenser
{
  private static final String __OBFID = "CL_00000353";
  
  public TileEntityDropper() {}
  
  public String getName()
  {
    return hasCustomName() ? this.field_146020_a : "container.dropper";
  }
  
  public String getGuiID()
  {
    return "minecraft:dropper";
  }
}
