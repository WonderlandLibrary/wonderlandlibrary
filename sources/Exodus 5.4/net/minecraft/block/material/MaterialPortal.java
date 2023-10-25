/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block.material;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class MaterialPortal
extends Material {
    @Override
    public boolean isSolid() {
        return false;
    }

    public MaterialPortal(MapColor mapColor) {
        super(mapColor);
    }

    @Override
    public boolean blocksMovement() {
        return false;
    }

    @Override
    public boolean blocksLight() {
        return false;
    }
}

