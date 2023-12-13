/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block.material;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class MaterialLogic
extends Material {
    private static final String __OBFID = "CL_00000539";

    public MaterialLogic(MapColor p_i2112_1_) {
        super(p_i2112_1_);
        this.setAdventureModeExempt();
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

