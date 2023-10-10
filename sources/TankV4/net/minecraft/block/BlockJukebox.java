package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
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

public class BlockJukebox extends BlockContainer {
   public static final PropertyBool HAS_RECORD = PropertyBool.create("has_record");

   public void dropBlockAsItemWithChance(World var1, BlockPos var2, IBlockState var3, float var4, int var5) {
      if (!var1.isRemote) {
         super.dropBlockAsItemWithChance(var1, var2, var3, var4, 0);
      }

   }

   public boolean hasComparatorInputOverride() {
      return true;
   }

   public boolean onBlockActivated(World var1, BlockPos var2, IBlockState var3, EntityPlayer var4, EnumFacing var5, float var6, float var7, float var8) {
      if ((Boolean)var3.getValue(HAS_RECORD)) {
         this.dropRecord(var1, var2, var3);
         var3 = var3.withProperty(HAS_RECORD, false);
         var1.setBlockState(var2, var3, 2);
         return true;
      } else {
         return false;
      }
   }

   public void insertRecord(World var1, BlockPos var2, IBlockState var3, ItemStack var4) {
      if (!var1.isRemote) {
         TileEntity var5 = var1.getTileEntity(var2);
         if (var5 instanceof BlockJukebox.TileEntityJukebox) {
            ((BlockJukebox.TileEntityJukebox)var5).setRecord(new ItemStack(var4.getItem(), 1, var4.getMetadata()));
            var1.setBlockState(var2, var3.withProperty(HAS_RECORD, true), 2);
         }
      }

   }

   public int getMetaFromState(IBlockState var1) {
      return (Boolean)var1.getValue(HAS_RECORD) ? 1 : 0;
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{HAS_RECORD});
   }

   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(HAS_RECORD, var1 > 0);
   }

   public TileEntity createNewTileEntity(World var1, int var2) {
      return new BlockJukebox.TileEntityJukebox();
   }

   public int getComparatorInputOverride(World var1, BlockPos var2) {
      TileEntity var3 = var1.getTileEntity(var2);
      if (var3 instanceof BlockJukebox.TileEntityJukebox) {
         ItemStack var4 = ((BlockJukebox.TileEntityJukebox)var3).getRecord();
         if (var4 != null) {
            return Item.getIdFromItem(var4.getItem()) + 1 - Item.getIdFromItem(Items.record_13);
         }
      }

      return 0;
   }

   protected BlockJukebox() {
      super(Material.wood, MapColor.dirtColor);
      this.setDefaultState(this.blockState.getBaseState().withProperty(HAS_RECORD, false));
      this.setCreativeTab(CreativeTabs.tabDecorations);
   }

   public void breakBlock(World var1, BlockPos var2, IBlockState var3) {
      this.dropRecord(var1, var2, var3);
      super.breakBlock(var1, var2, var3);
   }

   public int getRenderType() {
      return 3;
   }

   private void dropRecord(World var1, BlockPos var2, IBlockState var3) {
      if (!var1.isRemote) {
         TileEntity var4 = var1.getTileEntity(var2);
         if (var4 instanceof BlockJukebox.TileEntityJukebox) {
            BlockJukebox.TileEntityJukebox var5 = (BlockJukebox.TileEntityJukebox)var4;
            ItemStack var6 = var5.getRecord();
            if (var6 != null) {
               var1.playAuxSFX(1005, var2, 0);
               var1.playRecord(var2, (String)null);
               var5.setRecord((ItemStack)null);
               float var7 = 0.7F;
               double var8 = (double)(var1.rand.nextFloat() * var7) + (double)(1.0F - var7) * 0.5D;
               double var10 = (double)(var1.rand.nextFloat() * var7) + (double)(1.0F - var7) * 0.2D + 0.6D;
               double var12 = (double)(var1.rand.nextFloat() * var7) + (double)(1.0F - var7) * 0.5D;
               ItemStack var14 = var6.copy();
               EntityItem var15 = new EntityItem(var1, (double)var2.getX() + var8, (double)var2.getY() + var10, (double)var2.getZ() + var12, var14);
               var15.setDefaultPickupDelay();
               var1.spawnEntityInWorld(var15);
            }
         }
      }

   }

   public static class TileEntityJukebox extends TileEntity {
      private ItemStack record;

      public void readFromNBT(NBTTagCompound var1) {
         super.readFromNBT(var1);
         if (var1.hasKey("RecordItem", 10)) {
            this.setRecord(ItemStack.loadItemStackFromNBT(var1.getCompoundTag("RecordItem")));
         } else if (var1.getInteger("Record") > 0) {
            this.setRecord(new ItemStack(Item.getItemById(var1.getInteger("Record")), 1, 0));
         }

      }

      public ItemStack getRecord() {
         return this.record;
      }

      public void writeToNBT(NBTTagCompound var1) {
         super.writeToNBT(var1);
         if (this.getRecord() != null) {
            var1.setTag("RecordItem", this.getRecord().writeToNBT(new NBTTagCompound()));
         }

      }

      public void setRecord(ItemStack var1) {
         this.record = var1;
         this.markDirty();
      }
   }
}
