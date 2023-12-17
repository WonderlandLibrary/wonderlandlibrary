// 
// Decompiled by Procyon v0.5.36
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
    private int previousBreakProgress;
    
    public EntityAIBreakDoor(final EntityLiving entityIn) {
        super(entityIn);
        this.previousBreakProgress = -1;
    }
    
    @Override
    public boolean shouldExecute() {
        if (!super.shouldExecute()) {
            return false;
        }
        if (!this.entity.world.getGameRules().getBoolean("mobGriefing")) {
            return false;
        }
        final BlockDoor blockdoor = this.doorBlock;
        return !BlockDoor.isOpen(this.entity.world, this.doorPosition);
    }
    
    @Override
    public void startExecuting() {
        super.startExecuting();
        this.breakingTime = 0;
    }
    
    @Override
    public boolean shouldContinueExecuting() {
        final double d0 = this.entity.getDistanceSq(this.doorPosition);
        if (this.breakingTime <= 240) {
            final BlockDoor blockdoor = this.doorBlock;
            if (!BlockDoor.isOpen(this.entity.world, this.doorPosition) && d0 < 4.0) {
                final boolean flag = true;
                return flag;
            }
        }
        final boolean flag = false;
        return flag;
    }
    
    @Override
    public void resetTask() {
        super.resetTask();
        this.entity.world.sendBlockBreakProgress(this.entity.getEntityId(), this.doorPosition, -1);
    }
    
    @Override
    public void updateTask() {
        super.updateTask();
        if (this.entity.getRNG().nextInt(20) == 0) {
            this.entity.world.playEvent(1019, this.doorPosition, 0);
        }
        ++this.breakingTime;
        final int i = (int)(this.breakingTime / 240.0f * 10.0f);
        if (i != this.previousBreakProgress) {
            this.entity.world.sendBlockBreakProgress(this.entity.getEntityId(), this.doorPosition, i);
            this.previousBreakProgress = i;
        }
        if (this.breakingTime == 240 && this.entity.world.getDifficulty() == EnumDifficulty.HARD) {
            this.entity.world.setBlockToAir(this.doorPosition);
            this.entity.world.playEvent(1021, this.doorPosition, 0);
            this.entity.world.playEvent(2001, this.doorPosition, Block.getIdFromBlock(this.doorBlock));
        }
    }
}
