/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIDoorInteract;
import net.minecraft.world.EnumDifficulty;

public class EntityAIBreakDoor
extends EntityAIDoorInteract {
    private int previousBreakProgress = -1;
    private int breakingTime;

    @Override
    public boolean shouldExecute() {
        if (!super.shouldExecute()) {
            return false;
        }
        if (!this.theEntity.worldObj.getGameRules().getBoolean("mobGriefing")) {
            return false;
        }
        BlockDoor blockDoor = this.doorBlock;
        return !BlockDoor.isOpen(this.theEntity.worldObj, this.doorPosition);
    }

    public EntityAIBreakDoor(EntityLiving entityLiving) {
        super(entityLiving);
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
        this.breakingTime = 0;
    }

    @Override
    public void resetTask() {
        super.resetTask();
        this.theEntity.worldObj.sendBlockBreakProgress(this.theEntity.getEntityId(), this.doorPosition, -1);
    }

    @Override
    public void updateTask() {
        super.updateTask();
        if (this.theEntity.getRNG().nextInt(20) == 0) {
            this.theEntity.worldObj.playAuxSFX(1010, this.doorPosition, 0);
        }
        ++this.breakingTime;
        int n = (int)((float)this.breakingTime / 240.0f * 10.0f);
        if (n != this.previousBreakProgress) {
            this.theEntity.worldObj.sendBlockBreakProgress(this.theEntity.getEntityId(), this.doorPosition, n);
            this.previousBreakProgress = n;
        }
        if (this.breakingTime == 240 && this.theEntity.worldObj.getDifficulty() == EnumDifficulty.HARD) {
            this.theEntity.worldObj.setBlockToAir(this.doorPosition);
            this.theEntity.worldObj.playAuxSFX(1012, this.doorPosition, 0);
            this.theEntity.worldObj.playAuxSFX(2001, this.doorPosition, Block.getIdFromBlock(this.doorBlock));
        }
    }

    @Override
    public boolean continueExecuting() {
        double d = this.theEntity.getDistanceSq(this.doorPosition);
        if (this.breakingTime <= 240) {
            BlockDoor blockDoor = this.doorBlock;
            if (!BlockDoor.isOpen(this.theEntity.worldObj, this.doorPosition) && d < 4.0) {
                boolean bl = true;
                return bl;
            }
        }
        boolean bl = false;
        return bl;
    }
}

