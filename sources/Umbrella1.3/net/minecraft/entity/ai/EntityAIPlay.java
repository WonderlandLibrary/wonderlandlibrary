/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.ai;

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
    private double field_75261_c;
    private int playTime;
    private static final String __OBFID = "CL_00001605";

    public EntityAIPlay(EntityVillager p_i1646_1_, double p_i1646_2_) {
        this.villagerObj = p_i1646_1_;
        this.field_75261_c = p_i1646_2_;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        Vec3 var8;
        if (this.villagerObj.getGrowingAge() >= 0) {
            return false;
        }
        if (this.villagerObj.getRNG().nextInt(400) != 0) {
            return false;
        }
        List var1 = this.villagerObj.worldObj.getEntitiesWithinAABB(EntityVillager.class, this.villagerObj.getEntityBoundingBox().expand(6.0, 3.0, 6.0));
        double var2 = Double.MAX_VALUE;
        for (EntityVillager var5 : var1) {
            double var6;
            if (var5 == this.villagerObj || var5.isPlaying() || var5.getGrowingAge() >= 0 || !((var6 = var5.getDistanceSqToEntity(this.villagerObj)) <= var2)) continue;
            var2 = var6;
            this.targetVillager = var5;
        }
        return this.targetVillager != null || (var8 = RandomPositionGenerator.findRandomTarget(this.villagerObj, 16, 3)) != null;
    }

    @Override
    public boolean continueExecuting() {
        return this.playTime > 0;
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
            if (this.villagerObj.getDistanceSqToEntity(this.targetVillager) > 4.0) {
                this.villagerObj.getNavigator().tryMoveToEntityLiving(this.targetVillager, this.field_75261_c);
            }
        } else if (this.villagerObj.getNavigator().noPath()) {
            Vec3 var1 = RandomPositionGenerator.findRandomTarget(this.villagerObj, 16, 3);
            if (var1 == null) {
                return;
            }
            this.villagerObj.getNavigator().tryMoveToXYZ(var1.xCoord, var1.yCoord, var1.zCoord, this.field_75261_c);
        }
    }
}

