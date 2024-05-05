/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.material.Material
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.block.material.IMaterial;
import net.minecraft.block.material.Material;
import org.jetbrains.annotations.Nullable;

public final class MaterialImpl
implements IMaterial {
    private final Material wrapped;

    @Override
    public boolean isReplaceable() {
        return this.wrapped.func_76222_j();
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof MaterialImpl && ((MaterialImpl)other).wrapped.equals(this.wrapped);
    }

    public final Material getWrapped() {
        return this.wrapped;
    }

    public MaterialImpl(Material wrapped) {
        this.wrapped = wrapped;
    }
}

