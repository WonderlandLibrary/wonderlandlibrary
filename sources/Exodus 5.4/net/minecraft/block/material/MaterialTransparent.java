/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block.material;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class MaterialTransparent
extends Material {
    @Override
    public boolean isSolid() {
        return false;
    }

    @Override
    public boolean blocksLight() {
        return false;
    }

    public MaterialTransparent(MapColor mapColor) {
        super(mapColor);
        this.setReplaceable();
    }

    @Override
    public boolean blocksMovement() {
        return false;
    }
}

