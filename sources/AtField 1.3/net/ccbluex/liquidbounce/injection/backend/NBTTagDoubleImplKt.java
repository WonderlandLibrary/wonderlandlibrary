/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.NBTTagDouble
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.nbt.INBTTagDouble;
import net.ccbluex.liquidbounce.injection.backend.NBTTagDoubleImpl;
import net.minecraft.nbt.NBTTagDouble;

public final class NBTTagDoubleImplKt {
    public static final NBTTagDouble unwrap(INBTTagDouble iNBTTagDouble) {
        boolean bl = false;
        return (NBTTagDouble)((NBTTagDoubleImpl)iNBTTagDouble).getWrapped();
    }

    public static final INBTTagDouble wrap(NBTTagDouble nBTTagDouble) {
        boolean bl = false;
        return new NBTTagDoubleImpl(nBTTagDouble);
    }
}

