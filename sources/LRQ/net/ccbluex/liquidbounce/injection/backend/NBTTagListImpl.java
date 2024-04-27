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
extends NBTBaseImpl<NBTTagList>
implements INBTTagList {
    @Override
    public boolean hasNoTags() {
        return ((NBTTagList)this.getWrapped()).func_82582_d();
    }

    @Override
    public int tagCount() {
        return ((NBTTagList)this.getWrapped()).func_74745_c();
    }

    @Override
    public INBTTagCompound getCompoundTagAt(int index) {
        NBTTagCompound $this$wrap$iv = ((NBTTagList)this.getWrapped()).func_150305_b(index);
        boolean $i$f$wrap = false;
        return new NBTTagCompoundImpl($this$wrap$iv);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void appendTag(INBTBase createNBTTagString) {
        void $this$unwrap$iv;
        INBTBase iNBTBase = createNBTTagString;
        NBTTagList nBTTagList = (NBTTagList)this.getWrapped();
        boolean $i$f$unwrap = false;
        Object t = ((NBTBaseImpl)$this$unwrap$iv).getWrapped();
        nBTTagList.func_74742_a(t);
    }

    public NBTTagListImpl(NBTTagList wrapped) {
        super((NBTBase)wrapped);
    }
}

