// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.util.EnumParticleTypes;
import net.minecraft.init.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.material.Material;

public class BlockFalling extends Block
{
    public static boolean fallInstantly;
    
    public BlockFalling() {
        super(Material.SAND);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }
    
    public BlockFalling(final Material materialIn) {
        super(materialIn);
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
    }
    
    @Override
    public void neighborChanged(final IBlockState state, final World worldIn, final BlockPos pos, final Block blockIn, final BlockPos fromPos) {
        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (!worldIn.isRemote) {
            this.checkFallable(worldIn, pos);
        }
    }
    
    private void checkFallable(final World worldIn, final BlockPos pos) {
        if (canFallThrough(worldIn.getBlockState(pos.down())) && pos.getY() >= 0) {
            final int i = 32;
            if (!BlockFalling.fallInstantly && worldIn.isAreaLoaded(pos.add(-32, -32, -32), pos.add(32, 32, 32))) {
                if (!worldIn.isRemote) {
                    final EntityFallingBlock entityfallingblock = new EntityFallingBlock(worldIn, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, worldIn.getBlockState(pos));
                    this.onStartFalling(entityfallingblock);
                    worldIn.spawnEntity(entityfallingblock);
                }
            }
            else {
                worldIn.setBlockToAir(pos);
                BlockPos blockpos;
                for (blockpos = pos.down(); canFallThrough(worldIn.getBlockState(blockpos)) && blockpos.getY() > 0; blockpos = blockpos.down()) {}
                if (blockpos.getY() > 0) {
                    worldIn.setBlockState(blockpos.up(), this.getDefaultState());
                }
            }
        }
    }
    
    protected void onStartFalling(final EntityFallingBlock fallingEntity) {
    }
    
    @Override
    public int tickRate(final World worldIn) {
        return 2;
    }
    
    public static boolean canFallThrough(final IBlockState state) {
        final Block block = state.getBlock();
        final Material material = state.getMaterial();
        return block == Blocks.FIRE || material == Material.AIR || material == Material.WATER || material == Material.LAVA;
    }
    
    public void onEndFalling(final World worldIn, final BlockPos pos, final IBlockState fallingState, final IBlockState hitState) {
    }
    
    public void onBroken(final World worldIn, final BlockPos pos) {
    }
    
    @Override
    public void randomDisplayTick(final IBlockState stateIn, final World worldIn, final BlockPos pos, final Random rand) {
        if (rand.nextInt(16) == 0) {
            final BlockPos blockpos = pos.down();
            if (canFallThrough(worldIn.getBlockState(blockpos))) {
                final double d0 = pos.getX() + rand.nextFloat();
                final double d2 = pos.getY() - 0.05;
                final double d3 = pos.getZ() + rand.nextFloat();
                worldIn.spawnParticle(EnumParticleTypes.FALLING_DUST, d0, d2, d3, 0.0, 0.0, 0.0, Block.getStateId(stateIn));
            }
        }
    }
    
    public int getDustColor(final IBlockState state) {
        return -16777216;
    }
}
