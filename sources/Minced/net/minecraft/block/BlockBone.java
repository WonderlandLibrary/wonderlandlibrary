// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class BlockBone extends BlockRotatedPillar
{
    public BlockBone() {
        super(Material.ROCK, MapColor.SAND);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        this.setHardness(2.0f);
        this.setSoundType(SoundType.STONE);
    }
}
