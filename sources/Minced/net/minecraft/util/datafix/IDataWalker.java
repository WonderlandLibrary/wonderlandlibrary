// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util.datafix;

import net.minecraft.nbt.NBTTagCompound;

public interface IDataWalker
{
    NBTTagCompound process(final IDataFixer p0, final NBTTagCompound p1, final int p2);
}
