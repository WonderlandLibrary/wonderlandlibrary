/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.NBTTagString
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.nbt.INBTTagString;
import net.ccbluex.liquidbounce.injection.backend.NBTTagStringImpl;
import net.minecraft.nbt.NBTTagString;

public final class NBTTagStringImplKt {
    public static final INBTTagString wrap(NBTTagString nBTTagString) {
        boolean bl = false;
        return new NBTTagStringImpl(nBTTagString);
    }

    public static final NBTTagString unwrap(INBTTagString iNBTTagString) {
        boolean bl = false;
        return (NBTTagString)((NBTTagStringImpl)iNBTTagString).getWrapped();
    }
}

