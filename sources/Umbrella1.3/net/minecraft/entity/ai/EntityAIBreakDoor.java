/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIDoorInteract;
import net.minecraft.world.EnumDifficulty;

public class EntityAIBreakDoor
extends EntityAIDoorInteract {
    private int breakingTime;
    private int field_75358_j = -1;
    private static final String __OBFID = "CL_00001577";

    public EntityAIBreakDoor(EntityLiving p_i1618_1_) {
        super(p_i1618_1_);
    }

    @Override
    public boolean shouldExecute() {
        if (!super.shouldExecute()) {
            return false;
        }
        if (!this.theEntity.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing")) {
            return false;
        }
        BlockDoor var10000 = this.doorBlock;
        return !BlockDoor.func_176514_f(this.theEntity.worldObj, this.field_179507_b);
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
        this.breakingTime = 0;
    }

    @Override
    public boolean continueExecuting() {
        double var1 = this.theEntity.getDistanceSq(this.field_179507_b);
        if (this.breakingTime <= 240) {
            BlockDoor var10000 = this.doorBlock;
            if (!BlockDoor.func_176514_f(this.theEntity.worldObj, this.field_179507_b) && var1 < 4.0) {
                boolean var3 = true;
                return var3;
            }
        }
        boolean var3 = false;
        return var3;
    }

    @Override
    public void resetTask() {
        super.resetTask();
        this.theEntity.worldObj.sendBlockBreakProgress(this.theEntity.getEntityId(), this.field_179507_b, -1);
    }

    @Override
    public void updateTask() {
        super.updateTask();
        if (this.theEntity.getRNG().nextInt(20) == 0) {
            this.theEntity.worldObj.playAuxSFX(1010, this.field_179507_b, 0);
        }
        ++this.breakingTime;
        int var1 = (int)((float)this.breakingTime / 240.0f * 10.0f);
        if (var1 != this.field_75358_j) {
            this.theEntity.worldObj.sendBlockBreakProgress(this.theEntity.getEntityId(), this.field_179507_b, var1);
            this.field_75358_j = var1;
        }
        if (this.breakingTime == 240 && this.theEntity.worldObj.getDifficulty() == EnumDifficulty.HARD) {
            this.theEntity.worldObj.setBlockToAir(this.field_179507_b);
            this.theEntity.worldObj.playAuxSFX(1012, this.field_179507_b, 0);
            this.theEntity.worldObj.playAuxSFX(2001, this.field_179507_b, Block.getIdFromBlock(this.doorBlock));
        }
    }
}

