package net.minecraft.block.material;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

static final class Material.1
extends Material {
    Material.1(MapColor color) {
        super(color);
    }

    public boolean blocksMovement() {
        return false;
    }
}
