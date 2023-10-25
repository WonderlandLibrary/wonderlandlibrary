package net.minecraft.tileentity;

public class TileEntityDropper extends TileEntityDispenser {
  public String getName() {
    return hasCustomName() ? this.customName : "container.dropper";
  }
  
  public String getGuiID() {
    return "minecraft:dropper";
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\tileentity\TileEntityDropper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */