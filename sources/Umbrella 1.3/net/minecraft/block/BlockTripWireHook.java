/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Objects
 */
package net.minecraft.block;

import com.google.common.base.Objects;
import java.util.Iterator;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTripWire;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTripWireHook
extends Block {
    public static final PropertyDirection field_176264_a = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyBool field_176263_b = PropertyBool.create("powered");
    public static final PropertyBool field_176265_M = PropertyBool.create("attached");
    public static final PropertyBool field_176266_N = PropertyBool.create("suspended");
    private static final String __OBFID = "CL_00000329";

    public BlockTripWireHook() {
        super(Material.circuits);
        this.setDefaultState(this.blockState.getBaseState().withProperty(field_176264_a, (Comparable)((Object)EnumFacing.NORTH)).withProperty(field_176263_b, Boolean.valueOf(false)).withProperty(field_176265_M, Boolean.valueOf(false)).withProperty(field_176266_N, Boolean.valueOf(false)));
        this.setCreativeTab(CreativeTabs.tabRedstone);
        this.setTickRandomly(true);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state.withProperty(field_176266_N, Boolean.valueOf(!World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetDown())));
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        return null;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
        return side.getAxis().isHorizontal() && worldIn.getBlockState(pos.offset(side.getOpposite())).getBlock().isNormalCube();
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        EnumFacing var4;
        Iterator var3 = EnumFacing.Plane.HORIZONTAL.iterator();
        do {
            if (var3.hasNext()) continue;
            return false;
        } while (!worldIn.getBlockState(pos.offset(var4 = (EnumFacing)var3.next())).getBlock().isNormalCube());
        return true;
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        IBlockState var9 = this.getDefaultState().withProperty(field_176263_b, Boolean.valueOf(false)).withProperty(field_176265_M, Boolean.valueOf(false)).withProperty(field_176266_N, Boolean.valueOf(false));
        if (facing.getAxis().isHorizontal()) {
            var9 = var9.withProperty(field_176264_a, (Comparable)((Object)facing));
        }
        return var9;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        this.func_176260_a(worldIn, pos, state, false, false, -1, null);
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        EnumFacing var5;
        if (neighborBlock != this && this.func_176261_e(worldIn, pos, state) && !worldIn.getBlockState(pos.offset((var5 = (EnumFacing)((Object)state.getValue(field_176264_a))).getOpposite())).getBlock().isNormalCube()) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
    }

    public void func_176260_a(World worldIn, BlockPos p_176260_2_, IBlockState p_176260_3_, boolean p_176260_4_, boolean p_176260_5_, int p_176260_6_, IBlockState p_176260_7_) {
        BlockPos var17;
        EnumFacing var8 = (EnumFacing)((Object)p_176260_3_.getValue(field_176264_a));
        boolean var9 = (Boolean)p_176260_3_.getValue(field_176265_M);
        boolean var10 = (Boolean)p_176260_3_.getValue(field_176263_b);
        boolean var11 = !World.doesBlockHaveSolidTopSurface(worldIn, p_176260_2_.offsetDown());
        boolean var12 = !p_176260_4_;
        boolean var13 = false;
        int var14 = 0;
        IBlockState[] var15 = new IBlockState[42];
        for (int var16 = 1; var16 < 42; ++var16) {
            var17 = p_176260_2_.offset(var8, var16);
            IBlockState var18 = worldIn.getBlockState(var17);
            if (var18.getBlock() == Blocks.tripwire_hook) {
                if (var18.getValue(field_176264_a) != var8.getOpposite()) break;
                var14 = var16;
                break;
            }
            if (var18.getBlock() != Blocks.tripwire && var16 != p_176260_6_) {
                var15[var16] = null;
                var12 = false;
                continue;
            }
            if (var16 == p_176260_6_) {
                var18 = (IBlockState)Objects.firstNonNull((Object)p_176260_7_, (Object)var18);
            }
            boolean var19 = (Boolean)var18.getValue(BlockTripWire.field_176295_N) == false;
            boolean var20 = (Boolean)var18.getValue(BlockTripWire.field_176293_a);
            boolean var21 = (Boolean)var18.getValue(BlockTripWire.field_176290_b);
            var12 &= var21 == var11;
            var13 |= var19 && var20;
            var15[var16] = var18;
            if (var16 != p_176260_6_) continue;
            worldIn.scheduleUpdate(p_176260_2_, this, this.tickRate(worldIn));
            var12 &= var19;
        }
        IBlockState var22 = this.getDefaultState().withProperty(field_176265_M, Boolean.valueOf(var12)).withProperty(field_176263_b, Boolean.valueOf(var13 &= (var12 &= var14 > 1)));
        if (var14 > 0) {
            var17 = p_176260_2_.offset(var8, var14);
            EnumFacing var24 = var8.getOpposite();
            worldIn.setBlockState(var17, var22.withProperty(field_176264_a, (Comparable)((Object)var24)), 3);
            this.func_176262_b(worldIn, var17, var24);
            this.func_180694_a(worldIn, var17, var12, var13, var9, var10);
        }
        this.func_180694_a(worldIn, p_176260_2_, var12, var13, var9, var10);
        if (!p_176260_4_) {
            worldIn.setBlockState(p_176260_2_, var22.withProperty(field_176264_a, (Comparable)((Object)var8)), 3);
            if (p_176260_5_) {
                this.func_176262_b(worldIn, p_176260_2_, var8);
            }
        }
        if (var9 != var12) {
            for (int var23 = 1; var23 < var14; ++var23) {
                BlockPos var25 = p_176260_2_.offset(var8, var23);
                IBlockState var26 = var15[var23];
                if (var26 == null || worldIn.getBlockState(var25).getBlock() == Blocks.air) continue;
                worldIn.setBlockState(var25, var26.withProperty(field_176265_M, Boolean.valueOf(var12)), 3);
            }
        }
    }

    @Override
    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        this.func_176260_a(worldIn, pos, state, false, true, -1, null);
    }

    private void func_180694_a(World worldIn, BlockPos p_180694_2_, boolean p_180694_3_, boolean p_180694_4_, boolean p_180694_5_, boolean p_180694_6_) {
        if (p_180694_4_ && !p_180694_6_) {
            worldIn.playSoundEffect((double)p_180694_2_.getX() + 0.5, (double)p_180694_2_.getY() + 0.1, (double)p_180694_2_.getZ() + 0.5, "random.click", 0.4f, 0.6f);
        } else if (!p_180694_4_ && p_180694_6_) {
            worldIn.playSoundEffect((double)p_180694_2_.getX() + 0.5, (double)p_180694_2_.getY() + 0.1, (double)p_180694_2_.getZ() + 0.5, "random.click", 0.4f, 0.5f);
        } else if (p_180694_3_ && !p_180694_5_) {
            worldIn.playSoundEffect((double)p_180694_2_.getX() + 0.5, (double)p_180694_2_.getY() + 0.1, (double)p_180694_2_.getZ() + 0.5, "random.click", 0.4f, 0.7f);
        } else if (!p_180694_3_ && p_180694_5_) {
            worldIn.playSoundEffect((double)p_180694_2_.getX() + 0.5, (double)p_180694_2_.getY() + 0.1, (double)p_180694_2_.getZ() + 0.5, "random.bowhit", 0.4f, 1.2f / (worldIn.rand.nextFloat() * 0.2f + 0.9f));
        }
    }

    private void func_176262_b(World worldIn, BlockPos p_176262_2_, EnumFacing p_176262_3_) {
        worldIn.notifyNeighborsOfStateChange(p_176262_2_, this);
        worldIn.notifyNeighborsOfStateChange(p_176262_2_.offset(p_176262_3_.getOpposite()), this);
    }

    private boolean func_176261_e(World worldIn, BlockPos p_176261_2_, IBlockState p_176261_3_) {
        if (!this.canPlaceBlockAt(worldIn, p_176261_2_)) {
            this.dropBlockAsItem(worldIn, p_176261_2_, p_176261_3_, 0);
            worldIn.setBlockToAir(p_176261_2_);
            return false;
        }
        return true;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
        float var3 = 0.1875f;
        switch (SwitchEnumFacing.field_177056_a[((EnumFacing)((Object)access.getBlockState(pos).getValue(field_176264_a))).ordinal()]) {
            case 1: {
                this.setBlockBounds(0.0f, 0.2f, 0.5f - var3, var3 * 2.0f, 0.8f, 0.5f + var3);
                break;
            }
            case 2: {
                this.setBlockBounds(1.0f - var3 * 2.0f, 0.2f, 0.5f - var3, 1.0f, 0.8f, 0.5f + var3);
                break;
            }
            case 3: {
                this.setBlockBounds(0.5f - var3, 0.2f, 0.0f, 0.5f + var3, 0.8f, var3 * 2.0f);
                break;
            }
            case 4: {
                this.setBlockBounds(0.5f - var3, 0.2f, 1.0f - var3 * 2.0f, 0.5f + var3, 0.8f, 1.0f);
            }
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        boolean var4 = (Boolean)state.getValue(field_176265_M);
        boolean var5 = (Boolean)state.getValue(field_176263_b);
        if (var4 || var5) {
            this.func_176260_a(worldIn, pos, state, true, false, -1, null);
        }
        if (var5) {
            worldIn.notifyNeighborsOfStateChange(pos, this);
            worldIn.notifyNeighborsOfStateChange(pos.offset(((EnumFacing)((Object)state.getValue(field_176264_a))).getOpposite()), this);
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
        return (Boolean)state.getValue(field_176263_b) != false ? 15 : 0;
    }

    @Override
    public int isProvidingStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
        return (Boolean)state.getValue(field_176263_b) == false ? 0 : (state.getValue(field_176264_a) == side ? 15 : 0);
    }

    @Override
    public boolean canProvidePower() {
        return true;
    }

    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT_MIPPED;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(field_176264_a, (Comparable)((Object)EnumFacing.getHorizontal(meta & 3))).withProperty(field_176263_b, Boolean.valueOf((meta & 8) > 0)).withProperty(field_176265_M, Boolean.valueOf((meta & 4) > 0));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int var2 = 0;
        int var3 = var2 | ((EnumFacing)((Object)state.getValue(field_176264_a))).getHorizontalIndex();
        if (((Boolean)state.getValue(field_176263_b)).booleanValue()) {
            var3 |= 8;
        }
        if (((Boolean)state.getValue(field_176265_M)).booleanValue()) {
            var3 |= 4;
        }
        return var3;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, field_176264_a, field_176263_b, field_176265_M, field_176266_N);
    }

    static final class SwitchEnumFacing {
        static final int[] field_177056_a = new int[EnumFacing.values().length];
        private static final String __OBFID = "CL_00002050";

        static {
            try {
                SwitchEnumFacing.field_177056_a[EnumFacing.EAST.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumFacing.field_177056_a[EnumFacing.WEST.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumFacing.field_177056_a[EnumFacing.SOUTH.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumFacing.field_177056_a[EnumFacing.NORTH.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
        }

        SwitchEnumFacing() {
        }
    }
}

