/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.ai;

import java.util.List;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntityVillager;

public class EntityAIFollowGolem
extends EntityAIBase {
    private EntityVillager theVillager;
    private EntityIronGolem theGolem;
    private int takeGolemRoseTick;
    private boolean tookGolemRose;
    private static final String __OBFID = "CL_00001615";

    public EntityAIFollowGolem(EntityVillager p_i1656_1_) {
        this.theVillager = p_i1656_1_;
        this.setMutexBits(3);
    }

    @Override
    public boolean shouldExecute() {
        if (this.theVillager.getGrowingAge() >= 0) {
            return false;
        }
        if (!this.theVillager.worldObj.isDaytime()) {
            return false;
        }
        List var1 = this.theVillager.worldObj.getEntitiesWithinAABB(EntityIronGolem.class, this.theVillager.getEntityBoundingBox().expand(6.0, 2.0, 6.0));
        if (var1.isEmpty()) {
            return false;
        }
        for (EntityIronGolem var3 : var1) {
            if (var3.getHoldRoseTick() <= 0) continue;
            this.theGolem = var3;
            break;
        }
        return this.theGolem != null;
    }

    @Override
    public boolean continueExecuting() {
        return this.theGolem.getHoldRoseTick() > 0;
    }

    @Override
    public void startExecuting() {
        this.takeGolemRoseTick = this.theVillager.getRNG().nextInt(320);
        this.tookGolemRose = false;
        this.theGolem.getNavigator().clearPathEntity();
    }

    @Override
    public void resetTask() {
        this.theGolem = null;
        this.theVillager.getNavigator().clearPathEntity();
    }

    @Override
    public void updateTask() {
        this.theVillager.getLookHelper().setLookPositionWithEntity(this.theGolem, 30.0f, 30.0f);
        if (this.theGolem.getHoldRoseTick() == this.takeGolemRoseTick) {
            this.theVillager.getNavigator().tryMoveToEntityLiving(this.theGolem, 0.5);
            this.tookGolemRose = true;
        }
        if (this.tookGolemRose && this.theVillager.getDistanceSqToEntity(this.theGolem) < 4.0) {
            this.theGolem.setHoldingRose(false);
            this.theVillager.getNavigator().clearPathEntity();
        }
    }
}

