// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.item.Item;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.init.Blocks;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;

public class BlockCocoa extends BlockDirectional implements IGrowable
{
    public static final PropertyInteger field_176501_a;
    private static final String __OBFID = "CL_00000216";
    
    public BlockCocoa() {
        super(Material.plants);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockDirectional.AGE, EnumFacing.NORTH).withProperty(BlockCocoa.field_176501_a, 0));
        this.setTickRandomly(true);
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (!this.canBlockStay(worldIn, pos, state)) {
            this.dropBlock(worldIn, pos, state);
        }
        else if (worldIn.rand.nextInt(5) == 0) {
            final int var5 = (int)state.getValue(BlockCocoa.field_176501_a);
            if (var5 < 2) {
                worldIn.setBlockState(pos, state.withProperty(BlockCocoa.field_176501_a, var5 + 1), 2);
            }
        }
    }
    
    public boolean canBlockStay(final World worldIn, BlockPos p_176499_2_, final IBlockState p_176499_3_) {
        p_176499_2_ = p_176499_2_.offset((EnumFacing)p_176499_3_.getValue(BlockDirectional.AGE));
        final IBlockState var4 = worldIn.getBlockState(p_176499_2_);
        return var4.getBlock() == Blocks.log && var4.getValue(BlockPlanks.VARIANT_PROP) == BlockPlanks.EnumType.JUNGLE;
    }
    
    @Override
    public boolean isFullCube() {
        return false;
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final World worldIn, final BlockPos pos, final IBlockState state) {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.getCollisionBoundingBox(worldIn, pos, state);
    }
    
    @Override
    public AxisAlignedBB getSelectedBoundingBox(final World worldIn, final BlockPos pos) {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.getSelectedBoundingBox(worldIn, pos);
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess access, final BlockPos pos) {
        final IBlockState var3 = access.getBlockState(pos);
        final EnumFacing var4 = (EnumFacing)var3.getValue(BlockDirectional.AGE);
        final int var5 = (int)var3.getValue(BlockCocoa.field_176501_a);
        final int var6 = 4 + var5 * 2;
        final int var7 = 5 + var5 * 2;
        final float var8 = var6 / 2.0f;
        switch (SwitchEnumFacing.FACINGARRAY[var4.ordinal()]) {
            case 1: {
                this.setBlockBounds((8.0f - var8) / 16.0f, (12.0f - var7) / 16.0f, (15.0f - var6) / 16.0f, (8.0f + var8) / 16.0f, 0.75f, 0.9375f);
                break;
            }
            case 2: {
                this.setBlockBounds((8.0f - var8) / 16.0f, (12.0f - var7) / 16.0f, 0.0625f, (8.0f + var8) / 16.0f, 0.75f, (1.0f + var6) / 16.0f);
                break;
            }
            case 3: {
                this.setBlockBounds(0.0625f, (12.0f - var7) / 16.0f, (8.0f - var8) / 16.0f, (1.0f + var6) / 16.0f, 0.75f, (8.0f + var8) / 16.0f);
                break;
            }
            case 4: {
                this.setBlockBounds((15.0f - var6) / 16.0f, (12.0f - var7) / 16.0f, (8.0f - var8) / 16.0f, 0.9375f, 0.75f, (8.0f + var8) / 16.0f);
                break;
            }
        }
    }
    
    @Override
    public void onBlockPlacedBy(final World worldIn, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
        final EnumFacing var6 = EnumFacing.fromAngle(placer.rotationYaw);
        worldIn.setBlockState(pos, state.withProperty(BlockDirectional.AGE, var6), 2);
    }
    
    @Override
    public IBlockState onBlockPlaced(final World worldIn, final BlockPos pos, EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        if (!facing.getAxis().isHorizontal()) {
            facing = EnumFacing.NORTH;
        }
        return this.getDefaultState().withProperty(BlockDirectional.AGE, facing.getOpposite()).withProperty(BlockCocoa.field_176501_a, 0);
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        if (!this.canBlockStay(worldIn, pos, state)) {
            this.dropBlock(worldIn, pos, state);
        }
    }
    
    private void dropBlock(final World worldIn, final BlockPos p_176500_2_, final IBlockState p_176500_3_) {
        worldIn.setBlockState(p_176500_2_, Blocks.air.getDefaultState(), 3);
        this.dropBlockAsItem(worldIn, p_176500_2_, p_176500_3_, 0);
    }
    
    @Override
    public void dropBlockAsItemWithChance(final World worldIn, final BlockPos pos, final IBlockState state, final float chance, final int fortune) {
        final int var6 = (int)state.getValue(BlockCocoa.field_176501_a);
        byte var7 = 1;
        if (var6 >= 2) {
            var7 = 3;
        }
        for (int var8 = 0; var8 < var7; ++var8) {
            Block.spawnAsEntity(worldIn, pos, new ItemStack(Items.dye, 1, EnumDyeColor.BROWN.getDyeColorDamage()));
        }
    }
    
    @Override
    public Item getItem(final World worldIn, final BlockPos pos) {
        return Items.dye;
    }
    
    @Override
    public int getDamageValue(final World worldIn, final BlockPos pos) {
        return EnumDyeColor.BROWN.getDyeColorDamage();
    }
    
    @Override
    public boolean isStillGrowing(final World worldIn, final BlockPos p_176473_2_, final IBlockState p_176473_3_, final boolean p_176473_4_) {
        return (int)p_176473_3_.getValue(BlockCocoa.field_176501_a) < 2;
    }
    
    @Override
    public boolean canUseBonemeal(final World worldIn, final Random p_180670_2_, final BlockPos p_180670_3_, final IBlockState p_180670_4_) {
        return true;
    }
    
    @Override
    public void grow(final World worldIn, final Random p_176474_2_, final BlockPos p_176474_3_, final IBlockState p_176474_4_) {
        worldIn.setBlockState(p_176474_3_, p_176474_4_.withProperty(BlockCocoa.field_176501_a, (int)p_176474_4_.getValue(BlockCocoa.field_176501_a) + 1), 2);
    }
    
    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockDirectional.AGE, EnumFacing.getHorizontal(meta)).withProperty(BlockCocoa.field_176501_a, (meta & 0xF) >> 2);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        final byte var2 = 0;
        int var3 = var2 | ((EnumFacing)state.getValue(BlockDirectional.AGE)).getHorizontalIndex();
        var3 |= (int)state.getValue(BlockCocoa.field_176501_a) << 2;
        return var3;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockDirectional.AGE, BlockCocoa.field_176501_a });
    }
    
    static {
        field_176501_a = PropertyInteger.create("age", 0, 2);
    }
    
    static final class SwitchEnumFacing
    {
        static final int[] FACINGARRAY;
        private static final String __OBFID = "CL_00002130";
        
        static {
            FACINGARRAY = new int[EnumFacing.values().length];
            try {
                SwitchEnumFacing.FACINGARRAY[EnumFacing.SOUTH.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                SwitchEnumFacing.FACINGARRAY[EnumFacing.NORTH.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError2) {}
            try {
                SwitchEnumFacing.FACINGARRAY[EnumFacing.WEST.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError3) {}
            try {
                SwitchEnumFacing.FACINGARRAY[EnumFacing.EAST.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError4) {}
        }
    }
}
