package net.minecraft.entity.ai;

import com.google.common.base.Predicate;
import java.util.Collections;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.scoreboard.Team;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityAIFindEntityNearestPlayer extends EntityAIBase {
   private EntityLivingBase field_179433_e;
   private final Predicate field_179435_c;
   private final EntityAINearestAttackableTarget.Sorter field_179432_d;
   private static final Logger field_179436_a = LogManager.getLogger();
   private EntityLiving field_179434_b;

   public boolean shouldExecute() {
      double var1 = this.func_179431_f();
      List var3 = this.field_179434_b.worldObj.getEntitiesWithinAABB(EntityPlayer.class, this.field_179434_b.getEntityBoundingBox().expand(var1, 4.0D, var1), this.field_179435_c);
      Collections.sort(var3, this.field_179432_d);
      if (var3.isEmpty()) {
         return false;
      } else {
         this.field_179433_e = (EntityLivingBase)var3.get(0);
         return true;
      }
   }

   public void startExecuting() {
      this.field_179434_b.setAttackTarget(this.field_179433_e);
      super.startExecuting();
   }

   public boolean continueExecuting() {
      EntityLivingBase var1 = this.field_179434_b.getAttackTarget();
      if (var1 == null) {
         return false;
      } else if (!var1.isEntityAlive()) {
         return false;
      } else if (var1 instanceof EntityPlayer && ((EntityPlayer)var1).capabilities.disableDamage) {
         return false;
      } else {
         Team var2 = this.field_179434_b.getTeam();
         Team var3 = var1.getTeam();
         if (var2 != null && var3 == var2) {
            return false;
         } else {
            double var4 = this.func_179431_f();
            return this.field_179434_b.getDistanceSqToEntity(var1) > var4 * var4 ? false : !(var1 instanceof EntityPlayerMP) || !((EntityPlayerMP)var1).theItemInWorldManager.isCreative();
         }
      }
   }

   public EntityAIFindEntityNearestPlayer(EntityLiving var1) {
      this.field_179434_b = var1;
      if (var1 instanceof EntityCreature) {
         field_179436_a.warn("Use NearestAttackableTargetGoal.class for PathfinerMob mobs!");
      }

      this.field_179435_c = new Predicate(this) {
         final EntityAIFindEntityNearestPlayer this$0;

         public boolean apply(Object var1) {
            return this.apply((Entity)var1);
         }

         public boolean apply(Entity var1) {
            if (!(var1 instanceof EntityPlayer)) {
               return false;
            } else if (((EntityPlayer)var1).capabilities.disableDamage) {
               return false;
            } else {
               double var2 = this.this$0.func_179431_f();
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

               return (double)var1.getDistanceToEntity(EntityAIFindEntityNearestPlayer.access$0(this.this$0)) > var2 ? false : EntityAITarget.isSuitableTarget(EntityAIFindEntityNearestPlayer.access$0(this.this$0), (EntityLivingBase)var1, false, true);
            }
         }

         {
            this.this$0 = var1;
         }
      };
      this.field_179432_d = new EntityAINearestAttackableTarget.Sorter(var1);
   }

   public void resetTask() {
      this.field_179434_b.setAttackTarget((EntityLivingBase)null);
      super.startExecuting();
   }

   protected double func_179431_f() {
      IAttributeInstance var1 = this.field_179434_b.getEntityAttribute(SharedMonsterAttributes.followRange);
      return var1 == null ? 16.0D : var1.getAttributeValue();
   }

   static EntityLiving access$0(EntityAIFindEntityNearestPlayer var0) {
      return var0.field_179434_b;
   }
}
