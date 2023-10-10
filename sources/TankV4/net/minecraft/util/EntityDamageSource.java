package net.minecraft.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class EntityDamageSource extends DamageSource {
   private boolean isThornsDamage = false;
   protected Entity damageSourceEntity;

   public EntityDamageSource setIsThornsDamage() {
      this.isThornsDamage = true;
      return this;
   }

   public Entity getEntity() {
      return this.damageSourceEntity;
   }

   public boolean getIsThornsDamage() {
      return this.isThornsDamage;
   }

   public IChatComponent getDeathMessage(EntityLivingBase var1) {
      ItemStack var2 = this.damageSourceEntity instanceof EntityLivingBase ? ((EntityLivingBase)this.damageSourceEntity).getHeldItem() : null;
      String var3 = "death.attack." + this.damageType;
      String var4 = var3 + ".item";
      return var2 != null && var2.hasDisplayName() && StatCollector.canTranslate(var4) ? new ChatComponentTranslation(var4, new Object[]{var1.getDisplayName(), this.damageSourceEntity.getDisplayName(), var2.getChatComponent()}) : new ChatComponentTranslation(var3, new Object[]{var1.getDisplayName(), this.damageSourceEntity.getDisplayName()});
   }

   public boolean isDifficultyScaled() {
      return this.damageSourceEntity != null && this.damageSourceEntity instanceof EntityLivingBase && !(this.damageSourceEntity instanceof EntityPlayer);
   }

   public EntityDamageSource(String var1, Entity var2) {
      super(var1);
      this.damageSourceEntity = var2;
   }
}
