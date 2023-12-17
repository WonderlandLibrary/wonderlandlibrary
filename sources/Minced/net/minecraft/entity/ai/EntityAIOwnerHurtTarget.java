// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;

public class EntityAIOwnerHurtTarget extends EntityAITarget
{
    EntityTameable tameable;
    EntityLivingBase attacker;
    private int timestamp;
    
    public EntityAIOwnerHurtTarget(final EntityTameable theEntityTameableIn) {
        super(theEntityTameableIn, false);
        this.tameable = theEntityTameableIn;
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
        this.attacker = entitylivingbase.getLastAttackedEntity();
        final int i = entitylivingbase.getLastAttackedEntityTime();
        return i != this.timestamp && this.isSuitableTarget(this.attacker, false) && this.tameable.shouldAttackEntity(this.attacker, entitylivingbase);
    }
    
    @Override
    public void startExecuting() {
        this.taskOwner.setAttackTarget(this.attacker);
        final EntityLivingBase entitylivingbase = this.tameable.getOwner();
        if (entitylivingbase != null) {
            this.timestamp = entitylivingbase.getLastAttackedEntityTime();
        }
        super.startExecuting();
    }
}
