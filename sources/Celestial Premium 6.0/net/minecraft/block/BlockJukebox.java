/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockJukebox
extends BlockContainer {
    public static final PropertyBool HAS_RECORD = PropertyBool.create("has_record");

    public static void registerFixesJukebox(DataFixer fixer) {
        fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackData(TileEntityJukebox.class, "RecordItem"));
    }

    protected BlockJukebox() {
        super(Material.WOOD, MapColor.DIRT);
        this.setDefaultState(this.blockState.getBaseState().withProperty(HAS_RECORD, false));
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing heldItem, float side, float hitX, float hitY) {
        if (state.getValue(HAS_RECORD).booleanValue()) {
            this.dropRecord(worldIn, pos, state);
            state = state.withProperty(HAS_RECORD, false);
            worldIn.setBlockState(pos, state, 2);
            return true;
        }
        return false;
    }

    public void insertRecord(World worldIn, BlockPos pos, IBlockState state, ItemStack recordStack) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityJukebox) {
            ((TileEntityJukebox)tileentity).setRecord(recordStack.copy());
            worldIn.setBlockState(pos, state.withProperty(HAS_RECORD, true), 2);
        }
    }

    private void dropRecord(World worldIn, BlockPos pos, IBlockState state) {
        TileEntityJukebox blockjukebox$tileentityjukebox;
        ItemStack itemstack;
        TileEntity tileentity;
        if (!worldIn.isRemote && (tileentity = worldIn.getTileEntity(pos)) instanceof TileEntityJukebox && !(itemstack = (blockjukebox$tileentityjukebox = (TileEntityJukebox)tileentity).getRecord()).isEmpty()) {
            worldIn.playEvent(1010, pos, 0);
            worldIn.playRecord(pos, null);
            blockjukebox$tileentityjukebox.setRecord(ItemStack.field_190927_a);
            float f = 0.7f;
            double d0 = (double)(worldIn.rand.nextFloat() * 0.7f) + (double)0.15f;
            double d1 = (double)(worldIn.rand.nextFloat() * 0.7f) + 0.06000000238418579 + 0.6;
            double d2 = (double)(worldIn.rand.nextFloat() * 0.7f) + (double)0.15f;
            ItemStack itemstack1 = itemstack.copy();
            EntityItem entityitem = new EntityItem(worldIn, (double)pos.getX() + d0, (double)pos.getY() + d1, (double)pos.getZ() + d2, itemstack1);
            entityitem.setDefaultPickupDelay();
            worldIn.spawnEntityInWorld(entityitem);
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
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
        ItemStack itemstack;
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityJukebox && !(itemstack = ((TileEntityJukebox)tileentity).getRecord()).isEmpty()) {
            return Item.getIdFromItem(itemstack.getItem()) + 1 - Item.getIdFromItem(Items.RECORD_13);
        }
        return 0;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(HAS_RECORD, meta > 0);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(HAS_RECORD) != false ? 1 : 0;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer((Block)this, HAS_RECORD);
    }

    public static class TileEntityJukebox
    extends TileEntity {
        private ItemStack record = ItemStack.field_190927_a;

        @Override
        public void readFromNBT(NBTTagCompound compound) {
            super.readFromNBT(compound);
            if (compound.hasKey("RecordItem", 10)) {
                this.setRecord(new ItemStack(compound.getCompoundTag("RecordItem")));
            } else if (compound.getInteger("Record") > 0) {
                this.setRecord(new ItemStack(Item.getItemById(compound.getInteger("Record"))));
            }
        }

        @Override
        public NBTTagCompound writeToNBT(NBTTagCompound compound) {
            super.writeToNBT(compound);
            if (!this.getRecord().isEmpty()) {
                compound.setTag("RecordItem", this.getRecord().writeToNBT(new NBTTagCompound()));
            }
            return compound;
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

