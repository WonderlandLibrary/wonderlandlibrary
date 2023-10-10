package net.minecraft.entity.ai;

import com.google.common.base.Predicate;
import java.util.Collections;
import java.util.List;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayerMP;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityAIFindEntityNearest extends EntityAIBase {
   private static final Logger field_179444_a = LogManager.getLogger();
   private final Predicate field_179443_c;
   private final EntityAINearestAttackableTarget.Sorter field_179440_d;
   private EntityLiving field_179442_b;
   private EntityLivingBase field_179441_e;
   private Class field_179439_f;

   static EntityLiving access$0(EntityAIFindEntityNearest var0) {
      return var0.field_179442_b;
   }

   public boolean continueExecuting() {
      EntityLivingBase var1 = this.field_179442_b.getAttackTarget();
      if (var1 == null) {
         return false;
      } else if (!var1.isEntityAlive()) {
         return false;
      } else {
         double var2 = this.func_179438_f();
         return this.field_179442_b.getDistanceSqToEntity(var1) > var2 * var2 ? false : !(var1 instanceof EntityPlayerMP) || !((EntityPlayerMP)var1).theItemInWorldManager.isCreative();
      }
   }

   public boolean shouldExecute() {
      double var1 = this.func_179438_f();
      List var3 = this.field_179442_b.worldObj.getEntitiesWithinAABB(this.field_179439_f, this.field_179442_b.getEntityBoundingBox().expand(var1, 4.0D, var1), this.field_179443_c);
      Collections.sort(var3, this.field_179440_d);
      if (var3.isEmpty()) {
         return false;
      } else {
         this.field_179441_e = (EntityLivingBase)var3.get(0);
         return true;
      }
   }

   public EntityAIFindEntityNearest(EntityLiving var1, Class var2) {
      this.field_179442_b = var1;
      this.field_179439_f = var2;
      if (var1 instanceof EntityCreature) {
         field_179444_a.warn("Use NearestAttackableTargetGoal.class for PathfinerMob mobs!");
      }

      this.field_179443_c = new Predicate(this) {
         final EntityAIFindEntityNearest this$0;

         public boolean apply(Object var1) {
            return this.apply((EntityLivingBase)var1);
         }

         {
            this.this$0 = var1;
         }

         public boolean apply(EntityLivingBase var1) {
            double var2 = this.this$0.func_179438_f();
            if (var1.isSneaking()) {
               var2 *= 0.800000011920929D;
            }

            return var1.isInvisible() ? false : ((double)var1.getDistanceToEntity(EntityAIFindEntityNearest.access$0(this.this$0)) > var2 ? false : EntityAITarget.isSuitableTarget(EntityAIFindEntityNearest.access$0(this.this$0), var1, false, true));
         }
      };
      this.field_179440_d = new EntityAINearestAttackableTarget.Sorter(var1);
   }

   public void resetTask() {
      this.field_179442_b.setAttackTarget((EntityLivingBase)null);
      super.startExecuting();
   }

   public void startExecuting() {
      this.field_179442_b.setAttackTarget(this.field_179441_e);
      super.startExecuting();
   }

   protected double func_179438_f() {
      IAttributeInstance var1 = this.field_179442_b.getEntityAttribute(SharedMonsterAttributes.followRange);
      return var1 == null ? 16.0D : var1.getAttributeValue();
   }
}
