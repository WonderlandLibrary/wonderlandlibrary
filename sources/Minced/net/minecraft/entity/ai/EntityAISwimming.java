// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.entity.EntityLiving;

public class EntityAISwimming extends EntityAIBase
{
    private final EntityLiving entity;
    
    public EntityAISwimming(final EntityLiving entityIn) {
        this.entity = entityIn;
        this.setMutexBits(4);
        if (entityIn.getNavigator() instanceof PathNavigateGround) {
            ((PathNavigateGround)entityIn.getNavigator()).setCanSwim(true);
        }
        else if (entityIn.getNavigator() instanceof PathNavigateFlying) {
            ((PathNavigateFlying)entityIn.getNavigator()).setCanFloat(true);
        }
    }
    
    @Override
    public boolean shouldExecute() {
        return this.entity.isInWater() || this.entity.isInLava();
    }
    
    @Override
    public void updateTask() {
        if (this.entity.getRNG().nextFloat() < 0.8f) {
            this.entity.getJumpHelper().setJumping();
        }
    }
}
