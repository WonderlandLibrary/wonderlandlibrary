package net.minecraft.entity.monster;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.util.MathHelper;

static class EntityGhast.AILookAround
extends EntityAIBase {
    private EntityGhast parentEntity;

    public EntityGhast.AILookAround(EntityGhast ghast) {
        this.parentEntity = ghast;
        this.setMutexBits(2);
    }

    public boolean shouldExecute() {
        return true;
    }

    public void updateTask() {
        if (this.parentEntity.getAttackTarget() == null) {
            this.parentEntity.renderYawOffset = this.parentEntity.rotationYaw = -((float)MathHelper.atan2((double)this.parentEntity.motionX, (double)this.parentEntity.motionZ)) * 180.0f / (float)Math.PI;
        } else {
            EntityLivingBase entitylivingbase = this.parentEntity.getAttackTarget();
            double d0 = 64.0;
            if (entitylivingbase.getDistanceSqToEntity((Entity)this.parentEntity) < d0 * d0) {
                double d1 = entitylivingbase.posX - this.parentEntity.posX;
                double d2 = entitylivingbase.posZ - this.parentEntity.posZ;
                this.parentEntity.renderYawOffset = this.parentEntity.rotationYaw = -((float)MathHelper.atan2((double)d1, (double)d2)) * 180.0f / (float)Math.PI;
            }
        }
    }
}
