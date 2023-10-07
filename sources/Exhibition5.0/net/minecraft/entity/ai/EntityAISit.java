// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityTameable;

public class EntityAISit extends EntityAIBase
{
    private EntityTameable theEntity;
    private boolean isSitting;
    private static final String __OBFID = "CL_00001613";
    
    public EntityAISit(final EntityTameable p_i1654_1_) {
        this.theEntity = p_i1654_1_;
        this.setMutexBits(5);
    }
    
    @Override
    public boolean shouldExecute() {
        if (!this.theEntity.isTamed()) {
            return false;
        }
        if (this.theEntity.isInWater()) {
            return false;
        }
        if (!this.theEntity.onGround) {
            return false;
        }
        final EntityLivingBase var1 = this.theEntity.func_180492_cm();
        return var1 == null || ((this.theEntity.getDistanceSqToEntity(var1) >= 144.0 || var1.getAITarget() == null) && this.isSitting);
    }
    
    @Override
    public void startExecuting() {
        this.theEntity.getNavigator().clearPathEntity();
        this.theEntity.setSitting(true);
    }
    
    @Override
    public void resetTask() {
        this.theEntity.setSitting(false);
    }
    
    public void setSitting(final boolean p_75270_1_) {
        this.isSitting = p_75270_1_;
    }
}
