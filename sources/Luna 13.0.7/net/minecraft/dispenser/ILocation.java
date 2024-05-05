package net.minecraft.dispenser;

import net.minecraft.world.World;

public abstract interface ILocation
  extends IPosition
{
  public abstract World getWorld();
}
