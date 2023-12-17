// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.feature;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.init.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import java.util.Random;
import net.minecraft.world.World;

public class WorldGeneratorBonusChest extends WorldGenerator
{
    @Override
    public boolean generate(final World worldIn, final Random rand, BlockPos position) {
        for (IBlockState iblockstate = worldIn.getBlockState(position); (iblockstate.getMaterial() == Material.AIR || iblockstate.getMaterial() == Material.LEAVES) && position.getY() > 1; position = position.down(), iblockstate = worldIn.getBlockState(position)) {}
        if (position.getY() < 1) {
            return false;
        }
        position = position.up();
        for (int i = 0; i < 4; ++i) {
            final BlockPos blockpos = position.add(rand.nextInt(4) - rand.nextInt(4), rand.nextInt(3) - rand.nextInt(3), rand.nextInt(4) - rand.nextInt(4));
            if (worldIn.isAirBlock(blockpos) && worldIn.getBlockState(blockpos.down()).isTopSolid()) {
                worldIn.setBlockState(blockpos, Blocks.CHEST.getDefaultState(), 2);
                final TileEntity tileentity = worldIn.getTileEntity(blockpos);
                if (tileentity instanceof TileEntityChest) {
                    ((TileEntityChest)tileentity).setLootTable(LootTableList.CHESTS_SPAWN_BONUS_CHEST, rand.nextLong());
                }
                final BlockPos blockpos2 = blockpos.east();
                final BlockPos blockpos3 = blockpos.west();
                final BlockPos blockpos4 = blockpos.north();
                final BlockPos blockpos5 = blockpos.south();
                if (worldIn.isAirBlock(blockpos3) && worldIn.getBlockState(blockpos3.down()).isTopSolid()) {
                    worldIn.setBlockState(blockpos3, Blocks.TORCH.getDefaultState(), 2);
                }
                if (worldIn.isAirBlock(blockpos2) && worldIn.getBlockState(blockpos2.down()).isTopSolid()) {
                    worldIn.setBlockState(blockpos2, Blocks.TORCH.getDefaultState(), 2);
                }
                if (worldIn.isAirBlock(blockpos4) && worldIn.getBlockState(blockpos4.down()).isTopSolid()) {
                    worldIn.setBlockState(blockpos4, Blocks.TORCH.getDefaultState(), 2);
                }
                if (worldIn.isAirBlock(blockpos5) && worldIn.getBlockState(blockpos5.down()).isTopSolid()) {
                    worldIn.setBlockState(blockpos5, Blocks.TORCH.getDefaultState(), 2);
                }
                return true;
            }
        }
        return false;
    }
}
