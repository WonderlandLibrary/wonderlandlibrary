package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class RenderPotion extends RenderSnowball {
   public ItemStack func_177082_d(Entity var1) {
      return this.func_177082_d((EntityPotion)var1);
   }

   public ItemStack func_177082_d(EntityPotion var1) {
      return new ItemStack(this.field_177084_a, 1, var1.getPotionDamage());
   }

   public RenderPotion(RenderManager var1, RenderItem var2) {
      super(var1, Items.potionitem, var2);
   }
}
