// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.util.math.Vec3d;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityVillager;

public class EntityAIPlay extends EntityAIBase
{
    private final EntityVillager villager;
    private EntityLivingBase targetVillager;
    private final double speed;
    private int playTime;
    
    public EntityAIPlay(final EntityVillager villagerIn, final double speedIn) {
        this.villager = villagerIn;
        this.speed = speedIn;
        this.setMutexBits(1);
    }
    
    @Override
    public boolean shouldExecute() {
        if (this.villager.getGrowingAge() >= 0) {
            return false;
        }
        if (this.villager.getRNG().nextInt(400) != 0) {
            return false;
        }
        final List<EntityVillager> list = this.villager.world.getEntitiesWithinAABB((Class<? extends EntityVillager>)EntityVillager.class, this.villager.getEntityBoundingBox().grow(6.0, 3.0, 6.0));
        double d0 = Double.MAX_VALUE;
        for (final EntityVillager entityvillager : list) {
            if (entityvillager != this.villager && !entityvillager.isPlaying() && entityvillager.getGrowingAge() < 0) {
                final double d2 = entityvillager.getDistanceSq(this.villager);
                if (d2 > d0) {
                    continue;
                }
                d0 = d2;
                this.targetVillager = entityvillager;
            }
        }
        if (this.targetVillager == null) {
            final Vec3d vec3d = RandomPositionGenerator.findRandomTarget(this.villager, 16, 3);
            if (vec3d == null) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean shouldContinueExecuting() {
        return this.playTime > 0;
    }
    
    @Override
    public void startExecuting() {
        if (this.targetVillager != null) {
            this.villager.setPlaying(true);
        }
        this.playTime = 1000;
    }
    
    @Override
    public void resetTask() {
        this.villager.setPlaying(false);
        this.targetVillager = null;
    }
    
    @Override
    public void updateTask() {
        --this.playTime;
        if (this.targetVillager != null) {
            if (this.villager.getDistanceSq(this.targetVillager) > 4.0) {
                this.villager.getNavigator().tryMoveToEntityLiving(this.targetVillager, this.speed);
            }
        }
        else if (this.villager.getNavigator().noPath()) {
            final Vec3d vec3d = RandomPositionGenerator.findRandomTarget(this.villager, 16, 3);
            if (vec3d == null) {
                return;
            }
            this.villager.getNavigator().tryMoveToXYZ(vec3d.x, vec3d.y, vec3d.z, this.speed);
        }
    }
}
