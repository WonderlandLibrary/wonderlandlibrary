// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.material.Material;

public abstract class BlockRedstoneDiode extends BlockDirectional
{
    protected final boolean isRepeaterPowered;
    private static final String __OBFID = "CL_00000226";
    
    protected BlockRedstoneDiode(final boolean p_i45400_1_) {
        super(Material.circuits);
        this.isRepeaterPowered = p_i45400_1_;
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.125f, 1.0f);
    }
    
    @Override
    public boolean isFullCube() {
        return false;
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetDown()) && super.canPlaceBlockAt(worldIn, pos);
    }
    
    public boolean func_176409_d(final World worldIn, final BlockPos p_176409_2_) {
        return World.doesBlockHaveSolidTopSurface(worldIn, p_176409_2_.offsetDown());
    }
    
    @Override
    public void randomTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random random) {
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (!this.func_176405_b(worldIn, pos, state)) {
            final boolean var5 = this.func_176404_e(worldIn, pos, state);
            if (this.isRepeaterPowered && !var5) {
                worldIn.setBlockState(pos, this.func_180675_k(state), 2);
            }
            else if (!this.isRepeaterPowered) {
                worldIn.setBlockState(pos, this.func_180674_e(state), 2);
                if (!var5) {
                    worldIn.func_175654_a(pos, this.func_180674_e(state).getBlock(), this.func_176399_m(state), -1);
                }
            }
        }
    }
    
    @Override
    public boolean shouldSideBeRendered(final IBlockAccess worldIn, final BlockPos pos, final EnumFacing side) {
        return side.getAxis() != EnumFacing.Axis.Y;
    }
    
    protected boolean func_176406_l(final IBlockState p_176406_1_) {
        return this.isRepeaterPowered;
    }
    
    @Override
    public int isProvidingStrongPower(final IBlockAccess worldIn, final BlockPos pos, final IBlockState state, final EnumFacing side) {
        return this.isProvidingWeakPower(worldIn, pos, state, side);
    }
    
    @Override
    public int isProvidingWeakPower(final IBlockAccess worldIn, final BlockPos pos, final IBlockState state, final EnumFacing side) {
        return this.func_176406_l(state) ? ((state.getValue(BlockDirectional.AGE) == side) ? this.func_176408_a(worldIn, pos, state) : 0) : 0;
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        if (this.func_176409_d(worldIn, pos)) {
            this.func_176398_g(worldIn, pos, state);
        }
        else {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
            for (final EnumFacing var8 : EnumFacing.values()) {
                worldIn.notifyNeighborsOfStateChange(pos.offset(var8), this);
            }
        }
    }
    
    protected void func_176398_g(final World worldIn, final BlockPos p_176398_2_, final IBlockState p_176398_3_) {
        if (!this.func_176405_b(worldIn, p_176398_2_, p_176398_3_)) {
            final boolean var4 = this.func_176404_e(worldIn, p_176398_2_, p_176398_3_);
            if (((this.isRepeaterPowered && !var4) || (!this.isRepeaterPowered && var4)) && !worldIn.isBlockTickPending(p_176398_2_, this)) {
                byte var5 = -1;
                if (this.func_176402_i(worldIn, p_176398_2_, p_176398_3_)) {
                    var5 = -3;
                }
                else if (this.isRepeaterPowered) {
                    var5 = -2;
                }
                worldIn.func_175654_a(p_176398_2_, this, this.func_176403_d(p_176398_3_), var5);
            }
        }
    }
    
    public boolean func_176405_b(final IBlockAccess p_176405_1_, final BlockPos p_176405_2_, final IBlockState p_176405_3_) {
        return false;
    }
    
    protected boolean func_176404_e(final World worldIn, final BlockPos p_176404_2_, final IBlockState p_176404_3_) {
        return this.func_176397_f(worldIn, p_176404_2_, p_176404_3_) > 0;
    }
    
    protected int func_176397_f(final World worldIn, final BlockPos p_176397_2_, final IBlockState p_176397_3_) {
        final EnumFacing var4 = (EnumFacing)p_176397_3_.getValue(BlockDirectional.AGE);
        final BlockPos var5 = p_176397_2_.offset(var4);
        final int var6 = worldIn.getRedstonePower(var5, var4);
        if (var6 >= 15) {
            return var6;
        }
        final IBlockState var7 = worldIn.getBlockState(var5);
        return Math.max(var6, (var7.getBlock() == Blocks.redstone_wire) ? ((int)var7.getValue(BlockRedstoneWire.POWER)) : 0);
    }
    
    protected int func_176407_c(final IBlockAccess p_176407_1_, final BlockPos p_176407_2_, final IBlockState p_176407_3_) {
        final EnumFacing var4 = (EnumFacing)p_176407_3_.getValue(BlockDirectional.AGE);
        final EnumFacing var5 = var4.rotateY();
        final EnumFacing var6 = var4.rotateYCCW();
        return Math.max(this.func_176401_c(p_176407_1_, p_176407_2_.offset(var5), var5), this.func_176401_c(p_176407_1_, p_176407_2_.offset(var6), var6));
    }
    
    protected int func_176401_c(final IBlockAccess p_176401_1_, final BlockPos p_176401_2_, final EnumFacing p_176401_3_) {
        final IBlockState var4 = p_176401_1_.getBlockState(p_176401_2_);
        final Block var5 = var4.getBlock();
        return (int)(this.func_149908_a(var5) ? ((var5 == Blocks.redstone_wire) ? var4.getValue(BlockRedstoneWire.POWER) : p_176401_1_.getStrongPower(p_176401_2_, p_176401_3_)) : 0);
    }
    
    @Override
    public boolean canProvidePower() {
        return true;
    }
    
    @Override
    public IBlockState onBlockPlaced(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        return this.getDefaultState().withProperty(BlockDirectional.AGE, placer.func_174811_aO().getOpposite());
    }
    
    @Override
    public void onBlockPlacedBy(final World worldIn, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
        if (this.func_176404_e(worldIn, pos, state)) {
            worldIn.scheduleUpdate(pos, this, 1);
        }
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        this.func_176400_h(worldIn, pos, state);
    }
    
    protected void func_176400_h(final World worldIn, final BlockPos p_176400_2_, final IBlockState p_176400_3_) {
        final EnumFacing var4 = (EnumFacing)p_176400_3_.getValue(BlockDirectional.AGE);
        final BlockPos var5 = p_176400_2_.offset(var4.getOpposite());
        worldIn.notifyBlockOfStateChange(var5, this);
        worldIn.notifyNeighborsOfStateExcept(var5, this, var4);
    }
    
    @Override
    public void onBlockDestroyedByPlayer(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (this.isRepeaterPowered) {
            for (final EnumFacing var7 : EnumFacing.values()) {
                worldIn.notifyNeighborsOfStateChange(pos.offset(var7), this);
            }
        }
        super.onBlockDestroyedByPlayer(worldIn, pos, state);
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    protected boolean func_149908_a(final Block p_149908_1_) {
        return p_149908_1_.canProvidePower();
    }
    
    protected int func_176408_a(final IBlockAccess p_176408_1_, final BlockPos p_176408_2_, final IBlockState p_176408_3_) {
        return 15;
    }
    
    public static boolean isRedstoneRepeaterBlockID(final Block p_149909_0_) {
        return Blocks.unpowered_repeater.func_149907_e(p_149909_0_) || Blocks.unpowered_comparator.func_149907_e(p_149909_0_);
    }
    
    public boolean func_149907_e(final Block p_149907_1_) {
        return p_149907_1_ == this.func_180674_e(this.getDefaultState()).getBlock() || p_149907_1_ == this.func_180675_k(this.getDefaultState()).getBlock();
    }
    
    public boolean func_176402_i(final World worldIn, final BlockPos p_176402_2_, final IBlockState p_176402_3_) {
        final EnumFacing var4 = ((EnumFacing)p_176402_3_.getValue(BlockDirectional.AGE)).getOpposite();
        final BlockPos var5 = p_176402_2_.offset(var4);
        return isRedstoneRepeaterBlockID(worldIn.getBlockState(var5).getBlock()) && worldIn.getBlockState(var5).getValue(BlockDirectional.AGE) != var4;
    }
    
    protected int func_176399_m(final IBlockState p_176399_1_) {
        return this.func_176403_d(p_176399_1_);
    }
    
    protected abstract int func_176403_d(final IBlockState p0);
    
    protected abstract IBlockState func_180674_e(final IBlockState p0);
    
    protected abstract IBlockState func_180675_k(final IBlockState p0);
    
    @Override
    public boolean isAssociatedBlock(final Block other) {
        return this.func_149907_e(other);
    }
    
    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }
}
