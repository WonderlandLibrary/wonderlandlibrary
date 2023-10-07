// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.tileentity;

import net.minecraft.world.World;
import net.minecraft.inventory.IInventory;

public interface IHopper extends IInventory
{
    World getWorld();
    
    double getXPos();
    
    double getYPos();
    
    double getZPos();
}
