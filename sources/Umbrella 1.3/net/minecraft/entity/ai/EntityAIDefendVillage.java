/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.village.Village;

public class EntityAIDefendVillage
extends EntityAITarget {
    EntityIronGolem irongolem;
    EntityLivingBase villageAgressorTarget;
    private static final String __OBFID = "CL_00001618";

    public EntityAIDefendVillage(EntityIronGolem p_i1659_1_) {
        super(p_i1659_1_, false, true);
        this.irongolem = p_i1659_1_;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        Village var1 = this.irongolem.getVillage();
        if (var1 == null) {
            return false;
        }
        this.villageAgressorTarget = var1.findNearestVillageAggressor(this.irongolem);
        if (!this.isSuitableTarget(this.villageAgressorTarget, false)) {
            if (this.taskOwner.getRNG().nextInt(20) == 0) {
                this.villageAgressorTarget = var1.func_82685_c(this.irongolem);
                return this.isSuitableTarget(this.villageAgressorTarget, false);
            }
            return false;
        }
        return true;
    }

    @Override
    public void startExecuting() {
        this.irongolem.setAttackTarget(this.villageAgressorTarget);
        super.startExecuting();
    }
}

