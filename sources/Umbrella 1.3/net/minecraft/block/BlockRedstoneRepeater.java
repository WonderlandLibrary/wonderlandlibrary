/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneDiode;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRedstoneRepeater
extends BlockRedstoneDiode {
    public static final PropertyBool field_176411_a = PropertyBool.create("locked");
    public static final PropertyInteger field_176410_b = PropertyInteger.create("delay", 1, 4);
    private static final String __OBFID = "CL_00000301";

    protected BlockRedstoneRepeater(boolean p_i45424_1_) {
        super(p_i45424_1_);
        this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, (Comparable)((Object)EnumFacing.NORTH)).withProperty(field_176410_b, Integer.valueOf(1)).withProperty(field_176411_a, Boolean.valueOf(false)));
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state.withProperty(field_176411_a, Boolean.valueOf(this.func_176405_b(worldIn, pos, state)));
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!playerIn.capabilities.allowEdit) {
            return false;
        }
        worldIn.setBlockState(pos, state.cycleProperty(field_176410_b), 3);
        return true;
    }

    @Override
    protected int func_176403_d(IBlockState p_176403_1_) {
        return (Integer)p_176403_1_.getValue(field_176410_b) * 2;
    }

    @Override
    protected IBlockState func_180674_e(IBlockState p_180674_1_) {
        Integer var2 = (Integer)p_180674_1_.getValue(field_176410_b);
        Boolean var3 = (Boolean)p_180674_1_.getValue(field_176411_a);
        EnumFacing var4 = (EnumFacing)((Object)p_180674_1_.getValue(AGE));
        return Blocks.powered_repeater.getDefaultState().withProperty(AGE, (Comparable)((Object)var4)).withProperty(field_176410_b, var2).withProperty(field_176411_a, var3);
    }

    @Override
    protected IBlockState func_180675_k(IBlockState p_180675_1_) {
        Integer var2 = (Integer)p_180675_1_.getValue(field_176410_b);
        Boolean var3 = (Boolean)p_180675_1_.getValue(field_176411_a);
        EnumFacing var4 = (EnumFacing)((Object)p_180675_1_.getValue(AGE));
        return Blocks.unpowered_repeater.getDefaultState().withProperty(AGE, (Comparable)((Object)var4)).withProperty(field_176410_b, var2).withProperty(field_176411_a, var3);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.repeater;
    }

    @Override
    public Item getItem(World worldIn, BlockPos pos) {
        return Items.repeater;
    }

    @Override
    public boolean func_176405_b(IBlockAccess p_176405_1_, BlockPos p_176405_2_, IBlockState p_176405_3_) {
        return this.func_176407_c(p_176405_1_, p_176405_2_, p_176405_3_) > 0;
    }

    @Override
    protected boolean func_149908_a(Block p_149908_1_) {
        return BlockRedstoneRepeater.isRedstoneRepeaterBlockID(p_149908_1_);
    }

    @Override
    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (this.isRepeaterPowered) {
            EnumFacing var5 = (EnumFacing)((Object)state.getValue(AGE));
            double var6 = (double)((float)pos.getX() + 0.5f) + (double)(rand.nextFloat() - 0.5f) * 0.2;
            double var8 = (double)((float)pos.getY() + 0.4f) + (double)(rand.nextFloat() - 0.5f) * 0.2;
            double var10 = (double)((float)pos.getZ() + 0.5f) + (double)(rand.nextFloat() - 0.5f) * 0.2;
            float var12 = -5.0f;
            if (rand.nextBoolean()) {
                var12 = (Integer)state.getValue(field_176410_b) * 2 - 1;
            }
            double var13 = (var12 /= 16.0f) * (float)var5.getFrontOffsetX();
            double var15 = var12 * (float)var5.getFrontOffsetZ();
            worldIn.spawnParticle(EnumParticleTypes.REDSTONE, var6 + var13, var8, var10 + var15, 0.0, 0.0, 0.0, new int[0]);
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        this.func_176400_h(worldIn, pos, state);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(AGE, (Comparable)((Object)EnumFacing.getHorizontal(meta))).withProperty(field_176411_a, Boolean.valueOf(false)).withProperty(field_176410_b, Integer.valueOf(1 + (meta >> 2)));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int var2 = 0;
        int var3 = var2 | ((EnumFacing)((Object)state.getValue(AGE))).getHorizontalIndex();
        return var3 |= (Integer)state.getValue(field_176410_b) - 1 << 2;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, AGE, field_176410_b, field_176411_a);
    }
}

