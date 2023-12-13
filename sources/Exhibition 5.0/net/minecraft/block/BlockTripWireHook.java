// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import com.google.common.base.Predicate;
import net.minecraft.block.state.BlockState;
import net.minecraft.util.EnumWorldBlockLayer;
import java.util.Random;
import com.google.common.base.Objects;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import java.util.Iterator;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;

public class BlockTripWireHook extends Block
{
    public static final PropertyDirection field_176264_a;
    public static final PropertyBool field_176263_b;
    public static final PropertyBool field_176265_M;
    public static final PropertyBool field_176266_N;
    private static final String __OBFID = "CL_00000329";
    
    public BlockTripWireHook() {
        super(Material.circuits);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockTripWireHook.field_176264_a, EnumFacing.NORTH).withProperty(BlockTripWireHook.field_176263_b, false).withProperty(BlockTripWireHook.field_176265_M, false).withProperty(BlockTripWireHook.field_176266_N, false));
        this.setCreativeTab(CreativeTabs.tabRedstone);
        this.setTickRandomly(true);
    }
    
    @Override
    public IBlockState getActualState(final IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        return state.withProperty(BlockTripWireHook.field_176266_N, !World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetDown()));
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final World worldIn, final BlockPos pos, final IBlockState state) {
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
    public boolean canPlaceBlockOnSide(final World worldIn, final BlockPos pos, final EnumFacing side) {
        return side.getAxis().isHorizontal() && worldIn.getBlockState(pos.offset(side.getOpposite())).getBlock().isNormalCube();
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        for (final EnumFacing var4 : EnumFacing.Plane.HORIZONTAL) {
            if (worldIn.getBlockState(pos.offset(var4)).getBlock().isNormalCube()) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public IBlockState onBlockPlaced(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        IBlockState var9 = this.getDefaultState().withProperty(BlockTripWireHook.field_176263_b, false).withProperty(BlockTripWireHook.field_176265_M, false).withProperty(BlockTripWireHook.field_176266_N, false);
        if (facing.getAxis().isHorizontal()) {
            var9 = var9.withProperty(BlockTripWireHook.field_176264_a, facing);
        }
        return var9;
    }
    
    @Override
    public void onBlockPlacedBy(final World worldIn, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
        this.func_176260_a(worldIn, pos, state, false, false, -1, null);
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        if (neighborBlock != this && this.func_176261_e(worldIn, pos, state)) {
            final EnumFacing var5 = (EnumFacing)state.getValue(BlockTripWireHook.field_176264_a);
            if (!worldIn.getBlockState(pos.offset(var5.getOpposite())).getBlock().isNormalCube()) {
                this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
            }
        }
    }
    
    public void func_176260_a(final World worldIn, final BlockPos p_176260_2_, final IBlockState p_176260_3_, final boolean p_176260_4_, final boolean p_176260_5_, final int p_176260_6_, final IBlockState p_176260_7_) {
        final EnumFacing var8 = (EnumFacing)p_176260_3_.getValue(BlockTripWireHook.field_176264_a);
        final boolean var9 = (boolean)p_176260_3_.getValue(BlockTripWireHook.field_176265_M);
        final boolean var10 = (boolean)p_176260_3_.getValue(BlockTripWireHook.field_176263_b);
        final boolean var11 = !World.doesBlockHaveSolidTopSurface(worldIn, p_176260_2_.offsetDown());
        boolean var12 = !p_176260_4_;
        boolean var13 = false;
        int var14 = 0;
        final IBlockState[] var15 = new IBlockState[42];
        int var16 = 1;
        while (var16 < 42) {
            final BlockPos var17 = p_176260_2_.offset(var8, var16);
            IBlockState var18 = worldIn.getBlockState(var17);
            if (var18.getBlock() == Blocks.tripwire_hook) {
                if (var18.getValue(BlockTripWireHook.field_176264_a) == var8.getOpposite()) {
                    var14 = var16;
                    break;
                }
                break;
            }
            else {
                if (var18.getBlock() != Blocks.tripwire && var16 != p_176260_6_) {
                    var15[var16] = null;
                    var12 = false;
                }
                else {
                    if (var16 == p_176260_6_) {
                        var18 = (IBlockState)Objects.firstNonNull((Object)p_176260_7_, (Object)var18);
                    }
                    final boolean var19 = !(boolean)var18.getValue(BlockTripWire.field_176295_N);
                    final boolean var20 = (boolean)var18.getValue(BlockTripWire.field_176293_a);
                    final boolean var21 = (boolean)var18.getValue(BlockTripWire.field_176290_b);
                    var12 &= (var21 == var11);
                    var13 |= (var19 && var20);
                    var15[var16] = var18;
                    if (var16 == p_176260_6_) {
                        worldIn.scheduleUpdate(p_176260_2_, this, this.tickRate(worldIn));
                        var12 &= var19;
                    }
                }
                ++var16;
            }
        }
        var12 &= (var14 > 1);
        var13 &= var12;
        final IBlockState var22 = this.getDefaultState().withProperty(BlockTripWireHook.field_176265_M, var12).withProperty(BlockTripWireHook.field_176263_b, var13);
        if (var14 > 0) {
            final BlockPos var17 = p_176260_2_.offset(var8, var14);
            final EnumFacing var23 = var8.getOpposite();
            worldIn.setBlockState(var17, var22.withProperty(BlockTripWireHook.field_176264_a, var23), 3);
            this.func_176262_b(worldIn, var17, var23);
            this.func_180694_a(worldIn, var17, var12, var13, var9, var10);
        }
        this.func_180694_a(worldIn, p_176260_2_, var12, var13, var9, var10);
        if (!p_176260_4_) {
            worldIn.setBlockState(p_176260_2_, var22.withProperty(BlockTripWireHook.field_176264_a, var8), 3);
            if (p_176260_5_) {
                this.func_176262_b(worldIn, p_176260_2_, var8);
            }
        }
        if (var9 != var12) {
            for (int var24 = 1; var24 < var14; ++var24) {
                final BlockPos var25 = p_176260_2_.offset(var8, var24);
                final IBlockState var26 = var15[var24];
                if (var26 != null && worldIn.getBlockState(var25).getBlock() != Blocks.air) {
                    worldIn.setBlockState(var25, var26.withProperty(BlockTripWireHook.field_176265_M, var12), 3);
                }
            }
        }
    }
    
    @Override
    public void randomTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random random) {
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        this.func_176260_a(worldIn, pos, state, false, true, -1, null);
    }
    
    private void func_180694_a(final World worldIn, final BlockPos p_180694_2_, final boolean p_180694_3_, final boolean p_180694_4_, final boolean p_180694_5_, final boolean p_180694_6_) {
        if (p_180694_4_ && !p_180694_6_) {
            worldIn.playSoundEffect(p_180694_2_.getX() + 0.5, p_180694_2_.getY() + 0.1, p_180694_2_.getZ() + 0.5, "random.click", 0.4f, 0.6f);
        }
        else if (!p_180694_4_ && p_180694_6_) {
            worldIn.playSoundEffect(p_180694_2_.getX() + 0.5, p_180694_2_.getY() + 0.1, p_180694_2_.getZ() + 0.5, "random.click", 0.4f, 0.5f);
        }
        else if (p_180694_3_ && !p_180694_5_) {
            worldIn.playSoundEffect(p_180694_2_.getX() + 0.5, p_180694_2_.getY() + 0.1, p_180694_2_.getZ() + 0.5, "random.click", 0.4f, 0.7f);
        }
        else if (!p_180694_3_ && p_180694_5_) {
            worldIn.playSoundEffect(p_180694_2_.getX() + 0.5, p_180694_2_.getY() + 0.1, p_180694_2_.getZ() + 0.5, "random.bowhit", 0.4f, 1.2f / (worldIn.rand.nextFloat() * 0.2f + 0.9f));
        }
    }
    
    private void func_176262_b(final World worldIn, final BlockPos p_176262_2_, final EnumFacing p_176262_3_) {
        worldIn.notifyNeighborsOfStateChange(p_176262_2_, this);
        worldIn.notifyNeighborsOfStateChange(p_176262_2_.offset(p_176262_3_.getOpposite()), this);
    }
    
    private boolean func_176261_e(final World worldIn, final BlockPos p_176261_2_, final IBlockState p_176261_3_) {
        if (!this.canPlaceBlockAt(worldIn, p_176261_2_)) {
            this.dropBlockAsItem(worldIn, p_176261_2_, p_176261_3_, 0);
            worldIn.setBlockToAir(p_176261_2_);
            return false;
        }
        return true;
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess access, final BlockPos pos) {
        final float var3 = 0.1875f;
        switch (SwitchEnumFacing.field_177056_a[((EnumFacing)access.getBlockState(pos).getValue(BlockTripWireHook.field_176264_a)).ordinal()]) {
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
                break;
            }
        }
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        final boolean var4 = (boolean)state.getValue(BlockTripWireHook.field_176265_M);
        final boolean var5 = (boolean)state.getValue(BlockTripWireHook.field_176263_b);
        if (var4 || var5) {
            this.func_176260_a(worldIn, pos, state, true, false, -1, null);
        }
        if (var5) {
            worldIn.notifyNeighborsOfStateChange(pos, this);
            worldIn.notifyNeighborsOfStateChange(pos.offset(((EnumFacing)state.getValue(BlockTripWireHook.field_176264_a)).getOpposite()), this);
        }
        super.breakBlock(worldIn, pos, state);
    }
    
    @Override
    public int isProvidingWeakPower(final IBlockAccess worldIn, final BlockPos pos, final IBlockState state, final EnumFacing side) {
        return state.getValue(BlockTripWireHook.field_176263_b) ? 15 : 0;
    }
    
    @Override
    public int isProvidingStrongPower(final IBlockAccess worldIn, final BlockPos pos, final IBlockState state, final EnumFacing side) {
        return state.getValue(BlockTripWireHook.field_176263_b) ? ((state.getValue(BlockTripWireHook.field_176264_a) == side) ? 15 : 0) : 0;
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
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockTripWireHook.field_176264_a, EnumFacing.getHorizontal(meta & 0x3)).withProperty(BlockTripWireHook.field_176263_b, (meta & 0x8) > 0).withProperty(BlockTripWireHook.field_176265_M, (meta & 0x4) > 0);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        final byte var2 = 0;
        int var3 = var2 | ((EnumFacing)state.getValue(BlockTripWireHook.field_176264_a)).getHorizontalIndex();
        if (state.getValue(BlockTripWireHook.field_176263_b)) {
            var3 |= 0x8;
        }
        if (state.getValue(BlockTripWireHook.field_176265_M)) {
            var3 |= 0x4;
        }
        return var3;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockTripWireHook.field_176264_a, BlockTripWireHook.field_176263_b, BlockTripWireHook.field_176265_M, BlockTripWireHook.field_176266_N });
    }
    
    static {
        field_176264_a = PropertyDirection.create("facing", (Predicate)EnumFacing.Plane.HORIZONTAL);
        field_176263_b = PropertyBool.create("powered");
        field_176265_M = PropertyBool.create("attached");
        field_176266_N = PropertyBool.create("suspended");
    }
    
    static final class SwitchEnumFacing
    {
        static final int[] field_177056_a;
        private static final String __OBFID = "CL_00002050";
        
        static {
            field_177056_a = new int[EnumFacing.values().length];
            try {
                SwitchEnumFacing.field_177056_a[EnumFacing.EAST.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                SwitchEnumFacing.field_177056_a[EnumFacing.WEST.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError2) {}
            try {
                SwitchEnumFacing.field_177056_a[EnumFacing.SOUTH.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError3) {}
            try {
                SwitchEnumFacing.field_177056_a[EnumFacing.NORTH.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError4) {}
        }
    }
}
