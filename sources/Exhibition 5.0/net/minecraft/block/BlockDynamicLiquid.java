// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import java.util.EnumSet;
import java.util.Set;
import java.util.Iterator;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.EnumFacing;
import java.util.Random;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.material.Material;

public class BlockDynamicLiquid extends BlockLiquid
{
    int field_149815_a;
    private static final String __OBFID = "CL_00000234";
    
    protected BlockDynamicLiquid(final Material p_i45403_1_) {
        super(p_i45403_1_);
    }
    
    private void func_180690_f(final World worldIn, final BlockPos p_180690_2_, final IBlockState p_180690_3_) {
        worldIn.setBlockState(p_180690_2_, BlockLiquid.getStaticLiquidForMaterial(this.blockMaterial).getDefaultState().withProperty(BlockLiquid.LEVEL, p_180690_3_.getValue(BlockLiquid.LEVEL)), 2);
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, IBlockState state, final Random rand) {
        int var5 = (int)state.getValue(BlockLiquid.LEVEL);
        byte var6 = 1;
        if (this.blockMaterial == Material.lava && !worldIn.provider.func_177500_n()) {
            var6 = 2;
        }
        int var7 = this.tickRate(worldIn);
        if (var5 > 0) {
            int var8 = -100;
            this.field_149815_a = 0;
            for (final EnumFacing var10 : EnumFacing.Plane.HORIZONTAL) {
                var8 = this.func_176371_a(worldIn, pos.offset(var10), var8);
            }
            int var11 = var8 + var6;
            if (var11 >= 8 || var8 < 0) {
                var11 = -1;
            }
            if (this.func_176362_e(worldIn, pos.offsetUp()) >= 0) {
                final int var12 = this.func_176362_e(worldIn, pos.offsetUp());
                if (var12 >= 8) {
                    var11 = var12;
                }
                else {
                    var11 = var12 + 8;
                }
            }
            if (this.field_149815_a >= 2 && this.blockMaterial == Material.water) {
                final IBlockState var13 = worldIn.getBlockState(pos.offsetDown());
                if (var13.getBlock().getMaterial().isSolid()) {
                    var11 = 0;
                }
                else if (var13.getBlock().getMaterial() == this.blockMaterial && (int)var13.getValue(BlockLiquid.LEVEL) == 0) {
                    var11 = 0;
                }
            }
            if (this.blockMaterial == Material.lava && var5 < 8 && var11 < 8 && var11 > var5 && rand.nextInt(4) != 0) {
                var7 *= 4;
            }
            if (var11 == var5) {
                this.func_180690_f(worldIn, pos, state);
            }
            else if ((var5 = var11) < 0) {
                worldIn.setBlockToAir(pos);
            }
            else {
                state = state.withProperty(BlockLiquid.LEVEL, var11);
                worldIn.setBlockState(pos, state, 2);
                worldIn.scheduleUpdate(pos, this, var7);
                worldIn.notifyNeighborsOfStateChange(pos, this);
            }
        }
        else {
            this.func_180690_f(worldIn, pos, state);
        }
        final IBlockState var14 = worldIn.getBlockState(pos.offsetDown());
        if (this.func_176373_h(worldIn, pos.offsetDown(), var14)) {
            if (this.blockMaterial == Material.lava && worldIn.getBlockState(pos.offsetDown()).getBlock().getMaterial() == Material.water) {
                worldIn.setBlockState(pos.offsetDown(), Blocks.stone.getDefaultState());
                this.func_180688_d(worldIn, pos.offsetDown());
                return;
            }
            if (var5 >= 8) {
                this.func_176375_a(worldIn, pos.offsetDown(), var14, var5);
            }
            else {
                this.func_176375_a(worldIn, pos.offsetDown(), var14, var5 + 8);
            }
        }
        else if (var5 >= 0 && (var5 == 0 || this.func_176372_g(worldIn, pos.offsetDown(), var14))) {
            final Set var15 = this.func_176376_e(worldIn, pos);
            int var12 = var5 + var6;
            if (var5 >= 8) {
                var12 = 1;
            }
            if (var12 >= 8) {
                return;
            }
            for (final EnumFacing var17 : var15) {
                this.func_176375_a(worldIn, pos.offset(var17), worldIn.getBlockState(pos.offset(var17)), var12);
            }
        }
    }
    
    private void func_176375_a(final World worldIn, final BlockPos p_176375_2_, final IBlockState p_176375_3_, final int p_176375_4_) {
        if (this.func_176373_h(worldIn, p_176375_2_, p_176375_3_)) {
            if (p_176375_3_.getBlock() != Blocks.air) {
                if (this.blockMaterial == Material.lava) {
                    this.func_180688_d(worldIn, p_176375_2_);
                }
                else {
                    p_176375_3_.getBlock().dropBlockAsItem(worldIn, p_176375_2_, p_176375_3_, 0);
                }
            }
            worldIn.setBlockState(p_176375_2_, this.getDefaultState().withProperty(BlockLiquid.LEVEL, p_176375_4_), 3);
        }
    }
    
    private int func_176374_a(final World worldIn, final BlockPos p_176374_2_, final int p_176374_3_, final EnumFacing p_176374_4_) {
        int var5 = 1000;
        for (final EnumFacing var7 : EnumFacing.Plane.HORIZONTAL) {
            if (var7 != p_176374_4_) {
                final BlockPos var8 = p_176374_2_.offset(var7);
                final IBlockState var9 = worldIn.getBlockState(var8);
                if (this.func_176372_g(worldIn, var8, var9) || (var9.getBlock().getMaterial() == this.blockMaterial && (int)var9.getValue(BlockLiquid.LEVEL) <= 0)) {
                    continue;
                }
                if (!this.func_176372_g(worldIn, var8.offsetDown(), var9)) {
                    return p_176374_3_;
                }
                if (p_176374_3_ >= 4) {
                    continue;
                }
                final int var10 = this.func_176374_a(worldIn, var8, p_176374_3_ + 1, var7.getOpposite());
                if (var10 >= var5) {
                    continue;
                }
                var5 = var10;
            }
        }
        return var5;
    }
    
    private Set func_176376_e(final World worldIn, final BlockPos p_176376_2_) {
        int var3 = 1000;
        final EnumSet var4 = EnumSet.noneOf(EnumFacing.class);
        for (final EnumFacing var6 : EnumFacing.Plane.HORIZONTAL) {
            final BlockPos var7 = p_176376_2_.offset(var6);
            final IBlockState var8 = worldIn.getBlockState(var7);
            if (!this.func_176372_g(worldIn, var7, var8) && (var8.getBlock().getMaterial() != this.blockMaterial || (int)var8.getValue(BlockLiquid.LEVEL) > 0)) {
                int var9;
                if (this.func_176372_g(worldIn, var7.offsetDown(), worldIn.getBlockState(var7.offsetDown()))) {
                    var9 = this.func_176374_a(worldIn, var7, 1, var6.getOpposite());
                }
                else {
                    var9 = 0;
                }
                if (var9 < var3) {
                    var4.clear();
                }
                if (var9 > var3) {
                    continue;
                }
                var4.add(var6);
                var3 = var9;
            }
        }
        return var4;
    }
    
    private boolean func_176372_g(final World worldIn, final BlockPos p_176372_2_, final IBlockState p_176372_3_) {
        final Block var4 = worldIn.getBlockState(p_176372_2_).getBlock();
        return var4 instanceof BlockDoor || var4 == Blocks.standing_sign || var4 == Blocks.ladder || var4 == Blocks.reeds || var4.blockMaterial == Material.portal || var4.blockMaterial.blocksMovement();
    }
    
    protected int func_176371_a(final World worldIn, final BlockPos p_176371_2_, final int p_176371_3_) {
        int var4 = this.func_176362_e(worldIn, p_176371_2_);
        if (var4 < 0) {
            return p_176371_3_;
        }
        if (var4 == 0) {
            ++this.field_149815_a;
        }
        if (var4 >= 8) {
            var4 = 0;
        }
        return (p_176371_3_ >= 0 && var4 >= p_176371_3_) ? p_176371_3_ : var4;
    }
    
    private boolean func_176373_h(final World worldIn, final BlockPos p_176373_2_, final IBlockState p_176373_3_) {
        final Material var4 = p_176373_3_.getBlock().getMaterial();
        return var4 != this.blockMaterial && var4 != Material.lava && !this.func_176372_g(worldIn, p_176373_2_, p_176373_3_);
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (!this.func_176365_e(worldIn, pos, state)) {
            worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
        }
    }
}
