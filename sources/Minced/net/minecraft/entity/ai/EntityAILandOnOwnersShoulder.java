// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.passive.EntityShoulderRiding;

public class EntityAILandOnOwnersShoulder extends EntityAIBase
{
    private final EntityShoulderRiding entity;
    private EntityPlayer owner;
    private boolean isSittingOnShoulder;
    
    public EntityAILandOnOwnersShoulder(final EntityShoulderRiding entityIn) {
        this.entity = entityIn;
    }
    
    @Override
    public boolean shouldExecute() {
        final EntityLivingBase entitylivingbase = this.entity.getOwner();
        final boolean flag = entitylivingbase != null && !((EntityPlayer)entitylivingbase).isSpectator() && !((EntityPlayer)entitylivingbase).capabilities.isFlying && !entitylivingbase.isInWater();
        return !this.entity.isSitting() && flag && this.entity.canSitOnShoulder();
    }
    
    @Override
    public boolean isInterruptible() {
        return !this.isSittingOnShoulder;
    }
    
    @Override
    public void startExecuting() {
        this.owner = (EntityPlayer)this.entity.getOwner();
        this.isSittingOnShoulder = false;
    }
    
    @Override
    public void updateTask() {
        if (!this.isSittingOnShoulder && !this.entity.isSitting() && !this.entity.getLeashed() && this.entity.getEntityBoundingBox().intersects(this.owner.getEntityBoundingBox())) {
            this.isSittingOnShoulder = this.entity.setEntityOnShoulder(this.owner);
        }
    }
}
