package net.minecraft.entity.ai;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EntitySelectors;

public class EntityAINearestAttackableTarget extends EntityAITarget {
   private final int targetChance;
   protected final Class targetClass;
   protected final EntityAINearestAttackableTarget.Sorter theNearestAttackableTargetSorter;
   protected Predicate targetEntitySelector;
   protected EntityLivingBase targetEntity;

   public EntityAINearestAttackableTarget(EntityCreature var1, Class var2, boolean var3) {
      this(var1, var2, var3, false);
   }

   public EntityAINearestAttackableTarget(EntityCreature var1, Class var2, boolean var3, boolean var4) {
      this(var1, var2, 10, var3, var4, (Predicate)null);
   }

   public void startExecuting() {
      this.taskOwner.setAttackTarget(this.targetEntity);
      super.startExecuting();
   }

   public boolean shouldExecute() {
      if (this.targetChance > 0 && this.taskOwner.getRNG().nextInt(this.targetChance) != 0) {
         return false;
      } else {
         double var1 = this.getTargetDistance();
         List var3 = this.taskOwner.worldObj.getEntitiesWithinAABB(this.targetClass, this.taskOwner.getEntityBoundingBox().expand(var1, 4.0D, var1), Predicates.and(this.targetEntitySelector, EntitySelectors.NOT_SPECTATING));
         Collections.sort(var3, this.theNearestAttackableTargetSorter);
         if (var3.isEmpty()) {
            return false;
         } else {
            this.targetEntity = (EntityLivingBase)var3.get(0);
            return true;
         }
      }
   }

   public EntityAINearestAttackableTarget(EntityCreature var1, Class var2, int var3, boolean var4, boolean var5, Predicate var6) {
      super(var1, var4, var5);
      this.targetClass = var2;
      this.targetChance = var3;
      this.theNearestAttackableTargetSorter = new EntityAINearestAttackableTarget.Sorter(var1);
      this.setMutexBits(1);
      this.targetEntitySelector = new Predicate(this, var6) {
         final EntityAINearestAttackableTarget this$0;
         private final Predicate val$targetSelector;

         {
            this.this$0 = var1;
            this.val$targetSelector = var2;
         }

         public boolean apply(Object var1) {
            return this.apply((EntityLivingBase)var1);
         }

         public boolean apply(EntityLivingBase var1) {
            if (this.val$targetSelector != null && !this.val$targetSelector.apply(var1)) {
               return false;
            } else {
               if (var1 instanceof EntityPlayer) {
                  double var2 = this.this$0.getTargetDistance();
                  if (var1.isSneaking()) {
                     var2 *= 0.800000011920929D;
                  }

                  if (var1.isInvisible()) {
                     float var4 = ((EntityPlayer)var1).getArmorVisibility();
                     if (var4 < 0.1F) {
                        var4 = 0.1F;
                     }

                     var2 *= (double)(0.7F * var4);
                  }

                  if ((double)var1.getDistanceToEntity(this.this$0.taskOwner) > var2) {
                     return false;
                  }
               }

               return this.this$0.isSuitableTarget(var1, false);
            }
         }
      };
   }

   public static class Sorter implements Comparator {
      private final Entity theEntity;

      public int compare(Object var1, Object var2) {
         return this.compare((Entity)var1, (Entity)var2);
      }

      public Sorter(Entity var1) {
         this.theEntity = var1;
      }

      public int compare(Entity var1, Entity var2) {
         double var3 = this.theEntity.getDistanceSqToEntity(var1);
         double var5 = this.theEntity.getDistanceSqToEntity(var2);
         return var3 < var5 ? -1 : (var3 > var5 ? 1 : 0);
      }
   }
}
