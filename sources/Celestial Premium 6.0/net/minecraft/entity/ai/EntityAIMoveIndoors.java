/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.village.Village;
import net.minecraft.village.VillageDoorInfo;

public class EntityAIMoveIndoors
extends EntityAIBase {
    private final EntityCreature entityObj;
    private VillageDoorInfo doorInfo;
    private int insidePosX = -1;
    private int insidePosZ = -1;

    public EntityAIMoveIndoors(EntityCreature entityObjIn) {
        this.entityObj = entityObjIn;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        BlockPos blockpos = new BlockPos(this.entityObj);
        if ((!this.entityObj.world.isDaytime() || this.entityObj.world.isRaining() && !this.entityObj.world.getBiome(blockpos).canRain()) && this.entityObj.world.provider.func_191066_m()) {
            if (this.entityObj.getRNG().nextInt(50) != 0) {
                return false;
            }
            if (this.insidePosX != -1 && this.entityObj.getDistanceSq(this.insidePosX, this.entityObj.posY, this.insidePosZ) < 4.0) {
                return false;
            }
            Village village = this.entityObj.world.getVillageCollection().getNearestVillage(blockpos, 14);
            if (village == null) {
                return false;
            }
            this.doorInfo = village.getDoorInfo(blockpos);
            return this.doorInfo != null;
        }
        return false;
    }

    @Override
    public boolean continueExecuting() {
        return !this.entityObj.getNavigator().noPath();
    }

    @Override
    public void startExecuting() {
        this.insidePosX = -1;
        BlockPos blockpos = this.doorInfo.getInsideBlockPos();
        int i = blockpos.getX();
        int j = blockpos.getY();
        int k = blockpos.getZ();
        if (this.entityObj.getDistanceSq(blockpos) > 256.0) {
            Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockTowards(this.entityObj, 14, 3, new Vec3d((double)i + 0.5, j, (double)k + 0.5));
            if (vec3d != null) {
                this.entityObj.getNavigator().tryMoveToXYZ(vec3d.x, vec3d.y, vec3d.z, 1.0);
            }
        } else {
            this.entityObj.getNavigator().tryMoveToXYZ((double)i + 0.5, j, (double)k + 0.5, 1.0);
        }
    }

    @Override
    public void resetTask() {
        this.insidePosX = this.doorInfo.getInsideBlockPos().getX();
        this.insidePosZ = this.doorInfo.getInsideBlockPos().getZ();
        this.doorInfo = null;
    }
}

