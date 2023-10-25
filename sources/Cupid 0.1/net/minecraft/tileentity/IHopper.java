package net.minecraft.tileentity;

import net.minecraft.inventory.IInventory;
import net.minecraft.world.World;

public interface IHopper extends IInventory {
  World getWorld();
  
  double getXPos();
  
  double getYPos();
  
  double getZPos();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\tileentity\IHopper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */