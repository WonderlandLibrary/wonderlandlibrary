/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class EntityAITradePlayer
extends EntityAIBase {
    private EntityVillager villager;

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
        EntityPlayer entityPlayer = this.villager.getCustomer();
        return entityPlayer == null ? false : (this.villager.getDistanceSqToEntity(entityPlayer) > 16.0 ? false : entityPlayer.openContainer instanceof Container);
    }

    @Override
    public void resetTask() {
        this.villager.setCustomer(null);
    }

    @Override
    public void startExecuting() {
        this.villager.getNavigator().clearPathEntity();
    }

    public EntityAITradePlayer(EntityVillager entityVillager) {
        this.villager = entityVillager;
        this.setMutexBits(5);
    }
}

