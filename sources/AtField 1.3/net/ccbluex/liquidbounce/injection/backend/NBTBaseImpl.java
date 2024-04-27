/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.NBTBase
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.nbt.INBTBase;
import net.minecraft.nbt.NBTBase;
import org.jetbrains.annotations.Nullable;

public class NBTBaseImpl
implements INBTBase {
    private final NBTBase wrapped;

    public NBTBaseImpl(NBTBase nBTBase) {
        this.wrapped = nBTBase;
    }

    public boolean equals(@Nullable Object object) {
        return object instanceof NBTBaseImpl && ((NBTBaseImpl)object).wrapped.equals(this.wrapped);
    }

    public final NBTBase getWrapped() {
        return this.wrapped;
    }
}

