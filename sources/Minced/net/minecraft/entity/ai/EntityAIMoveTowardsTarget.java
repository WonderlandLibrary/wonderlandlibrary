// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityCreature;

public class EntityAIMoveTowardsTarget extends EntityAIBase
{
    private final EntityCreature creature;
    private EntityLivingBase targetEntity;
    private double movePosX;
    private double movePosY;
    private double movePosZ;
    private final double speed;
    private final float maxTargetDistance;
    
    public EntityAIMoveTowardsTarget(final EntityCreature creature, final double speedIn, final float targetMaxDistance) {
        this.creature = creature;
        this.speed = speedIn;
        this.maxTargetDistance = targetMaxDistance;
        this.setMutexBits(1);
    }
    
    @Override
    public boolean shouldExecute() {
        this.targetEntity = this.creature.getAttackTarget();
        if (this.targetEntity == null) {
            return false;
        }
        if (this.targetEntity.getDistanceSq(this.creature) > this.maxTargetDistance * this.maxTargetDistance) {
            return false;
        }
        final Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockTowards(this.creature, 16, 7, new Vec3d(this.targetEntity.posX, this.targetEntity.posY, this.targetEntity.posZ));
        if (vec3d == null) {
            return false;
        }
        this.movePosX = vec3d.x;
        this.movePosY = vec3d.y;
        this.movePosZ = vec3d.z;
        return true;
    }
    
    @Override
    public boolean shouldContinueExecuting() {
        return !this.creature.getNavigator().noPath() && this.targetEntity.isEntityAlive() && this.targetEntity.getDistanceSq(this.creature) < this.maxTargetDistance * this.maxTargetDistance;
    }
    
    @Override
    public void resetTask() {
        this.targetEntity = null;
    }
    
    @Override
    public void startExecuting() {
        this.creature.getNavigator().tryMoveToXYZ(this.movePosX, this.movePosY, this.movePosZ, this.speed);
    }
}
