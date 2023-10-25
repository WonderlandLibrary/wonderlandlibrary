/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block.material;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class MaterialLiquid
extends Material {
    @Override
    public boolean isLiquid() {
        return true;
    }

    public MaterialLiquid(MapColor mapColor) {
        super(mapColor);
        this.setReplaceable();
        this.setNoPushMobility();
    }

    @Override
    public boolean isSolid() {
        return false;
    }

    @Override
    public boolean blocksMovement() {
        return false;
    }
}

