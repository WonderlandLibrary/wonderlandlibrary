// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.util.math.Vec3d;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityLlama;

public class EntityAILlamaFollowCaravan extends EntityAIBase
{
    public EntityLlama llama;
    private double speedModifier;
    private int distCheckCounter;
    
    public EntityAILlamaFollowCaravan(final EntityLlama llamaIn, final double speedModifierIn) {
        this.llama = llamaIn;
        this.speedModifier = speedModifierIn;
        this.setMutexBits(1);
    }
    
    @Override
    public boolean shouldExecute() {
        if (this.llama.getLeashed() || this.llama.inCaravan()) {
            return false;
        }
        final List<EntityLlama> list = this.llama.world.getEntitiesWithinAABB(this.llama.getClass(), this.llama.getEntityBoundingBox().grow(9.0, 4.0, 9.0));
        EntityLlama entityllama = null;
        double d0 = Double.MAX_VALUE;
        for (final EntityLlama entityllama2 : list) {
            if (entityllama2.inCaravan() && !entityllama2.hasCaravanTrail()) {
                final double d2 = this.llama.getDistanceSq(entityllama2);
                if (d2 > d0) {
                    continue;
                }
                d0 = d2;
                entityllama = entityllama2;
            }
        }
        if (entityllama == null) {
            for (final EntityLlama entityllama3 : list) {
                if (entityllama3.getLeashed() && !entityllama3.hasCaravanTrail()) {
                    final double d3 = this.llama.getDistanceSq(entityllama3);
                    if (d3 > d0) {
                        continue;
                    }
                    d0 = d3;
                    entityllama = entityllama3;
                }
            }
        }
        if (entityllama == null) {
            return false;
        }
        if (d0 < 4.0) {
            return false;
        }
        if (!entityllama.getLeashed() && !this.firstIsLeashed(entityllama, 1)) {
            return false;
        }
        this.llama.joinCaravan(entityllama);
        return true;
    }
    
    @Override
    public boolean shouldContinueExecuting() {
        if (this.llama.inCaravan() && this.llama.getCaravanHead().isEntityAlive() && this.firstIsLeashed(this.llama, 0)) {
            final double d0 = this.llama.getDistanceSq(this.llama.getCaravanHead());
            if (d0 > 676.0) {
                if (this.speedModifier <= 3.0) {
                    this.speedModifier *= 1.2;
                    this.distCheckCounter = 40;
                    return true;
                }
                if (this.distCheckCounter == 0) {
                    return false;
                }
            }
            if (this.distCheckCounter > 0) {
                --this.distCheckCounter;
            }
            return true;
        }
        return false;
    }
    
    @Override
    public void resetTask() {
        this.llama.leaveCaravan();
        this.speedModifier = 2.1;
    }
    
    @Override
    public void updateTask() {
        if (this.llama.inCaravan()) {
            final EntityLlama entityllama = this.llama.getCaravanHead();
            final double d0 = this.llama.getDistance(entityllama);
            final float f = 2.0f;
            final Vec3d vec3d = new Vec3d(entityllama.posX - this.llama.posX, entityllama.posY - this.llama.posY, entityllama.posZ - this.llama.posZ).normalize().scale(Math.max(d0 - 2.0, 0.0));
            this.llama.getNavigator().tryMoveToXYZ(this.llama.posX + vec3d.x, this.llama.posY + vec3d.y, this.llama.posZ + vec3d.z, this.speedModifier);
        }
    }
    
    private boolean firstIsLeashed(final EntityLlama p_190858_1_, int p_190858_2_) {
        if (p_190858_2_ > 8) {
            return false;
        }
        if (!p_190858_1_.inCaravan()) {
            return false;
        }
        if (p_190858_1_.getCaravanHead().getLeashed()) {
            return true;
        }
        final EntityLlama entityllama = p_190858_1_.getCaravanHead();
        ++p_190858_2_;
        return this.firstIsLeashed(entityllama, p_190858_2_);
    }
}
