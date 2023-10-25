/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockPackedIce
extends Block {
    public BlockPackedIce() {
        super(Material.PACKED_ICE);
        this.slipperiness = 0.98f;
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }
}

