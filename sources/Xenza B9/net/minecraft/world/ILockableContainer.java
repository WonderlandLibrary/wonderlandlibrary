// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.world;

import net.minecraft.inventory.IInventory;

public interface ILockableContainer extends IInventory, IInteractionObject
{
    boolean isLocked();
    
    void setLockCode(final LockCode p0);
    
    LockCode getLockCode();
}
