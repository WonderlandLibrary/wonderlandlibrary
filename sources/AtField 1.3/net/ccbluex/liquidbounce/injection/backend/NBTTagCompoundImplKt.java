/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.NBTTagCompound
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.nbt.INBTTagCompound;
import net.ccbluex.liquidbounce.injection.backend.NBTTagCompoundImpl;
import net.minecraft.nbt.NBTTagCompound;

public final class NBTTagCompoundImplKt {
    public static final NBTTagCompound unwrap(INBTTagCompound iNBTTagCompound) {
        boolean bl = false;
        return (NBTTagCompound)((NBTTagCompoundImpl)iNBTTagCompound).getWrapped();
    }

    public static final INBTTagCompound wrap(NBTTagCompound nBTTagCompound) {
        boolean bl = false;
        return new NBTTagCompoundImpl(nBTTagCompound);
    }
}

