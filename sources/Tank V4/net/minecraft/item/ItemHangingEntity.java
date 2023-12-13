package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemHangingEntity extends Item {
   private final Class hangingEntityClass;

   public boolean onItemUse(ItemStack var1, EntityPlayer var2, World var3, BlockPos var4, EnumFacing var5, float var6, float var7, float var8) {
      if (var5 == EnumFacing.DOWN) {
         return false;
      } else if (var5 == EnumFacing.UP) {
         return false;
      } else {
         BlockPos var9 = var4.offset(var5);
         if (!var2.canPlayerEdit(var9, var5, var1)) {
            return false;
         } else {
            EntityHanging var10 = this.createEntity(var3, var9, var5);
            if (var10 != null && var10.onValidSurface()) {
               if (!var3.isRemote) {
                  var3.spawnEntityInWorld(var10);
               }

               --var1.stackSize;
            }

            return true;
         }
      }
   }

   public ItemHangingEntity(Class var1) {
      this.hangingEntityClass = var1;
      this.setCreativeTab(CreativeTabs.tabDecorations);
   }

   private EntityHanging createEntity(World var1, BlockPos var2, EnumFacing var3) {
      return (EntityHanging)(this.hangingEntityClass == EntityPainting.class ? new EntityPainting(var1, var2, var3) : (this.hangingEntityClass == EntityItemFrame.class ? new EntityItemFrame(var1, var2, var3) : null));
   }
}
