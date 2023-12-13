// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import java.util.Iterator;
import net.minecraft.util.EnumFacing;
import net.minecraft.init.Blocks;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;

public class BlockReed extends Block
{
    public static final PropertyInteger field_176355_a;
    private static final String __OBFID = "CL_00000300";
    
    protected BlockReed() {
        super(Material.plants);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockReed.field_176355_a, 0));
        final float var1 = 0.375f;
        this.setBlockBounds(0.5f - var1, 0.0f, 0.5f - var1, 0.5f + var1, 1.0f, 0.5f + var1);
        this.setTickRandomly(true);
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if ((worldIn.getBlockState(pos.offsetDown()).getBlock() == Blocks.reeds || this.func_176353_e(worldIn, pos, state)) && worldIn.isAirBlock(pos.offsetUp())) {
            int var5;
            for (var5 = 1; worldIn.getBlockState(pos.offsetDown(var5)).getBlock() == this; ++var5) {}
            if (var5 < 3) {
                final int var6 = (int)state.getValue(BlockReed.field_176355_a);
                if (var6 == 15) {
                    worldIn.setBlockState(pos.offsetUp(), this.getDefaultState());
                    worldIn.setBlockState(pos, state.withProperty(BlockReed.field_176355_a, 0), 4);
                }
                else {
                    worldIn.setBlockState(pos, state.withProperty(BlockReed.field_176355_a, var6 + 1), 4);
                }
            }
        }
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        final Block var3 = worldIn.getBlockState(pos.offsetDown()).getBlock();
        if (var3 == this) {
            return true;
        }
        if (var3 != Blocks.grass && var3 != Blocks.dirt && var3 != Blocks.sand) {
            return false;
        }
        for (final EnumFacing var5 : EnumFacing.Plane.HORIZONTAL) {
            if (worldIn.getBlockState(pos.offset(var5).offsetDown()).getBlock().getMaterial() == Material.water) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        this.func_176353_e(worldIn, pos, state);
    }
    
    protected final boolean func_176353_e(final World worldIn, final BlockPos p_176353_2_, final IBlockState p_176353_3_) {
        if (this.func_176354_d(worldIn, p_176353_2_)) {
            return true;
        }
        this.dropBlockAsItem(worldIn, p_176353_2_, p_176353_3_, 0);
        worldIn.setBlockToAir(p_176353_2_);
        return false;
    }
    
    public boolean func_176354_d(final World worldIn, final BlockPos p_176354_2_) {
        return this.canPlaceBlockAt(worldIn, p_176354_2_);
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final World worldIn, final BlockPos pos, final IBlockState state) {
        return null;
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Items.reeds;
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
    public Item getItem(final World worldIn, final BlockPos pos) {
        return Items.reeds;
    }
    
    @Override
    public int colorMultiplier(final IBlockAccess worldIn, final BlockPos pos, final int renderPass) {
        return worldIn.getBiomeGenForCoords(pos).func_180627_b(pos);
    }
    
    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockReed.field_176355_a, meta);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return (int)state.getValue(BlockReed.field_176355_a);
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockReed.field_176355_a });
    }
    
    static {
        field_176355_a = PropertyInteger.create("age", 0, 15);
    }
}
