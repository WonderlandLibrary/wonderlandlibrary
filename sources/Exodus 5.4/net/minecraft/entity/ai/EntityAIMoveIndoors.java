/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.village.Village;
import net.minecraft.village.VillageDoorInfo;

public class EntityAIMoveIndoors
extends EntityAIBase {
    private EntityCreature entityObj;
    private int insidePosX = -1;
    private int insidePosZ = -1;
    private VillageDoorInfo doorInfo;

    @Override
    public void resetTask() {
        this.insidePosX = this.doorInfo.getInsideBlockPos().getX();
        this.insidePosZ = this.doorInfo.getInsideBlockPos().getZ();
        this.doorInfo = null;
    }

    @Override
    public void startExecuting() {
        this.insidePosX = -1;
        BlockPos blockPos = this.doorInfo.getInsideBlockPos();
        int n = blockPos.getX();
        int n2 = blockPos.getY();
        int n3 = blockPos.getZ();
        if (this.entityObj.getDistanceSq(blockPos) > 256.0) {
            Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockTowards(this.entityObj, 14, 3, new Vec3((double)n + 0.5, n2, (double)n3 + 0.5));
            if (vec3 != null) {
                this.entityObj.getNavigator().tryMoveToXYZ(vec3.xCoord, vec3.yCoord, vec3.zCoord, 1.0);
            }
        } else {
            this.entityObj.getNavigator().tryMoveToXYZ((double)n + 0.5, n2, (double)n3 + 0.5, 1.0);
        }
    }

    public EntityAIMoveIndoors(EntityCreature entityCreature) {
        this.entityObj = entityCreature;
        this.setMutexBits(1);
    }

    @Override
    public boolean continueExecuting() {
        return !this.entityObj.getNavigator().noPath();
    }

    @Override
    public boolean shouldExecute() {
        BlockPos blockPos = new BlockPos(this.entityObj);
        if ((!this.entityObj.worldObj.isDaytime() || this.entityObj.worldObj.isRaining() && !this.entityObj.worldObj.getBiomeGenForCoords(blockPos).canSpawnLightningBolt()) && !this.entityObj.worldObj.provider.getHasNoSky()) {
            if (this.entityObj.getRNG().nextInt(50) != 0) {
                return false;
            }
            if (this.insidePosX != -1 && this.entityObj.getDistanceSq(this.insidePosX, this.entityObj.posY, this.insidePosZ) < 4.0) {
                return false;
            }
            Village village = this.entityObj.worldObj.getVillageCollection().getNearestVillage(blockPos, 14);
            if (village == null) {
                return false;
            }
            this.doorInfo = village.getDoorInfo(blockPos);
            return this.doorInfo != null;
        }
        return false;
    }
}

