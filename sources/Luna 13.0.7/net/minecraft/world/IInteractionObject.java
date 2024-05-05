package net.minecraft.world;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

public abstract interface IInteractionObject
  extends IWorldNameable
{
  public abstract Container createContainer(InventoryPlayer paramInventoryPlayer, EntityPlayer paramEntityPlayer);
  
  public abstract String getGuiID();
}
