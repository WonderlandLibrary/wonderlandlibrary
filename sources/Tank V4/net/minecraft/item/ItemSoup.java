package net.minecraft.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.world.World;

public class ItemSoup extends ItemFood {
   public ItemSoup(int var1) {
      super(var1, false);
      this.setMaxStackSize(1);
   }

   public ItemStack onItemUseFinish(ItemStack var1, World var2, EntityPlayer var3) {
      super.onItemUseFinish(var1, var2, var3);
      return new ItemStack(Items.bowl);
   }
}
