package net.minecraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockJukebox;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockJukebox
extends BlockContainer {
    public static final PropertyBool HAS_RECORD = PropertyBool.create((String)"has_record");

    protected BlockJukebox() {
        super(Material.wood, MapColor.dirtColor);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty)HAS_RECORD, (Comparable)Boolean.valueOf((boolean)false)));
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (((Boolean)state.getValue((IProperty)HAS_RECORD)).booleanValue()) {
            this.dropRecord(worldIn, pos, state);
            state = state.withProperty((IProperty)HAS_RECORD, (Comparable)Boolean.valueOf((boolean)false));
            worldIn.setBlockState(pos, state, 2);
            return true;
        }
        return false;
    }

    public void insertRecord(World worldIn, BlockPos pos, IBlockState state, ItemStack recordStack) {
        TileEntity tileentity;
        if (!worldIn.isRemote && (tileentity = worldIn.getTileEntity(pos)) instanceof TileEntityJukebox) {
            ((TileEntityJukebox)tileentity).setRecord(new ItemStack(recordStack.getItem(), 1, recordStack.getMetadata()));
            worldIn.setBlockState(pos, state.withProperty((IProperty)HAS_RECORD, (Comparable)Boolean.valueOf((boolean)true)), 2);
        }
    }

    private void dropRecord(World worldIn, BlockPos pos, IBlockState state) {
        TileEntityJukebox blockjukebox$tileentityjukebox;
        ItemStack itemstack;
        TileEntity tileentity;
        if (!worldIn.isRemote && (tileentity = worldIn.getTileEntity(pos)) instanceof TileEntityJukebox && (itemstack = (blockjukebox$tileentityjukebox = (TileEntityJukebox)tileentity).getRecord()) != null) {
            worldIn.playAuxSFX(1005, pos, 0);
            worldIn.playRecord(pos, (String)null);
            blockjukebox$tileentityjukebox.setRecord((ItemStack)null);
            float f = 0.7f;
            double d0 = (double)(worldIn.rand.nextFloat() * f) + (double)(1.0f - f) * 0.5;
            double d1 = (double)(worldIn.rand.nextFloat() * f) + (double)(1.0f - f) * 0.2 + 0.6;
            double d2 = (double)(worldIn.rand.nextFloat() * f) + (double)(1.0f - f) * 0.5;
            ItemStack itemstack1 = itemstack.copy();
            EntityItem entityitem = new EntityItem(worldIn, (double)pos.getX() + d0, (double)pos.getY() + d1, (double)pos.getZ() + d2, itemstack1);
            entityitem.setDefaultPickupDelay();
            worldIn.spawnEntityInWorld((Entity)entityitem);
        }
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        this.dropRecord(worldIn, pos, state);
        super.breakBlock(worldIn, pos, state);
    }

    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
        if (!worldIn.isRemote) {
            super.dropBlockAsItemWithChance(worldIn, pos, state, chance, 0);
        }
    }

    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityJukebox();
    }

    public boolean hasComparatorInputOverride() {
        return true;
    }

    public int getComparatorInputOverride(World worldIn, BlockPos pos) {
        ItemStack itemstack;
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityJukebox && (itemstack = ((TileEntityJukebox)tileentity).getRecord()) != null) {
            return Item.getIdFromItem((Item)itemstack.getItem()) + 1 - Item.getIdFromItem((Item)Items.record_13);
        }
        return 0;
    }

    public int getRenderType() {
        return 3;
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty((IProperty)HAS_RECORD, (Comparable)Boolean.valueOf((meta > 0 ? 1 : 0) != 0));
    }

    public int getMetaFromState(IBlockState state) {
        return (Boolean)state.getValue((IProperty)HAS_RECORD) != false ? 1 : 0;
    }

    protected BlockState createBlockState() {
        return new BlockState((Block)this, new IProperty[]{HAS_RECORD});
    }
}
