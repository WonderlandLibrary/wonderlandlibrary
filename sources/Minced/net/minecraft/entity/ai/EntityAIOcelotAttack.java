// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;

public class EntityAIOcelotAttack extends EntityAIBase
{
    World world;
    EntityLiving entity;
    EntityLivingBase target;
    int attackCountdown;
    
    public EntityAIOcelotAttack(final EntityLiving theEntityIn) {
        this.entity = theEntityIn;
        this.world = theEntityIn.world;
        this.setMutexBits(3);
    }
    
    @Override
    public boolean shouldExecute() {
        final EntityLivingBase entitylivingbase = this.entity.getAttackTarget();
        if (entitylivingbase == null) {
            return false;
        }
        this.target = entitylivingbase;
        return true;
    }
    
    @Override
    public boolean shouldContinueExecuting() {
        return this.target.isEntityAlive() && this.entity.getDistanceSq(this.target) <= 225.0 && (!this.entity.getNavigator().noPath() || this.shouldExecute());
    }
    
    @Override
    public void resetTask() {
        this.target = null;
        this.entity.getNavigator().clearPath();
    }
    
    @Override
    public void updateTask() {
        this.entity.getLookHelper().setLookPositionWithEntity(this.target, 30.0f, 30.0f);
        final double d0 = this.entity.width * 2.0f * this.entity.width * 2.0f;
        final double d2 = this.entity.getDistanceSq(this.target.posX, this.target.getEntityBoundingBox().minY, this.target.posZ);
        double d3 = 0.8;
        if (d2 > d0 && d2 < 16.0) {
            d3 = 1.33;
        }
        else if (d2 < 225.0) {
            d3 = 0.6;
        }
        this.entity.getNavigator().tryMoveToEntityLiving(this.target, d3);
        this.attackCountdown = Math.max(this.attackCountdown - 1, 0);
        if (d2 <= d0 && this.attackCountdown <= 0) {
            this.attackCountdown = 20;
            this.entity.attackEntityAsMob(this.target);
        }
    }
}
