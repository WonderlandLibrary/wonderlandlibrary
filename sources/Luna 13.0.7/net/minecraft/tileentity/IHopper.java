package net.minecraft.tileentity;

import net.minecraft.inventory.IInventory;
import net.minecraft.world.World;

public abstract interface IHopper
  extends IInventory
{
  public abstract World getWorld();
  
  public abstract double getXPos();
  
  public abstract double getYPos();
  
  public abstract double getZPos();
}
