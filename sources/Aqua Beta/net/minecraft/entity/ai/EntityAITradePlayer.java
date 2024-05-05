package net.minecraft.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class EntityAITradePlayer
extends EntityAIBase {
    private EntityVillager villager;

    public EntityAITradePlayer(EntityVillager villagerIn) {
        this.villager = villagerIn;
        this.setMutexBits(5);
    }

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
        EntityPlayer entityplayer = this.villager.getCustomer();
        return entityplayer == null ? false : (this.villager.getDistanceSqToEntity((Entity)entityplayer) > 16.0 ? false : entityplayer.openContainer instanceof Container);
    }

    public void startExecuting() {
        this.villager.getNavigator().clearPathEntity();
    }

    public void resetTask() {
        this.villager.setCustomer((EntityPlayer)null);
    }
}
