// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.stats.StatList;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;

public class BlockSnow extends Block
{
    public static final PropertyInteger LAYERS_PROP;
    private static final String __OBFID = "CL_00000309";
    
    protected BlockSnow() {
        super(Material.snow);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockSnow.LAYERS_PROP, 1));
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.125f, 1.0f);
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabDecorations);
        this.setBlockBoundsForItemRender();
    }
    
    @Override
    public boolean isPassable(final IBlockAccess blockAccess, final BlockPos pos) {
        return (int)blockAccess.getBlockState(pos).getValue(BlockSnow.LAYERS_PROP) < 5;
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final World worldIn, final BlockPos pos, final IBlockState state) {
        final int var4 = (int)state.getValue(BlockSnow.LAYERS_PROP) - 1;
        final float var5 = 0.125f;
        return new AxisAlignedBB(pos.getX() + this.minX, pos.getY() + this.minY, pos.getZ() + this.minZ, pos.getX() + this.maxX, pos.getY() + var4 * var5, pos.getZ() + this.maxZ);
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
    public void setBlockBoundsForItemRender() {
        this.getBoundsForLayers(0);
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess access, final BlockPos pos) {
        final IBlockState var3 = access.getBlockState(pos);
        this.getBoundsForLayers((int)var3.getValue(BlockSnow.LAYERS_PROP));
    }
    
    protected void getBoundsForLayers(final int p_150154_1_) {
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, p_150154_1_ / 8.0f, 1.0f);
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        final IBlockState var3 = worldIn.getBlockState(pos.offsetDown());
        final Block var4 = var3.getBlock();
        return var4 != Blocks.ice && var4 != Blocks.packed_ice && (var4.getMaterial() == Material.leaves || (var4 == this && (int)var3.getValue(BlockSnow.LAYERS_PROP) == 7) || (var4.isOpaqueCube() && var4.blockMaterial.blocksMovement()));
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        this.checkAndDropBlock(worldIn, pos, state);
    }
    
    private boolean checkAndDropBlock(final World worldIn, final BlockPos p_176314_2_, final IBlockState p_176314_3_) {
        if (!this.canPlaceBlockAt(worldIn, p_176314_2_)) {
            this.dropBlockAsItem(worldIn, p_176314_2_, p_176314_3_, 0);
            worldIn.setBlockToAir(p_176314_2_);
            return false;
        }
        return true;
    }
    
    @Override
    public void harvestBlock(final World worldIn, final EntityPlayer playerIn, final BlockPos pos, final IBlockState state, final TileEntity te) {
        Block.spawnAsEntity(worldIn, pos, new ItemStack(Items.snowball, (int)state.getValue(BlockSnow.LAYERS_PROP) + 1, 0));
        worldIn.setBlockToAir(pos);
        playerIn.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock(this)]);
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Items.snowball;
    }
    
    @Override
    public int quantityDropped(final Random random) {
        return 0;
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (worldIn.getLightFor(EnumSkyBlock.BLOCK, pos) > 11) {
            this.dropBlockAsItem(worldIn, pos, worldIn.getBlockState(pos), 0);
            worldIn.setBlockToAir(pos);
        }
    }
    
    @Override
    public boolean shouldSideBeRendered(final IBlockAccess worldIn, final BlockPos pos, final EnumFacing side) {
        return side == EnumFacing.UP || super.shouldSideBeRendered(worldIn, pos, side);
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockSnow.LAYERS_PROP, (meta & 0x7) + 1);
    }
    
    @Override
    public boolean isReplaceable(final World worldIn, final BlockPos pos) {
        return (int)worldIn.getBlockState(pos).getValue(BlockSnow.LAYERS_PROP) == 1;
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return (int)state.getValue(BlockSnow.LAYERS_PROP) - 1;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockSnow.LAYERS_PROP });
    }
    
    static {
        LAYERS_PROP = PropertyInteger.create("layers", 1, 8);
    }
}
