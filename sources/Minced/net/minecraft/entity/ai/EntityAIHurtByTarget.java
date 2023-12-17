// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityCreature;

public class EntityAIHurtByTarget extends EntityAITarget
{
    private final boolean entityCallsForHelp;
    private int revengeTimerOld;
    private final Class<?>[] excludedReinforcementTypes;
    
    public EntityAIHurtByTarget(final EntityCreature creatureIn, final boolean entityCallsForHelpIn, final Class<?>... excludedReinforcementTypes) {
        super(creatureIn, true);
        this.entityCallsForHelp = entityCallsForHelpIn;
        this.excludedReinforcementTypes = excludedReinforcementTypes;
        this.setMutexBits(1);
    }
    
    @Override
    public boolean shouldExecute() {
        final int i = this.taskOwner.getRevengeTimer();
        final EntityLivingBase entitylivingbase = this.taskOwner.getRevengeTarget();
        return i != this.revengeTimerOld && entitylivingbase != null && this.isSuitableTarget(entitylivingbase, false);
    }
    
    @Override
    public void startExecuting() {
        this.taskOwner.setAttackTarget(this.taskOwner.getRevengeTarget());
        this.target = this.taskOwner.getAttackTarget();
        this.revengeTimerOld = this.taskOwner.getRevengeTimer();
        this.unseenMemoryTicks = 300;
        if (this.entityCallsForHelp) {
            this.alertOthers();
        }
        super.startExecuting();
    }
    
    protected void alertOthers() {
        final double d0 = this.getTargetDistance();
        for (final EntityCreature entitycreature : this.taskOwner.world.getEntitiesWithinAABB(this.taskOwner.getClass(), new AxisAlignedBB(this.taskOwner.posX, this.taskOwner.posY, this.taskOwner.posZ, this.taskOwner.posX + 1.0, this.taskOwner.posY + 1.0, this.taskOwner.posZ + 1.0).grow(d0, 10.0, d0))) {
            if (this.taskOwner != entitycreature && entitycreature.getAttackTarget() == null && (!(this.taskOwner instanceof EntityTameable) || ((EntityTameable)this.taskOwner).getOwner() == ((EntityTameable)entitycreature).getOwner()) && !entitycreature.isOnSameTeam(this.taskOwner.getRevengeTarget())) {
                boolean flag = false;
                for (final Class<?> oclass : this.excludedReinforcementTypes) {
                    if (entitycreature.getClass() == oclass) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    continue;
                }
                this.setEntityAttackTarget(entitycreature, this.taskOwner.getRevengeTarget());
            }
        }
    }
    
    protected void setEntityAttackTarget(final EntityCreature creatureIn, final EntityLivingBase entityLivingBaseIn) {
        creatureIn.setAttackTarget(entityLivingBaseIn);
    }
}
