// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.stats.StatList;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.properties.PropertyInteger;

public class BlockCake extends Block
{
    public static final PropertyInteger BITES;
    protected static final AxisAlignedBB[] CAKE_AABB;
    
    protected BlockCake() {
        super(Material.CAKE);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockCake.BITES, 0));
        this.setTickRandomly(true);
    }
    
    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
        return BlockCake.CAKE_AABB[state.getValue((IProperty<Integer>)BlockCake.BITES)];
    }
    
    @Override
    @Deprecated
    public boolean isFullCube(final IBlockState state) {
        return false;
    }
    
    @Override
    @Deprecated
    public boolean isOpaqueCube(final IBlockState state) {
        return false;
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
        if (!worldIn.isRemote) {
            return this.eatCake(worldIn, pos, state, playerIn);
        }
        final ItemStack itemstack = playerIn.getHeldItem(hand);
        return this.eatCake(worldIn, pos, state, playerIn) || itemstack.isEmpty();
    }
    
    private boolean eatCake(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer player) {
        if (!player.canEat(false)) {
            return false;
        }
        player.addStat(StatList.CAKE_SLICES_EATEN);
        player.getFoodStats().addStats(2, 0.1f);
        final int i = state.getValue((IProperty<Integer>)BlockCake.BITES);
        if (i < 6) {
            worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockCake.BITES, i + 1), 3);
        }
        else {
            worldIn.setBlockToAir(pos);
        }
        return true;
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos) && this.canBlockStay(worldIn, pos);
    }
    
    @Override
    public void neighborChanged(final IBlockState state, final World worldIn, final BlockPos pos, final Block blockIn, final BlockPos fromPos) {
        if (!this.canBlockStay(worldIn, pos)) {
            worldIn.setBlockToAir(pos);
        }
    }
    
    private boolean canBlockStay(final World worldIn, final BlockPos pos) {
        return worldIn.getBlockState(pos.down()).getMaterial().isSolid();
    }
    
    @Override
    public int quantityDropped(final Random random) {
        return 0;
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Items.AIR;
    }
    
    @Override
    public ItemStack getItem(final World worldIn, final BlockPos pos, final IBlockState state) {
        return new ItemStack(Items.CAKE);
    }
    
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockCake.BITES, meta);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue((IProperty<Integer>)BlockCake.BITES);
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockCake.BITES });
    }
    
    @Override
    @Deprecated
    public int getComparatorInputOverride(final IBlockState blockState, final World worldIn, final BlockPos pos) {
        return (7 - blockState.getValue((IProperty<Integer>)BlockCake.BITES)) * 2;
    }
    
    @Override
    @Deprecated
    public boolean hasComparatorInputOverride(final IBlockState state) {
        return true;
    }
    
    @Override
    @Deprecated
    public BlockFaceShape getBlockFaceShape(final IBlockAccess worldIn, final IBlockState state, final BlockPos pos, final EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
    
    static {
        BITES = PropertyInteger.create("bites", 0, 6);
        CAKE_AABB = new AxisAlignedBB[] { new AxisAlignedBB(0.0625, 0.0, 0.0625, 0.9375, 0.5, 0.9375), new AxisAlignedBB(0.1875, 0.0, 0.0625, 0.9375, 0.5, 0.9375), new AxisAlignedBB(0.3125, 0.0, 0.0625, 0.9375, 0.5, 0.9375), new AxisAlignedBB(0.4375, 0.0, 0.0625, 0.9375, 0.5, 0.9375), new AxisAlignedBB(0.5625, 0.0, 0.0625, 0.9375, 0.5, 0.9375), new AxisAlignedBB(0.6875, 0.0, 0.0625, 0.9375, 0.5, 0.9375), new AxisAlignedBB(0.8125, 0.0, 0.0625, 0.9375, 0.5, 0.9375) };
    }
}
