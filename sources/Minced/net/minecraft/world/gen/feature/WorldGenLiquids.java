// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.feature;

import net.minecraft.block.state.IBlockState;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.block.Block;

public class WorldGenLiquids extends WorldGenerator
{
    private final Block block;
    
    public WorldGenLiquids(final Block blockIn) {
        this.block = blockIn;
    }
    
    @Override
    public boolean generate(final World worldIn, final Random rand, final BlockPos position) {
        if (worldIn.getBlockState(position.up()).getBlock() != Blocks.STONE) {
            return false;
        }
        if (worldIn.getBlockState(position.down()).getBlock() != Blocks.STONE) {
            return false;
        }
        final IBlockState iblockstate = worldIn.getBlockState(position);
        if (iblockstate.getMaterial() != Material.AIR && iblockstate.getBlock() != Blocks.STONE) {
            return false;
        }
        int i = 0;
        if (worldIn.getBlockState(position.west()).getBlock() == Blocks.STONE) {
            ++i;
        }
        if (worldIn.getBlockState(position.east()).getBlock() == Blocks.STONE) {
            ++i;
        }
        if (worldIn.getBlockState(position.north()).getBlock() == Blocks.STONE) {
            ++i;
        }
        if (worldIn.getBlockState(position.south()).getBlock() == Blocks.STONE) {
            ++i;
        }
        int j = 0;
        if (worldIn.isAirBlock(position.west())) {
            ++j;
        }
        if (worldIn.isAirBlock(position.east())) {
            ++j;
        }
        if (worldIn.isAirBlock(position.north())) {
            ++j;
        }
        if (worldIn.isAirBlock(position.south())) {
            ++j;
        }
        if (i == 3 && j == 1) {
            final IBlockState iblockstate2 = this.block.getDefaultState();
            worldIn.setBlockState(position, iblockstate2, 2);
            worldIn.immediateBlockTick(position, iblockstate2, rand);
        }
        return true;
    }
}
