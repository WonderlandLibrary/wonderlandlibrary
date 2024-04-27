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
    public static final NBTTagList unwrap(INBTTagList $this$unwrap) {
        int $i$f$unwrap = 0;
        return (NBTTagList)((NBTTagListImpl)$this$unwrap).getWrapped();
    }

    public static final INBTTagList wrap(NBTTagList $this$wrap) {
        int $i$f$wrap = 0;
        return new NBTTagListImpl($this$wrap);
    }
}

