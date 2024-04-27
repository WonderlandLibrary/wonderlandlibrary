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
extends NBTBaseImpl<NBTTagCompound>
implements INBTTagCompound {
    @Override
    public boolean hasKey(String name) {
        return ((NBTTagCompound)this.getWrapped()).func_74764_b(name);
    }

    @Override
    public short getShort(String name) {
        return ((NBTTagCompound)this.getWrapped()).func_74765_d(name);
    }

    @Override
    public void setString(String key, String value) {
        ((NBTTagCompound)this.getWrapped()).func_74778_a(key, value);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void setTag(String key, INBTBase tag) {
        void $this$unwrap$iv;
        INBTBase iNBTBase = tag;
        String string = key;
        NBTTagCompound nBTTagCompound = (NBTTagCompound)this.getWrapped();
        boolean $i$f$unwrap = false;
        Object t = ((NBTBaseImpl)$this$unwrap$iv).getWrapped();
        nBTTagCompound.func_74782_a(string, t);
    }

    @Override
    public void setInteger(String key, int value) {
        ((NBTTagCompound)this.getWrapped()).func_74768_a(key, value);
    }

    public NBTTagCompoundImpl(NBTTagCompound wrapped) {
        super((NBTBase)wrapped);
    }
}

