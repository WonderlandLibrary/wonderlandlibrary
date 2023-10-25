/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class EntityAIMoveTowardsRestriction
extends EntityAIBase {
    private EntityCreature theEntity;
    private double movePosY;
    private double movementSpeed;
    private double movePosX;
    private double movePosZ;

    @Override
    public boolean continueExecuting() {
        return !this.theEntity.getNavigator().noPath();
    }

    @Override
    public boolean shouldExecute() {
        if (this.theEntity.isWithinHomeDistanceCurrentPosition()) {
            return false;
        }
        BlockPos blockPos = this.theEntity.getHomePosition();
        Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockTowards(this.theEntity, 16, 7, new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ()));
        if (vec3 == null) {
            return false;
        }
        this.movePosX = vec3.xCoord;
        this.movePosY = vec3.yCoord;
        this.movePosZ = vec3.zCoord;
        return true;
    }

    public EntityAIMoveTowardsRestriction(EntityCreature entityCreature, double d) {
        this.theEntity = entityCreature;
        this.movementSpeed = d;
        this.setMutexBits(1);
    }

    @Override
    public void startExecuting() {
        this.theEntity.getNavigator().tryMoveToXYZ(this.movePosX, this.movePosY, this.movePosZ, this.movementSpeed);
    }
}

