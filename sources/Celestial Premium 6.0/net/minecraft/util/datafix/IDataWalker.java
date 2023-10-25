/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.util.datafix;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IDataFixer;

public interface IDataWalker {
    public NBTTagCompound process(IDataFixer var1, NBTTagCompound var2, int var3);
}

