package net.minecraft.world;

import net.minecraft.util.IChatComponent;

public abstract interface IWorldNameable
{
  public abstract String getName();
  
  public abstract boolean hasCustomName();
  
  public abstract IChatComponent getDisplayName();
}
