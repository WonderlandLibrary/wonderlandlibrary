package net.minecraft.entity.ai;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntityVillager;

public class EntityAIFollowGolem
extends EntityAIBase {
    private EntityVillager theVillager;
    private EntityIronGolem theGolem;
    private int takeGolemRoseTick;
    private boolean tookGolemRose;

    public EntityAIFollowGolem(EntityVillager theVillagerIn) {
        this.theVillager = theVillagerIn;
        this.setMutexBits(3);
    }

    public boolean shouldExecute() {
        if (this.theVillager.getGrowingAge() >= 0) {
            return false;
        }
        if (!this.theVillager.worldObj.isDaytime()) {
            return false;
        }
        List list = this.theVillager.worldObj.getEntitiesWithinAABB(EntityIronGolem.class, this.theVillager.getEntityBoundingBox().expand(6.0, 2.0, 6.0));
        if (list.isEmpty()) {
            return false;
        }
        for (EntityIronGolem entityirongolem : list) {
            if (entityirongolem.getHoldRoseTick() <= 0) continue;
            this.theGolem = entityirongolem;
            break;
        }
        return this.theGolem != null;
    }

    public boolean continueExecuting() {
        return this.theGolem.getHoldRoseTick() > 0;
    }

    public void startExecuting() {
        this.takeGolemRoseTick = this.theVillager.getRNG().nextInt(320);
        this.tookGolemRose = false;
        this.theGolem.getNavigator().clearPathEntity();
    }

    public void resetTask() {
        this.theGolem = null;
        this.theVillager.getNavigator().clearPathEntity();
    }

    public void updateTask() {
        this.theVillager.getLookHelper().setLookPositionWithEntity((Entity)this.theGolem, 30.0f, 30.0f);
        if (this.theGolem.getHoldRoseTick() == this.takeGolemRoseTick) {
            this.theVillager.getNavigator().tryMoveToEntityLiving((Entity)this.theGolem, 0.5);
            this.tookGolemRose = true;
        }
        if (this.tookGolemRose && this.theVillager.getDistanceSqToEntity((Entity)this.theGolem) < 4.0) {
            this.theGolem.setHoldingRose(false);
            this.theVillager.getNavigator().clearPathEntity();
        }
    }
}
