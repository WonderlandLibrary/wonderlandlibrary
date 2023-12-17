// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import net.minecraft.block.properties.IProperty;
import net.minecraft.init.Blocks;
import java.util.Iterator;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;

public class BlockMushroom extends BlockBush implements IGrowable
{
    protected static final AxisAlignedBB MUSHROOM_AABB;
    
    protected BlockMushroom() {
        this.setTickRandomly(true);
    }
    
    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
        return BlockMushroom.MUSHROOM_AABB;
    }
    
    @Override
    public void updateTick(final World worldIn, BlockPos pos, final IBlockState state, final Random rand) {
        if (rand.nextInt(25) == 0) {
            int i = 5;
            final int j = 4;
            for (final BlockPos blockpos : BlockPos.getAllInBoxMutable(pos.add(-4, -1, -4), pos.add(4, 1, 4))) {
                if (worldIn.getBlockState(blockpos).getBlock() == this && --i <= 0) {
                    return;
                }
            }
            BlockPos blockpos2 = pos.add(rand.nextInt(3) - 1, rand.nextInt(2) - rand.nextInt(2), rand.nextInt(3) - 1);
            for (int k = 0; k < 4; ++k) {
                if (worldIn.isAirBlock(blockpos2) && this.canBlockStay(worldIn, blockpos2, this.getDefaultState())) {
                    pos = blockpos2;
                }
                blockpos2 = pos.add(rand.nextInt(3) - 1, rand.nextInt(2) - rand.nextInt(2), rand.nextInt(3) - 1);
            }
            if (worldIn.isAirBlock(blockpos2) && this.canBlockStay(worldIn, blockpos2, this.getDefaultState())) {
                worldIn.setBlockState(blockpos2, this.getDefaultState(), 2);
            }
        }
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos) && this.canBlockStay(worldIn, pos, this.getDefaultState());
    }
    
    @Override
    protected boolean canSustainBush(final IBlockState state) {
        return state.isFullBlock();
    }
    
    @Override
    public boolean canBlockStay(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (pos.getY() >= 0 && pos.getY() < 256) {
            final IBlockState iblockstate = worldIn.getBlockState(pos.down());
            return iblockstate.getBlock() == Blocks.MYCELIUM || (iblockstate.getBlock() == Blocks.DIRT && iblockstate.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.PODZOL) || (worldIn.getLight(pos) < 13 && this.canSustainBush(iblockstate));
        }
        return false;
    }
    
    public boolean generateBigMushroom(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        worldIn.setBlockToAir(pos);
        WorldGenerator worldgenerator = null;
        if (this == Blocks.BROWN_MUSHROOM) {
            worldgenerator = new WorldGenBigMushroom(Blocks.BROWN_MUSHROOM_BLOCK);
        }
        else if (this == Blocks.RED_MUSHROOM) {
            worldgenerator = new WorldGenBigMushroom(Blocks.RED_MUSHROOM_BLOCK);
        }
        if (worldgenerator != null && worldgenerator.generate(worldIn, rand, pos)) {
            return true;
        }
        worldIn.setBlockState(pos, state, 3);
        return false;
    }
    
    @Override
    public boolean canGrow(final World worldIn, final BlockPos pos, final IBlockState state, final boolean isClient) {
        return true;
    }
    
    @Override
    public boolean canUseBonemeal(final World worldIn, final Random rand, final BlockPos pos, final IBlockState state) {
        return rand.nextFloat() < 0.4;
    }
    
    @Override
    public void grow(final World worldIn, final Random rand, final BlockPos pos, final IBlockState state) {
        this.generateBigMushroom(worldIn, pos, state, rand);
    }
    
    static {
        MUSHROOM_AABB = new AxisAlignedBB(0.30000001192092896, 0.0, 0.30000001192092896, 0.699999988079071, 0.4000000059604645, 0.699999988079071);
    }
}
