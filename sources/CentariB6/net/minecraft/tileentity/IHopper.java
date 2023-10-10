package net.minecraft.tileentity;

import net.minecraft.inventory.IInventory;
import net.minecraft.world.World;

public interface IHopper extends IInventory {
   World getWorld();

   double getYPos();

   double getZPos();

   double getXPos();
}
