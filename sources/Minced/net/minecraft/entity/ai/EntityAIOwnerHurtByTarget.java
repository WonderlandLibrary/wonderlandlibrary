// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;

public class EntityAIOwnerHurtByTarget extends EntityAITarget
{
    EntityTameable tameable;
    EntityLivingBase attacker;
    private int timestamp;
    
    public EntityAIOwnerHurtByTarget(final EntityTameable theDefendingTameableIn) {
        super(theDefendingTameableIn, false);
        this.tameable = theDefendingTameableIn;
        this.setMutexBits(1);
    }
    
    @Override
    public boolean shouldExecute() {
        if (!this.tameable.isTamed()) {
            return false;
        }
        final EntityLivingBase entitylivingbase = this.tameable.getOwner();
        if (entitylivingbase == null) {
            return false;
        }
        this.attacker = entitylivingbase.getRevengeTarget();
        final int i = entitylivingbase.getRevengeTimer();
        return i != this.timestamp && this.isSuitableTarget(this.attacker, false) && this.tameable.shouldAttackEntity(this.attacker, entitylivingbase);
    }
    
    @Override
    public void startExecuting() {
        this.taskOwner.setAttackTarget(this.attacker);
        final EntityLivingBase entitylivingbase = this.tameable.getOwner();
        if (entitylivingbase != null) {
            this.timestamp = entitylivingbase.getRevengeTimer();
        }
        super.startExecuting();
    }
}
