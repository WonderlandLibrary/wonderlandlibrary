/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockButton
extends Block {
    public static final PropertyDirection FACING_PROP = PropertyDirection.create("facing");
    public static final PropertyBool POWERED_PROP = PropertyBool.create("powered");
    private final boolean wooden;
    private static final String __OBFID = "CL_00000209";

    protected BlockButton(boolean wooden) {
        super(Material.circuits);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING_PROP, (Comparable)((Object)EnumFacing.NORTH)).withProperty(POWERED_PROP, Boolean.valueOf(false)));
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabRedstone);
        this.wooden = wooden;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        return null;
    }

    @Override
    public int tickRate(World worldIn) {
        return this.wooden ? 30 : 20;
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
        return worldIn.getBlockState(pos.offset(side.getOpposite())).getBlock().isNormalCube();
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        for (EnumFacing var6 : EnumFacing.values()) {
            if (!worldIn.getBlockState(pos.offset(var6)).getBlock().isNormalCube()) continue;
            return true;
        }
        return false;
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return worldIn.getBlockState(pos.offset(facing.getOpposite())).getBlock().isNormalCube() ? this.getDefaultState().withProperty(FACING_PROP, (Comparable)((Object)facing)).withProperty(POWERED_PROP, Boolean.valueOf(false)) : this.getDefaultState().withProperty(FACING_PROP, (Comparable)((Object)EnumFacing.DOWN)).withProperty(POWERED_PROP, Boolean.valueOf(false));
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        EnumFacing var5;
        if (this.func_176583_e(worldIn, pos, state) && !worldIn.getBlockState(pos.offset((var5 = (EnumFacing)((Object)state.getValue(FACING_PROP))).getOpposite())).getBlock().isNormalCube()) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
    }

    private boolean func_176583_e(World worldIn, BlockPos p_176583_2_, IBlockState p_176583_3_) {
        if (!this.canPlaceBlockAt(worldIn, p_176583_2_)) {
            this.dropBlockAsItem(worldIn, p_176583_2_, p_176583_3_, 0);
            worldIn.setBlockToAir(p_176583_2_);
            return false;
        }
        return true;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
        this.func_180681_d(access.getBlockState(pos));
    }

    private void func_180681_d(IBlockState p_180681_1_) {
        EnumFacing var2 = (EnumFacing)((Object)p_180681_1_.getValue(FACING_PROP));
        boolean var3 = (Boolean)p_180681_1_.getValue(POWERED_PROP);
        float var4 = 0.25f;
        float var5 = 0.375f;
        float var6 = (float)(var3 ? 1 : 2) / 16.0f;
        float var7 = 0.125f;
        float var8 = 0.1875f;
        switch (SwitchEnumFacing.field_180420_a[var2.ordinal()]) {
            case 1: {
                this.setBlockBounds(0.0f, 0.375f, 0.3125f, var6, 0.625f, 0.6875f);
                break;
            }
            case 2: {
                this.setBlockBounds(1.0f - var6, 0.375f, 0.3125f, 1.0f, 0.625f, 0.6875f);
                break;
            }
            case 3: {
                this.setBlockBounds(0.3125f, 0.375f, 0.0f, 0.6875f, 0.625f, var6);
                break;
            }
            case 4: {
                this.setBlockBounds(0.3125f, 0.375f, 1.0f - var6, 0.6875f, 0.625f, 1.0f);
                break;
            }
            case 5: {
                this.setBlockBounds(0.3125f, 0.0f, 0.375f, 0.6875f, 0.0f + var6, 0.625f);
                break;
            }
            case 6: {
                this.setBlockBounds(0.3125f, 1.0f - var6, 0.375f, 0.6875f, 1.0f, 0.625f);
            }
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (((Boolean)state.getValue(POWERED_PROP)).booleanValue()) {
            return true;
        }
        worldIn.setBlockState(pos, state.withProperty(POWERED_PROP, Boolean.valueOf(true)), 3);
        worldIn.markBlockRangeForRenderUpdate(pos, pos);
        worldIn.playSoundEffect((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, "random.click", 0.3f, 0.6f);
        this.func_176582_b(worldIn, pos, (EnumFacing)((Object)state.getValue(FACING_PROP)));
        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
        return true;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (((Boolean)state.getValue(POWERED_PROP)).booleanValue()) {
            this.func_176582_b(worldIn, pos, (EnumFacing)((Object)state.getValue(FACING_PROP)));
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
        return (Boolean)state.getValue(POWERED_PROP) != false ? 15 : 0;
    }

    @Override
    public int isProvidingStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
        return (Boolean)state.getValue(POWERED_PROP) == false ? 0 : (state.getValue(FACING_PROP) == side ? 15 : 0);
    }

    @Override
    public boolean canProvidePower() {
        return true;
    }

    @Override
    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!worldIn.isRemote && ((Boolean)state.getValue(POWERED_PROP)).booleanValue()) {
            if (this.wooden) {
                this.func_180680_f(worldIn, pos, state);
            } else {
                worldIn.setBlockState(pos, state.withProperty(POWERED_PROP, Boolean.valueOf(false)));
                this.func_176582_b(worldIn, pos, (EnumFacing)((Object)state.getValue(FACING_PROP)));
                worldIn.playSoundEffect((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, "random.click", 0.3f, 0.5f);
                worldIn.markBlockRangeForRenderUpdate(pos, pos);
            }
        }
    }

    @Override
    public void setBlockBoundsForItemRender() {
        float var1 = 0.1875f;
        float var2 = 0.125f;
        float var3 = 0.125f;
        this.setBlockBounds(0.5f - var1, 0.5f - var2, 0.5f - var3, 0.5f + var1, 0.5f + var2, 0.5f + var3);
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        if (!worldIn.isRemote && this.wooden && !((Boolean)state.getValue(POWERED_PROP)).booleanValue()) {
            this.func_180680_f(worldIn, pos, state);
        }
    }

    private void func_180680_f(World worldIn, BlockPos p_180680_2_, IBlockState p_180680_3_) {
        this.func_180681_d(p_180680_3_);
        List var4 = worldIn.getEntitiesWithinAABB(EntityArrow.class, new AxisAlignedBB((double)p_180680_2_.getX() + this.minX, (double)p_180680_2_.getY() + this.minY, (double)p_180680_2_.getZ() + this.minZ, (double)p_180680_2_.getX() + this.maxX, (double)p_180680_2_.getY() + this.maxY, (double)p_180680_2_.getZ() + this.maxZ));
        boolean var5 = !var4.isEmpty();
        boolean var6 = (Boolean)p_180680_3_.getValue(POWERED_PROP);
        if (var5 && !var6) {
            worldIn.setBlockState(p_180680_2_, p_180680_3_.withProperty(POWERED_PROP, Boolean.valueOf(true)));
            this.func_176582_b(worldIn, p_180680_2_, (EnumFacing)((Object)p_180680_3_.getValue(FACING_PROP)));
            worldIn.markBlockRangeForRenderUpdate(p_180680_2_, p_180680_2_);
            worldIn.playSoundEffect((double)p_180680_2_.getX() + 0.5, (double)p_180680_2_.getY() + 0.5, (double)p_180680_2_.getZ() + 0.5, "random.click", 0.3f, 0.6f);
        }
        if (!var5 && var6) {
            worldIn.setBlockState(p_180680_2_, p_180680_3_.withProperty(POWERED_PROP, Boolean.valueOf(false)));
            this.func_176582_b(worldIn, p_180680_2_, (EnumFacing)((Object)p_180680_3_.getValue(FACING_PROP)));
            worldIn.markBlockRangeForRenderUpdate(p_180680_2_, p_180680_2_);
            worldIn.playSoundEffect((double)p_180680_2_.getX() + 0.5, (double)p_180680_2_.getY() + 0.5, (double)p_180680_2_.getZ() + 0.5, "random.click", 0.3f, 0.5f);
        }
        if (var5) {
            worldIn.scheduleUpdate(p_180680_2_, this, this.tickRate(worldIn));
        }
    }

    private void func_176582_b(World worldIn, BlockPos p_176582_2_, EnumFacing p_176582_3_) {
        worldIn.notifyNeighborsOfStateChange(p_176582_2_, this);
        worldIn.notifyNeighborsOfStateChange(p_176582_2_.offset(p_176582_3_.getOpposite()), this);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing var2;
        switch (meta & 7) {
            case 0: {
                var2 = EnumFacing.DOWN;
                break;
            }
            case 1: {
                var2 = EnumFacing.EAST;
                break;
            }
            case 2: {
                var2 = EnumFacing.WEST;
                break;
            }
            case 3: {
                var2 = EnumFacing.SOUTH;
                break;
            }
            case 4: {
                var2 = EnumFacing.NORTH;
                break;
            }
            default: {
                var2 = EnumFacing.UP;
            }
        }
        return this.getDefaultState().withProperty(FACING_PROP, (Comparable)((Object)var2)).withProperty(POWERED_PROP, Boolean.valueOf((meta & 8) > 0));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int var2;
        switch (SwitchEnumFacing.field_180420_a[((EnumFacing)((Object)state.getValue(FACING_PROP))).ordinal()]) {
            case 1: {
                var2 = 1;
                break;
            }
            case 2: {
                var2 = 2;
                break;
            }
            case 3: {
                var2 = 3;
                break;
            }
            case 4: {
                var2 = 4;
                break;
            }
            default: {
                var2 = 5;
                break;
            }
            case 6: {
                var2 = 0;
            }
        }
        if (((Boolean)state.getValue(POWERED_PROP)).booleanValue()) {
            var2 |= 8;
        }
        return var2;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, FACING_PROP, POWERED_PROP);
    }

    static final class SwitchEnumFacing {
        static final int[] field_180420_a = new int[EnumFacing.values().length];
        private static final String __OBFID = "CL_00002131";

        static {
            try {
                SwitchEnumFacing.field_180420_a[EnumFacing.EAST.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumFacing.field_180420_a[EnumFacing.WEST.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumFacing.field_180420_a[EnumFacing.SOUTH.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumFacing.field_180420_a[EnumFacing.NORTH.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumFacing.field_180420_a[EnumFacing.UP.ordinal()] = 5;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumFacing.field_180420_a[EnumFacing.DOWN.ordinal()] = 6;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
        }

        SwitchEnumFacing() {
        }
    }
}

