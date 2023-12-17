// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.storage;

import javax.annotation.Nullable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.player.EntityPlayer;

public interface IPlayerFileData
{
    void writePlayerData(final EntityPlayer p0);
    
    @Nullable
    NBTTagCompound readPlayerData(final EntityPlayer p0);
    
    String[] getAvailablePlayerDat();
}
