// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.renderer;

import net.minecraft.entity.Entity;
import com.google.common.base.Predicate;

class EntityRenderer$1 implements Predicate
{
    final EntityRenderer field_90032_a;
    
    EntityRenderer$1(final EntityRenderer p_i1243_1_) {
        this.field_90032_a = p_i1243_1_;
    }
    
    public boolean apply(final Entity p_apply_1_) {
        return p_apply_1_.canBeCollidedWith();
    }
    
    @Override
    public boolean apply(final Object p_apply_1_) {
        return this.apply((Entity)p_apply_1_);
    }
}
