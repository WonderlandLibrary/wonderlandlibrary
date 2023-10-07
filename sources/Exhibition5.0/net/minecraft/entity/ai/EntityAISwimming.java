// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.ai;

import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.entity.EntityLiving;

public class EntityAISwimming extends EntityAIBase
{
    private EntityLiving theEntity;
    private static final String __OBFID = "CL_00001584";
    
    public EntityAISwimming(final EntityLiving p_i1624_1_) {
        this.theEntity = p_i1624_1_;
        this.setMutexBits(4);
        ((PathNavigateGround)p_i1624_1_.getNavigator()).func_179693_d(true);
    }
    
    @Override
    public boolean shouldExecute() {
        return this.theEntity.isInWater() || this.theEntity.func_180799_ab();
    }
    
    @Override
    public void updateTask() {
        if (this.theEntity.getRNG().nextFloat() < 0.8f) {
            this.theEntity.getJumpHelper().setJumping();
        }
    }
}
