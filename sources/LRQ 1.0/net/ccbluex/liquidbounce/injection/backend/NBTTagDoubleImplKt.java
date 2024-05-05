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
    public static final NBTTagDouble unwrap(INBTTagDouble $this$unwrap) {
        int $i$f$unwrap = 0;
        return (NBTTagDouble)((NBTTagDoubleImpl)$this$unwrap).getWrapped();
    }

    public static final INBTTagDouble wrap(NBTTagDouble $this$wrap) {
        int $i$f$wrap = 0;
        return new NBTTagDoubleImpl<NBTTagDouble>($this$wrap);
    }
}

