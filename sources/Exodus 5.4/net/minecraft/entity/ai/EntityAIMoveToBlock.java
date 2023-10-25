/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public abstract class EntityAIMoveToBlock
extends EntityAIBase {
    private int searchLength;
    protected int runDelay;
    private final EntityCreature theEntity;
    protected BlockPos destinationBlock = BlockPos.ORIGIN;
    private int field_179490_f;
    private boolean isAboveDestination;
    private int timeoutCounter;
    private final double movementSpeed;

    protected boolean getIsAboveDestination() {
        return this.isAboveDestination;
    }

    @Override
    public boolean continueExecuting() {
        return this.timeoutCounter >= -this.field_179490_f && this.timeoutCounter <= 1200 && this.shouldMoveTo(this.theEntity.worldObj, this.destinationBlock);
    }

    public EntityAIMoveToBlock(EntityCreature entityCreature, double d, int n) {
        this.theEntity = entityCreature;
        this.movementSpeed = d;
        this.searchLength = n;
        this.setMutexBits(5);
    }

    protected abstract boolean shouldMoveTo(World var1, BlockPos var2);

    @Override
    public void resetTask() {
    }

    private boolean searchForDestination() {
        int n = this.searchLength;
        boolean bl = true;
        BlockPos blockPos = new BlockPos(this.theEntity);
        int n2 = 0;
        while (n2 <= 1) {
            int n3 = 0;
            while (n3 < n) {
                int n4 = 0;
                while (n4 <= n3) {
                    int n5 = n4 < n3 && n4 > -n3 ? n3 : 0;
                    while (n5 <= n3) {
                        BlockPos blockPos2 = blockPos.add(n4, n2 - 1, n5);
                        if (this.theEntity.isWithinHomeDistanceFromPosition(blockPos2) && this.shouldMoveTo(this.theEntity.worldObj, blockPos2)) {
                            this.destinationBlock = blockPos2;
                            return true;
                        }
                        int n6 = n5 = n5 > 0 ? -n5 : 1 - n5;
                    }
                    int n7 = n4 = n4 > 0 ? -n4 : 1 - n4;
                }
                ++n3;
            }
            int n8 = n2 = n2 > 0 ? -n2 : 1 - n2;
        }
        return false;
    }

    @Override
    public void startExecuting() {
        this.theEntity.getNavigator().tryMoveToXYZ((double)this.destinationBlock.getX() + 0.5, this.destinationBlock.getY() + 1, (double)this.destinationBlock.getZ() + 0.5, this.movementSpeed);
        this.timeoutCounter = 0;
        this.field_179490_f = this.theEntity.getRNG().nextInt(this.theEntity.getRNG().nextInt(1200) + 1200) + 1200;
    }

    @Override
    public boolean shouldExecute() {
        if (this.runDelay > 0) {
            --this.runDelay;
            return false;
        }
        this.runDelay = 200 + this.theEntity.getRNG().nextInt(200);
        return this.searchForDestination();
    }

    @Override
    public void updateTask() {
        if (this.theEntity.getDistanceSqToCenter(this.destinationBlock.up()) > 1.0) {
            this.isAboveDestination = false;
            ++this.timeoutCounter;
            if (this.timeoutCounter % 40 == 0) {
                this.theEntity.getNavigator().tryMoveToXYZ((double)this.destinationBlock.getX() + 0.5, this.destinationBlock.getY() + 1, (double)this.destinationBlock.getZ() + 0.5, this.movementSpeed);
            }
        } else {
            this.isAboveDestination = true;
            --this.timeoutCounter;
        }
    }
}

