// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;

public class EntityAIOwnerHurtByTarget extends EntityAITarget
{
    EntityTameable theDefendingTameable;
    EntityLivingBase theOwnerAttacker;
    private int field_142051_e;
    private static final String __OBFID = "CL_00001624";
    
    public EntityAIOwnerHurtByTarget(final EntityTameable p_i1667_1_) {
        super(p_i1667_1_, false);
        this.theDefendingTameable = p_i1667_1_;
        this.setMutexBits(1);
    }
    
    @Override
    public boolean shouldExecute() {
        if (!this.theDefendingTameable.isTamed()) {
            return false;
        }
        final EntityLivingBase var1 = this.theDefendingTameable.func_180492_cm();
        if (var1 == null) {
            return false;
        }
        this.theOwnerAttacker = var1.getAITarget();
        final int var2 = var1.getRevengeTimer();
        return var2 != this.field_142051_e && this.isSuitableTarget(this.theOwnerAttacker, false) && this.theDefendingTameable.func_142018_a(this.theOwnerAttacker, var1);
    }
    
    @Override
    public void startExecuting() {
        this.taskOwner.setAttackTarget(this.theOwnerAttacker);
        final EntityLivingBase var1 = this.theDefendingTameable.func_180492_cm();
        if (var1 != null) {
            this.field_142051_e = var1.getRevengeTimer();
        }
        super.startExecuting();
    }
}
