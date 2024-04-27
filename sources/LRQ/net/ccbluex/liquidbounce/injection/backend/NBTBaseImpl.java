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

public class NBTBaseImpl<T extends NBTBase>
implements INBTBase {
    private final T wrapped;

    public boolean equals(@Nullable Object other) {
        return other instanceof NBTBaseImpl && ((NBTBaseImpl)other).wrapped.equals(this.wrapped);
    }

    public final T getWrapped() {
        return this.wrapped;
    }

    public NBTBaseImpl(T wrapped) {
        this.wrapped = wrapped;
    }
}

