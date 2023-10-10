package net.minecraft.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.world.World;

public class ItemMapBase extends Item {
   public Packet createMapDataPacket(ItemStack var1, World var2, EntityPlayer var3) {
      return null;
   }

   public boolean isMap() {
      return true;
   }
}
