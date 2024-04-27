/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.NBTTagList
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.nbt.INBTTagList;
import net.ccbluex.liquidbounce.injection.backend.NBTTagListImpl;
import net.minecraft.nbt.NBTTagList;

public final class NBTTagListImplKt {
    public static final NBTTagList unwrap(INBTTagList iNBTTagList) {
        boolean bl = false;
        return (NBTTagList)((NBTTagListImpl)iNBTTagList).getWrapped();
    }

    public static final INBTTagList wrap(NBTTagList nBTTagList) {
        boolean bl = false;
        return new NBTTagListImpl(nBTTagList);
    }
}

