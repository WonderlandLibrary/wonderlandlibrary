package net.minecraft.item;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemFirework extends Item {
   public void addInformation(ItemStack var1, EntityPlayer var2, List var3, boolean var4) {
      if (var1.hasTagCompound()) {
         NBTTagCompound var5 = var1.getTagCompound().getCompoundTag("Fireworks");
         if (var5 != null) {
            if (var5.hasKey("Flight", 99)) {
               var3.add(StatCollector.translateToLocal("item.fireworks.flight") + " " + var5.getByte("Flight"));
            }

            NBTTagList var6 = var5.getTagList("Explosions", 10);
            if (var6 != null && var6.tagCount() > 0) {
               for(int var7 = 0; var7 < var6.tagCount(); ++var7) {
                  NBTTagCompound var8 = var6.getCompoundTagAt(var7);
                  ArrayList var9 = Lists.newArrayList();
                  ItemFireworkCharge.addExplosionInfo(var8, var9);
                  if (var9.size() > 0) {
                     for(int var10 = 1; var10 < var9.size(); ++var10) {
                        var9.set(var10, "  " + (String)var9.get(var10));
                     }

                     var3.addAll(var9);
                  }
               }
            }
         }
      }

   }

   public boolean onItemUse(ItemStack var1, EntityPlayer var2, World var3, BlockPos var4, EnumFacing var5, float var6, float var7, float var8) {
      if (!var3.isRemote) {
         EntityFireworkRocket var9 = new EntityFireworkRocket(var3, (double)((float)var4.getX() + var6), (double)((float)var4.getY() + var7), (double)((float)var4.getZ() + var8), var1);
         var3.spawnEntityInWorld(var9);
         if (!var2.capabilities.isCreativeMode) {
            --var1.stackSize;
         }

         return true;
      } else {
         return false;
      }
   }
}
