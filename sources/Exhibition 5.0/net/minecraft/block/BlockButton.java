// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import java.util.List;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.Entity;
import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;

public abstract class BlockButton extends Block
{
    public static final PropertyDirection FACING_PROP;
    public static final PropertyBool POWERED_PROP;
    private final boolean wooden;
    private static final String __OBFID = "CL_00000209";
    
    protected BlockButton(final boolean wooden) {
        super(Material.circuits);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockButton.FACING_PROP, EnumFacing.NORTH).withProperty(BlockButton.POWERED_PROP, false));
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabRedstone);
        this.wooden = wooden;
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final World worldIn, final BlockPos pos, final IBlockState state) {
        return null;
    }
    
    @Override
    public int tickRate(final World worldIn) {
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
    public boolean canPlaceBlockOnSide(final World worldIn, final BlockPos pos, final EnumFacing side) {
        return worldIn.getBlockState(pos.offset(side.getOpposite())).getBlock().isNormalCube();
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        for (final EnumFacing var6 : EnumFacing.values()) {
            if (worldIn.getBlockState(pos.offset(var6)).getBlock().isNormalCube()) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public IBlockState onBlockPlaced(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        return worldIn.getBlockState(pos.offset(facing.getOpposite())).getBlock().isNormalCube() ? this.getDefaultState().withProperty(BlockButton.FACING_PROP, facing).withProperty(BlockButton.POWERED_PROP, false) : this.getDefaultState().withProperty(BlockButton.FACING_PROP, EnumFacing.DOWN).withProperty(BlockButton.POWERED_PROP, false);
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        if (this.func_176583_e(worldIn, pos, state)) {
            final EnumFacing var5 = (EnumFacing)state.getValue(BlockButton.FACING_PROP);
            if (!worldIn.getBlockState(pos.offset(var5.getOpposite())).getBlock().isNormalCube()) {
                this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
            }
        }
    }
    
    private boolean func_176583_e(final World worldIn, final BlockPos p_176583_2_, final IBlockState p_176583_3_) {
        if (!this.canPlaceBlockAt(worldIn, p_176583_2_)) {
            this.dropBlockAsItem(worldIn, p_176583_2_, p_176583_3_, 0);
            worldIn.setBlockToAir(p_176583_2_);
            return false;
        }
        return true;
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess access, final BlockPos pos) {
        this.func_180681_d(access.getBlockState(pos));
    }
    
    private void func_180681_d(final IBlockState p_180681_1_) {
        final EnumFacing var2 = (EnumFacing)p_180681_1_.getValue(BlockButton.FACING_PROP);
        final boolean var3 = (boolean)p_180681_1_.getValue(BlockButton.POWERED_PROP);
        final float var4 = 0.25f;
        final float var5 = 0.375f;
        final float var6 = (var3 ? 1 : 2) / 16.0f;
        final float var7 = 0.125f;
        final float var8 = 0.1875f;
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
                break;
            }
        }
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        if (state.getValue(BlockButton.POWERED_PROP)) {
            return true;
        }
        worldIn.setBlockState(pos, state.withProperty(BlockButton.POWERED_PROP, true), 3);
        worldIn.markBlockRangeForRenderUpdate(pos, pos);
        worldIn.playSoundEffect(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, "random.click", 0.3f, 0.6f);
        this.func_176582_b(worldIn, pos, (EnumFacing)state.getValue(BlockButton.FACING_PROP));
        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
        return true;
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (state.getValue(BlockButton.POWERED_PROP)) {
            this.func_176582_b(worldIn, pos, (EnumFacing)state.getValue(BlockButton.FACING_PROP));
        }
        super.breakBlock(worldIn, pos, state);
    }
    
    @Override
    public int isProvidingWeakPower(final IBlockAccess worldIn, final BlockPos pos, final IBlockState state, final EnumFacing side) {
        return state.getValue(BlockButton.POWERED_PROP) ? 15 : 0;
    }
    
    @Override
    public int isProvidingStrongPower(final IBlockAccess worldIn, final BlockPos pos, final IBlockState state, final EnumFacing side) {
        return state.getValue(BlockButton.POWERED_PROP) ? ((state.getValue(BlockButton.FACING_PROP) == side) ? 15 : 0) : 0;
    }
    
    @Override
    public boolean canProvidePower() {
        return true;
    }
    
    @Override
    public void randomTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random random) {
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (!worldIn.isRemote && (boolean)state.getValue(BlockButton.POWERED_PROP)) {
            if (this.wooden) {
                this.func_180680_f(worldIn, pos, state);
            }
            else {
                worldIn.setBlockState(pos, state.withProperty(BlockButton.POWERED_PROP, false));
                this.func_176582_b(worldIn, pos, (EnumFacing)state.getValue(BlockButton.FACING_PROP));
                worldIn.playSoundEffect(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, "random.click", 0.3f, 0.5f);
                worldIn.markBlockRangeForRenderUpdate(pos, pos);
            }
        }
    }
    
    @Override
    public void setBlockBoundsForItemRender() {
        final float var1 = 0.1875f;
        final float var2 = 0.125f;
        final float var3 = 0.125f;
        this.setBlockBounds(0.5f - var1, 0.5f - var2, 0.5f - var3, 0.5f + var1, 0.5f + var2, 0.5f + var3);
    }
    
    @Override
    public void onEntityCollidedWithBlock(final World worldIn, final BlockPos pos, final IBlockState state, final Entity entityIn) {
        if (!worldIn.isRemote && this.wooden && !(boolean)state.getValue(BlockButton.POWERED_PROP)) {
            this.func_180680_f(worldIn, pos, state);
        }
    }
    
    private void func_180680_f(final World worldIn, final BlockPos p_180680_2_, final IBlockState p_180680_3_) {
        this.func_180681_d(p_180680_3_);
        final List var4 = worldIn.getEntitiesWithinAABB(EntityArrow.class, new AxisAlignedBB(p_180680_2_.getX() + this.minX, p_180680_2_.getY() + this.minY, p_180680_2_.getZ() + this.minZ, p_180680_2_.getX() + this.maxX, p_180680_2_.getY() + this.maxY, p_180680_2_.getZ() + this.maxZ));
        final boolean var5 = !var4.isEmpty();
        final boolean var6 = (boolean)p_180680_3_.getValue(BlockButton.POWERED_PROP);
        if (var5 && !var6) {
            worldIn.setBlockState(p_180680_2_, p_180680_3_.withProperty(BlockButton.POWERED_PROP, true));
            this.func_176582_b(worldIn, p_180680_2_, (EnumFacing)p_180680_3_.getValue(BlockButton.FACING_PROP));
            worldIn.markBlockRangeForRenderUpdate(p_180680_2_, p_180680_2_);
            worldIn.playSoundEffect(p_180680_2_.getX() + 0.5, p_180680_2_.getY() + 0.5, p_180680_2_.getZ() + 0.5, "random.click", 0.3f, 0.6f);
        }
        if (!var5 && var6) {
            worldIn.setBlockState(p_180680_2_, p_180680_3_.withProperty(BlockButton.POWERED_PROP, false));
            this.func_176582_b(worldIn, p_180680_2_, (EnumFacing)p_180680_3_.getValue(BlockButton.FACING_PROP));
            worldIn.markBlockRangeForRenderUpdate(p_180680_2_, p_180680_2_);
            worldIn.playSoundEffect(p_180680_2_.getX() + 0.5, p_180680_2_.getY() + 0.5, p_180680_2_.getZ() + 0.5, "random.click", 0.3f, 0.5f);
        }
        if (var5) {
            worldIn.scheduleUpdate(p_180680_2_, this, this.tickRate(worldIn));
        }
    }
    
    private void func_176582_b(final World worldIn, final BlockPos p_176582_2_, final EnumFacing p_176582_3_) {
        worldIn.notifyNeighborsOfStateChange(p_176582_2_, this);
        worldIn.notifyNeighborsOfStateChange(p_176582_2_.offset(p_176582_3_.getOpposite()), this);
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        EnumFacing var2 = null;
        switch (meta & 0x7) {
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
                break;
            }
        }
        return this.getDefaultState().withProperty(BlockButton.FACING_PROP, var2).withProperty(BlockButton.POWERED_PROP, (meta & 0x8) > 0);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int var2 = 0;
        switch (SwitchEnumFacing.field_180420_a[((EnumFacing)state.getValue(BlockButton.FACING_PROP)).ordinal()]) {
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
                break;
            }
        }
        if (state.getValue(BlockButton.POWERED_PROP)) {
            var2 |= 0x8;
        }
        return var2;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockButton.FACING_PROP, BlockButton.POWERED_PROP });
    }
    
    static {
        FACING_PROP = PropertyDirection.create("facing");
        POWERED_PROP = PropertyBool.create("powered");
    }
    
    static final class SwitchEnumFacing
    {
        static final int[] field_180420_a;
        private static final String __OBFID = "CL_00002131";
        
        static {
            field_180420_a = new int[EnumFacing.values().length];
            try {
                SwitchEnumFacing.field_180420_a[EnumFacing.EAST.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                SwitchEnumFacing.field_180420_a[EnumFacing.WEST.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError2) {}
            try {
                SwitchEnumFacing.field_180420_a[EnumFacing.SOUTH.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError3) {}
            try {
                SwitchEnumFacing.field_180420_a[EnumFacing.NORTH.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError4) {}
            try {
                SwitchEnumFacing.field_180420_a[EnumFacing.UP.ordinal()] = 5;
            }
            catch (NoSuchFieldError noSuchFieldError5) {}
            try {
                SwitchEnumFacing.field_180420_a[EnumFacing.DOWN.ordinal()] = 6;
            }
            catch (NoSuchFieldError noSuchFieldError6) {}
        }
    }
}
