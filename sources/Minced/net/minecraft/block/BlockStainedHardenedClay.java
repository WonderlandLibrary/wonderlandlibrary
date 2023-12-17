// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.properties.IProperty;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MapColor;

public class BlockStainedHardenedClay extends BlockColored
{
    private static final MapColor[] MAP_COLORS;
    
    public BlockStainedHardenedClay() {
        super(Material.ROCK);
    }
    
    @Override
    @Deprecated
    public MapColor getMapColor(final IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        return BlockStainedHardenedClay.MAP_COLORS[state.getValue(BlockStainedHardenedClay.COLOR).getMetadata()];
    }
    
    static {
        MAP_COLORS = new MapColor[] { MapColor.WHITE_STAINED_HARDENED_CLAY, MapColor.ORANGE_STAINED_HARDENED_CLAY, MapColor.MAGENTA_STAINED_HARDENED_CLAY, MapColor.LIGHT_BLUE_STAINED_HARDENED_CLAY, MapColor.YELLOW_STAINED_HARDENED_CLAY, MapColor.LIME_STAINED_HARDENED_CLAY, MapColor.PINK_STAINED_HARDENED_CLAY, MapColor.GRAY_STAINED_HARDENED_CLAY, MapColor.SILVER_STAINED_HARDENED_CLAY, MapColor.CYAN_STAINED_HARDENED_CLAY, MapColor.PURPLE_STAINED_HARDENED_CLAY, MapColor.BLUE_STAINED_HARDENED_CLAY, MapColor.BROWN_STAINED_HARDENED_CLAY, MapColor.GREEN_STAINED_HARDENED_CLAY, MapColor.RED_STAINED_HARDENED_CLAY, MapColor.BLACK_STAINED_HARDENED_CLAY };
    }
}
