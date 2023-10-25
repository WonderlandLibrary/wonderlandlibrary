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

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, HAS_RECORD);
    }

    protected BlockJukebox() {
        super(Material.wood, MapColor.dirtColor);
        this.setDefaultState(this.blockState.getBaseState().withProperty(HAS_RECORD, false));
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    @Override
    public void dropBlockAsItemWithChance(World world, BlockPos blockPos, IBlockState iBlockState, float f, int n) {
        if (!world.isRemote) {
            super.dropBlockAsItemWithChance(world, blockPos, iBlockState, f, 0);
        }
    }

    @Override
    public IBlockState getStateFromMeta(int n) {
        return this.getDefaultState().withProperty(HAS_RECORD, n > 0);
    }

    public void insertRecord(World world, BlockPos blockPos, IBlockState iBlockState, ItemStack itemStack) {
        TileEntity tileEntity;
        if (!world.isRemote && (tileEntity = world.getTileEntity(blockPos)) instanceof TileEntityJukebox) {
            ((TileEntityJukebox)tileEntity).setRecord(new ItemStack(itemStack.getItem(), 1, itemStack.getMetadata()));
            world.setBlockState(blockPos, iBlockState.withProperty(HAS_RECORD, true), 2);
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState iBlockState, EntityPlayer entityPlayer, EnumFacing enumFacing, float f, float f2, float f3) {
        if (iBlockState.getValue(HAS_RECORD).booleanValue()) {
            this.dropRecord(world, blockPos, iBlockState);
            iBlockState = iBlockState.withProperty(HAS_RECORD, false);
            world.setBlockState(blockPos, iBlockState, 2);
            return true;
        }
        return false;
    }

    @Override
    public int getComparatorInputOverride(World world, BlockPos blockPos) {
        ItemStack itemStack;
        TileEntity tileEntity = world.getTileEntity(blockPos);
        if (tileEntity instanceof TileEntityJukebox && (itemStack = ((TileEntityJukebox)tileEntity).getRecord()) != null) {
            return Item.getIdFromItem(itemStack.getItem()) + 1 - Item.getIdFromItem(Items.record_13);
        }
        return 0;
    }

    @Override
    public int getRenderType() {
        return 3;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int n) {
        return new TileEntityJukebox();
    }

    private void dropRecord(World world, BlockPos blockPos, IBlockState iBlockState) {
        TileEntityJukebox tileEntityJukebox;
        ItemStack itemStack;
        TileEntity tileEntity;
        if (!world.isRemote && (tileEntity = world.getTileEntity(blockPos)) instanceof TileEntityJukebox && (itemStack = (tileEntityJukebox = (TileEntityJukebox)tileEntity).getRecord()) != null) {
            world.playAuxSFX(1005, blockPos, 0);
            world.playRecord(blockPos, null);
            tileEntityJukebox.setRecord(null);
            float f = 0.7f;
            double d = (double)(world.rand.nextFloat() * f) + (double)(1.0f - f) * 0.5;
            double d2 = (double)(world.rand.nextFloat() * f) + (double)(1.0f - f) * 0.2 + 0.6;
            double d3 = (double)(world.rand.nextFloat() * f) + (double)(1.0f - f) * 0.5;
            ItemStack itemStack2 = itemStack.copy();
            EntityItem entityItem = new EntityItem(world, (double)blockPos.getX() + d, (double)blockPos.getY() + d2, (double)blockPos.getZ() + d3, itemStack2);
            entityItem.setDefaultPickupDelay();
            world.spawnEntityInWorld(entityItem);
        }
    }

    @Override
    public void breakBlock(World world, BlockPos blockPos, IBlockState iBlockState) {
        this.dropRecord(world, blockPos, iBlockState);
        super.breakBlock(world, blockPos, iBlockState);
    }

    @Override
    public int getMetaFromState(IBlockState iBlockState) {
        return iBlockState.getValue(HAS_RECORD) != false ? 1 : 0;
    }

    public static class TileEntityJukebox
    extends TileEntity {
        private ItemStack record;

        @Override
        public void writeToNBT(NBTTagCompound nBTTagCompound) {
            super.writeToNBT(nBTTagCompound);
            if (this.getRecord() != null) {
                nBTTagCompound.setTag("RecordItem", this.getRecord().writeToNBT(new NBTTagCompound()));
            }
        }

        public void setRecord(ItemStack itemStack) {
            this.record = itemStack;
            this.markDirty();
        }

        @Override
        public void readFromNBT(NBTTagCompound nBTTagCompound) {
            super.readFromNBT(nBTTagCompound);
            if (nBTTagCompound.hasKey("RecordItem", 10)) {
                this.setRecord(ItemStack.loadItemStackFromNBT(nBTTagCompound.getCompoundTag("RecordItem")));
            } else if (nBTTagCompound.getInteger("Record") > 0) {
                this.setRecord(new ItemStack(Item.getItemById(nBTTagCompound.getInteger("Record")), 1, 0));
            }
        }

        public ItemStack getRecord() {
            return this.record;
        }
    }
}

