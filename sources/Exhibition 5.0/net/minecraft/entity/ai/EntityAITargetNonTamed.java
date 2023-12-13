// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import com.google.common.base.Predicate;
import net.minecraft.entity.passive.EntityTameable;

public class EntityAITargetNonTamed extends EntityAINearestAttackableTarget
{
    private EntityTameable theTameable;
    private static final String __OBFID = "CL_00001623";
    
    public EntityAITargetNonTamed(final EntityTameable p_i45876_1_, final Class p_i45876_2_, final boolean p_i45876_3_, final Predicate p_i45876_4_) {
        super(p_i45876_1_, p_i45876_2_, 10, p_i45876_3_, false, p_i45876_4_);
        this.theTameable = p_i45876_1_;
    }
    
    @Override
    public boolean shouldExecute() {
        return !this.theTameable.isTamed() && super.shouldExecute();
    }
}
