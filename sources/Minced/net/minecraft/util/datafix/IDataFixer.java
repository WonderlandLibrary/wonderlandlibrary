// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util.datafix;

import net.minecraft.nbt.NBTTagCompound;

public interface IDataFixer
{
    NBTTagCompound process(final IFixType p0, final NBTTagCompound p1, final int p2);
}
