/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityAIBeg
extends EntityAIBase {
    private EntityWolf theWolf;
    private EntityPlayer thePlayer;
    private World worldObject;
    private float minPlayerDistance;
    private int timeoutCounter;

    public EntityAIBeg(EntityWolf wolf, float minDistance) {
        this.theWolf = wolf;
        this.worldObject = wolf.worldObj;
        this.minPlayerDistance = minDistance;
        this.setMutexBits(2);
    }

    @Override
    public boolean shouldExecute() {
        this.thePlayer = this.worldObject.getClosestPlayerToEntity(this.theWolf, this.minPlayerDistance);
        if (this.thePlayer == null) {
            return false;
        }
        boolean bl = this.hasPlayerGotBoneInHand(this.thePlayer);
        return bl;
    }

    @Override
    public boolean continueExecuting() {
        if (!this.thePlayer.isEntityAlive()) {
            return false;
        }
        if (this.theWolf.getDistanceSqToEntity(this.thePlayer) > (double)(this.minPlayerDistance * this.minPlayerDistance)) {
            return false;
        }
        if (this.timeoutCounter <= 0) return false;
        if (!this.hasPlayerGotBoneInHand(this.thePlayer)) return false;
        return true;
    }

    @Override
    public void startExecuting() {
        this.theWolf.setBegging(true);
        this.timeoutCounter = 40 + this.theWolf.getRNG().nextInt(40);
    }

    @Override
    public void resetTask() {
        this.theWolf.setBegging(false);
        this.thePlayer = null;
    }

    @Override
    public void updateTask() {
        this.theWolf.getLookHelper().setLookPosition(this.thePlayer.posX, this.thePlayer.posY + (double)this.thePlayer.getEyeHeight(), this.thePlayer.posZ, 10.0f, this.theWolf.getVerticalFaceSpeed());
        --this.timeoutCounter;
    }

    private boolean hasPlayerGotBoneInHand(EntityPlayer player) {
        ItemStack itemstack = player.inventory.getCurrentItem();
        if (itemstack == null) {
            return false;
        }
        if (!this.theWolf.isTamed() && itemstack.getItem() == Items.bone) {
            return true;
        }
        boolean bl = this.theWolf.isBreedingItem(itemstack);
        return bl;
    }
}

