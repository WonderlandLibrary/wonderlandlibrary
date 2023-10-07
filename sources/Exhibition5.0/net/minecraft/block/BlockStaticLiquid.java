// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.util.EnumFacing;
import net.minecraft.init.Blocks;
import java.util.Random;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.material.Material;

public class BlockStaticLiquid extends BlockLiquid
{
    private static final String __OBFID = "CL_00000315";
    
    protected BlockStaticLiquid(final Material p_i45429_1_) {
        super(p_i45429_1_);
        this.setTickRandomly(false);
        if (p_i45429_1_ == Material.lava) {
            this.setTickRandomly(true);
        }
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        if (!this.func_176365_e(worldIn, pos, state)) {
            this.updateLiquid(worldIn, pos, state);
        }
    }
    
    private void updateLiquid(final World worldIn, final BlockPos p_176370_2_, final IBlockState p_176370_3_) {
        final BlockDynamicLiquid var4 = BlockLiquid.getDynamicLiquidForMaterial(this.blockMaterial);
        worldIn.setBlockState(p_176370_2_, var4.getDefaultState().withProperty(BlockLiquid.LEVEL, p_176370_3_.getValue(BlockLiquid.LEVEL)), 2);
        worldIn.scheduleUpdate(p_176370_2_, var4, this.tickRate(worldIn));
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (this.blockMaterial == Material.lava && worldIn.getGameRules().getGameRuleBooleanValue("doFireTick")) {
            final int var5 = rand.nextInt(3);
            if (var5 > 0) {
                BlockPos var6 = pos;
                for (int var7 = 0; var7 < var5; ++var7) {
                    var6 = var6.add(rand.nextInt(3) - 1, 1, rand.nextInt(3) - 1);
                    final Block var8 = worldIn.getBlockState(var6).getBlock();
                    if (var8.blockMaterial == Material.air) {
                        if (this.isSurroundingBlockFlammable(worldIn, var6)) {
                            worldIn.setBlockState(var6, Blocks.fire.getDefaultState());
                            return;
                        }
                    }
                    else if (var8.blockMaterial.blocksMovement()) {
                        return;
                    }
                }
            }
            else {
                for (int var9 = 0; var9 < 3; ++var9) {
                    final BlockPos var10 = pos.add(rand.nextInt(3) - 1, 0, rand.nextInt(3) - 1);
                    if (worldIn.isAirBlock(var10.offsetUp()) && this.getCanBlockBurn(worldIn, var10)) {
                        worldIn.setBlockState(var10.offsetUp(), Blocks.fire.getDefaultState());
                    }
                }
            }
        }
    }
    
    protected boolean isSurroundingBlockFlammable(final World worldIn, final BlockPos p_176369_2_) {
        for (final EnumFacing var6 : EnumFacing.values()) {
            if (this.getCanBlockBurn(worldIn, p_176369_2_.offset(var6))) {
                return true;
            }
        }
        return false;
    }
    
    private boolean getCanBlockBurn(final World worldIn, final BlockPos p_176368_2_) {
        return worldIn.getBlockState(p_176368_2_).getBlock().getMaterial().getCanBurn();
    }
}
