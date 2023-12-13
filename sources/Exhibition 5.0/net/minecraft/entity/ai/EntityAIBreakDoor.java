// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.BlockDoor;
import net.minecraft.entity.EntityLiving;

public class EntityAIBreakDoor extends EntityAIDoorInteract
{
    private int breakingTime;
    private int field_75358_j;
    private static final String __OBFID = "CL_00001577";
    
    public EntityAIBreakDoor(final EntityLiving p_i1618_1_) {
        super(p_i1618_1_);
        this.field_75358_j = -1;
    }
    
    @Override
    public boolean shouldExecute() {
        if (!super.shouldExecute()) {
            return false;
        }
        if (!this.theEntity.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing")) {
            return false;
        }
        final BlockDoor var10000 = this.doorBlock;
        return !BlockDoor.func_176514_f(this.theEntity.worldObj, this.field_179507_b);
    }
    
    @Override
    public void startExecuting() {
        super.startExecuting();
        this.breakingTime = 0;
    }
    
    @Override
    public boolean continueExecuting() {
        final double var1 = this.theEntity.getDistanceSq(this.field_179507_b);
        if (this.breakingTime <= 240) {
            final BlockDoor var2 = this.doorBlock;
            if (!BlockDoor.func_176514_f(this.theEntity.worldObj, this.field_179507_b) && var1 < 4.0) {
                final boolean var3 = true;
                return var3;
            }
        }
        final boolean var3 = false;
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
        final int var1 = (int)(this.breakingTime / 240.0f * 10.0f);
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
