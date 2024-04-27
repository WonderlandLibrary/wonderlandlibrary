/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.NBTBase
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.nbt.INBTBase;
import net.ccbluex.liquidbounce.injection.backend.NBTBaseImpl;
import net.minecraft.nbt.NBTBase;

public final class NBTBaseImplKt {
    public static final NBTBase unwrap(INBTBase iNBTBase) {
        boolean bl = false;
        return ((NBTBaseImpl)iNBTBase).getWrapped();
    }

    public static final INBTBase wrap(NBTBase nBTBase) {
        boolean bl = false;
        return new NBTBaseImpl(nBTBase);
    }
}

