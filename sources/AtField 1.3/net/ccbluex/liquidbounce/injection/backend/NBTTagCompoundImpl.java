/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.NBTBase
 *  net.minecraft.nbt.NBTTagCompound
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.nbt.INBTBase;
import net.ccbluex.liquidbounce.api.minecraft.nbt.INBTTagCompound;
import net.ccbluex.liquidbounce.injection.backend.NBTBaseImpl;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public final class NBTTagCompoundImpl
extends NBTBaseImpl
implements INBTTagCompound {
    public NBTTagCompoundImpl(NBTTagCompound nBTTagCompound) {
        super((NBTBase)nBTTagCompound);
    }

    @Override
    public void setInteger(String string, int n) {
        ((NBTTagCompound)this.getWrapped()).func_74768_a(string, n);
    }

    @Override
    public short getShort(String string) {
        return ((NBTTagCompound)this.getWrapped()).func_74765_d(string);
    }

    @Override
    public boolean hasKey(String string) {
        return ((NBTTagCompound)this.getWrapped()).func_74764_b(string);
    }

    @Override
    public void setTag(String string, INBTBase iNBTBase) {
        INBTBase iNBTBase2 = iNBTBase;
        String string2 = string;
        NBTTagCompound nBTTagCompound = (NBTTagCompound)this.getWrapped();
        boolean bl = false;
        NBTBase nBTBase = ((NBTBaseImpl)iNBTBase2).getWrapped();
        nBTTagCompound.func_74782_a(string2, nBTBase);
    }

    @Override
    public void setString(String string, String string2) {
        ((NBTTagCompound)this.getWrapped()).func_74778_a(string, string2);
    }
}

