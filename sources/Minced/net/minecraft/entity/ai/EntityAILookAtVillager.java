// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.monster.EntityIronGolem;

public class EntityAILookAtVillager extends EntityAIBase
{
    private final EntityIronGolem ironGolem;
    private EntityVillager villager;
    private int lookTime;
    
    public EntityAILookAtVillager(final EntityIronGolem ironGolemIn) {
        this.ironGolem = ironGolemIn;
        this.setMutexBits(3);
    }
    
    @Override
    public boolean shouldExecute() {
        if (!this.ironGolem.world.isDaytime()) {
            return false;
        }
        if (this.ironGolem.getRNG().nextInt(8000) != 0) {
            return false;
        }
        this.villager = this.ironGolem.world.findNearestEntityWithinAABB(EntityVillager.class, this.ironGolem.getEntityBoundingBox().grow(6.0, 2.0, 6.0), (EntityVillager)this.ironGolem);
        return this.villager != null;
    }
    
    @Override
    public boolean shouldContinueExecuting() {
        return this.lookTime > 0;
    }
    
    @Override
    public void startExecuting() {
        this.lookTime = 400;
        this.ironGolem.setHoldingRose(true);
    }
    
    @Override
    public void resetTask() {
        this.ironGolem.setHoldingRose(false);
        this.villager = null;
    }
    
    @Override
    public void updateTask() {
        this.ironGolem.getLookHelper().setLookPositionWithEntity(this.villager, 30.0f, 30.0f);
        --this.lookTime;
    }
}
