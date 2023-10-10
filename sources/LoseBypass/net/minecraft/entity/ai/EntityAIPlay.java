/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.ai;

import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.Vec3;

public class EntityAIPlay
extends EntityAIBase {
    private EntityVillager villagerObj;
    private EntityLivingBase targetVillager;
    private double speed;
    private int playTime;

    public EntityAIPlay(EntityVillager villagerObjIn, double speedIn) {
        this.villagerObj = villagerObjIn;
        this.speed = speedIn;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if (this.villagerObj.getGrowingAge() >= 0) {
            return false;
        }
        if (this.villagerObj.getRNG().nextInt(400) != 0) {
            return false;
        }
        List<EntityVillager> list = this.villagerObj.worldObj.getEntitiesWithinAABB(EntityVillager.class, this.villagerObj.getEntityBoundingBox().expand(6.0, 3.0, 6.0));
        double d0 = Double.MAX_VALUE;
        Iterator<EntityVillager> iterator = list.iterator();
        while (true) {
            double d1;
            if (!iterator.hasNext()) {
                if (this.targetVillager != null) return true;
                Vec3 vec3 = RandomPositionGenerator.findRandomTarget(this.villagerObj, 16, 3);
                if (vec3 != null) return true;
                return false;
            }
            EntityVillager entityvillager = iterator.next();
            if (entityvillager == this.villagerObj || entityvillager.isPlaying() || entityvillager.getGrowingAge() >= 0 || !((d1 = entityvillager.getDistanceSqToEntity(this.villagerObj)) <= d0)) continue;
            d0 = d1;
            this.targetVillager = entityvillager;
        }
    }

    @Override
    public boolean continueExecuting() {
        if (this.playTime <= 0) return false;
        return true;
    }

    @Override
    public void startExecuting() {
        if (this.targetVillager != null) {
            this.villagerObj.setPlaying(true);
        }
        this.playTime = 1000;
    }

    @Override
    public void resetTask() {
        this.villagerObj.setPlaying(false);
        this.targetVillager = null;
    }

    @Override
    public void updateTask() {
        --this.playTime;
        if (this.targetVillager != null) {
            if (!(this.villagerObj.getDistanceSqToEntity(this.targetVillager) > 4.0)) return;
            this.villagerObj.getNavigator().tryMoveToEntityLiving(this.targetVillager, this.speed);
            return;
        }
        if (!this.villagerObj.getNavigator().noPath()) return;
        Vec3 vec3 = RandomPositionGenerator.findRandomTarget(this.villagerObj, 16, 3);
        if (vec3 == null) {
            return;
        }
        this.villagerObj.getNavigator().tryMoveToXYZ(vec3.xCoord, vec3.yCoord, vec3.zCoord, this.speed);
    }
}

