package net.minecraft.client.stream;

import net.minecraft.entity.EntityLivingBase;

public class MetadataCombat extends Metadata {
   public MetadataCombat(EntityLivingBase var1, EntityLivingBase var2) {
      super("player_combat");
      this.func_152808_a("player", var1.getName());
      if (var2 != null) {
         this.func_152808_a("primary_opponent", var2.getName());
      }

      if (var2 != null) {
         this.func_152807_a("Combat between " + var1.getName() + " and " + var2.getName());
      } else {
         this.func_152807_a("Combat between " + var1.getName() + " and others");
      }

   }
}
