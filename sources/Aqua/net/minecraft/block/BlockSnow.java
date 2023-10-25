package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSnow
extends Block {
    public static final PropertyInteger LAYERS = PropertyInteger.create((String)"layers", (int)1, (int)8);

    protected BlockSnow() {
        super(Material.snow);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty)LAYERS, (Comparable)Integer.valueOf((int)1)));
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.125f, 1.0f);
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabDecorations);
        this.setBlockBoundsForItemRender();
    }

    public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        return (Integer)worldIn.getBlockState(pos).getValue((IProperty)LAYERS) < 5;
    }

    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        int i = (Integer)state.getValue((IProperty)LAYERS) - 1;
        float f = 0.125f;
        return new AxisAlignedBB((double)pos.getX() + this.minX, (double)pos.getY() + this.minY, (double)pos.getZ() + this.minZ, (double)pos.getX() + this.maxX, (double)((float)pos.getY() + (float)i * f), (double)pos.getZ() + this.maxZ);
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean isFullCube() {
        return false;
    }

    public void setBlockBoundsForItemRender() {
        this.getBoundsForLayers(0);
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        this.getBoundsForLayers((Integer)iblockstate.getValue((IProperty)LAYERS));
    }

    protected void getBoundsForLayers(int p_150154_1_) {
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, (float)p_150154_1_ / 8.0f, 1.0f);
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        IBlockState iblockstate = worldIn.getBlockState(pos.down());
        Block block = iblockstate.getBlock();
        return block != Blocks.ice && block != Blocks.packed_ice ? (block.getMaterial() == Material.leaves ? true : (block == this && (Integer)iblockstate.getValue((IProperty)LAYERS) >= 7 ? true : block.isOpaqueCube() && block.blockMaterial.blocksMovement())) : false;
    }

    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        this.checkAndDropBlock(worldIn, pos, state);
    }

    private boolean checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (!this.canPlaceBlockAt(worldIn, pos)) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
            return false;
        }
        return true;
    }

    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te) {
        BlockSnow.spawnAsEntity((World)worldIn, (BlockPos)pos, (ItemStack)new ItemStack(Items.snowball, (Integer)state.getValue((IProperty)LAYERS) + 1, 0));
        worldIn.setBlockToAir(pos);
        player.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock((Block)this)]);
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.snowball;
    }

    public int quantityDropped(Random random) {
        return 0;
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (worldIn.getLightFor(EnumSkyBlock.BLOCK, pos) > 11) {
            this.dropBlockAsItem(worldIn, pos, worldIn.getBlockState(pos), 0);
            worldIn.setBlockToAir(pos);
        }
    }

    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return side == EnumFacing.UP ? true : super.shouldSideBeRendered(worldIn, pos, side);
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty((IProperty)LAYERS, (Comparable)Integer.valueOf((int)((meta & 7) + 1)));
    }

    public boolean isReplaceable(World worldIn, BlockPos pos) {
        return (Integer)worldIn.getBlockState(pos).getValue((IProperty)LAYERS) == 1;
    }

    public int getMetaFromState(IBlockState state) {
        return (Integer)state.getValue((IProperty)LAYERS) - 1;
    }

    protected BlockState createBlockState() {
        return new BlockState((Block)this, new IProperty[]{LAYERS});
    }
}
