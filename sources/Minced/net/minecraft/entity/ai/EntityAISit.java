// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityTameable;

public class EntityAISit extends EntityAIBase
{
    private final EntityTameable tameable;
    private boolean isSitting;
    
    public EntityAISit(final EntityTameable entityIn) {
        this.tameable = entityIn;
        this.setMutexBits(5);
    }
    
    @Override
    public boolean shouldExecute() {
        if (!this.tameable.isTamed()) {
            return false;
        }
        if (this.tameable.isInWater()) {
            return false;
        }
        if (!this.tameable.onGround) {
            return false;
        }
        final EntityLivingBase entitylivingbase = this.tameable.getOwner();
        return entitylivingbase == null || ((this.tameable.getDistanceSq(entitylivingbase) >= 144.0 || entitylivingbase.getRevengeTarget() == null) && this.isSitting);
    }
    
    @Override
    public void startExecuting() {
        this.tameable.getNavigator().clearPath();
        this.tameable.setSitting(true);
    }
    
    @Override
    public void resetTask() {
        this.tameable.setSitting(false);
    }
    
    public void setSitting(final boolean sitting) {
        this.isSitting = sitting;
    }
}
