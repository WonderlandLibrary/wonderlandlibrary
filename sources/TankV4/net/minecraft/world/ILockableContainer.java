package net.minecraft.world;

import net.minecraft.inventory.IInventory;

public interface ILockableContainer extends IInteractionObject, IInventory {
   void setLockCode(LockCode var1);

   boolean isLocked();

   LockCode getLockCode();
}
