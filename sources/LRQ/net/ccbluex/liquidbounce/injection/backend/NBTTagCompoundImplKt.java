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
    public static final NBTTagCompound unwrap(INBTTagCompound $this$unwrap) {
        int $i$f$unwrap = 0;
        return (NBTTagCompound)((NBTTagCompoundImpl)$this$unwrap).getWrapped();
    }

    public static final INBTTagCompound wrap(NBTTagCompound $this$wrap) {
        int $i$f$wrap = 0;
        return new NBTTagCompoundImpl($this$wrap);
    }
}

