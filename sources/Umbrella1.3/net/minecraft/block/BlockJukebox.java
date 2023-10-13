/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import net.minecraft.block.BlockContainer;
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
    private static final String __OBFID = "CL_00000260";

    protected BlockJukebox() {
        super(Material.wood);
        this.setDefaultState(this.blockState.getBaseState().withProperty(HAS_RECORD, Boolean.valueOf(false)));
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (((Boolean)state.getValue(HAS_RECORD)).booleanValue()) {
            this.dropRecord(worldIn, pos, state);
            state = state.withProperty(HAS_RECORD, Boolean.valueOf(false));
            worldIn.setBlockState(pos, state, 2);
            return true;
        }
        return false;
    }

    public void insertRecord(World worldIn, BlockPos pos, IBlockState state, ItemStack recordStack) {
        TileEntity var5;
        if (!worldIn.isRemote && (var5 = worldIn.getTileEntity(pos)) instanceof TileEntityJukebox) {
            ((TileEntityJukebox)var5).setRecord(new ItemStack(recordStack.getItem(), 1, recordStack.getMetadata()));
            worldIn.setBlockState(pos, state.withProperty(HAS_RECORD, Boolean.valueOf(true)), 2);
        }
    }

    private void dropRecord(World worldIn, BlockPos pos, IBlockState state) {
        TileEntityJukebox var5;
        ItemStack var6;
        TileEntity var4;
        if (!worldIn.isRemote && (var4 = worldIn.getTileEntity(pos)) instanceof TileEntityJukebox && (var6 = (var5 = (TileEntityJukebox)var4).getRecord()) != null) {
            worldIn.playAuxSFX(1005, pos, 0);
            worldIn.func_175717_a(pos, null);
            var5.setRecord(null);
            float var7 = 0.7f;
            double var8 = (double)(worldIn.rand.nextFloat() * var7) + (double)(1.0f - var7) * 0.5;
            double var10 = (double)(worldIn.rand.nextFloat() * var7) + (double)(1.0f - var7) * 0.2 + 0.6;
            double var12 = (double)(worldIn.rand.nextFloat() * var7) + (double)(1.0f - var7) * 0.5;
            ItemStack var14 = var6.copy();
            EntityItem var15 = new EntityItem(worldIn, (double)pos.getX() + var8, (double)pos.getY() + var10, (double)pos.getZ() + var12, var14);
            var15.setDefaultPickupDelay();
            worldIn.spawnEntityInWorld(var15);
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        this.dropRecord(worldIn, pos, state);
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
        if (!worldIn.isRemote) {
            super.dropBlockAsItemWithChance(worldIn, pos, state, chance, 0);
        }
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
        ItemStack var4;
        TileEntity var3 = worldIn.getTileEntity(pos);
        if (var3 instanceof TileEntityJukebox && (var4 = ((TileEntityJukebox)var3).getRecord()) != null) {
            return Item.getIdFromItem(var4.getItem()) + 1 - Item.getIdFromItem(Items.record_13);
        }
        return 0;
    }

    @Override
    public int getRenderType() {
        return 3;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(HAS_RECORD, Boolean.valueOf(meta > 0));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return (Boolean)state.getValue(HAS_RECORD) != false ? 1 : 0;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, HAS_RECORD);
    }

    public static class TileEntityJukebox
    extends TileEntity {
        private ItemStack record;
        private static final String __OBFID = "CL_00000261";

        @Override
        public void readFromNBT(NBTTagCompound compound) {
            super.readFromNBT(compound);
            if (compound.hasKey("RecordItem", 10)) {
                this.setRecord(ItemStack.loadItemStackFromNBT(compound.getCompoundTag("RecordItem")));
            } else if (compound.getInteger("Record") > 0) {
                this.setRecord(new ItemStack(Item.getItemById(compound.getInteger("Record")), 1, 0));
            }
        }

        @Override
        public void writeToNBT(NBTTagCompound compound) {
            super.writeToNBT(compound);
            if (this.getRecord() != null) {
                compound.setTag("RecordItem", this.getRecord().writeToNBT(new NBTTagCompound()));
            }
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

