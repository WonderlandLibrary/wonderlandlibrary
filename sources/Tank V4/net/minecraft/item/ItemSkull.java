package net.minecraft.item;

import com.mojang.authlib.GameProfile;
import java.util.List;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemSkull extends Item {
   private static final String[] skullTypes = new String[]{"skeleton", "wither", "zombie", "char", "creeper"};

   public int getMetadata(int var1) {
      return var1;
   }

   public String getUnlocalizedName(ItemStack var1) {
      int var2 = var1.getMetadata();
      if (var2 < 0 || var2 >= skullTypes.length) {
         var2 = 0;
      }

      return super.getUnlocalizedName() + "." + skullTypes[var2];
   }

   public ItemSkull() {
      this.setCreativeTab(CreativeTabs.tabDecorations);
      this.setMaxDamage(0);
      this.setHasSubtypes(true);
   }

   public String getItemStackDisplayName(ItemStack var1) {
      if (var1.getMetadata() == 3 && var1.hasTagCompound()) {
         if (var1.getTagCompound().hasKey("SkullOwner", 8)) {
            return StatCollector.translateToLocalFormatted("item.skull.player.name", var1.getTagCompound().getString("SkullOwner"));
         }

         if (var1.getTagCompound().hasKey("SkullOwner", 10)) {
            NBTTagCompound var2 = var1.getTagCompound().getCompoundTag("SkullOwner");
            if (var2.hasKey("Name", 8)) {
               return StatCollector.translateToLocalFormatted("item.skull.player.name", var2.getString("Name"));
            }
         }
      }

      return super.getItemStackDisplayName(var1);
   }

   public boolean updateItemStackNBT(NBTTagCompound var1) {
      super.updateItemStackNBT(var1);
      if (var1.hasKey("SkullOwner", 8) && var1.getString("SkullOwner").length() > 0) {
         GameProfile var2 = new GameProfile((UUID)null, var1.getString("SkullOwner"));
         var2 = TileEntitySkull.updateGameprofile(var2);
         var1.setTag("SkullOwner", NBTUtil.writeGameProfile(new NBTTagCompound(), var2));
         return true;
      } else {
         return false;
      }
   }

   public boolean onItemUse(ItemStack var1, EntityPlayer var2, World var3, BlockPos var4, EnumFacing var5, float var6, float var7, float var8) {
      if (var5 == EnumFacing.DOWN) {
         return false;
      } else {
         IBlockState var9 = var3.getBlockState(var4);
         Block var10 = var9.getBlock();
         boolean var11 = var10.isReplaceable(var3, var4);
         if (!var11) {
            if (!var3.getBlockState(var4).getBlock().getMaterial().isSolid()) {
               return false;
            }

            var4 = var4.offset(var5);
         }

         if (!var2.canPlayerEdit(var4, var5, var1)) {
            return false;
         } else if (!Blocks.skull.canPlaceBlockAt(var3, var4)) {
            return false;
         } else {
            if (!var3.isRemote) {
               var3.setBlockState(var4, Blocks.skull.getDefaultState().withProperty(BlockSkull.FACING, var5), 3);
               int var12 = 0;
               if (var5 == EnumFacing.UP) {
                  var12 = MathHelper.floor_double((double)(var2.rotationYaw * 16.0F / 360.0F) + 0.5D) & 15;
               }

               TileEntity var13 = var3.getTileEntity(var4);
               if (var13 instanceof TileEntitySkull) {
                  TileEntitySkull var14 = (TileEntitySkull)var13;
                  if (var1.getMetadata() == 3) {
                     GameProfile var15 = null;
                     if (var1.hasTagCompound()) {
                        NBTTagCompound var16 = var1.getTagCompound();
                        if (var16.hasKey("SkullOwner", 10)) {
                           var15 = NBTUtil.readGameProfileFromNBT(var16.getCompoundTag("SkullOwner"));
                        } else if (var16.hasKey("SkullOwner", 8) && var16.getString("SkullOwner").length() > 0) {
                           var15 = new GameProfile((UUID)null, var16.getString("SkullOwner"));
                        }
                     }

                     var14.setPlayerProfile(var15);
                  } else {
                     var14.setType(var1.getMetadata());
                  }

                  var14.setSkullRotation(var12);
                  Blocks.skull.checkWitherSpawn(var3, var4, var14);
               }

               --var1.stackSize;
            }

            return true;
         }
      }
   }

   public void getSubItems(Item var1, CreativeTabs var2, List var3) {
      for(int var4 = 0; var4 < skullTypes.length; ++var4) {
         var3.add(new ItemStack(var1, 1, var4));
      }

   }
}
