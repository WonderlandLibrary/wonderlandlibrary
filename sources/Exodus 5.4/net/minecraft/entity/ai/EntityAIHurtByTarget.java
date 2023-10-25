/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.util.AxisAlignedBB;

public class EntityAIHurtByTarget
extends EntityAITarget {
    private int revengeTimerOld;
    private boolean entityCallsForHelp;
    private final Class[] targetClasses;

    public EntityAIHurtByTarget(EntityCreature entityCreature, boolean bl, Class ... classArray) {
        super(entityCreature, false);
        this.entityCallsForHelp = bl;
        this.targetClasses = classArray;
        this.setMutexBits(1);
    }

    protected void setEntityAttackTarget(EntityCreature entityCreature, EntityLivingBase entityLivingBase) {
        entityCreature.setAttackTarget(entityLivingBase);
    }

    @Override
    public boolean shouldExecute() {
        int n = this.taskOwner.getRevengeTimer();
        return n != this.revengeTimerOld && this.isSuitableTarget(this.taskOwner.getAITarget(), false);
    }

    @Override
    public void startExecuting() {
        this.taskOwner.setAttackTarget(this.taskOwner.getAITarget());
        this.revengeTimerOld = this.taskOwner.getRevengeTimer();
        if (this.entityCallsForHelp) {
            double d = this.getTargetDistance();
            for (EntityCreature entityCreature : this.taskOwner.worldObj.getEntitiesWithinAABB(this.taskOwner.getClass(), new AxisAlignedBB(this.taskOwner.posX, this.taskOwner.posY, this.taskOwner.posZ, this.taskOwner.posX + 1.0, this.taskOwner.posY + 1.0, this.taskOwner.posZ + 1.0).expand(d, 10.0, d))) {
                if (this.taskOwner == entityCreature || entityCreature.getAttackTarget() != null || entityCreature.isOnSameTeam(this.taskOwner.getAITarget())) continue;
                boolean bl = false;
                Class[] classArray = this.targetClasses;
                int n = this.targetClasses.length;
                int n2 = 0;
                while (n2 < n) {
                    Class clazz = classArray[n2];
                    if (entityCreature.getClass() == clazz) {
                        bl = true;
                        break;
                    }
                    ++n2;
                }
                if (bl) continue;
                this.setEntityAttackTarget(entityCreature, this.taskOwner.getAITarget());
            }
        }
        super.startExecuting();
    }
}

