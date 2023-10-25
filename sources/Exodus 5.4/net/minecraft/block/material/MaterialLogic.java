/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block.material;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class MaterialLogic
extends Material {
    @Override
    public boolean blocksMovement() {
        return false;
    }

    public MaterialLogic(MapColor mapColor) {
        super(mapColor);
        this.setAdventureModeExempt();
    }

    @Override
    public boolean blocksLight() {
        return false;
    }

    @Override
    public boolean isSolid() {
        return false;
    }
}

