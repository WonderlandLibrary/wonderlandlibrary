/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockJukebox
extends BlockContainer {
    public static final PropertyBool HAS_RECORD = PropertyBool.create("has_record");

    protected BlockJukebox() {
        super(Material.wood, MapColor.dirtColor);
        this.setDefaultState(this.blockState.getBaseState().withProperty(HAS_RECORD, false));
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (state.getValue(HAS_RECORD) == false) return false;
        this.dropRecord(worldIn, pos, state);
        state = state.withProperty(HAS_RECORD, false);
        worldIn.setBlockState(pos, state, 2);
        return true;
    }

    public void insertRecord(World worldIn, BlockPos pos, IBlockState state, ItemStack recordStack) {
        if (worldIn.isRemote) return;
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (!(tileentity instanceof TileEntityJukebox)) return;
        ((TileEntityJukebox)tileentity).setRecord(new ItemStack(recordStack.getItem(), 1, recordStack.getMetadata()));
        worldIn.setBlockState(pos, state.withProperty(HAS_RECORD, true), 2);
    }

    private void dropRecord(World worldIn, BlockPos pos, IBlockState state) {
        if (worldIn.isRemote) return;
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (!(tileentity instanceof TileEntityJukebox)) return;
        TileEntityJukebox blockjukebox$tileentityjukebox = (TileEntityJukebox)tileentity;
        ItemStack itemstack = blockjukebox$tileentityjukebox.getRecord();
        if (itemstack == null) return;
        worldIn.playAuxSFX(1005, pos, 0);
        worldIn.playRecord(pos, null);
        blockjukebox$tileentityjukebox.setRecord(null);
        float f = 0.7f;
        double d0 = (double)(worldIn.rand.nextFloat() * f) + (double)(1.0f - f) * 0.5;
        double d1 = (double)(worldIn.rand.nextFloat() * f) + (double)(1.0f - f) * 0.2 + 0.6;
        double d2 = (double)(worldIn.rand.nextFloat() * f) + (double)(1.0f - f) * 0.5;
        ItemStack itemstack1 = itemstack.copy();
        EntityItem entityitem = new EntityItem(worldIn, (double)pos.getX() + d0, (double)pos.getY() + d1, (double)pos.getZ() + d2, itemstack1);
        entityitem.setDefaultPickupDelay();
        worldIn.spawnEntityInWorld(entityitem);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        this.dropRecord(worldIn, pos, state);
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
        if (worldIn.isRemote) return;
        super.dropBlockAsItemWithChance(worldIn, pos, state, chance, 0);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityJukebox();
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride(World worldIn, BlockPos pos) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (!(tileentity instanceof TileEntityJukebox)) return 0;
        ItemStack itemstack = ((TileEntityJukebox)tileentity).getRecord();
        if (itemstack == null) return 0;
        return Item.getIdFromItem(itemstack.getItem()) + 1 - Item.getIdFromItem(Items.record_13);
    }

    @Override
    public int getRenderType() {
        return 3;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        boolean bl;
        IBlockState iBlockState = this.getDefaultState();
        if (meta > 0) {
            bl = true;
            return iBlockState.withProperty(HAS_RECORD, bl);
        }
        bl = false;
        return iBlockState.withProperty(HAS_RECORD, bl);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        if (state.getValue(HAS_RECORD) == false) return 0;
        return 1;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, HAS_RECORD);
    }

    public static class TileEntityJukebox
    extends TileEntity {
        private ItemStack record;

        @Override
        public void readFromNBT(NBTTagCompound compound) {
            super.readFromNBT(compound);
            if (compound.hasKey("RecordItem", 10)) {
                this.setRecord(ItemStack.loadItemStackFromNBT(compound.getCompoundTag("RecordItem")));
                return;
            }
            if (compound.getInteger("Record") <= 0) return;
            this.setRecord(new ItemStack(Item.getItemById(compound.getInteger("Record")), 1, 0));
        }

        @Override
        public void writeToNBT(NBTTagCompound compound) {
            super.writeToNBT(compound);
            if (this.getRecord() == null) return;
            compound.setTag("RecordItem", this.getRecord().writeToNBT(new NBTTagCompound()));
        }

        public ItemStack getRecord() {
            return this.record;
        }

        public void setRecord(ItemStack recordStack) {
            this.record = recordStack;
            this.markDirty();
        }
    }
}

