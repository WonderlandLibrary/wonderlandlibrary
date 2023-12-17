// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.SoundEvent;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackData;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.block.properties.PropertyBool;

public class BlockJukebox extends BlockContainer
{
    public static final PropertyBool HAS_RECORD;
    
    public static void registerFixesJukebox(final DataFixer fixer) {
        fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackData(TileEntityJukebox.class, new String[] { "RecordItem" }));
    }
    
    protected BlockJukebox() {
        super(Material.WOOD, MapColor.DIRT);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockJukebox.HAS_RECORD, false));
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, IBlockState state, final EntityPlayer playerIn, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
        if (state.getValue((IProperty<Boolean>)BlockJukebox.HAS_RECORD)) {
            this.dropRecord(worldIn, pos, state);
            state = state.withProperty((IProperty<Comparable>)BlockJukebox.HAS_RECORD, false);
            worldIn.setBlockState(pos, state, 2);
            return true;
        }
        return false;
    }
    
    public void insertRecord(final World worldIn, final BlockPos pos, final IBlockState state, final ItemStack recordStack) {
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityJukebox) {
            ((TileEntityJukebox)tileentity).setRecord(recordStack.copy());
            worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockJukebox.HAS_RECORD, true), 2);
        }
    }
    
    private void dropRecord(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (!worldIn.isRemote) {
            final TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof TileEntityJukebox) {
                final TileEntityJukebox blockjukebox$tileentityjukebox = (TileEntityJukebox)tileentity;
                final ItemStack itemstack = blockjukebox$tileentityjukebox.getRecord();
                if (!itemstack.isEmpty()) {
                    worldIn.playEvent(1010, pos, 0);
                    worldIn.playRecord(pos, null);
                    blockjukebox$tileentityjukebox.setRecord(ItemStack.EMPTY);
                    final float f = 0.7f;
                    final double d0 = worldIn.rand.nextFloat() * 0.7f + 0.15000000596046448;
                    final double d2 = worldIn.rand.nextFloat() * 0.7f + 0.06000000238418579 + 0.6;
                    final double d3 = worldIn.rand.nextFloat() * 0.7f + 0.15000000596046448;
                    final ItemStack itemstack2 = itemstack.copy();
                    final EntityItem entityitem = new EntityItem(worldIn, pos.getX() + d0, pos.getY() + d2, pos.getZ() + d3, itemstack2);
                    entityitem.setDefaultPickupDelay();
                    worldIn.spawnEntity(entityitem);
                }
            }
        }
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        this.dropRecord(worldIn, pos, state);
        super.breakBlock(worldIn, pos, state);
    }
    
    @Override
    public void dropBlockAsItemWithChance(final World worldIn, final BlockPos pos, final IBlockState state, final float chance, final int fortune) {
        if (!worldIn.isRemote) {
            super.dropBlockAsItemWithChance(worldIn, pos, state, chance, 0);
        }
    }
    
    @Override
    public TileEntity createNewTileEntity(final World worldIn, final int meta) {
        return new TileEntityJukebox();
    }
    
    @Override
    @Deprecated
    public boolean hasComparatorInputOverride(final IBlockState state) {
        return true;
    }
    
    @Override
    @Deprecated
    public int getComparatorInputOverride(final IBlockState blockState, final World worldIn, final BlockPos pos) {
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityJukebox) {
            final ItemStack itemstack = ((TileEntityJukebox)tileentity).getRecord();
            if (!itemstack.isEmpty()) {
                return Item.getIdFromItem(itemstack.getItem()) + 1 - Item.getIdFromItem(Items.RECORD_13);
            }
        }
        return 0;
    }
    
    @Override
    @Deprecated
    public EnumBlockRenderType getRenderType(final IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockJukebox.HAS_RECORD, meta > 0);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return ((boolean)state.getValue((IProperty<Boolean>)BlockJukebox.HAS_RECORD)) ? 1 : 0;
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockJukebox.HAS_RECORD });
    }
    
    static {
        HAS_RECORD = PropertyBool.create("has_record");
    }
    
    public static class TileEntityJukebox extends TileEntity
    {
        private ItemStack record;
        
        public TileEntityJukebox() {
            this.record = ItemStack.EMPTY;
        }
        
        @Override
        public void readFromNBT(final NBTTagCompound compound) {
            super.readFromNBT(compound);
            if (compound.hasKey("RecordItem", 10)) {
                this.setRecord(new ItemStack(compound.getCompoundTag("RecordItem")));
            }
            else if (compound.getInteger("Record") > 0) {
                this.setRecord(new ItemStack(Item.getItemById(compound.getInteger("Record"))));
            }
        }
        
        @Override
        public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
            super.writeToNBT(compound);
            if (!this.getRecord().isEmpty()) {
                compound.setTag("RecordItem", this.getRecord().writeToNBT(new NBTTagCompound()));
            }
            return compound;
        }
        
        public ItemStack getRecord() {
            return this.record;
        }
        
        public void setRecord(final ItemStack recordStack) {
            this.record = recordStack;
            this.markDirty();
        }
    }
}
