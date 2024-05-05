package net.minecraft.entity.boss;

import net.minecraft.util.IChatComponent;

public abstract interface IBossDisplayData
{
  public abstract float getMaxHealth();
  
  public abstract float getHealth();
  
  public abstract IChatComponent getDisplayName();
}
