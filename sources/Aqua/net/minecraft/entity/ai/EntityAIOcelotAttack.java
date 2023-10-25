package net.minecraft.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.world.World;

public class EntityAIOcelotAttack
extends EntityAIBase {
    World theWorld;
    EntityLiving theEntity;
    EntityLivingBase theVictim;
    int attackCountdown;

    public EntityAIOcelotAttack(EntityLiving theEntityIn) {
        this.theEntity = theEntityIn;
        this.theWorld = theEntityIn.worldObj;
        this.setMutexBits(3);
    }

    public boolean shouldExecute() {
        EntityLivingBase entitylivingbase = this.theEntity.getAttackTarget();
        if (entitylivingbase == null) {
            return false;
        }
        this.theVictim = entitylivingbase;
        return true;
    }

    public boolean continueExecuting() {
        return !this.theVictim.isEntityAlive() ? false : (this.theEntity.getDistanceSqToEntity((Entity)this.theVictim) > 225.0 ? false : !this.theEntity.getNavigator().noPath() || this.shouldExecute());
    }

    public void resetTask() {
        this.theVictim = null;
        this.theEntity.getNavigator().clearPathEntity();
    }

    public void updateTask() {
        this.theEntity.getLookHelper().setLookPositionWithEntity((Entity)this.theVictim, 30.0f, 30.0f);
        double d0 = this.theEntity.width * 2.0f * this.theEntity.width * 2.0f;
        double d1 = this.theEntity.getDistanceSq(this.theVictim.posX, this.theVictim.getEntityBoundingBox().minY, this.theVictim.posZ);
        double d2 = 0.8;
        if (d1 > d0 && d1 < 16.0) {
            d2 = 1.33;
        } else if (d1 < 225.0) {
            d2 = 0.6;
        }
        this.theEntity.getNavigator().tryMoveToEntityLiving((Entity)this.theVictim, d2);
        this.attackCountdown = Math.max((int)(this.attackCountdown - 1), (int)0);
        if (d1 <= d0 && this.attackCountdown <= 0) {
            this.attackCountdown = 20;
            this.theEntity.attackEntityAsMob((Entity)this.theVictim);
        }
    }
}
