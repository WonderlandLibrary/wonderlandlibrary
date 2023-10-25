/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.util.math.AxisAlignedBB;

public class EntityAIHurtByTarget
extends EntityAITarget {
    private final boolean entityCallsForHelp;
    private int revengeTimerOld;
    private final Class<?>[] targetClasses;

    public EntityAIHurtByTarget(EntityCreature creatureIn, boolean entityCallsForHelpIn, Class<?> ... targetClassesIn) {
        super(creatureIn, true);
        this.entityCallsForHelp = entityCallsForHelpIn;
        this.targetClasses = targetClassesIn;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        int i = this.taskOwner.getRevengeTimer();
        EntityLivingBase entitylivingbase = this.taskOwner.getAITarget();
        return i != this.revengeTimerOld && entitylivingbase != null && this.isSuitableTarget(entitylivingbase, false);
    }

    @Override
    public void startExecuting() {
        this.taskOwner.setAttackTarget(this.taskOwner.getAITarget());
        this.target = this.taskOwner.getAttackTarget();
        this.revengeTimerOld = this.taskOwner.getRevengeTimer();
        this.unseenMemoryTicks = 300;
        if (this.entityCallsForHelp) {
            this.alertOthers();
        }
        super.startExecuting();
    }

    protected void alertOthers() {
        double d0 = this.getTargetDistance();
        for (EntityCreature entitycreature : this.taskOwner.world.getEntitiesWithinAABB(this.taskOwner.getClass(), new AxisAlignedBB(this.taskOwner.posX, this.taskOwner.posY, this.taskOwner.posZ, this.taskOwner.posX + 1.0, this.taskOwner.posY + 1.0, this.taskOwner.posZ + 1.0).expand(d0, 10.0, d0))) {
            if (this.taskOwner == entitycreature || entitycreature.getAttackTarget() != null || this.taskOwner instanceof EntityTameable && ((EntityTameable)this.taskOwner).getOwner() != ((EntityTameable)entitycreature).getOwner() || entitycreature.isOnSameTeam(this.taskOwner.getAITarget())) continue;
            boolean flag = false;
            for (Class<?> oclass : this.targetClasses) {
                if (entitycreature.getClass() != oclass) continue;
                flag = true;
                break;
            }
            if (flag) continue;
            this.setEntityAttackTarget(entitycreature, this.taskOwner.getAITarget());
        }
    }

    protected void setEntityAttackTarget(EntityCreature creatureIn, EntityLivingBase entityLivingBaseIn) {
        creatureIn.setAttackTarget(entityLivingBaseIn);
    }
}

