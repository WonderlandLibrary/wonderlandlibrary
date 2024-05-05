package net.minecraft.world;

import net.minecraft.inventory.IInventory;

public abstract interface ILockableContainer
  extends IInventory, IInteractionObject
{
  public abstract boolean isLocked();
  
  public abstract void setLockCode(LockCode paramLockCode);
  
  public abstract LockCode getLockCode();
}
