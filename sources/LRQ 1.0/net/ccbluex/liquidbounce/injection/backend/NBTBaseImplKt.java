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
    public static final NBTBase unwrap(INBTBase $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((NBTBaseImpl)$this$unwrap).getWrapped();
    }

    public static final INBTBase wrap(NBTBase $this$wrap) {
        int $i$f$wrap = 0;
        return new NBTBaseImpl<NBTBase>($this$wrap);
    }
}

