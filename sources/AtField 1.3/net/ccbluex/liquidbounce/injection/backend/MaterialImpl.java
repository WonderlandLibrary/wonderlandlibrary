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

    public final Material getWrapped() {
        return this.wrapped;
    }

    public boolean equals(@Nullable Object object) {
        return object instanceof MaterialImpl && ((MaterialImpl)object).wrapped.equals(this.wrapped);
    }

    @Override
    public boolean isReplaceable() {
        return this.wrapped.func_76222_j();
    }

    public MaterialImpl(Material material) {
        this.wrapped = material;
    }
}

