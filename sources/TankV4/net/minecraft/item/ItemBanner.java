package net.minecraft.item;

import java.util.List;
import net.minecraft.block.BlockStandingSign;
import net.minecraft.block.BlockWallSign;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemBanner extends ItemBlock {
   private EnumDyeColor getBaseColor(ItemStack var1) {
      NBTTagCompound var2 = var1.getSubCompound("BlockEntityTag", false);
      EnumDyeColor var3 = null;
      if (var2 != null && var2.hasKey("Base")) {
         var3 = EnumDyeColor.byDyeDamage(var2.getInteger("Base"));
      } else {
         var3 = EnumDyeColor.byDyeDamage(var1.getMetadata());
      }

      return var3;
   }

   public void getSubItems(Item var1, CreativeTabs var2, List var3) {
      EnumDyeColor[] var7;
      int var6 = (var7 = EnumDyeColor.values()).length;

      for(int var5 = 0; var5 < var6; ++var5) {
         EnumDyeColor var4 = var7[var5];
         NBTTagCompound var8 = new NBTTagCompound();
         TileEntityBanner.func_181020_a(var8, var4.getDyeDamage(), (NBTTagList)null);
         NBTTagCompound var9 = new NBTTagCompound();
         var9.setTag("BlockEntityTag", var8);
         ItemStack var10 = new ItemStack(var1, 1, var4.getDyeDamage());
         var10.setTagCompound(var9);
         var3.add(var10);
      }

   }

   public int getColorFromItemStack(ItemStack var1, int var2) {
      if (var2 == 0) {
         return 16777215;
      } else {
         EnumDyeColor var3 = this.getBaseColor(var1);
         return var3.getMapColor().colorValue;
      }
   }

   public CreativeTabs getCreativeTab() {
      return CreativeTabs.tabDecorations;
   }

   public void addInformation(ItemStack var1, EntityPlayer var2, List var3, boolean var4) {
      NBTTagCompound var5 = var1.getSubCompound("BlockEntityTag", false);
      if (var5 != null && var5.hasKey("Patterns")) {
         NBTTagList var6 = var5.getTagList("Patterns", 10);

         for(int var7 = 0; var7 < var6.tagCount() && var7 < 6; ++var7) {
            NBTTagCompound var8 = var6.getCompoundTagAt(var7);
            EnumDyeColor var9 = EnumDyeColor.byDyeDamage(var8.getInteger("Color"));
            TileEntityBanner.EnumBannerPattern var10 = TileEntityBanner.EnumBannerPattern.getPatternByID(var8.getString("Pattern"));
            if (var10 != null) {
               var3.add(StatCollector.translateToLocal("item.banner." + var10.getPatternName() + "." + var9.getUnlocalizedName()));
            }
         }
      }

   }

   public ItemBanner() {
      super(Blocks.standing_banner);
      this.maxStackSize = 16;
      this.setCreativeTab(CreativeTabs.tabDecorations);
      this.setHasSubtypes(true);
      this.setMaxDamage(0);
   }

   public String getItemStackDisplayName(ItemStack var1) {
      String var2 = "item.banner.";
      EnumDyeColor var3 = this.getBaseColor(var1);
      var2 = var2 + var3.getUnlocalizedName() + ".name";
      return StatCollector.translateToLocal(var2);
   }

   public boolean onItemUse(ItemStack var1, EntityPlayer var2, World var3, BlockPos var4, EnumFacing var5, float var6, float var7, float var8) {
      if (var5 == EnumFacing.DOWN) {
         return false;
      } else if (!var3.getBlockState(var4).getBlock().getMaterial().isSolid()) {
         return false;
      } else {
         var4 = var4.offset(var5);
         if (!var2.canPlayerEdit(var4, var5, var1)) {
            return false;
         } else if (!Blocks.standing_banner.canPlaceBlockAt(var3, var4)) {
            return false;
         } else if (var3.isRemote) {
            return true;
         } else {
            if (var5 == EnumFacing.UP) {
               int var9 = MathHelper.floor_double((double)((var2.rotationYaw + 180.0F) * 16.0F / 360.0F) + 0.5D) & 15;
               var3.setBlockState(var4, Blocks.standing_banner.getDefaultState().withProperty(BlockStandingSign.ROTATION, var9), 3);
            } else {
               var3.setBlockState(var4, Blocks.wall_banner.getDefaultState().withProperty(BlockWallSign.FACING, var5), 3);
            }

            --var1.stackSize;
            TileEntity var10 = var3.getTileEntity(var4);
            if (var10 instanceof TileEntityBanner) {
               ((TileEntityBanner)var10).setItemValues(var1);
            }

            return true;
         }
      }
   }
}
