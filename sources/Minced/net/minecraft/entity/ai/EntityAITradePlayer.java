// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityVillager;

public class EntityAITradePlayer extends EntityAIBase
{
    private final EntityVillager villager;
    
    public EntityAITradePlayer(final EntityVillager villagerIn) {
        this.villager = villagerIn;
        this.setMutexBits(5);
    }
    
    @Override
    public boolean shouldExecute() {
        if (!this.villager.isEntityAlive()) {
            return false;
        }
        if (this.villager.isInWater()) {
            return false;
        }
        if (!this.villager.onGround) {
            return false;
        }
        if (this.villager.velocityChanged) {
            return false;
        }
        final EntityPlayer entityplayer = this.villager.getCustomer();
        return entityplayer != null && this.villager.getDistanceSq(entityplayer) <= 16.0 && entityplayer.openContainer != null;
    }
    
    @Override
    public void startExecuting() {
        this.villager.getNavigator().clearPath();
    }
    
    @Override
    public void resetTask() {
        this.villager.setCustomer(null);
    }
}
