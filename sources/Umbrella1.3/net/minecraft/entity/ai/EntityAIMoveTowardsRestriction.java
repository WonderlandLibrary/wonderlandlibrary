/*
 * Decompiled with CFR 0.150.
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
    private double movePosX;
    private double movePosY;
    private double movePosZ;
    private double movementSpeed;
    private static final String __OBFID = "CL_00001598";

    public EntityAIMoveTowardsRestriction(EntityCreature p_i2347_1_, double p_i2347_2_) {
        this.theEntity = p_i2347_1_;
        this.movementSpeed = p_i2347_2_;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if (this.theEntity.isWithinHomeDistanceCurrentPosition()) {
            return false;
        }
        BlockPos var1 = this.theEntity.func_180486_cf();
        Vec3 var2 = RandomPositionGenerator.findRandomTargetBlockTowards(this.theEntity, 16, 7, new Vec3(var1.getX(), var1.getY(), var1.getZ()));
        if (var2 == null) {
            return false;
        }
        this.movePosX = var2.xCoord;
        this.movePosY = var2.yCoord;
        this.movePosZ = var2.zCoord;
        return true;
    }

    @Override
    public boolean continueExecuting() {
        return !this.theEntity.getNavigator().noPath();
    }

    @Override
    public void startExecuting() {
        this.theEntity.getNavigator().tryMoveToXYZ(this.movePosX, this.movePosY, this.movePosZ, this.movementSpeed);
    }
}

