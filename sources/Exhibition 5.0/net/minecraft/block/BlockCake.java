// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.util.EnumFacing;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;

public class BlockCake extends Block
{
    public static final PropertyInteger BITES;
    private static final String __OBFID = "CL_00000211";
    
    protected BlockCake() {
        super(Material.cake);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockCake.BITES, 0));
        this.setTickRandomly(true);
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess access, final BlockPos pos) {
        final float var3 = 0.0625f;
        final float var4 = (1 + (int)access.getBlockState(pos).getValue(BlockCake.BITES) * 2) / 16.0f;
        final float var5 = 0.5f;
        this.setBlockBounds(var4, 0.0f, var3, 1.0f - var3, var5, 1.0f - var3);
    }
    
    @Override
    public void setBlockBoundsForItemRender() {
        final float var1 = 0.0625f;
        final float var2 = 0.5f;
        this.setBlockBounds(var1, 0.0f, var1, 1.0f - var1, var2, 1.0f - var1);
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final World worldIn, final BlockPos pos, final IBlockState state) {
        final float var4 = 0.0625f;
        final float var5 = (1 + (int)state.getValue(BlockCake.BITES) * 2) / 16.0f;
        final float var6 = 0.5f;
        return new AxisAlignedBB(pos.getX() + var5, pos.getY(), pos.getZ() + var4, pos.getX() + 1 - var4, pos.getY() + var6, pos.getZ() + 1 - var4);
    }
    
    @Override
    public AxisAlignedBB getSelectedBoundingBox(final World worldIn, final BlockPos pos) {
        return this.getCollisionBoundingBox(worldIn, pos, worldIn.getBlockState(pos));
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
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        this.eatCake(worldIn, pos, state, playerIn);
        return true;
    }
    
    @Override
    public void onBlockClicked(final World worldIn, final BlockPos pos, final EntityPlayer playerIn) {
        this.eatCake(worldIn, pos, worldIn.getBlockState(pos), playerIn);
    }
    
    private void eatCake(final World worldIn, final BlockPos p_180682_2_, final IBlockState p_180682_3_, final EntityPlayer p_180682_4_) {
        if (p_180682_4_.canEat(false)) {
            p_180682_4_.getFoodStats().addStats(2, 0.1f);
            final int var5 = (int)p_180682_3_.getValue(BlockCake.BITES);
            if (var5 < 6) {
                worldIn.setBlockState(p_180682_2_, p_180682_3_.withProperty(BlockCake.BITES, var5 + 1), 3);
            }
            else {
                worldIn.setBlockToAir(p_180682_2_);
            }
        }
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos) && this.canBlockStay(worldIn, pos);
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        if (!this.canBlockStay(worldIn, pos)) {
            worldIn.setBlockToAir(pos);
        }
    }
    
    private boolean canBlockStay(final World worldIn, final BlockPos p_176588_2_) {
        return worldIn.getBlockState(p_176588_2_.offsetDown()).getBlock().getMaterial().isSolid();
    }
    
    @Override
    public int quantityDropped(final Random random) {
        return 0;
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return null;
    }
    
    @Override
    public Item getItem(final World worldIn, final BlockPos pos) {
        return Items.cake;
    }
    
    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockCake.BITES, meta);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return (int)state.getValue(BlockCake.BITES);
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockCake.BITES });
    }
    
    @Override
    public int getComparatorInputOverride(final World worldIn, final BlockPos pos) {
        return (7 - (int)worldIn.getBlockState(pos).getValue(BlockCake.BITES)) * 2;
    }
    
    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }
    
    static {
        BITES = PropertyInteger.create("bites", 0, 6);
    }
}
