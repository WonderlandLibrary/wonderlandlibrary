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
    public static final NBTTagString unwrap(INBTTagString $this$unwrap) {
        int $i$f$unwrap = 0;
        return (NBTTagString)((NBTTagStringImpl)$this$unwrap).getWrapped();
    }

    public static final INBTTagString wrap(NBTTagString $this$wrap) {
        int $i$f$wrap = 0;
        return new NBTTagStringImpl<NBTTagString>($this$wrap);
    }
}

