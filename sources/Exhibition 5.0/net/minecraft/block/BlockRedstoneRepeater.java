// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.properties.PropertyBool;

public class BlockRedstoneRepeater extends BlockRedstoneDiode
{
    public static final PropertyBool field_176411_a;
    public static final PropertyInteger field_176410_b;
    private static final String __OBFID = "CL_00000301";
    
    protected BlockRedstoneRepeater(final boolean p_i45424_1_) {
        super(p_i45424_1_);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockDirectional.AGE, EnumFacing.NORTH).withProperty(BlockRedstoneRepeater.field_176410_b, 1).withProperty(BlockRedstoneRepeater.field_176411_a, false));
    }
    
    @Override
    public IBlockState getActualState(final IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        return state.withProperty(BlockRedstoneRepeater.field_176411_a, this.func_176405_b(worldIn, pos, state));
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        if (!playerIn.capabilities.allowEdit) {
            return false;
        }
        worldIn.setBlockState(pos, state.cycleProperty(BlockRedstoneRepeater.field_176410_b), 3);
        return true;
    }
    
    @Override
    protected int func_176403_d(final IBlockState p_176403_1_) {
        return (int)p_176403_1_.getValue(BlockRedstoneRepeater.field_176410_b) * 2;
    }
    
    @Override
    protected IBlockState func_180674_e(final IBlockState p_180674_1_) {
        final Integer var2 = (Integer)p_180674_1_.getValue(BlockRedstoneRepeater.field_176410_b);
        final Boolean var3 = (Boolean)p_180674_1_.getValue(BlockRedstoneRepeater.field_176411_a);
        final EnumFacing var4 = (EnumFacing)p_180674_1_.getValue(BlockDirectional.AGE);
        return Blocks.powered_repeater.getDefaultState().withProperty(BlockDirectional.AGE, var4).withProperty(BlockRedstoneRepeater.field_176410_b, var2).withProperty(BlockRedstoneRepeater.field_176411_a, var3);
    }
    
    @Override
    protected IBlockState func_180675_k(final IBlockState p_180675_1_) {
        final Integer var2 = (Integer)p_180675_1_.getValue(BlockRedstoneRepeater.field_176410_b);
        final Boolean var3 = (Boolean)p_180675_1_.getValue(BlockRedstoneRepeater.field_176411_a);
        final EnumFacing var4 = (EnumFacing)p_180675_1_.getValue(BlockDirectional.AGE);
        return Blocks.unpowered_repeater.getDefaultState().withProperty(BlockDirectional.AGE, var4).withProperty(BlockRedstoneRepeater.field_176410_b, var2).withProperty(BlockRedstoneRepeater.field_176411_a, var3);
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Items.repeater;
    }
    
    @Override
    public Item getItem(final World worldIn, final BlockPos pos) {
        return Items.repeater;
    }
    
    @Override
    public boolean func_176405_b(final IBlockAccess p_176405_1_, final BlockPos p_176405_2_, final IBlockState p_176405_3_) {
        return this.func_176407_c(p_176405_1_, p_176405_2_, p_176405_3_) > 0;
    }
    
    @Override
    protected boolean func_149908_a(final Block p_149908_1_) {
        return BlockRedstoneDiode.isRedstoneRepeaterBlockID(p_149908_1_);
    }
    
    @Override
    public void randomDisplayTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (this.isRepeaterPowered) {
            final EnumFacing var5 = (EnumFacing)state.getValue(BlockDirectional.AGE);
            final double var6 = pos.getX() + 0.5f + (rand.nextFloat() - 0.5f) * 0.2;
            final double var7 = pos.getY() + 0.4f + (rand.nextFloat() - 0.5f) * 0.2;
            final double var8 = pos.getZ() + 0.5f + (rand.nextFloat() - 0.5f) * 0.2;
            float var9 = -5.0f;
            if (rand.nextBoolean()) {
                var9 = (int)state.getValue(BlockRedstoneRepeater.field_176410_b) * 2 - 1;
            }
            var9 /= 16.0f;
            final double var10 = var9 * var5.getFrontOffsetX();
            final double var11 = var9 * var5.getFrontOffsetZ();
            worldIn.spawnParticle(EnumParticleTypes.REDSTONE, var6 + var10, var7, var8 + var11, 0.0, 0.0, 0.0, new int[0]);
        }
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        this.func_176400_h(worldIn, pos, state);
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockDirectional.AGE, EnumFacing.getHorizontal(meta)).withProperty(BlockRedstoneRepeater.field_176411_a, false).withProperty(BlockRedstoneRepeater.field_176410_b, 1 + (meta >> 2));
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        final byte var2 = 0;
        int var3 = var2 | ((EnumFacing)state.getValue(BlockDirectional.AGE)).getHorizontalIndex();
        var3 |= (int)state.getValue(BlockRedstoneRepeater.field_176410_b) - 1 << 2;
        return var3;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockDirectional.AGE, BlockRedstoneRepeater.field_176410_b, BlockRedstoneRepeater.field_176411_a });
    }
    
    static {
        field_176411_a = PropertyBool.create("locked");
        field_176410_b = PropertyInteger.create("delay", 1, 4);
    }
}
