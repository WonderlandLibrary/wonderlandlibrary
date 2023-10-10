package net.minecraft.entity.ai;

import com.google.common.base.Predicate;
import net.minecraft.entity.passive.EntityTameable;

public class EntityAITargetNonTamed extends EntityAINearestAttackableTarget {
   private EntityTameable theTameable;

   public EntityAITargetNonTamed(EntityTameable var1, Class var2, boolean var3, Predicate var4) {
      super(var1, var2, 10, var3, false, var4);
      this.theTameable = var1;
   }

   public boolean shouldExecute() {
      return !this.theTameable.isTamed() && super.shouldExecute();
   }
}
