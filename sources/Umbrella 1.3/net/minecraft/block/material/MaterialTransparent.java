/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block.material;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class MaterialTransparent
extends Material {
    private static final String __OBFID = "CL_00000540";

    public MaterialTransparent(MapColor p_i2113_1_) {
        super(p_i2113_1_);
        this.setReplaceable();
    }

    @Override
    public boolean isSolid() {
        return false;
    }

    @Override
    public boolean blocksLight() {
        return false;
    }

    @Override
    public boolean blocksMovement() {
        return false;
    }
}

