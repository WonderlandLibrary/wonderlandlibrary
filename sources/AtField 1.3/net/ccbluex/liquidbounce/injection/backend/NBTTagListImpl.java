/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.NBTBase
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.nbt.NBTTagList
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.nbt.INBTBase;
import net.ccbluex.liquidbounce.api.minecraft.nbt.INBTTagCompound;
import net.ccbluex.liquidbounce.api.minecraft.nbt.INBTTagList;
import net.ccbluex.liquidbounce.injection.backend.NBTBaseImpl;
import net.ccbluex.liquidbounce.injection.backend.NBTTagCompoundImpl;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public final class NBTTagListImpl
extends NBTBaseImpl
implements INBTTagList {
    @Override
    public boolean hasNoTags() {
        return ((NBTTagList)this.getWrapped()).func_82582_d();
    }

    @Override
    public int tagCount() {
        return ((NBTTagList)this.getWrapped()).func_74745_c();
    }

    public NBTTagListImpl(NBTTagList nBTTagList) {
        super((NBTBase)nBTTagList);
    }

    @Override
    public void appendTag(INBTBase iNBTBase) {
        INBTBase iNBTBase2 = iNBTBase;
        NBTTagList nBTTagList = (NBTTagList)this.getWrapped();
        boolean bl = false;
        NBTBase nBTBase = ((NBTBaseImpl)iNBTBase2).getWrapped();
        nBTTagList.func_74742_a(nBTBase);
    }

    @Override
    public INBTTagCompound getCompoundTagAt(int n) {
        NBTTagCompound nBTTagCompound = ((NBTTagList)this.getWrapped()).func_150305_b(n);
        boolean bl = false;
        return new NBTTagCompoundImpl(nBTTagCompound);
    }
}

